package com.Client;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.net.SocketException;

import javax.swing.*;

import com.Server.Server;

/*The main chatting frame*/
public class BasicFrame extends JFrame {
	
	public JTextField txtField = null;
	public JTextArea txtArea = null;
	private Client client;
	
	public BasicFrame() {
		client = Client.getInstance();
		
		this.setLocation(560,300);
		this.setSize(300,300);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
		
		this.setMainFrame();
	}
	
	private void setMainFrame() {
		txtField = new JTextField();
		txtArea = new JTextArea();
		
		txtArea.setRows(15);
		txtArea.setAutoscrolls(true);
		txtArea.setEditable(false);
		
		this.add(new JScrollPane(txtArea),BorderLayout.NORTH);
		this.add(txtField,BorderLayout.SOUTH);
		this.setResizable(false);
		this.setVisible(true);
		
		txtField.requestFocus();	//txtField gets the focus
		txtField.addActionListener(new TFListener());
	}
	
	public class TFListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String str = txtField.getText().trim();
			txtField.setText("");
			txtArea.setText(txtArea.getText() + "Alice:\n" + str + '\n');
			client.sendMsg(str);
		}
	}
	
	
}
