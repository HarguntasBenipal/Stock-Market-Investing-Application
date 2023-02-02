/*
Name: Harguntas Singh Benipal
Teacher Name: Mr. Bains
Course : ICS4U0-A
Date: January 27, 2023
Description: This is the main class and start of the stock market program. It calls the StockGameSsdpClient class to 
get the server.
*/

public class Main //Name of class
{ 
	
	public static void main(String[] args) throws java.net.MalformedURLException, java.io.IOException, org.json.simple.parser.ParseException //Main Method (Throws exceptions due to StockGameSsdpClient using JSON libraries) 
	{
		
		new StockGameSsdpClient(); //Calls StockGameSsdpClient class to get server
		
	} //End of main
} //End of class
