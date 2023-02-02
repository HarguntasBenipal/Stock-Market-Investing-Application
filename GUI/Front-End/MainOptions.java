/*
Name: Harguntas Singh Benipal
Teacher Name: Mr. Bains
Course : ICS4U0-A
Date: January 27, 2023
Description: This is the MainOptions class that is that main screen for the program. It holds buttons that link to the 
rest of the classes (Trade Stock, See Portfolio and Search Stock). It also allows you to log out. 
*/

import javax.swing.*; //Import swing library

//Import exceptions
import javax.naming.AuthenticationException;
import org.json.simple.parser.ParseException;
import java.io.IOException;
import java.net.MalformedURLException;

//Import libraries that allow for detail of GUI components
import java.awt.Font;
import java.awt.Color;
import java.awt.Button;

//Allows for button events
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

//Allows for decimal format in numbers ($)
import java.text.DecimalFormat;

public class MainOptions implements ActionListener { //Name of Class
	
	//Create main frame and panel
	static JFrame mainFrame = new JFrame();
	static JPanel mainPanel = new JPanel();
	
	//Create buttons
	JButton tradeStocks = new JButton("Trade Stocks");
	JButton openPortfolio = new JButton("See portfolio");
	JButton searchStocks = new JButton("See Stocks");
	JButton loggingOut = new JButton("Logout");

	//Initializes the user_manager object which is used for all transactions, logging in, etc.
	protected user_manager manage;
	
	//Initializes the String "username" which is passed from loginInfo 
	protected String username;
	
	//Intitialize balance variables
	private Double startingBalance = 50000.00;
	private Double userBalance = -1.00;
	private Double netProfit=0.0;
		
	public MainOptions (user_manager manage, final String username,final String password)
	{ //Constructor
		
		this.manage=manage; //Gets the user_manager
		this.username=username; //Gets the username
		
		try //Try catch to catch MalformedURL, IO and parse exceptions
		{
			try //Try catch to catch authentication exception
			{
				userBalance = manage.get_balance(); //Gets the current balance of the user
			} catch(javax.naming.AuthenticationException f) //Catches the authentication exception
			{
				logOut bye = new logOut(); //Logs out
			}
		} catch (java.net.MalformedURLException error) //Catches the MalformedURL exception
		{
			System.out.println(error.toString()); //Prints error to console
			System.exit(0); //Exits program
		}
		catch (java.io.IOException error) //Catches the IOException exception
		{
			System.out.println(error.toString()); //Prints error to console
			System.exit(0); //Exits program
		}
		catch(org.json.simple.parser.ParseException error) //Catches the Parse exception
		{
			System.out.println(error.toString()); //Prints error to console
			System.exit(0); //Exits program
		}
		
		//Details of mainFrame
		mainFrame.setTitle("Stock Market Program");
		mainFrame.setSize(1257,642);
		mainFrame.setLayout(null);
		
	    //Details of mainPanel
		mainPanel.setBackground(new Color(255, 255, 255));
		mainPanel.setBounds(0, 0, 1243, 616);
		mainPanel.setLayout(null);
		mainFrame.add(mainPanel);//Adds the mainPanel to the mainFrame
		
		//Creates "topPanel" + Details 
		JPanel topPanel = new JPanel();
		topPanel.setBackground(Color.ORANGE);
		topPanel.setBounds(296, 11, 820, 55);
		topPanel.setLayout(null);
		mainPanel.add(topPanel); //Adds the topPanel to the MainPanel
		
	    //Creates "sidePanel" + Details
		JPanel sidePanel = new JPanel();
		sidePanel.setBackground(new Color(102, 51, 255));
		sidePanel.setBounds(0, 0, 169, 605);
		sidePanel.setLayout(null); 
		mainPanel.add(sidePanel); //Adds the sidePanel to the mainPanel
		
		//Details of tradeStocks button
		tradeStocks.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 15));
		tradeStocks.setForeground(Color.WHITE);
		tradeStocks.setBackground(new Color(133, 94, 255));
		tradeStocks.setBounds(0, 11, 169, 82);
		sidePanel.add(tradeStocks); //Adds tradeStocks button to the sidePanel
		
		//Details of openPortfolio button
		openPortfolio.setForeground(Color.WHITE);
		openPortfolio.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 15));
		openPortfolio.setBackground(new Color(123, 79, 255));
		openPortfolio.setBounds(0, 115, 169, 90);
		sidePanel.add(openPortfolio); //Adds openPortfolio button to the sidePanel
		
		//Details of searchStocks button
		searchStocks.setForeground(Color.WHITE);
		searchStocks.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 15));
		searchStocks.setBackground(new Color(110, 54, 255));
		searchStocks.setBounds(0, 233, 169, 90);
		sidePanel.add(searchStocks); //Adds searchStocks button to the sidePanel
		
		//Details of loggingOut button
		loggingOut.setForeground(Color.WHITE);
		loggingOut.setBackground(new Color(174, 94, 255));
		loggingOut.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 14));
		loggingOut.setBounds(28, 368, 113, 44);
		sidePanel.add(loggingOut); //Adds loggingOut button to the sidePanel
		
		//Adds action listener to all the buttons
		loggingOut.addActionListener(this);
		searchStocks.addActionListener(this);
		openPortfolio.addActionListener(this);
		tradeStocks.addActionListener(this);
		
		//Creates "welcome" label + details
		JLabel welcomeLabel = new JLabel("Welcome, "+username);
		welcomeLabel.setFont(new Font("Segoe UI Black", Font.BOLD | Font.ITALIC, 18));
		welcomeLabel.setBounds(281, 11, 209, 33);
		topPanel.add(welcomeLabel); //Adds the label to the topPanel
		
		//Creates "Current Balance" label + details
		JLabel currBalanceLabel = new JLabel("Current Balance:");
		currBalanceLabel.setFont(new Font("Franklin Gothic Medium", Font.BOLD | Font.ITALIC, 18));
		currBalanceLabel.setBounds(296, 203, 160, 44);
		mainPanel.add(currBalanceLabel); //Adds the label to the mainPanel
		
		DecimalFormat df = new DecimalFormat("0.00"); //DecimalFormat object to display money amounts
		
		//Creates the currBalanceShow label that displays the current balance + details
		JLabel currBalanceShow = new JLabel("$"+df.format(userBalance)); 
		currBalanceShow.setForeground(new Color(107, 142, 35));
		currBalanceShow.setFont(new Font("Segoe UI Black", Font.BOLD | Font.ITALIC, 20));
		currBalanceShow.setBounds(466, 208, 308, 31);
		mainPanel.add(currBalanceShow); //Adds the label to the mainPanel
		
		//Creates "Starting Balance" label + details
		JLabel startBalanceLabel = new JLabel("Starting Balance:");
		startBalanceLabel.setFont(new Font("Franklin Gothic Medium", Font.BOLD | Font.ITALIC, 18));
		startBalanceLabel.setBounds(296, 137, 181, 21);
		mainPanel.add(startBalanceLabel); //Adds the label to the mainPanel
		
		//Creates the startBalanceShow label that displays the starting balance + details
		JLabel startBalanceShow = new JLabel("$"+df.format(startingBalance));
		startBalanceShow.setForeground(new Color(30, 144, 255));
		startBalanceShow.setFont(new Font("Segoe UI Black", Font.BOLD | Font.ITALIC, 20));
		startBalanceShow.setBounds(469, 130, 124, 31);
		mainPanel.add(startBalanceShow); //Adds the label to the mainPanel

		//Creates "Net Profit" label + details
		JLabel netProfitLabel = new JLabel("Net Profit:");
		netProfitLabel.setFont(new Font("Franklin Gothic Medium", Font.BOLD | Font.ITALIC, 18));
		netProfitLabel.setBounds(296, 290, 181, 21);
		mainPanel.add(netProfitLabel); //Adds the label to the mainPanel
		
		//Creates the netProfitShow label that displays the net Profit + details
		netProfit = userBalance-startingBalance; //Calculates the net profit
		JLabel netProfitShow = new JLabel("$"+df.format(netProfit));
		netProfitShow.setFont(new Font("Segoe UI Black", Font.BOLD | Font.ITALIC, 20));
		netProfitShow.setBounds(469, 283, 124, 31);
		
		if(netProfit<0.00) //If the profit is negative, display in red colour
		{
			netProfitShow.setForeground(new Color(255, 0, 0));
		}
		
		else if (netProfit>0.00) //If the profit is positive, display in green colour (If it is exactly 0.00, then it is black)
		{
			netProfitShow.setForeground(new Color(107, 142, 35));
		}
		
		mainPanel.add(netProfitShow); //Adds the label to the mainPanel
		
		//Creates the introduction label + details
		JLabel introLabel = new JLabel("Introduction:");
		introLabel.setForeground(new Color(147, 112, 219));
		introLabel.setFont(new Font("Franklin Gothic Medium", Font.BOLD | Font.ITALIC, 18));
		introLabel.setBounds(862, 137, 181, 21);
		mainPanel.add(introLabel); //Adds the label to the mainPanel
		
		//Creates the introduction text that describes the program + details
		JTextArea txtWelcomeToProgram = new JTextArea();
		txtWelcomeToProgram.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 17));
		txtWelcomeToProgram.setText("Welcome to Robby Sodhi's and Harguntas Benipal's stock \r\nmarket program! You can use this to learn basic investing \r\nfundamentals through trading stocks. Additionally, you can\r\naccess your portfolio to see your stocks and balance.\r\nThanks, ");
		txtWelcomeToProgram.setBounds(703, 169, 479, 128);
		mainPanel.add(txtWelcomeToProgram); //Adds the text to the mainPanel
		
		//Creates the label for "Robby" + details
		JLabel robbySodhi = new JLabel("Robby Sodhi");
		robbySodhi.setForeground(new Color(184, 134, 11));
		robbySodhi.setFont(new Font("Microsoft YaHei UI", Font.ITALIC, 15));
		robbySodhi.setBounds(703, 291, 124, 23);
		mainPanel.add(robbySodhi); //Adds the label to the mainPanel
		
		//Creates the label for "Harguntas" + details
		JLabel harguntasSinghBenipal = new JLabel("Harguntas Singh Benipal");
		harguntasSinghBenipal.setForeground(new Color(255, 140, 0));
		harguntasSinghBenipal.setFont(new Font("Microsoft YaHei UI", Font.ITALIC, 15));
		harguntasSinghBenipal.setBounds(703, 325, 187, 21);
		mainPanel.add(harguntasSinghBenipal); //Adds the label to the mainPanel
		
		mainFrame.setVisible(true); //Sets the mainFrame to true
	} //End of constructor
	
	
	@Override
	public void actionPerformed(ActionEvent e) //Action performed method 
	{

		if (e.getSource () == tradeStocks) //If the source is from the tradeStocks button
        {
			tradeStock trade = new tradeStock(mainFrame,mainPanel, manage); //Creates the trade object to trade Stocks (Calls the tradeStock class)
        } //End of if statement
        
        else if (e.getSource () == openPortfolio) //If the source is from the openPortfolio button
        {
        	seePortfolio seePort= new seePortfolio(mainFrame, mainPanel, manage); //Creates the seePort object to see portfolio (Calls the seePortfolio class)	
		} //End of else if statement
			
	    else if (e.getSource () == searchStocks) //If the source is from the searchStocks button
	    {	
			searchStock search = new searchStock(mainFrame,mainPanel, manage); //Creates the search object to search Stocks (Calls the searchStock class)	
		} //End of else if statement
        
	    else if (e.getSource() == loggingOut) //If the source is from the loggingOut button
	    {
	    	logOut bye = new logOut(); //Creates the bye object to log out (Calls the logOut class)
	    } //End of else if statement
        
	} //End of method
	
} //End of class

