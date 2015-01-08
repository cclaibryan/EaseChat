package com.Client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.EOFException;
import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;

import javax.management.modelmbean.ModelMBean;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import com.Common.ClientInfo;
import com.Server.Server;

/*The main chatting frame*/
public class GroupChatFrame extends JFrame {
	
	public JList nameList = null;
	public JTextField txtField = null;
	public JTextArea txtArea = null;
	public JScrollPane scrollPane = null;
	
	private TcpClient client;
	
	private String userName = null;		//my name
	
	
	
	public GroupChatFrame(String name,TcpClient client) {
		super(name);
		
		this.setLocation(560,300);
		this.setSize(450,300);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(null);
		
		this.userName = name;
		this.client = client;
		
		this.setMainFrame();
		
	}
	
	private void setMainFrame() {
		txtField = new JTextField();
		txtField.setSize(352, 30);
		txtField.setLocation(100, 250);
		
		txtArea = new JTextArea();
		txtArea.setSize(100, 100);
		txtArea.setLocation(0, 0);
		txtArea.setRows(15);
		txtArea.validate();
		txtArea.setAutoscrolls(true);
		txtArea.setEditable(false);
		
		nameList = new JList(client.nameListModel);
		nameList.setSize(100,300);
		nameList.setLocation(0, 0);
		nameList.setBorder(new LineBorder(Color.black));
		
		scrollPane = new JScrollPane(txtArea);
		scrollPane.setLocation(100, 0);
		scrollPane.setSize(350, 250);
		scrollPane.setBorder(new LineBorder(Color.black));
		
		this.add(nameList);
		this.add(scrollPane);
		this.add(txtField);
		this.setResizable(false);
		this.setVisible(true);
		
		txtField.requestFocus();	//txtField gets the focus
		txtField.addActionListener(new TFListener());
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				client.disconnect();
				System.exit(0);
			}
		});
	}
	
	public class TFListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String str = txtField.getText().trim();
			txtField.setText("");
			//txtArea.setText(txtArea.getText() + userName + ":\n" + str + '\n');
			client.sendMsg(str,userName);
		}
	}
}
