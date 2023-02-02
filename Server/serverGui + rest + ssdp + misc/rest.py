# Robby Sodhi
# J.Bains
# 2023
# the fastAPI rest api server.
# Exposes web headers that we can make requests to for data
# essentially allowing us to use all of the database manager methods over a web request

from fastapi import FastAPI, Query, Response, status
import database_manager
from typing import List
import json
import datetime
import constants

app = FastAPI()  # instance of the FastAPI library
# my database manager class (lets us manager the user_daata and stock_data databases)
database = database_manager.database_manager()

# example of url paramters: /get_stock_history_by_ticker?ticker=AAPL&start=2022-01-01&end=2023-01-26

# this header allows you pass a ticker, start and end date (format yyyy-mm-dd) as url paramtere and receive the history for a stock ticker


@app.get("/get_stock_history_by_ticker")
async def get_stock_history_by_ticker(
    response: Response,
    ticker: str = Query(None),
    start: str = Query(
        default=None, regex="^\d{4}-\d{2}-\d{2}$", format="date"),  # the regex ensures that the passed argument matches yyyy-mm-dd
    end: str = Query(default=None, regex="^\d{4}-\d{2}-\d{2}$", format="date")
):
    # data is our response object, valid=false means that it didn't complete the request properly, true means it did
    data = {"valid": "true"}
    if ticker is None or start is None or end is None:
        data["valid"] = "false"
        # return status code 422 when data received is invalid
        response.status_code = status.HTTP_422_UNPROCESSABLE_ENTITY
        return Response(content=json.dumps(data), media_type="application/json")
    start = datetime.datetime.strptime(start, constants.date_format)
    end = datetime.datetime.strptime(end, constants.date_format)
    # need proper error checking, yfinance could fail, sqlite3 could fail, ...
    # call our get_stock_history_by_ticker method from our database manager
    ticker_data = database.get_stock_history_by_ticker(ticker, start, end)
    data[ticker] = ticker_data  # add the data to the data object
    # return the data in a json format
    return Response(content=json.dumps(data), media_type="application/json")


# get_balance header returns the balance for a user. Takes the id as a url paramter
@app.get("/get_balance")
async def get_balance(response: Response, id: str = Query(None)):
    data = {"valid": "true"}
    if id is None:
        data["valid"] = "false"
        response.status_code = status.HTTP_422_UNPROCESSABLE_ENTITY
        return Response(content=json.dumps(data), media_type="application/json")
    balance = database.get_user_balance(id)
    data["balance"] = balance
    return Response(content=json.dumps(data), media_type="application/json")

# buy_stock header buys a stock on a users account


@app.get("/buy_stock")
async def buy_stock(response: Response, id: str = Query(None), ticker: str = Query(None), amount: int = Query(None)):
    data = {"valid": "true"}
    if id is None or ticker is None or amount is None or amount < 0:
        data["valid"] = "false"
        response.status_code = status.HTTP_422_UNPROCESSABLE_ENTITY
        return Response(content=json.dumps(data), media_type="application/json")
    response = database.buy_stock(id, ticker, amount)
    if response is None:
        response = "false"
    data["valid"] = str(response).lower()
    return Response(content=json.dumps(data), media_type="application/json")

# sell_stock header sells a stock on a users account


@app.get("/sell_stock")
async def sell_stock(response: Response, id: str = Query(None), ticker: str = Query(None), amount: int = Query(None)):
    data = {"valid": "true"}
    if id is None or ticker is None or amount is None or amount < 0:
        data["valid"] = "false"
        response.status_code = status.HTTP_422_UNPROCESSABLE_ENTITY
        return Response(content=json.dumps(data), media_type="application/json")
    response = database.sell_stock(id, ticker, amount)
    if response is None:
        response = "false"
    data["valid"] = str(response).lower()
    return Response(content=json.dumps(data), media_type="application/json")

# get_current_stock_price header gets the current stock price of a ticker


@app.get("/get_current_stock_price")
async def get_current_stock_price(response: Response, ticker: str = Query(None)):
    data = {"valid": "true"}
    if (ticker is None):
        data["valid"] = "false"
        response.status_code = status.HTTP_422_UNPROCESSABLE_ENTITY
        return Response(content=json.dumps(data), media_type="application/json")
    price = database.get_current_stock_price(ticker)
    if price is None:
        data["valid"] = "false"
        return Response(content=json.dumps(data), media_type="application/json")
    data["price"] = price
    return Response(content=json.dumps(data), media_type="application/json")

# login_user header logs in user (returns the id) takes username and password as url params


@app.get("/login_user")
async def login_user(response: Response, username: str = Query(None), password: str = Query(None)):
    data = {"valid": "true"}
    if (username is None or password is None):
        data["valid"] = "false"
        response.status_code = status.HTTP_422_UNPROCESSABLE_ENTITY
        return Response(content=json.dumps(data), media_type="application/json")
    sessionKey = database.login_user(username, password)
    if sessionKey is None:
        data["valid"] = "false"
        return Response(content=json.dumps(data), media_type="application/json")
    data["sessionKey"] = sessionKey
    return Response(content=json.dumps(data), media_type="application/json")

# create_user header creates the user and logs them in (returns the id), takes a username and password as url params


@app.get("/create_user")
async def login_user(response: Response, username: str = Query(None), password: str = Query(None)):
    data = {"valid": "true"}
    if (username is None or password is None):
        data["valid"] = "false"
        response.status_code = status.HTTP_422_UNPROCESSABLE_ENTITY
        return Response(content=json.dumps(data), media_type="application/json")
    sessionKey = database.create_user(username, password)
    if sessionKey is None:
        data["valid"] = "false"
        return Response(content=json.dumps(data), media_type="application/json")
    data["sessionKey"] = sessionKey
    return Response(content=json.dumps(data), media_type="application/json")


# get_user_ticker_data headers returns all of the tickers a user owns, takes an id as url param
@app.get("/get_user_ticker_data")
async def get_user_ticker_data(response: Response, id: str = Query(None)):
    data = {"valid": "true"}
    if (id is None):
        data["valid"] = "false"
        response.status_code = status.HTTP_422_UNPROCESSABLE_ENTITY
        return Response(content=json.dumps(data), media_type="application/json")
    user_ticker_data = database.get_user_ticker_data(id)
    if user_ticker_data is None:  # id/username doesn't exist
        data["valid"] = "false"
        return Response(content=json.dumps(data), media_type="application/json")
    data["user_ticker_data"] = user_ticker_data
    return Response(content=json.dumps(data), media_type="application/json")
