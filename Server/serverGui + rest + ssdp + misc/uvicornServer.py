# Robby Sodhi
# J.Bains
# 2023
# class that lets us start the uvicorn server (fastapi, rest) as its own thread (so it doesn't override the mainthread, gui)

import threading
import uvicorn


class uvicornServer(uvicorn.Server):
    def start(self):
        self.thread = threading.Thread(target=self.run)
        self.thread.start()

    def stop(self):
        self.should_exit = True
        self.thread.join()
