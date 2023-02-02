# Robby Sodhi
# J.Bains
# 2023
# stockData_manager is a child of SQliteWrapper
# this class manages all the interactions between the stock_data.db

import constants
import datetime
from SQLiteWrapper import SQLiteWrapper


class stockData_manager(SQLiteWrapper):  # inherit SQLiteWrapper

    # creates the database and the corresponding table for it
    def create_database(self):

        # executive the sql statements to create the table
        self.execute(
            """CREATE TABLE IF NOT EXISTS stock_data (
                    date text NOT NULL,
                    ticker text NOT NULL,
                    open numeric NOT NULL,
                    high numeric NOT NULL,
                    low numeric NOT NULL,
                    close numeric NOT NULL,
                    volume numeric NOT NULL,
                    dividends numeric NOT NULL,
                    stock_splits numeric NOT NULL,
                    PRIMARY KEY (ticker, date))
                    """
        )

        # make an index between date and ticker for optimization
        self.execute(
            "create UNIQUE index IF NOT EXISTS stock_data_by_date on stock_data (date, ticker)"
        )

    # constructor takes the database path and calls the SQlitewrapper super constructor
    def __init__(self, database_path):
        super().__init__(database_path)
        self.insert_statement = "INSERT INTO stock_data (date, ticker, open, high, low, close, volume, dividends, stock_splits) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"

    # searches for a ticker in the database
    def searchForTicker(self, ticker, start, end):
        start = start.strftime(constants.date_format)
        end = end.strftime(constants.date_format)

        self.execute(
            "SELECT * FROM stock_data WHERE ticker=? AND date BETWEEN ? AND ?",
            (ticker, start, end),
        )

        return self.fetchall()

    # write many ticker data entries to database (date, ticker, open, high, low, close, volume, dividends, stock_splits)
    def writeManyTickerDataEntry(self, arrOfRowTuple):
        statement = self.insert_statement

        self.executemany(statement, arrOfRowTuple)

    # returns the last date in the database for a given ticker (allows us to check if we have the latest data for a ticker)
    def getLastDateForTicker(self, ticker):
        self.execute(
            "SELECT MAX(date) FROM stock_data WHERE ticker=?", (ticker,))
        data = self.fetchone()
        if data[0] is None:
            return None
        return datetime.datetime.strptime(
            data[0],
            constants.date_format,
        )
