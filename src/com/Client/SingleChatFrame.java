package com.Client;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.EOFException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import com.Client.GroupChatFrame.TFListener;

public class SingleChatFrame extends JFrame {

	public JTextField txtField = null;
	public JTextArea txtArea = null;
	public JScrollPane scrollPane = null;
	
	UdpClient client = null;
	public String userName;
	
	public SingleChatFrame(String name) {
		super(name);
		this.userName = name;
		this.setLocation(560,300);
		this.setSize(450,300);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(null);
		
		this.setMainFrame();
	}
	
	private void setMainFrame() {
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
	
	
	public static void main(String[] args) {
		new SingleChatFrame("Alice -> Bob");

	}

}
