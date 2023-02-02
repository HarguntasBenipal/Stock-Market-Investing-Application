# Robby Sodhi
# J.Bains
# 2023
# this is a small gui program that allows a user to start the server(rest + ssdp), stop the server and dump the databases to a csv


import tkinter as tk  # gui library
from serverManager import serverManager
from dump_database_to_csv import dump_database_to_csv
from ctypes import windll
# this is just a fix for my laptop monitor, because my laptop uses fractional scaling, text can sometimes become fuzzy. This allows text to resize as it needs to ensure it looks crisp on my laptop.
windll.shcore.SetProcessDpiAwareness(1)

# class to represent our main gui window


class App:
    # constructor for tkinter
    def __init__(self, master):
        # my server manager class, manages the SSDP and uvicorn(rest) servers
        self.server = serverManager()

        self.master = master
        self.create_widgets()

    # creates all of the components for the gui (called in the constructor)
    def create_widgets(self):

        # create start, stop and dump buttons (also set their size and what their command (method when pressed) is)
        self.start_button = tk.Button(
            self.master, text="Start Server", command=self.start_server
        )
        self.start_button.grid(row=0, column=0, sticky="ew", padx=10, pady=10)

        self.stop_button = tk.Button(
            self.master, text="Stop Server", command=self.stop_server
        )
        self.stop_button.grid(row=0, column=1, sticky="ew", padx=10, pady=10)

        self.dump_button = tk.Button(
            self.master, text="Dump Database to CSV", command=self.dump_database
        )
        self.dump_button.grid(row=0, column=2, sticky="ew", padx=10, pady=10)

        # create our optionmenu component with the options of stock_data.db and user_data.db. Default is stock_data.db
        self.database = tk.StringVar(self.master)
        self.database.set("stock_data.db")  # default value
        self.option_menu = tk.OptionMenu(
            self.master, self.database, "stock_data.db", "user_data.db"
        )

        # add text labels to explain that there is a dropdown menu (because it really is not easy to tell and I am too lazy to make it look better)
        self.label = tk.Label(self.master, text="Dropdown menu ->")
        self.label.grid(row=2, column=1, sticky="ew", padx=10, pady=10)

        self.option_menu.grid(row=2, column=2, sticky="ew", padx=10, pady=10)

        # Update the state of the buttons initially
        self.update_buttons()

    # ran when the start server button is pressed
    def start_server(self):
        print("Starting server...")
        self.server.start()  # calls start in our server manager class

        self.update_buttons()  # updates the state of the buttons (when u start the server, the start button greys and the stop can now be pressed)

    # ran when the stop server button is pressed
    def stop_server(self):

        print("Stopping server...")
        self.server.stop()

        self.update_buttons()

    # ran when the dump database button is pressed
    def dump_database(self):
        from constants import stock_data_database_path, user_data_database_path

        # get the selected database (dropdown menu)
        selected_database = self.database.get()

        # call the dump method with correct argument
        print("Dumping database to CSV...")
        if selected_database == "stock_data.db":
            dump_database_to_csv(stock_data_database_path)
        elif selected_database == "user_data.db":
            dump_database_to_csv(user_data_database_path)

    # updates the state of the buttons (ensures that u cant start the server twice , stop the server when its not running and dump the database to csv while it is in use)
    def update_buttons(self):
        if self.server.isRunning:
            self.dump_button.configure(state="disabled")
            self.start_button.configure(state="disabled")
            self.stop_button.configure(state="active")
        else:
            self.dump_button.configure(state="active")
            self.start_button.configure(state="active")
            self.stop_button.configure(state="disabled")

    # ran when the gui closes (just runs the stop_server method(same as the button), so our database doesn't get corrupted or anything)
    def on_close(self):
        if self.server.isRunning:
            # Stop the server when the window is closed
            self.stop_server()
        self.master.destroy()


# if __name__=="main", is basically pythons equivalent of a main (you can run code without it, but its best practice to use it as it makes it clear where the program starts)
if __name__ == "__main__":
    root = tk.Tk()
    root.geometry("650x150")  # Set the  size of the window
    # set the name of the window
    root.title("Robby-Harguntas Stock simulator server manager")
    app = App(root)
    root.protocol(
        "WM_DELETE_WINDOW", app.on_close
    )  # Set the on_close method to be called when the window is closed
    root.resizable(False, False)
    root.mainloop()  # start the gui
