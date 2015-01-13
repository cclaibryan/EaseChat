package com.Client;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

import com.Common.Msg;

public class SingleChatFrame extends JFrame {

	public JTextField txtField;
	public JTextArea txtArea;
	public JScrollPane scrollPane;
	
	String userName;	//my name
	int userPort;		//my port
	String peerName;	//peer's name
	String peerIp;		//peer's ip
	int peerPort = 0;	//peer's port
	
	UdpClient client;
	boolean infoIsSent = false;
	
	public SingleChatFrame(String userName, String peerName, String peerIp, int port) {
		super(String.format("%s---->%s", userName,peerName));
		this.userName = userName;	//my name
		this.userPort = port;		//my port
		this.peerName = peerName;	//peer's name
		this.peerIp = peerIp;		//peer's ip
		
		client = new UdpClient(userName,userPort,peerName,peerIp,0,this);
		this.setMainFrame();
	}
	
	private void setMainFrame() {
		this.setLocation(560,300);
		this.setSize(450,300);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(null);
		
		txtField = new JTextField();
		txtField.setSize(452, 30);
		txtField.setLocation(0, 250);
		
		txtArea = new JTextArea();
		txtArea.setSize(100, 100);
		txtArea.setLocation(0, 0);
		txtArea.setRows(15);
		txtArea.setAutoscrolls(true);
		txtArea.setEditable(false);
		
		scrollPane = new JScrollPane(txtArea);
		scrollPane.setLocation(0, 0);
		scrollPane.setSize(450, 250);
		scrollPane.setBorder(new LineBorder(Color.black));
		
		this.add(scrollPane);
		this.add(txtField);
		this.setResizable(false);
		this.setVisible(true);
		
		txtField.requestFocus();	//txtField gets the focus
		
		this.txtField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String contentString = txtField.getText();
				client.sendMsg(contentString);
				txtField.setText("");
				txtArea.setText(txtArea.getText() + userName + ":\n" + contentString + "\n");
			}
		});
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				System.exit(0);
			}
		});
	}
	
	void setPeerPort(int port) {
		this.peerPort = port;
		client.peerPort = port;
	}
}
