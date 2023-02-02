# Robby Sodhi
# J.Bains
# 2023
# this wraps the userData_manager and the stockData_manager classes(not a child, just creates instances when needed)
# this is because the original sqlitewrapper class is meant to be ran with a context manager
# the context manager(with statement in python) ensures that the data in the database stays correct (can't have it change while in use)


import yfinance as yf
import pandas as pd
from stockData_manager import stockData_manager
import constants
import datetime
from userData_manager import userData_manager


class database_manager:

    # start and end should be datetime objects in format constants.date_format
    # this method gets the price history for a stock given a start and end date range.
    # it either pulls from yfinance or accesses a local cache(stock_data)
    def get_stock_history_by_ticker(
        self, ticker, start, end
    ):  # start and end in format yyyy-mm-dd ex. 2005-02-08
        with stockData_manager(
            constants.stock_data_database_path
        ) as db:  # begin transaction, close transaction when with is finished. Ensures thread safety as two threads can't both acknowledge that the database is missing data and overwrite each other
            # check to make sure the end time is not greater or equal to today, this scraper does not provide daily info and there is no such thing as greater than today (hasn't happened)
            if end >= constants.getCurrentDate(constants.date_format):
                raise ValueError(
                    "end date must not be greater than or equal today")

            # if the database is not up to date
            # if what we are saerching for is newer than what is in the database, try to fill the database with the missing data
            # assumes that we will have prices for every single day, this is bad because if we don't this will run on each call for a ticker (spamming yahoo), need a fix
            if not db.getLastDateForTicker(ticker) is None:
                if end > db.getLastDateForTicker(ticker):

                    # working around bug in yfinance and how it handls yahoo api, see: https://github.com/ranaroussi/yfinance/issues/1272
                    if db.getLastDateForTicker(ticker) + datetime.timedelta(
                        days=1
                    ) != constants.getCurrentDate(
                        constants.date_format
                    ) - datetime.timedelta(
                        days=1
                    ):
                        # get the missing data from yfinance (to make our cache/database up to date)
                        df = yf.Ticker(ticker).history(
                            start=(
                                db.getLastDateForTicker(ticker)
                                + datetime.timedelta(days=1)
                            ).strftime(constants.date_format),
                            end=constants.getCurrentDate(
                                constants.date_format
                            ).strftime(constants.date_format),
                            raise_errors=False,
                        )
                        num_rows = len(df.index)
                        # if we got some data
                        if num_rows > 0:
                            db.writeManyTickerDataEntry(
                                self.dump_stockdf_to_list(df, ticker)
                            )

            # search the database for our ticker
            data = db.searchForTicker(ticker, start, end)

            # if our search resulted in nothing
            if len(data) <= 0:
                # if the end date is less than the last date in the database, assume that it is up to date and there is no data (beacuse we do period="max" when we fetch)
                if not db.getLastDateForTicker(
                    ticker
                ) is None and end < db.getLastDateForTicker(ticker):
                    return None
                # get all the avaiable data for a ticker
                df = yf.Ticker(ticker).history(
                    period="max", raise_errors=False)
                num_rows = len(df.index)
                # if no data for ticker
                if num_rows <= 0:
                    return None
                # write all of the data we retrieved to the database (cache it)
                db.writeManyTickerDataEntry(
                    self.dump_stockdf_to_list(df, ticker))
                # search the database for our data (we just wrote it, so it should be there)
                data = db.searchForTicker(
                    ticker, start, end
                )
            return data


    # wraps the userData buy_stock method and also provides it with the current market value of the stock you're buying
    def buy_stock(self, id, ticker, amount):
        if (not self.does_ticker_exist(ticker)):
            return None
        with userData_manager(constants.user_data_database_path) as db:
            return db.buy_stock(id, ticker, amount, self.get_current_stock_price(ticker))
    # wraps the userData sell_stock method and provides it with the current market value of the stock you're selling
    def sell_stock(self, id, ticker, amount):
        if (not self.does_ticker_exist(ticker)):
            return None
        with userData_manager(constants.user_data_database_path) as db:
            return db.sell_stock(id, ticker, self.get_current_stock_price(ticker), amount)
    # wraps the userData get_user_ticker_data method
    def get_user_ticker_data(self, id):
        with userData_manager(constants.user_data_database_path) as db:
            return db.get_user_ticker_data(id)
    # returns the current stock price of a given ticker
    def get_current_stock_price(self, ticker):
        # implement this in a cached way!!!!!
        df = yf.Ticker(ticker).history(period='1d')  # ['Close'][0]
        num_rows = len(df.index)
        if num_rows <= 0:
            return None
        return df['Close'][0]
    # wraps the userData get_suer_balance method
    def get_user_balance(self, id):
        with userData_manager(constants.user_data_database_path) as db:
            return db.get_user_balance(db.get_user_from_id(id))

    # checks if a given ticker exists
    # useful to run before we do anything with tickers so ensure that there will be data for them
    def does_ticker_exist(self, ticker):
        if (self.get_stock_history_by_ticker(ticker, start=datetime.datetime.strptime("1800-01-01", constants.date_format), end=(constants.getCurrentDate(constants.date_format) - datetime.timedelta(days=1))) is None):
            return False
        return True

    # wraps the userData login_user method
    def login_user(self, username, password):
        with userData_manager(constants.user_data_database_path) as db:
            return db.login_user(username, password)
    # wraps the userData creater_user method

    def create_user(self, username, password):
        with userData_manager(constants.user_data_database_path) as db:
            return db.create_user(username, password)

    # the yfinance library returns a pandas dataframe, before putting it in the database we need to convert it to a list
    # this loops through the dataframe and just transfers all of the data into a python list aka an array
    def dump_stockdf_to_list(self, df, ticker):

        values = [
            (
                pd.Timestamp(index).strftime(constants.date_format),
                ticker,
                row["Open"],
                row["High"],
                row["Low"],
                row["Close"],
                row["Volume"],
                row["Dividends"],
                row["Stock Splits"],
            )
            for index, row in df.iterrows()
        ]

        return values
