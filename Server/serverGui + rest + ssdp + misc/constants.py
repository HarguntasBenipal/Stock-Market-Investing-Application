 # Robby Sodhi
 # J.Bains
 # 2023
 # Constants file, useful methods/variables needed throughout the program

import datetime

stock_data_database_path = "stock_data.db"
user_data_database_path = "user_data.db"

date_format = "%Y-%m-%d"

starting_balance = 50000


def getCurrentDate(format):
    # get the current time, turn it into a format string, then convert it back to a date time object (erases the time, we just want date)
    return datetime.datetime.strptime(datetime.datetime.now().strftime(format), format)


def find_first_occurence_in_2D_Array(arr, value):
    for i in arr:
        for x in i:
            if (x == value):
                return i
    return None
