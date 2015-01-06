package com.Client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;

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
		txtArea.setAutoscrolls(true);
		txtArea.setEditable(false);
		
		nameList = new JList(new DataModel(client.getInfos()));
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
	}
	
	@SuppressWarnings("serial")
	public class DataModel extends AbstractListModel {
		ArrayList<ClientInfo> infos = null;
		
		public DataModel(ArrayList<ClientInfo> infos) {
			this.infos = infos;
		}
		@Override
		public Object getElementAt(int arg0) {
			return infos.get(arg0);
		}

		@Override
		public int getSize() {
			return infos.size();
		}	
	}
	
	public class TFListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String str = txtField.getText().trim();
			txtField.setText("");
			txtArea.setText(txtArea.getText() + userName + ":\n" + str + '\n');
			//client.sendMsg(str);
		}
	}
	
	
}
