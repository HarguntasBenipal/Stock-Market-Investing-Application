/*
Name: Harguntas Singh Benipal
Teacher Name: Mr. Bains
Course : ICS4U0-A
Date: January 27, 2023
Description: This is the loginInfo class that makes the user create a new account or login with a old one into the program. 
It then calls the mainOptions class.
*/

import javax.swing.*;  //Import swing library
import java.awt.Font; //Allows me to choose whatever font I want
import java.awt.Color; //Add colour to GUI elements
import javax.swing.border.SoftBevelBorder; //Add a soft bevel border 
import javax.swing.border.BevelBorder; //Add a normal bevel border
import java.awt.event.ActionListener; //Add action listeners for buttons
import java.awt.event.WindowEvent; //Window events to perform actions
import java.awt.event.ActionEvent; //Action events to perform actions

public class loginInfo implements ActionListener { //Name of class
	
	//Creates the username and password that the user will enter when logging in/creating a account
	static String userNameEntered;
	static String passWordEntered;
	
	//Creates the loginFrame
	JFrame loginFrame = new JFrame();
	
	//Creates buttons for signing up and logging in
	JButton logIn = new JButton("Login");
	JButton signUp = new JButton("Sign Up");
	
	//Creates textfield and password field for writing the username and password
	JPasswordField txtPassword = new JPasswordField();
	JTextField txtUsername = new JTextField();
	
	//Initializes the String "location" which is passed from StockGameSsdpClient and then to user_manager for logging in
    protected String location;
	
	//Initializes the user_manager object which is used for all transactions, logging in, etc.
	protected user_manager manage;
	
	public loginInfo (String location) { //Constructor
		
		this.location=location; //Gets the location
		
		//Details of loginFrame
		loginFrame.setFont(new Font("Segoe UI Symbol", Font.BOLD, 27));
		loginFrame.setTitle("Stock Market");
		loginFrame.setSize(597,348);
		loginFrame.setResizable(false);
		loginFrame.setLocationRelativeTo(null);
		loginFrame.setLayout(null);
		
		//Creates sidePanel + details
		JPanel sidePanel = new JPanel();
		sidePanel.setBounds(0, 0, 274, 311);
		sidePanel.setBackground(new Color(54, 33, 89));
		sidePanel.setLayout(null);
		loginFrame.add(sidePanel);
		
		//Creates loginPanel + details
		JPanel loginPanel = new JPanel();
		loginPanel.setBackground(Color.WHITE);
		loginPanel.setBounds(171, 0, 565, 391);
		loginPanel.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		loginPanel.setLayout(null);
		loginFrame.add(loginPanel);
		
		//Creates JLabel + details
		JLabel programTitle1 = new JLabel("Robby and Harguntas's");
		programTitle1.setForeground(Color.WHITE);
		programTitle1.setBounds(20, 11, 254, 28);
		programTitle1.setFont(new Font("Microsoft YaHei", Font.BOLD | Font.ITALIC, 20));
		sidePanel.add(programTitle1);
		
		//Creates JLabel + details
		JLabel programTitle2 = new JLabel("Stock Market Program");
		programTitle2.setForeground(Color.BLACK);
		programTitle2.setBounds(126, 11, 575, 32);
		programTitle2.setFont(new Font("Microsoft YaHei", Font.BOLD | Font.ITALIC, 21));
		loginPanel.add(programTitle2);
		
		//Details of username textfield
		txtUsername.setText("Username");
		txtUsername.setBorder(null);
		txtUsername.setBounds(176, 54, 96, 20);
		txtUsername.setBackground(Color.WHITE);
		txtUsername.setColumns(10);
		loginPanel.add(txtUsername);
	
		//Details of password field
		txtPassword.setText("Password");
		txtPassword.setBorder(null);
		txtPassword.setBounds(176, 104, 125, 20);
		loginPanel.add(txtPassword);
		
		//Creates lines under the text fields
		JLabel line1 = new JLabel("________________________");
		line1.setBounds(176, 68, 172, 14);
		loginPanel.add(line1);
				
		JLabel line2 = new JLabel("________________________");
		line2.setBounds(176, 122, 172, 14);
		loginPanel.add(line2);
		
		//Details of sign up button
		signUp.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 15));
		signUp.setBounds(242, 166, 149, 23);
		signUp.setBackground(new Color(224, 255, 255));
		signUp.addActionListener(this);
		loginPanel.add(signUp);
		
		//Details of log in button
		logIn.setBounds(126, 166, 112, 23);
		logIn.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 15));
		logIn.setBackground(new Color(245, 222, 179));
		logIn.addActionListener(this);
		loginPanel.add(logIn);
		
		//Sets the loginFrame to true
		loginFrame.setVisible(true);
	} //End of constructor

	
	public void login_signup(String username, String password, Boolean create) 
	{ //login_singup method that get the username and password plus whether or not the account was created
		
		try //Try catch to catch MalformedURL, IO and parse exceptions
		{
			
			try //Try catch to catch authentication exception if the username and password entered are wrong
			{
				manage = new user_manager (location, username, password, create); //Passes variables to user_manager object
				loginFrame.setVisible(false); //If it works, set the frame to false
				loginFrame.dispatchEvent(new WindowEvent(loginFrame, WindowEvent.WINDOW_CLOSING)); //Closes the frame
				MainOptions mainScreen = new MainOptions(manage, userNameEntered, passWordEntered); //Creates the MainOptions object after logging in
			} catch (javax.naming.AuthenticationException f) //Catches the authentication exception
			{
			 JOptionPane.showMessageDialog(null, "The username and password did not match?");
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
	
	} //End of method

	
	@Override
	public void actionPerformed(ActionEvent e) //Action performed method   
	{ 

		if (e.getSource () == signUp) //If the source is from the sign up button
		{
			
			userNameEntered = txtUsername.getText(); //Get the value of the username
			passWordEntered = String.valueOf(txtPassword.getPassword()); //Get the value of the password (It is set of chars so you have to parse to string)
			
			if(userNameEntered.matches(".*\\s.*")||userNameEntered.equalsIgnoreCase("username")||passWordEntered.matches(".*\\\\s.*")||passWordEntered.equalsIgnoreCase("password")) 
			{ //If statement if the username and password are not changed
				JOptionPane.showMessageDialog(null, "No whitespace in user/password and username != \"username\" and password != \"password\"");
			} 
			
			else 
			{		
				login_signup(userNameEntered, passWordEntered, true); //If the above is not true, call the login_signup method to sign up (Notice how the boolean value is true, confirming that the user signed up)
			}

        } //End of if statement
 
        else if (e.getSource () == logIn) //If the source is from the log in button
        {
        	userNameEntered = txtUsername.getText(); //Get the value of the username
			passWordEntered = String.valueOf(txtPassword.getPassword()); //Get the value of the password (It is set of chars so you have to parse to string)
		
			if(userNameEntered.matches(".*\\s.*")||userNameEntered.equalsIgnoreCase("username")||passWordEntered.matches(".*\\\\s.*")||passWordEntered.equalsIgnoreCase("password")) 
			{ //If statement if the username and password are not changed
				JOptionPane.showMessageDialog(null, "No whitespace in user/password and username != \"username\" and password != \"password\"");
			} 
			
			else 
			{	
				login_signup(userNameEntered, passWordEntered, false); //If the above is not true, call the login_signup method to sign up (Notice how the boolean value is false, confirming that the user didn't sign up)
			}
			     
		} //End of else if statement
      
	} //End of method 
	
} //End of class
