/*
Name: Harguntas Singh Benipal
Teacher Name: Mr. Bains
Course : ICS4U0-A
Date: January 27, 2023
Description: This is the searchStock class. It allows the user to search for any stock using it's ticker. You can choose
from a list of tickers or enter your own. It then brings you to the seeStock frame/class. 
*/

import javax.swing.*; //Import swing library

//Import libraries that allow for detail of GUI components
import java.awt.Color;
import java.awt.Font;

//Allows for button events
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class searchStock implements ActionListener
{ //Name of class
	
	//Create main frame and old panel (old panel is used to set the older panel to false while mainFrame is the universal frame of the program)
	JFrame mainFrame;
	JPanel oldPanel;
	
	//Create search panel (Used in this frame)
	private final JPanel searchPanel = new JPanel();
	
	//Create combo box for tickers
	private final JComboBox comboBox = new JComboBox();
	
	//Create textfield for entering the ticker
	private JTextField txtTicker;
	
	//Creating buttons to go back and search
	JButton goBack = new JButton("Go Back");
	JButton clickSearch = new JButton("Search");
	
	//Initializes the String "ticker" which is passed to seeStock class
	String ticker;

	//Initializes the seeStocks object
	seeStocks see;
	
	//Initializes the user_manager object which is used for all transactions, logging in, etc.
	user_manager manage;
	
	//Creates search stock label 
	private final JLabel searchStocksLabel = new JLabel("Search Stocks");
	
	public searchStock(final JFrame mainFrame, final JPanel mainPanel, user_manager manage) 
	{ //Constructor
		
		this.mainFrame=mainFrame; //Gets the mainFrame
		
		oldPanel = mainPanel; //Gets the mainPanel and makes it the oldPanel
		
		this.manage = manage; //Gets the user_manager
		
		oldPanel.setVisible(false); //Sets the oldPanel to false
		
		//Details of searchPanel
	    searchPanel.setBackground(new Color(255, 255, 255));
	    searchPanel.setBounds(0, 0, 1257,642);
		searchPanel.setLayout(null);
		mainFrame.add(searchPanel); //Adds the searchPanel to the mainFrame
		
		//Creates the ticker label + details
		JLabel tickerLabel = new JLabel("Ticker:");
		tickerLabel.setBounds(449, 137, 78, 22);
		tickerLabel.setFont(new Font("Segoe UI", Font.BOLD | Font.ITALIC, 21));
		searchPanel.add(tickerLabel); //Adds the label to the searchPanel
		
		//Creates the txtTicker textfield + details
		txtTicker = new JTextField();
		txtTicker.setBackground(new Color(204, 255, 204));
		txtTicker.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 18));
		txtTicker.setBounds(537, 131, 166, 35);
		txtTicker.setColumns(10);
		searchPanel.add(txtTicker); //Adds the textfield to the searchPanel
		searchPanel.setVisible(true);
		
		//Details of goBack button
		goBack.setBackground(new Color(255, 255, 0));
		goBack.setFont(new Font("Franklin Gothic Medium", Font.BOLD | Font.ITALIC, 18));
		goBack.setBounds(63, 498, 154, 89);
		goBack.addActionListener(this);
		searchPanel.add(goBack); //Adds the button to the searchPanel
		
		//Details of clickSearch button
		clickSearch.setForeground(new Color(255, 255, 255));
		clickSearch.setBackground(new Color(153, 0, 255));
		clickSearch.setFont(new Font("Segoe UI Black", Font.BOLD | Font.ITALIC, 21));
		clickSearch.setBounds(550, 255, 154, 97);
		clickSearch.addActionListener(this); 
		searchPanel.add(clickSearch); //Adds the button to the searchPanel
		
		//Details of comboBox
		comboBox.setForeground(new Color(0, 0, 0));
		comboBox.setBounds(579, 192, 89, 22);
		comboBox.setBackground(new Color(255, 255, 255));
		comboBox.setFont(new Font("Segoe UI", Font.BOLD | Font.ITALIC, 18));
		//Adds some tickers from online
		comboBox.addItem("AAPL");
		comboBox.addItem("IBM");
		comboBox.addItem("NVDA");
		comboBox.addItem("AMZN");
		comboBox.addItem("MSFT");
		comboBox.addItem("TSLA");
		comboBox.addItem("AMD");
		comboBox.addItem("PFE");
		comboBox.addItem("F");
		comboBox.addItem("AAL");
		comboBox.addItem("SOFI");
		comboBox.addItem("INTC");
		comboBox.addItem("GOOGL");
		comboBox.addItem("META");
		comboBox.addItem("WBD");
		comboBox.addItem("WFC");
		comboBox.addItem("LYFT");
		comboBox.addActionListener(this);
		searchPanel.add(comboBox); //Adds comboBox to searchPanel
	
		//Creates topPanel + details
		JPanel topPanel = new JPanel();
		topPanel.setBackground(Color.ORANGE);
		topPanel.setBounds(120, 11, 1021, 56);
		topPanel.setLayout(null);
		searchPanel.add(topPanel); //Adds the topPanel to the searchPanel
		
		//Details of the label 
		searchStocksLabel.setFont(new Font("Franklin Gothic Medium", Font.BOLD | Font.ITALIC, 28));
		searchStocksLabel.setBounds(407, 11, 229, 34);
		topPanel.add(searchStocksLabel); //Adds the label to the topPanel
		
		mainFrame.setVisible(true); //Sets the mainFrame to true
	} //End of constructor
	
	
	@Override
	public void actionPerformed(ActionEvent e) //Action performed method
	{

        if (e.getSource () == goBack) //If the source is from the goBack button
        {
        	searchPanel.setVisible(false); //Set the searchPanel to false
			oldPanel.setVisible(true); //Set the oldPanel to true
        } //End of if statement
        
        else if (e.getSource () == clickSearch) //If the source is from the clickSearch button
        {
        	//Initializes variables
        	int count=0;
        	int count1=0;
        	
        	if (txtTicker.getText().isBlank()) 
        	{//Checks if the ticker is blank or not
        		JOptionPane.showMessageDialog(null, "Please enter a ticker");
        		count++;
        	}
        	
        	ticker = txtTicker.getText().toUpperCase(); //Sets the ticker to uppercase that was entered by the user
      
        	char[] ch = ticker.toCharArray(); //Converts to char array and checks if there are any numbers
        	for (char c : ch) 
        	{
        		if(!Character.isLetter(c)) 
        		{
        			count1++; //Adds 1 to the count1
        		}		   
        	}
        	
        	if(count1>=1) //If the count1 is greater than or equal to one
        	{
        		JOptionPane.showMessageDialog(null,  "Please enter the ticker in the correct format");
        	}
            
        	else if(count==0&&count1==0) 
        	{ //If it passes all the tests
        		
        		try //Try catch to catch MalformedURL, IO and parse exceptions  
        		{
        			try //Try catch to catch illegal argument exception
        			{
        				manage.get_current_stock_price(ticker); //Gets the current stock price from the user_manager
        				searchPanel.setVisible(false); //Sets the searchPanel to false
        				see = new seeStocks(ticker, mainFrame, searchPanel, manage); //Calls the seeStocks class
        			} catch (IllegalArgumentException error) //Catches the illegal argument exception
        			{
        			JOptionPane.showMessageDialog(null, "This ticker does not exist"); //Outputs error
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
        	} //End of else if
        } //End of else if
        
        else if (e.getSource() == comboBox) //If the source is from the comboBox
        {

        	ticker = (String) comboBox.getSelectedItem(); //Gets the ticker selected from the comboBox
        	
        	try //Try catch to catch MalformedURL, IO and parse exceptions
        	{
        		searchPanel.setVisible(false); //Sets the searchPanel to false
        		see = new seeStocks(ticker, mainFrame, searchPanel, manage); //Calls the seeStocks calls
	   
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
    		catch (org.json.simple.parser.ParseException error) //Catches the Parse exception
        	{
    			System.out.println(error.toString()); //Prints error to console 
    			System.exit(0); //Exits program 
    		}
        	
        } //End of else if
	} //End of Method
	
} //End of class

