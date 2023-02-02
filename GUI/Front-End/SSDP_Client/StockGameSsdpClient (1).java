/*
Name: Harguntas Singh Benipal
Teacher Name: Mr. Bains
Course : ICS4U0-A
Date: January 27, 2023
Description: This is the StockGameSsdpClient class that asks the user to choose which server they want. It then brings 
them to the login page.
*/

import javax.swing.*; //Imports swing library that allows you to use GUI elements
import java.awt.event.ActionEvent; //Imports action event library in AWT
import java.awt.event.ActionListener; //Add action listeners for buttons
import java.io.IOException;
import java.util.ArrayList; //Allows you to use resizable arrays for the servers
import java.util.LinkedHashMap; //ALllows you to use Hash Maps for finding the servers
import java.awt.Font; //Allows you to change the font of the GUI elements
import java.awt.Color; //Action events to perform actions


public class StockGameSsdpClient implements ActionListener { //Name of class 

	 private JFrame frame = new JFrame (); //Creates JFrame
	 private JButton button;
	 
	 public StockGameSsdpClient() throws java.io.IOException //Constructor
	 {
		 
		//Details of frame
        frame.getContentPane().setBackground(new Color(255, 255, 255));
        frame.setBackground(new Color(255, 255, 255));
        frame.setSize(589,363);
        frame.setTitle("Stock Market Program");
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        
        //Creates JLabel
        JLabel label = new JLabel ("Looking for servers...");
        label.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 40));
        label.setBounds(102, 120, 441, 61);
        frame.add(label);
        frame.setVisible(true);
        
        //Creates a SSDP_Client object to get server
        SSDP_Client client = new SSDP_Client();
        client.setUserAgent("StockGameClient");
        client.setSt("ssdp:Robby-Harguntas-Stock-Server");
        
        //Creates ArrayList based on the response
        ArrayList<LinkedHashMap<String, String>> responses = client.send();
        
        if (responses.size()==0) 
        {//If there is no response, do this
    		JOptionPane.showMessageDialog(null, "Please restart the server, no servers were found");
    		button = new JButton ("Retry");
    		button.setBounds(50, 75, 150, 50);
    		frame.add(button);
    		button.addActionListener(this);
        }
        
        //Creates a JButton array based on size of response to create that many buttons
        JButton[] arr = new JButton[responses.size()];
        
        int count = 0;
        
        //Loop that goes through all the responses
        for (LinkedHashMap<String, String> stockService : responses)
        {
        	//Gets the location and usn of the server
        	String location = stockService.get("LOCATION"); 
        	String usn = stockService.get("USN");
        	
        	//Creates a new JButton for each server
        	arr[count] = new JButton();
        	arr[count].setBounds(50,75 * count,490,50);
        	arr[count].setText(usn + " " + location);
        	arr[count].addActionListener(this);
        	
        	//Adds the button to the frame
        	frame.add(arr[count]);
        	
        	count++;
        } //End of loop
        
        frame.remove(label); //Remove the label saying "searching for servers" after servers are found
        frame.repaint(); //Repaints the frame to show the buttons
	 } //End of constructor
    
    @Override
	public void actionPerformed(ActionEvent e) //Action performed method 
    {
	 if(e.getSource()== button) {
		 try {
			 frame.setVisible(false);
			new StockGameSsdpClient(); //Run the class again
		} catch (IOException e1) {
			System.exit(0);
		}
		 
	 }
    	JButton btn = (JButton)e.getSource(); //Gets the source of the button
    	
    	frame.setVisible(false); //Sets the frame to false
    	
    	//Splits the details of the server to get the url
    	String text = btn.getText(); 
    	String[] parts = text.split(" ");
    	String url = parts[1];
    	new loginInfo(url); //Calls the loginInfo class while passing the url 
    }  //End of method
       
} //End of class
    

   
    


