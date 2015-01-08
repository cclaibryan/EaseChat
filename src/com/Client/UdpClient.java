package com.Client;

import java.io.EOFException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/*
 * UdpCLient used for user to connect to other user for single chat
 */
public class UdpClient {

	//socket for UDP connection to other users
    DatagramSocket dsRecv = null;	//socket to receive message
    DatagramSocket dsSend = null;	//socket to send message
    
    DatagramPacket recvPacket = null;	//received package
    DatagramPacket sendPacket = null;	//send package
    
    //IP and port information
    String myIp = null;
    String hisIp = null;
    int port = -1;
    
  //received messages and buffer
    public String receivedMsgs = new String();
    private byte[] buffer = new byte[2048];	//received message buffer
    
    boolean bConnected = false;
    
    Thread tRecvFromUser = null;	//monitor for other user
    
    public UdpClient() {
    	try {
			dsSend = new DatagramSocket();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
    }
    
    public void sendMsg(String str) {
		
		byte buf[] = str.getBytes();
		try {
			sendPacket = new DatagramPacket(buf, buf.length,InetAddress.getByName("192.168.1.222"),55555);
			dsSend.send(sendPacket);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
}
