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
	
	private JTextField txtField = null;
	private JTextArea txtArea = null;
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
	}
	
	public class TFListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String str = txtField.getText().trim();
			txtField.setText("");
			
			try {
				client.dos.writeUTF(str);
				client.dos.flush();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	private class RecvThread implements Runnable {
		
		public void run () {
			try {
				while (client.bConnected) {
					String str = client.dis.readUTF();
					txtArea.setText(txtArea.getText() + str + '\n');
				}
			} catch (SocketException e) {
				System.out.println("退出了,bye!");
			} catch (EOFException e) {
				System.out.println("退出了,bye!");
			} catch (IOException e ) {
				e.printStackTrace();
			}				
		}
	}
}
