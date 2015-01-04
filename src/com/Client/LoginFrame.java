package com.Client;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;


/*For server to input the monitored port*/
public class LoginFrame extends JFrame {

	public JLabel nameLbl = null;
	public JLabel tipLbl = null;
	public JTextField nameField = null;
	public JLabel serverLbl = null;
	public JTextField serverField = null;
	public JButton loginButton = null;
	
	private  Client client = null;
	
	public LoginFrame() {
		this.setLocation(560,300);
		this.setSize(300,300);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(null);
		this.setResizable(false);
		
		nameLbl = new JLabel("User Name:");
		nameLbl.setLocation(20, 20);
		nameLbl.setSize(80, 20);
		
		tipLbl = new JLabel("<html>User name can only be chosen from the <br>following names: Alice, Bob, Candy, Dog, Egg.</html>");
		tipLbl.setSize(250, 100);
		tipLbl.setLocation(30, 80);
		
		nameField = new JTextField(15);
		nameField.setLocation(100, 20);
		nameField.setSize(100, 20);
		
		serverLbl = new JLabel("Server ip:");
		serverLbl.setSize(250, 100);
		serverLbl.setLocation(30, 10);
		
		serverField = new JTextField(15);
		serverField.setLocation(100, 50);
		serverField.setSize(100, 20);
		
		loginButton = new JButton("Log In");
		loginButton.setSize(50, 20);
		loginButton.setLocation(220, 20);
		
		this.add(nameLbl);
		this.add(nameField);
		this.add(loginButton);
		this.add(serverLbl);
		this.add(serverField);
		this.add(tipLbl);
		this.setVisible(true);
		
		loginButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String userNameStr = nameField.getText();
				if (userNameStr != "Alice" && userNameStr != "Bob" &&
					userNameStr != "Candy" && userNameStr != "Dog" && 
					userNameStr != "Egg" ) {
					tipLbl.setText("<html>Invalid User name! <br>User name can only be chosen from the <br>following names: Alice, Bob, Candy, Dog, Egg.</html>");
					return;
				}
				else {
						client = Client.getInstance();
						client.setServerIp(serverField.getText());
				}
			}
		});
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new LoginFrame();
			}
		});
	}
}
