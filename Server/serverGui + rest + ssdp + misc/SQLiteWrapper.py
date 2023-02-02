# Robby Sodhi
# J.Bains
# 2023
# parent class for stockData_manager and userData_manager
# provide us with some generic methods we can use to talk to a sqlite3 database


import sqlite3


class SQLiteWrapper:

    # keep track of whether this is the first run of this class (or its children)
    has_been_instantiated = False
    # we only want to create the database files once

    # this is meant to be overriden by children, so if it is not, throw an exception
    def create_database():
        raise Exception("Create_database() must be overriden")

    # constructor, takes our db file i.e user_data.db
    def __init__(self, db_file):
        # get database connection(finds the database) and cursor (allows us to traverse/access the database)
        # check_same_thread=False makes it thread safe, incase we wanted multiple rest workers (we only have one because I couldn't get it to work with pyinstaller, so that flag doesn't matter)
        self.conn = sqlite3.connect(db_file, check_same_thread=False)
        self.cursor = self.conn.cursor()
        # print("connected to database, initiated cursor")

        # if this is the first time running, create the database
        if not self.has_been_instantiated:
            # print("Trying to create database tables...")
            self.create_database()

            self.has_been_instantiated = True

    # this class is meant to be used within a context manager (with statemnet), this is what happens when it starts
    # it creates a database transaction, this is meant to ensure safety incase multiple people are accessing the database
    def __enter__(self):
        self.begin()  # begin the transaction
        return self

    def __exit__(self, exc_type, exc_value, traceback):
        # exc_type is the exception type (if there was an error thrown within the context manager (with statement))
        if exc_type is None:
            # print("transaction successful")
            self.commit()  # complete the transaction (this is when all of the changes to the database actually happen)
        else:
            # print("transaction failed, rolling back")
            self.conn.rollback()  # revert the transaction (because an error occured)
        # print("Close db cursor, close db conn")
        self.cursor.close()  # close the cursor
        self.conn.close()  # close the connection

    # begin transactions
    def begin(self):
        self.execute("BEGIN")
        # print("transaction began")

    # commit(end) transactions
    def commit(self):
        self.execute("COMMIT")
       # print("transaction committed")

    # execute a sql statement with a single set of values
    def execute(self, statement, bind_vars=None):
        # bind_vars hold our values
        # statement holds the sql statement to execute
        if bind_vars is None:
            self.cursor.execute(statement)
        else:
            if isinstance(bind_vars, tuple):
                self.cursor.execute(statement, bind_vars)
            else:
                raise ValueError("bind_vars must be a tuple")

    # execute a sql statement with multiple sets of values
    def executemany(self, statement, bind_vars):
        if isinstance(bind_vars, list) and isinstance(bind_vars[0], tuple):
            self.cursor.executemany(statement, bind_vars)
        else:
            raise ValueError(
                "bind_vars must be a list and its data must be a tuple")

    # fetch all or one of the data after a statement(Used after select statements to get the data we queried)

    def fetchall(self):
        return self.cursor.fetchall()

    def fetchone(self):
        return self.cursor.fetchone()
