/*
Name: Harguntas Singh Benipal
Teacher Name: Mr. Bains
Course : ICS4U0-A
Date: January 27, 2023
Description: This is the tradeStock class that allows the user to buy or sell stocks. It also performs error checking. 
*/

import javax.swing.*; //Import swing library

//Allows for button events
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

//Import libraries that allow for detail of GUI components
import java.awt.Font;
import java.awt.Color;

public class tradeStock implements ActionListener 
{ //Name of Class
	
	//Create main frame and old panel (old panel is used to set the older panel to false while mainFrame is the universal frame of the program)
	JFrame mainFrame;
	JPanel oldPanel;
	
	//Create trade and top panel (Used in this frame)
	private final JPanel tradePanel = new JPanel();
	private final JPanel topPanel = new JPanel();
	
	//Creates text fields and combo box for getting ticker, number of shares and buy or sell
	private JTextField txtTicker;
	private JTextField txtNumShares;
	private final JComboBox comboBox = new JComboBox();
	
	//Creates buttons for completing transaction and going back
	JButton goBack = new JButton("Go Back");
	JButton tradingStocks = new JButton("Complete Transaction");

	//Creates label at top
	private final JLabel tradeStocksLabel = new JLabel("Trade Stocks");
	
	//Initializes the user_manager object which is used for all transactions, logging in, etc.
	user_manager manage;
	
	//Initializes the String "ticker" which is passed from seeStock
	String ticker;
	
	//Initializes the String "buyOrSell" which is used to see whether to buy or sell
	static String buyOrSell;
	
	public tradeStock(JFrame mainFrame, final JPanel seePanel, String ticker, user_manager manage) 
	{ //Constructor for values of the seeStock class
		this(mainFrame, seePanel, manage); //Gets values of mainFrame, seePanel and manage from seeStock
		this.ticker=ticker; //Gets value of ticker
		
		txtTicker.setText(ticker); //Sets textfield text to the ticker
	} //End of constructor
	
	public tradeStock(JFrame mainFrame, final JPanel mainPanel, user_manager manage) 
	{ //Constructor for values of MainOptions class
		
		this.mainFrame = mainFrame; //Gets the mainFrame
		
		oldPanel = mainPanel; //Gets the mainPanel and makes it the oldPanel
		
		this.manage=manage; //Gets the user_manager
		
		oldPanel.setVisible(false); //Sets the oldPanel to false
		
		//Details of tradePanel
		tradePanel.setBackground(new Color(255, 255, 255));
		tradePanel.setBounds(0, 0, 1257,642);
		tradePanel.setLayout(null);
		mainFrame.add(tradePanel); //Adds the tradePanel to the mainFrame
		
		//Details of goBack button
		goBack.setBackground(new Color(255, 255, 51));
		goBack.setFont(new Font("Franklin Gothic Medium", Font.BOLD | Font.ITALIC, 20));
		goBack.addActionListener(this);
		goBack.setBounds(73, 518, 150, 67);
		tradePanel.add(goBack); //Adds button to the tradePanel
		
		//Details of tradingStocks button
		tradingStocks.setBackground(new Color(204, 255, 153));
		tradingStocks.setFont(new Font("Franklin Gothic Medium", Font.BOLD | Font.ITALIC, 21));
		tradingStocks.setBounds(426, 315, 248, 119);
		tradingStocks.addActionListener(this);
		tradePanel.add(tradingStocks); //Adds button to the tradePanel
		
		//Creates "tickerLabel" label + Details
		JLabel tickerLabel = new JLabel("Ticker");
		tickerLabel.setFont(new Font("Yu Gothic UI", Font.BOLD | Font.ITALIC, 22));
		tickerLabel.setBounds(440, 106, 89, 39);
		tradePanel.add(tickerLabel); //Adds label to tradePanel
		
		//Details of txtTicker textfield
		txtTicker = new JTextField();
		txtTicker.setFont(new Font("Segoe UI", Font.BOLD | Font.ITALIC, 18));
		txtTicker.setBounds(532, 112, 124, 33);
		txtTicker.setColumns(10);
		tradePanel.add(txtTicker); //Adds textfield to tradePanel
		
		//Creates "numSharesLabel" label + Details
		JLabel numSharesLabel = new JLabel("Number of Shares");
		numSharesLabel.setFont(new Font("Yu Gothic UI", Font.BOLD | Font.ITALIC, 18));
		numSharesLabel.setBounds(355, 159, 186, 39);
		tradePanel.add(numSharesLabel); //Adds label to tradePanel
		
		//Details of txtNumShares textfield
		txtNumShares = new JTextField();
		txtNumShares.setBounds(535, 169, 121, 26);
		txtNumShares.setColumns(10);
		tradePanel.add(txtNumShares); //Adds textfield to tradePanel
		
		//Details of comboBox
		comboBox.setFont(new Font("Segoe UI Light", Font.BOLD | Font.ITALIC, 18));
		comboBox.setBounds(554, 223, 89, 34);
		comboBox.addItem("Buy");
		comboBox.addItem("Sell");
		comboBox.addActionListener(this);
		tradePanel.add(comboBox); //Adds comboBox to tradePanel
		
		//Details of topPanel
		topPanel.setBackground(new Color(255, 204, 51));
		topPanel.setBounds(178, 11, 916, 52);
		topPanel.setLayout(null);
		tradePanel.add(topPanel); //Adds topPanel to tradePanel
		
		//Details of "Trade Stocks" label
		tradeStocksLabel.setFont(new Font("Franklin Gothic Medium", Font.BOLD | Font.ITALIC, 28));
		tradeStocksLabel.setBounds(355, 11, 252, 30);
		topPanel.add(tradeStocksLabel); //Adds label to topPanel
		
		mainFrame.setVisible(true); //Sets the mainFrame visible
	} //End of constructor
	
	
	@Override
	public void actionPerformed(ActionEvent e) //Action performed method 
	{

		if (e.getSource () == goBack) //If the source is from the goBack button
		{
			tradePanel.setVisible(false); //Set the tradePanel to false
			oldPanel.setVisible(true); //Set the oldPanel to true	
        } //End of if statement
        
        else if (e.getSource () == tradingStocks) //If the source is from the tradingStocks button
        {
        	//Initializes variables
        	int count=0;
        	int count1=0;
        	int count2=0;
        	int count3=0;
        	String getTicker; 
        	
        	if (txtTicker.getText().isBlank()) //Checks if the ticker is blank or not
        	{
        		JOptionPane.showMessageDialog(null, "Please enter a ticker");
        		count2++;
        	}
        	
        	getTicker= txtTicker.getText().toUpperCase(); //Sets the ticker to uppercase
        	char[] ch = getTicker.toCharArray(); //Converts to char array and checks if there are any numbers
        	for (char c : ch) 
        	{
        		if(!Character.isLetter(c)) 
        		{
        			count++; //Adds 1 to the count
        		}	   
        	}
           
        	if(count>=1) //If the count is greater than or equal to one
        	{
        		JOptionPane.showMessageDialog(null, "Please write the ticker in the correct format");
        	}
        		 
        	if (txtNumShares.getText().isBlank()) //Checks if the checker is blank or not
        	{
        		JOptionPane.showMessageDialog(null, "Please enter the number of shares you want to buy/sell");
        		count3++;
        	}
         		
        	int numShare=0;
        	String numSharesTest;
        	numSharesTest = txtNumShares.getText(); //Gets the number of shares
        	char[] ch1 = numSharesTest.toCharArray(); //Converts to array to check if there are any letters
        	for (char c : ch1) 
        	{
        		if(!Character.isDigit(c)) 
        		{
        			count1++; //Adds one to count1
        		}   
        	}
         	
        	if (count1>=1) //If count1 is greater than or equal to one
        	{
        		JOptionPane.showMessageDialog(null, "Please write the number of shares in the correct format");
         	}
        	
        	if(count==0&&count1==0&&count2==0&&count3==0) //If it passes all the tests
        	{
        		numShare = Integer.parseInt(txtNumShares.getText()); //Gets the number of shares
        		String numShares = Integer.toString(numShare); //Converts to a string
        		String buyOrSell = (String) comboBox.getSelectedItem(); //Gets whether to buy or sell
        		if(buyOrSell.equalsIgnoreCase("buy")) //If buy
        		{
        			buyOrSell = "buy";
        			
        			try //Try catch to catch MalformedURL, IO and parse exceptions 
        			{
        				try //Try catch to catch authentication exception
        				{
        					try //Try catch to catch illegal argument exception
        					{
        						manage.buy_stock(getTicker, numShares); //Tells user_manager to buy stock
        						JOptionPane.showMessageDialog(null, "Your transaction was sucessful!");
        					} catch (IllegalArgumentException error) //Catches the illegal argument exception
        					{
        						JOptionPane.showMessageDialog(null, "This ticker does not exist"); //Outputs error
        					}
        				
        				} catch (javax.naming.AuthenticationException f) //Catches the authentication exception
        				{
        					JOptionPane.showMessageDialog(null, "Please re-enter"); //Outputs error
                        }
        			}catch (java.net.MalformedURLException error) //Catches the MalformedURL exception
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
                   
        		} //End of smaller if statement
        			
        		else if (buyOrSell.equalsIgnoreCase("sell")) //If buy
        		{
        			buyOrSell = "sell";
        			
        			try //Try catch to catch MalformedURL, IO and parse exceptions 
        			{
        				try //Try catch to catch authentication exception
        				{
        					try //Try catch to catch illegal argument exception
        					{
        						manage.sell_stock(getTicker, numShares); //Tells user_manager to sell stock
        						JOptionPane.showMessageDialog(null, "Your transaction was sucessful!");
        					} catch (IllegalArgumentException error) //Catches the illegal argument exception
        					{
        						JOptionPane.showMessageDialog(null, "This ticker does not exist"); //Outputs error
        					}
           
        				} catch (javax.naming.AuthenticationException f) //Catches the authentication exception
        				{
        					JOptionPane.showMessageDialog(null, "Please re-enter"); //Outputs error
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
        			catch (org.json.simple.parser.ParseException error) //Catches the Parse exception
        			{
        				System.out.println(error.toString()); //Prints error to console
        				System.exit(0); //Exits program
        			}
         		     
        		} //End of smaller else if statement		
			
        	} //End of if statement 
        	
        } //End of else if statement
        
        else if (e.getSource() == comboBox) //If the source is from the comboBox
        {  
        	
        } //End of else if statement
	
	} //End of method
	
} //End of class