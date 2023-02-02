/*
Name: Harguntas Singh Benipal
Teacher Name: Mr. Bains
Course : ICS4U0-A
Date: January 27, 2023
Description: This is the seePortfolio class that allows the user to look at the stocks they own, their number of shares, 
the total book value and current price. Additionally, it allows you to go to the graphs of each stock. 
*/

import javax.swing.*; //Import swing library

//Import exceptions
import javax.naming.AuthenticationException;
import java.io.IOException;
import java.net.MalformedURLException;
import org.json.simple.parser.ParseException;

//Allows for button events
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//Import libraries that allow for detail of GUI components
import java.text.DecimalFormat;
import javax.swing.border.LineBorder;
import java.awt.Font;
import java.awt.Color;

//Import libraries that allow for use of GUI elements like buttons and JTables
import javax.swing.table.DefaultTableModel;
import javax.swing.JButton;

public class seePortfolio implements ActionListener
{ //Name of class
	
	//Create main frame and old panel (old panel is used to set the older panel to false while mainFrame is the universal frame of the program)
	JFrame mainFrame;
	JPanel oldPanel;
	
	//Create portfolio panel (Used in this frame)
	private final JPanel portfolioPanel = new JPanel();
	
	//Create combo box for stocks
	private final JComboBox comboBox = new JComboBox();
	
	//Create table componenets
	DefaultTableModel tableModel;
	private JTable table;
	
	//Create goBack button
	JButton goBack = new JButton("Go Back");

	//Initializes the user_manager object which is used for all transactions, logging in, etc.
	user_manager manage;
	
	//Initializes the String "tickerBox" which is passed to seeStock class
	String tickerBox="";

	public seePortfolio (JFrame mainFrame, final JPanel mainPanel, user_manager manage) 
	{ //Constructor
		
		this.mainFrame = mainFrame; //Gets the mainFrame
		
		oldPanel = mainPanel; //Gets the mainPanel and makes it the oldPanel
		
		this.manage=manage;  //Gets the user_manager
	
		oldPanel.setVisible(false); //Sets the oldPanel to false
		
		//Details of portfolioPanel
		portfolioPanel.setBackground(new Color(255, 255, 255));
		portfolioPanel.setBounds(0, 0, 1257,642);
		portfolioPanel.setLayout(null);
		mainFrame.add(portfolioPanel); //Adds the portfolioPanel to the mainFrame
		
		//Details of goBack button
		goBack.setBackground(new Color(255, 255, 51));
		goBack.setFont(new Font("Franklin Gothic Medium", Font.BOLD | Font.ITALIC, 20));
		goBack.setBounds(48, 526, 159, 69);
		goBack.addActionListener(this);
		portfolioPanel.add(goBack); //Adds button to the portfolioPanel
	
		//Creates tableModel + Details
		tableModel = new DefaultTableModel();
		JTable table = new JTable(tableModel);
		table.setBackground(new Color(240, 255, 240));
		table.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		table.setFont(new Font("Segoe UI", Font.BOLD | Font.ITALIC, 17));
		table.setForeground(new Color(0, 0, 0));
		
	    //Adds columns to table
		tableModel.addColumn("Ticker");
		tableModel.addColumn("# of Shares");
		tableModel.addColumn("Book Value");
		tableModel.addColumn("Current Price");
		tableModel.addRow(new Object [] {"Ticker", "# of Shares", "Book Value","Current Price"}); //Adds row
		
		try //Try catch to catch MalformedURL, IO and parse exceptions
		{
			try //Try catch to catch authentication exception
			{
			
				String [][] arr = manage.get_user_ticker_data(); //Creates array to get the data from JSON Response
				for (int i =0; i<arr.length; i++) //Loops through array
				{
					String ticker = arr [i][1]; //Gets ticker from array
					String amount = arr [i][2]; //Gets amount from array
					
					Double currPrice = manage.get_current_stock_price(ticker); //Gets current price from user_manager
					
					Double bookVal = currPrice*(Double.parseDouble(amount)); //Gets book value
					
					//Gets current price and book value in money format
					DecimalFormat df = new DecimalFormat("0.00");
					String currentPrice = df.format(currPrice);
					String bookValue = df.format(bookVal);
					
					tableModel.addRow(new Object [] {ticker, amount, "$"+bookValue, "$"+currentPrice}); //Adds row to table
					comboBox.addItem(ticker); //Adds the ticker to combo box
				} //End of loop
				
		
			} catch (AuthenticationException e) //Catches the authentication exception
			{
				JOptionPane.showMessageDialog(null, "Please re-enter"); //Outputs error
			}
		} catch (MalformedURLException e) //Catches the MalformedURL exception
		{	
		 System.exit(0); //Exits program
		} 
		catch (IOException e) //Catches the IOException exception
		{
		 System.exit(0); //Exits program
		} 
		catch (ParseException e) //Catches the Parse exception
		{
		 System.exit(0); //Exits program
		}

		table.setBounds(608, 91, 546, 429);
		portfolioPanel.add(table); //Adds table to portfolioPanel
		
		//Details of comboBox
		comboBox.setFont(new Font("Yu Gothic UI", Font.BOLD, 20));
		comboBox.setBackground(new Color(255, 255, 255));
		comboBox.setBounds(376, 280, 111, 39);
		comboBox.addActionListener(this);
		portfolioPanel.add(comboBox); //Adds comboBox to portfolioPanel
	
		double balance = -1.0; //Intializes balance
		
		try //Try catch to catch MalformedURL, IO and parse exceptions 
		{
			try //Try catch to catch authentication exception
			{
				balance = manage.get_balance(); //Gets balance from user_manager
			} 
			catch(javax.naming.AuthenticationException f) //Catches the authentication exception
			{
				logOut bye = new logOut(); //Calls logOut class
			}
		} catch (java.net.MalformedURLException error) //Catches the MalformedURL exception
		{
			System.out.println(error.toString()); //Outputs error
			System.exit(0); //Exits program
		}
		catch (java.io.IOException error) //Catches the IOException exception
		{
			System.out.println(error.toString()); //Outputs error
			System.exit(0); //Exits program
		}
		catch (org.json.simple.parser.ParseException error) //Catches the Parse exception
		{
			System.out.println(error.toString()); //Outputs error
			System.exit(0); //Exits program
		}
	
		//Creates balanceLabel label + details
		JLabel balanceLabel = new JLabel("Balance: ");
		balanceLabel.setFont(new Font("Microsoft YaHei UI", Font.BOLD | Font.ITALIC, 20));
		balanceLabel.setBounds(270, 204, 94, 39);
		portfolioPanel.add(balanceLabel); //Adds label to portfolioPanel
	
		//Creates "showBalance" label + details
		DecimalFormat df = new DecimalFormat("0.00");
		JLabel showBalance = new JLabel("$"+df.format(balance));
		showBalance.setForeground(new Color(65, 105, 225));
		showBalance.setFont(new Font("Microsoft YaHei UI", Font.BOLD | Font.ITALIC, 20));
		showBalance.setBounds(374, 204, 178, 39);
		portfolioPanel.add(showBalance); //Adds label to portfolioPanel
	
		//Creates topPanel + details
		JPanel topPanel = new JPanel();
		topPanel.setBackground(new Color(255, 215, 0));
		topPanel.setBounds(195, 11, 875, 57);
		topPanel.setLayout(null);
		portfolioPanel.add(topPanel); //Adds topPanel to portfolioPanel
		
        //Creates portfolioLabel + details
		JLabel portfolioLabel = new JLabel("Portfolio");
		portfolioLabel.setFont(new Font("Franklin Gothic Medium", Font.BOLD | Font.ITALIC, 23));
		portfolioLabel.setBounds(389, 11, 189, 35);
		topPanel.add(portfolioLabel); //Adds label to topPanel
		
		mainFrame.setVisible(true); //Sets the mainFrame to true
	} //End of constructor
	
	
	@Override
	public void actionPerformed(ActionEvent e) //Action performed method
	{
		
		if (e.getSource () == goBack) //If the source is from the goBack button
		{
			portfolioPanel.setVisible(false); //Set the portfolioPanel to false
        	oldPanel.setVisible(true); //Set the oldPanel to true	
		} //End of if statement
        
		else if(e.getSource() == comboBox) //If the source is from the comboBox
		{
			//Gets the value of ticker in the comboBox
			tickerBox = (String) comboBox.getSelectedItem();
			
        	try //Try catch to catch MalformedURL, IO and parse exceptions 
        	{
        		portfolioPanel.setVisible(false); //Sets the portfolioPanel to false
        		seeStocks searching = new seeStocks(tickerBox, mainFrame, portfolioPanel, manage); //Creates the seeStocks object
    	       
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
		} //End of else if statement
        
        
	} //End of method
	
} //End of class