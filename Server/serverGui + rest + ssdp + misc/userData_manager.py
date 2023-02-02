# Robby Sodhi
# J.Bains
# 2023
# userData_manager is a child of SQliteWrapper
# this class manages all the interactions between the user_data.db


from SQLiteWrapper import SQLiteWrapper
import constants


class userData_manager(SQLiteWrapper):  # inherit SQLiteWrapper

    # create the user_data.db tables
    def create_database(self):
        self.execute(
            """
        CREATE TABLE IF NOT EXISTS user_pass_bal
                    (
                    username text UNIQUE NOT NULL,
                    password text NOT NULL,
                    balance numeric NOT NULL,
                    id TEXT DEFAULT (hex(randomblob(4))) PRIMARY KEY 
                    );
                    """
            # hex(randomblob(4)) creates the random unique ids whenever a user is created
        )

        self.execute(
            """
                    CREATE TABLE IF NOT EXISTS user_ticker
                    (
                    username text NOT NULL,
                    ticker text NOT NULL,
                    amount numeric NOT NULL,
                    PRIMARY KEY (username, ticker)
                    );
                    """
        )

    # constructor calls the superconstructor for sqliteWrapper
    def __init__(self, database_path):
        super().__init__(database_path)

    # Takes an id and returns the associated username
    def get_user_from_id(self, id):

        self.execute("SELECT username FROM user_pass_bal WHERE id=?", (id,))
        data = self.fetchone()
        if data is None:
            return None
        return data[0]

    # takes a username and password and if it doesn't already exist, create the user in the database
    def create_user(self, username, password):
        if (self.does_user_exist(username)):
            return None

        statement = "INSERT INTO user_pass_bal (username, password, balance) VALUES (LOWER(?), ?, ?)"

        self.execute(statement, (username, password,
                     constants.starting_balance))
        return self.login_user(username, password)  # when done, login the user

    # buys a stock for a user (adds the stock to the user_ticker table then subtracts the balance)
    def buy_stock(self, id, ticker, amount, stockPrice):
        username = self.get_user_from_id(id)
        if (username is None):
            return None
        # if invalid arguments
        if (amount <= 0 or stockPrice <= 0):
            return None
        # add some validation to check if ticker is real!!!

        balance = self.get_user_balance(username)

        cost = amount * stockPrice

        # if they can't afford it
        if (balance is None or balance <= 0 or balance < cost):
            return None

        # check here if the user already has an entry for a specific ticker, it should just update the amount, not create another entry
        data = self.get_user_ticker_data(id)
        already_exists = False
        if (not data is None):
            for user_ticker_data in data:
                if (user_ticker_data[1] == ticker):
                    data = user_ticker_data
                    already_exists = True
        # if they already have an entry for the stock, update it. Otherwise, create it
        if already_exists:
            self.execute("UPDATE user_ticker SET amount=? WHERE username=LOWER(?) AND ticker=?",
                         (data[2] + amount, username, ticker))
        else:
            self.execute("INSERT INTO user_ticker (username, ticker, amount) VALUES (LOWER(?), ?, ?)",
                         (username, ticker, amount))

        # update the users balance to reflect the amount they spent
        self.set_user_balance(username, balance - cost)

        return True

    # updates the user balance
    def set_user_balance(self, username, balance):
        self.execute(
            "UPDATE user_pass_bal SET balance=? WHERE username=LOWER(?)", (balance, username))

    # get the user balance
    def get_user_balance(self, username):
        if (not self.does_user_exist(username)):
            return None

        self.execute(
            "SELECT * FROM user_pass_bal WHERE username=LOWER(?)", (username, ))
        data = self.fetchone()
        return data[2]

    # checks if a user exists
    def does_user_exist(self, username):
        statement = "SELECT * FROM user_pass_bal WHERE username=LOWER(?)"
        self.execute(statement, (username,))
        data = self.fetchone()
        if data is None:
            return None
        return True

    # get  all of the tickers a user owns
    def get_user_ticker_data(self, id):
        username = self.get_user_from_id(id)
        if (username is None):
            return None
        statement = "SELECT * FROM user_ticker WHERE username=LOWER(?)"
        self.execute(statement, (username, ))
        data = self.fetchall()
        return data

    # sell a stock
    def sell_stock(self, id, ticker, sellPrice, sellAmount):
        username = self.get_user_from_id(id)
        if (username is None):
            return None
        # get all stocks they own
        user_data = self.get_user_ticker_data(id)
        user_data = constants.find_first_occurence_in_2D_Array(
            user_data, ticker)
        if (user_data is None):
            return None
        num_shares = user_data[2]

        # if they don't own the amount they're trying to sell
        if (num_shares < sellAmount):
            return None

        # modify the amount
        self.execute(
            "UPDATE user_ticker SET amount=? WHERE username=LOWER(?) AND ticker=?", (num_shares - sellAmount, username, ticker))

        # update balance
        self.set_user_balance(username, self.get_user_balance(
            username) + sellAmount * sellPrice)

        return True

    # logs in user (gets their unique id)
    def login_user(self, username, password):
        if (not self.does_user_exist(username)):
            return None

        self.execute(
            "SELECT id FROM user_pass_bal WHERE username=LOWER(?) and password=?", (username, password))

        data = self.fetchone()
        if (data is None):
            return None

        return data[0]  # username
