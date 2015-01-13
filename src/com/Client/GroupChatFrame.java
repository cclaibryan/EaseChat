package com.Client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.EOFException;
import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

import javax.management.modelmbean.ModelMBean;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import com.Common.ClientInfo;
import com.Server.Server;

/*The main chatting frame*/
public class GroupChatFrame extends JFrame {
	
	public JList nameList;
	public JTextField txtField;
	public JTextArea txtArea;
	public JScrollPane scrollPane;
	
	private TcpClient tcpClient;
	private UdpClient udpClient;
	
	private String userName;		//my name
	
	//for single chat
	boolean[] portSet; 			//single chat frame monitor port set
	ArrayList<SingleChatFrame> singleChatFramesList;
	
	public GroupChatFrame(String name,String serverIp) {
		super(name);
		this.userName = name;
		
		//initiate the TcpClient
		tcpClient = new TcpClient(userName, serverIp);
		tcpClient.connect();
		tcpClient.setChatFrame(this);		
		
		//initiate for single chat
		portSet = new boolean[5000];//port from 20000 to 25000
		singleChatFramesList = new ArrayList<SingleChatFrame>();

		//initiate the UdpClient
		udpClient = new UdpClient(name,this);
				
		this.setMainFrame();
	}
	
	private void setMainFrame() {
		this.setLocation(560,300);
		this.setSize(450,300);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(null);
		
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
		
		nameList = new JList(tcpClient.nameListModel);
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
		txtField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String str = txtField.getText().trim();
				txtField.setText("");
				tcpClient.sendMsg(str,userName);
			}
		});
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				tcpClient.disconnect();
				System.exit(0);
			}
		});
		
		this.nameList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				JList theList  = (JList)e.getSource();
				
				if (e.getClickCount() == 2)  { //double click
					int index = theList.getSelectedIndex();
					String peerIp = tcpClient.infos.get(index).getIp();
					if (index >= 0)	 {
						String peerName = (String) theList.getModel().getElementAt(index);	//peer's name
						
						if (peerName.equals(userName)) return;		//myself,return
						
						boolean found = false;
						for(int i =  0; i< singleChatFramesList.size(); i++) {
							SingleChatFrame temp = singleChatFramesList.get(i);
							if (temp.peerName.equals(peerName)) { //the frame has been opened
								found = true;
								temp.setVisible(true);			 //set frame to above
								temp.txtField.requestFocus();
								break;
							}
						}
						if (found == false) {		//frame not opened
							SingleChatFrame chatFrame = addSingleChatFrame(userName,peerName,peerIp,true);
							chatFrame.client.sendInfo();
							chatFrame.infoIsSent = true;
						}
					}
				}
			}
		});
	}
	
	//initiate a new single chat frame
	//add if a single chat frame is not initiated
	//fetch if the single chat frame has been initiated
	public SingleChatFrame addSingleChatFrame(String userName,String peerName, String peerIp,boolean isShown) {
		System.out.println("num:" + singleChatFramesList.size());
		for (int i = 0; i< singleChatFramesList.size(); i++) {
			SingleChatFrame temp = singleChatFramesList.get(i);
			if (temp.userName.equals(userName) && temp.peerName.equals(peerName)) return temp;
		}
		
		int randomNum = (int) (Math.random() * 5000);
		while (portSet[randomNum] == true) 
			randomNum = (int) (Math.random() * 5000);
		portSet[randomNum] = true;
		SingleChatFrame tempChatFrame = new SingleChatFrame(userName, peerName, peerIp, randomNum + 20000);
		singleChatFramesList.add(tempChatFrame);
		
		if (isShown) tempChatFrame.setVisible(true);
		else 		 tempChatFrame.setVisible(false);
		
		return tempChatFrame;
	}
}
