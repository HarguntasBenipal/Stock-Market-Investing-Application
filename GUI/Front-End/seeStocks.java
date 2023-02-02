/*
Name: Harguntas Singh Benipal
Teacher Name: Mr. Bains
Course : ICS4U0-A
Date: January 27, 2023
Description: This is the seeStocks class. It allows the user to see the graphs of the stock. The domain can be changed:
It ranges from past week to max. Additionally, you can see the current price and trade the stock or go back.
*/

import javax.swing.*; //Import swing library

//Allows for button events
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//Import libraries that allow for detail of GUI components
import java.awt.Color;
import java.text.DecimalFormat;
import java.awt.Font;
import javax.swing.JFrame;  
import javax.swing.SwingUtilities; 

//Import libraries from org.jfree jar that allow to make charts and graphs
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.ChartFactory;  
import org.jfree.chart.ChartPanel;  
import org.jfree.chart.JFreeChart;  
import org.jfree.data.category.DefaultCategoryDataset;
import javax.swing.border.EtchedBorder;  
import javax.lang.model.element.PackageElement;


public class seeStocks implements ActionListener //Name of class
{ 
	
	//Create main frame and old panel (old panel is used to set the older panel to false while mainFrame is the universal frame of the program)
	JFrame mainFrame;
	JPanel oldPanel;

	//Create see panel (Used in this frame)
	private final JPanel seePanel = new JPanel();
	
	//Create combo box for date ranges
	private final JComboBox comboBox = new JComboBox();
	
	//Creating buttons to go back and trade
	JButton goBack = new JButton("Go Back");
	JButton clickBuying = new JButton("Trade");
	
	//Initializes the String "ticker" which is passed to tradeStock class
	String ticker;
	
	//Initializes the range for the graph
	String range="Past week";
	
	//Initializes the user_manager object which is used for all transactions, logging in, etc.
	user_manager manage;

	//Create the chart + chartpanel (Both are needed for making the graph) (I have 4 different graphs)
	JFreeChart chart1;
	ChartPanel graph1;
	
	JFreeChart chart2;
    ChartPanel graph2;
	
    JFreeChart chart3;
    ChartPanel graph3;
	  
    JFreeChart chart4;
    ChartPanel graph4;
	  
    //Creates top label
	private final JPanel topPanel = new JPanel();
	
	//Creates seeStockLabel
	private final JLabel seeStockLabel = new JLabel("Stock Details");
	  
	public seeStocks(final String ticker, final JFrame mainFrame, final JPanel panel, user_manager manage) throws java.net.MalformedURLException, java.io.IOException, org.json.simple.parser.ParseException, java.lang.NullPointerException, IllegalArgumentException 
	{ //Constructor
		
		this.manage=manage; //Gets the user_manager
		
		this.ticker=ticker; //Gets the ticker
		
		this.mainFrame=mainFrame; //Gets the mainFrame

		oldPanel = panel; //Gets the panel that was passed and makes it the oldPanel
		
		oldPanel.setVisible(false); //Sets the oldPanel to false
		
		//Details of seePanel
		seePanel.setBackground(new Color(255, 255, 255));
		seePanel.setBounds(0,0,2500,800);
		seePanel.setLayout(null); 
		mainFrame.add(seePanel); //Adds the seePanel to the mainFrame
		
		//Details of goBack button
		goBack.setFont(new Font("Franklin Gothic Medium", Font.BOLD | Font.ITALIC, 21));
		goBack.setBackground(new Color(255, 255, 102));
		goBack.setBounds(10, 508, 169, 83);
		goBack.addActionListener(this);
		seePanel.add(goBack); //Adds the button to the seePanel
		
		//Details of tickerLabel label
		JLabel tickerLabel = new JLabel("Ticker: "+this.ticker);
		tickerLabel.setForeground(new Color(102, 153, 255));
		tickerLabel.setBounds(518, 395, 183, 57);
		tickerLabel.setFont(new Font("Franklin Gothic Medium", Font.BOLD | Font.ITALIC, 20));
		tickerLabel.setBackground(Color.GREEN);
		seePanel.add(tickerLabel); //Adds label to seePanel
		
		//Details of clickBuying button
		clickBuying.setForeground(new Color(255, 255, 255));
		clickBuying.setFont(new Font("Segoe UI", Font.BOLD | Font.ITALIC, 22));
		clickBuying.setBounds(455, 485, 246, 83);
		clickBuying.addActionListener(this);
		clickBuying.setBackground(new Color(204, 102, 255));
		seePanel.add(clickBuying); //Adds button to seePanel
	
		//Created the graph
		XYDataset dataset = createDataset(range); //Calls the dataset method while passing the range to get the data for that range
		chart1 = ChartFactory.createTimeSeriesChart(ticker, "Date", "Price", dataset); //Creates a JFreeChart using the ticker, date, price and dataset
		XYPlot plot = (XYPlot) chart1.getPlot(); //Creates a plot using the chart
		plot.setBackgroundPaint(new Color(255,228,196)); //Sets background color
		graph1 = new ChartPanel(chart1); //Creates a new graph using the chart
		graph1.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		graph1.setBackground(new Color(255, 255, 255));
		graph1.setBounds(10, 129, 754, 255);
		seePanel.add(graph1); //Adds the graph to the seePanel

		//Details of comboBox (Gives ranges of dates)
		comboBox.setBackground(new Color(204, 255, 153));
		comboBox.setFont(new Font("Yu Gothic UI", Font.BOLD, 22));
		comboBox.setBounds(886, 129, 157, 34);
		comboBox.addItem("Past Week");
		comboBox.addItem("Past Month");
		comboBox.addItem("Past Year");
		comboBox.addItem("Max");
		comboBox.addActionListener(this);
		seePanel.add(comboBox); //Adds the comboBox to the seePanel
    
		//Details of "Current Stock Price" label
		JLabel currStockPriceLabel = new JLabel("Current Stock Price: ");
		currStockPriceLabel.setFont(new Font("Franklin Gothic Medium", Font.BOLD | Font.ITALIC, 19));
		currStockPriceLabel.setBounds(864, 267, 183, 34);
		seePanel.add(currStockPriceLabel); //Adds label to seePanel
    
		DecimalFormat df = new DecimalFormat("0.00"); //Money format
   
		try //Try catch to catch illegal argument exception
		{
			JLabel currStockPriceShow=new JLabel("$"+df.format(manage.get_current_stock_price(ticker))); //Gets the current stock price from user_manager
			currStockPriceShow.setForeground(new Color(0, 204, 102));
			currStockPriceShow.setFont(new Font("Segoe UI", Font.BOLD | Font.ITALIC, 20));
			currStockPriceShow.setBounds(1050, 265, 218, 34);
			seePanel.add(currStockPriceShow); //Adds label to seePanel
			
			mainFrame.add(seePanel); //Adds seePanel to mainFrame
		} catch (IllegalArgumentException e) //Catches the illegal argument exception
		{
			JOptionPane.showMessageDialog(null, "This ticker does not exist"); //Outputs the error
			mainFrame.setVisible(false);
		}
		
		//Details of topPanel
		topPanel.setBackground(new Color(255, 204, 51));
		topPanel.setBounds(155, 11, 954, 52);
		topPanel.setLayout(null);
		seePanel.add(topPanel); //Adds topPanel to seePanel
		
		//Details of seeStockLabel
		seeStockLabel.setFont(new Font("Franklin Gothic Medium", Font.BOLD | Font.ITALIC, 24));
		seeStockLabel.setBounds(383, 11, 190, 30);
		topPanel.add(seeStockLabel); //Adds to seePanel
		
		mainFrame.setVisible(true); //Sets the mainFrame to true
	} //End of Constructor
	
	
	private XYDataset createDataset(String range) 
	{  //createDataset method
	
		TimeSeriesCollection dataset = new TimeSeriesCollection(); //Creates time series collection dataset	 
		TimeSeries series1 = new TimeSeries("Series1");  //creates new time series
	 
		String rangeDays = "0"; //Initializes rangeDays
		
		if(range != null) //If the range is not null
		{
			if (range.equalsIgnoreCase("Past week")) //If range is past week
			{	 
				rangeDays="7"; 
			}
	 	
			else if (range.equalsIgnoreCase("Past Month")) //If range is past month
			{
				rangeDays = "30";
			}
	 
			else if (range.equalsIgnoreCase("Past Year")) //If range is past year
			{
				rangeDays = "365";
			}
	 
			else if (range.equalsIgnoreCase("Max")) //if range is max
			{
				rangeDays ="max";
			}
		}
   
		try //Try catch to catch MalformedURL, IO and parse exceptions 
		{
			try //Try catch to catch authentication exception
			{
				try //Try catch to catch illegal argument exception
				{
					String[][] data = manage.get_stock_history_by_ticker(ticker, rangeDays); //Get the stock history as JSON and convert to string
					for (int i = 0; i < data.length; i++) //Loop through array
					{
						String date = data[i][0]; //Get date
						String[] arr = date.split("-"); //Split date
				    	String year = arr[0]; //Get year
				    	String month = arr[1]; //Get month 
				    	String day = arr[2]; //Get day
				    	String ticker = data [i][1]; //Get ticker 
				    	String open = data [i][2]; //Get opening price
				    	String high = data [i][3]; //Get highest price
				    	String low = data [i][4]; //Get the lowest price
				    	String close = data [i][5]; //Get the closing price
				    	String volume = data [i][6]; //Get the volume of shares
				    	
				    	//Add the data to the time series
				    	series1.add(new Day(Integer.parseInt(day), Integer.parseInt(month), Integer.parseInt(year)), Double.valueOf(close));
				 } //End of for loop
					
				} catch (IllegalArgumentException e) //Catches the illegal argument exception
				{
					JOptionPane.showMessageDialog(null, "This ticker does not exist"); //Outputs error
					mainFrame.setVisible(false);
				}
			}
			catch(javax.naming.AuthenticationException f) //Catches the authentication exception
			{
				logOut bye = new logOut(); //Signs user out of program
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
	 
		dataset.addSeries(series1);  //Adds the time series to the dataset
 
		return dataset; //returns the dataset to the chart
	}  //End of method
	
	
	@Override
	public void actionPerformed(ActionEvent e) //Action performed method 
	{
	
        if (e.getSource () == goBack) //If the source is from the goBack button
        {
        	seePanel.setVisible(false); //Set the seePanel to false
        	oldPanel.setVisible(true);	//Set the oldPanel to true
        } //End of if statement
        
        else if (e.getSource () == clickBuying) //If the source is from the clickBuying button
        {
        	seePanel.setVisible(false); //Set the seePanel to false
			tradeStock buyings = new tradeStock(mainFrame, seePanel, ticker, manage); //Call the tradeStock method
		} //End of if statement
        
        else if (e.getSource() == comboBox) //If the source is from the comboBox (Changing the range)
        {
        	range = (String) comboBox.getSelectedItem(); //Get the range from the comboBox
        
        	if (range.equalsIgnoreCase("Past Week")) //If the range is the past week
        	{
        		//Created the graph
        		XYDataset dataset = createDataset(range); //Calls the dataset method while passing the range to get the data for that range
        		chart1 = ChartFactory.createTimeSeriesChart(ticker, "Date", "Price", dataset); //Creates a JFreeChart using the ticker, date, price and dataset    
        		XYPlot plot = (XYPlot) chart1.getPlot(); //Creates a plot using the chart
        		plot.setBackgroundPaint(new Color(255,228,196)); //Sets background color 
                  
        		graph1 = new ChartPanel(chart1); //Creates a new graph using the chart
        		graph1.setBounds(10, 129, 754, 255);
        		graph1.setVisible(true);
        		seePanel.add(graph1); //Adds the graph to the seePanel
        		mainFrame.add(seePanel); //Adds the seePanel to the mainFrame
        		
        		//Sets the rest of the graphs to false
        		graph2.setVisible(false); 
        		graph3.setVisible(false);
        		graph4.setVisible(false);
        	} //End of if
       
        	else if(range.equalsIgnoreCase("Past month")) //If the range is the past month
        	{
        		//Created the graph
        		XYDataset dataset = createDataset(range); //Calls the dataset method while passing the range to get the data for that range
        		chart2 = ChartFactory.createTimeSeriesChart(ticker, "Date", "Price", dataset); //Creates a JFreeChart using the ticker, date, price and dataset 
                XYPlot plot = (XYPlot) chart2.getPlot(); //Creates a plot using the chart
                plot.setBackgroundPaint(new Color(255,228,196)); //Sets background color 
               
                graph2 = new ChartPanel(chart2); //Creates a new graph using the chart
                graph2.setBounds(10, 129, 754, 255);
                graph2.setVisible(true);
                seePanel.add(graph2); //Adds the graph to the seePanel
                mainFrame.add(seePanel); //Adds the seePanel to the mainFrame
                
                //Sets the rest of the graphs to false
                graph1.setVisible(false);
                graph3.setVisible(false);
                graph4.setVisible(false);
            } //End of else if
        	
        	else if(range.equalsIgnoreCase("Past year")) //If the range is the past year
        	{
        		//Created the graph
        		XYDataset dataset = createDataset(range); //Calls the dataset method while passing the range to get the data for that range
        		chart3 = ChartFactory.createTimeSeriesChart(ticker, "Date", "Price", dataset);  //Creates a JFreeChart using the ticker, date, price and dataset
        		XYPlot plot = (XYPlot) chart3.getPlot(); //Creates a plot using the chart
        		plot.setBackgroundPaint(new Color(255,228,196)); //Sets background color  
                       
        		graph3 = new ChartPanel(chart3); //Creates a new graph using the chart
        		graph3.setBounds(10, 129, 754, 255);
        		graph3.setVisible(true);
        		seePanel.add(graph3); //Adds the graph to the seePanel
        		mainFrame.add(seePanel); //Adds the seePanel to the mainFrame
        		
        		//Sets the rest of the graphs to false
        		graph1.setVisible(false);
        		graph2.setVisible(false);
        		graph4.setVisible(false);
        	} //End of else if
                
        	else if(range.equalsIgnoreCase("Max")) //If the range is max
        	{
        		//Created the graph
        		XYDataset dataset = createDataset(range); //Calls the dataset method while passing the range to get the data for that range
        		chart4 = ChartFactory.createTimeSeriesChart(ticker, "Date", "Price", dataset); //Creates a JFreeChart using the ticker, date, price and dataset  
        		XYPlot plot = (XYPlot) chart4.getPlot(); //Creates a plot using the chart
        		plot.setBackgroundPaint(new Color(255,228,196)); //Sets background color  
                       
        		graph4 = new ChartPanel(chart4); //Creates a new graph using the chart
        		graph4.setBounds(10, 129, 754, 255);
        		graph4.setVisible(true);
        		seePanel.add(graph4); //Adds the graph to the seePanel
        		mainFrame.add(seePanel); //Adds the seePanel to the mainFrame
        		
        		//Sets the rest of the graphs to false
        		graph1.setVisible(false);
        		graph2.setVisible(false);
        		graph3.setVisible(false);
        	} //End of else if
        	
        } //End of else if
        
	} //End of method
} //End of class
