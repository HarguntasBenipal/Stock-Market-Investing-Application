# Stock-Market-Investing-Application

Requirements:
-Java(TM) SE Runtime Environment 17.0.5 (newer ones would probably work as well, haven’t tested though)

-stockMarketServer.exe (the server)

-finalStockMarket.jar (the frontend)

How to run:
Open stockMarketServer.exe and allow full firewall permissions if prompted. Download the server here (Google Drive will give you a message that it is dangerous for the computer, ignore that and download it anyway): https://drive.google.com/drive/folders/1aW3-vAzrrse72EbDhDheMdZMD77nBdsf?usp=sharing

Start server from the GUI menu. This will automatically create user_data and stock_data database files in the directory. If deleted, simply relaunch the server to recreate them.
In the GUI, you can start/stop servers and dump databases to excel CSV. (allows you to read user credentials, user balance/owned-stocks and subsequently what stocks are cached) Note: server must be off for database dumping.
Open finalStockMarket.jar (frontend) and select the server to connect to. Multiple servers can be used on different devices and the frontend will display all of them for your choosing. (Try it out, find another device on your network, run the stockMarketServer.exe on it and restart the frontend and you should be able to see all of the servers) Each server will have its own user data. An example of where multiple servers would be useful would be multiple classrooms in the same school network, a student could note which server is their teacher and subsequently only use that one. (I was planning to make a teacher dashboard to manage student data, did not have time)
Create an account. Login credentials, stocks, and balance are stored in the database for future use. (if the server and frontend are closed, as long as the user_data.db remains, you will be able to start the server again, connect to it and log in with the exact same details)
Trade stocks(buy/sell), see portfolio(what you own), balance, net profit, and price history of stocks(see stocks button).
Explore the frontend and try different stock tickers (not just from the provided list, you can input your own into the search box. Anything on yahoo finance is a valid ticker)
On the graph page, use the mouse to highlight and zoom in on sections of the graph for more granular control compared to the past week/month options.
There is error checking for everything, so you can’t buy non-existing stocks, can’t buy stocks if you don’t have enough money, sell stocks you don’t own, etc. I recommend just exploring the frontend and trying a bunch of different things. 


Read the README for the frontend here: https://docs.google.com/document/d/1hSSPlbb99h2WC_5vxPWmB-HhZLGZ3c77/edit?usp=sharing&ouid=113822872269965528703&rtpof=true&sd=true

Read the README for the backend and serer here: https://docs.google.com/document/d/1OiXEdArQS9MadkQ1e-wFep8V3Km_JaIl/edit?usp=sharing&ouid=113822872269965528703&rtpof=true&sd=true


Example of program:


https://user-images.githubusercontent.com/115516035/216447328-01630862-3a41-4d25-b6e1-8b3a62a2fc6a.mp4

