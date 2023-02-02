# Robby Sodhi
# J.Bains
# 2023
# server manager class
# this handles starting and stopping the ssdp and uvicorn(rest/fastapi) servers

import socket
from multiprocessing import Process, freeze_support
from ssdpy import SSDPServer
import uvicorn
from uvicornServer import uvicornServer


class serverManager:

    uvicornServerManager = None
    SSDPProcess = None

    isRunning = False

    def __init__(self):
        freeze_support()  # run this so uvicorn can be compiled to an exe

    # returns the computers ipv4 address and a random free port (we use this to start the server on a free port)
    def get_location(self):
        # get free port
        sock = socket.socket()
        sock.bind(("", 0))
        port = sock.getsockname()[1]
        sock.close()

        # get ipvr4
        hostName = socket.gethostname()
        ipAddress = socket.gethostbyname(hostName)

        # return ipv4 + free port
        location = (ipAddress, port, hostName)
        return location

    # starts the ssdp server as its own process (so it runs in the background)
    def start_SSDP_process(self, location):
        ipAddress, port, hostName = location

        service_type = "Robby-Harguntas-Stock-Server"

        # the location is the link to the rest api (ipvr + free port)
        # ssdp runs on port 1900 which will automatically be avaiable on windows, so no need to get two free ports
        location = "http://" + str(ipAddress) + ":" + str(port)
        # create server instance from SSDPServer library (the headers need to match what we are looking for in the java discovery client)
        server = SSDPServer(
            "name:{name}::urn:{domainName}:service:{serviceType}:{ver}".format(
                name=hostName,
                domainName="schemas-upnp-org",
                serviceType=service_type,
                ver="1",
            ),
            device_type="ssdp:" + service_type,
            location=location,
        )

        # create a process to run the SSDP server
        # set it daemon true so it closes when our main thread closes
        self.SSDPProcess = Process(target=server.serve_forever)
        self.SSDPProcess.daemon = True

        # start the process (which runs ssdpServer.serve_forever())
        self.SSDPProcess.start()

    # start the uvicorn server (uvicorn is an ASGI, a program that manages and runs our rest api (fastapi) )
    def start_uvicorn_process(self, location):
        ipAddress, port, hostName = location  # get the ipv4 and free port

        # configure uvicorn accordingly
        config = uvicorn.Config(
            "rest:app",
            host=str(ipAddress),
            port=int(port),
            reload=False,
            # more than one worker does not work with pyinstaller (compiling to exe)
            workers=1,
        )
        # start the uvicorn (rest) server
        self.uvicornServerManager = uvicornServer(config=config)

        self.uvicornServerManager.start()

    # we need to make sure that the SSDP server and the uvicorn(rest) server run and close together, so we have start and stop methods which ensure that
    def start(self):
        self.isRunning = True

        location = self.get_location()
        self.start_uvicorn_process(location)
        self.start_SSDP_process(location)

    def stop(self):
        self.isRunning = False

        self.SSDPProcess.terminate()
        self.SSDPProcess.join()

        self.uvicornServerManager.stop()
