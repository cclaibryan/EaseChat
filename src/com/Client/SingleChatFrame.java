package com.Client;

import java.awt.Color;
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
	
	public SingleChatFrame(String name) {
		super(name);
		
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
	}
	
	private class RecvFromUserThread implements Runnable {
		
		public void run () {
			try {
				dsRecv = new DatagramSocket(55555);	//monitor 55555 port
				recvPacket = new DatagramPacket(buffer, buffer.length);
				while (bConnected) {
				    dsRecv.receive(recvPacket);
				    System.out.println(recvPacket.getData());
				    chatFrame.txtArea.setText(chatFrame.txtArea.getText() + "Bob:\n" + new String(recvPacket.getData(),0,recvPacket.getLength()) + '\n');
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
	public static void main(String[] args) {
		new SingleChatFrame("Alice -> Bob");

	}

}
