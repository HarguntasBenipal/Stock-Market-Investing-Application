# Robby Sodhi
# J.Bains
# 2023
# handles writing a database to a csv file
# this creates another thread to ensure that the main program doesn't freeze while it is doing this
# I also made sure to only load one row of the database into memory at once, so it shouldn't use too many system resources


import sqlite3
import csv


def __dump_database_to_csv(database_path):
    # Connect to the database
    conn = sqlite3.connect(database_path)
    c = conn.cursor()

    # Get all table names
    c.execute("SELECT name FROM sqlite_master WHERE type='table';")
    table_names = [x[0] for x in c.fetchall()]

    # go through each table and write each row into a csv file
    for table_name in table_names:
        with open(f"{table_name}.csv", "w", newline="") as f:
            writer = csv.writer(f)

            # Write the table headers
            c.execute(f"PRAGMA table_info({table_name})")
            headers = [x[1] for x in c.fetchall()]
            writer.writerow(headers)

            # Write the table rows using the iterator to avoid loading all data into memory
            c.execute(f"SELECT * FROM {table_name}")
            for row in c:
                writer.writerow(row)

    # Close the connection
    conn.close()

    print("Done dumping")


def dump_database_to_csv(database_path):
    # create a thread and then run the previous method in that thread to ensure the main program doesn't freeze
    import threading

    t = threading.Thread(target=__dump_database_to_csv, args=(database_path,))
    t.start()
