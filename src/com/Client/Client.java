package com.Client;
import com.Common.*;
import com.sun.tools.jdi.Packet;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

import sun.nio.cs.ext.PCK;

public class Client {
	
	public BasicFrame chatFrame = null;
	
	//socket and stream for TCP connection to server
	public ObjectInputStream dis = null;
	public ObjectOutputStream dos = null;
    private Socket s = null;

    //socket for UDP connection to other users
    DatagramSocket dsRecv = null;	//socket to receive message
    DatagramSocket dsSend = null;	//socket to send message
    
    DatagramPacket recvPacket = null;	//received package
    DatagramPacket sendPacket = null;	//send package
    
    private byte[] buffer = new byte[2048];	//received message buffer
    
    //received messages
    public String receivedMsgs = new String();
    
    //server info
	private String serverIp = null;
	private int serverPort = 8888;
	
	//client info
	private String userName = null;
	private String localIp;

	boolean bConnected = false;
	
	//store login info for other users.
	ArrayList<ClientInfo> infos = null;
	
	Thread tRecvFromServer = null;	//monitor for server 
	Thread tRecvFromUser = null;	//monitor for other users
	
	private static Client instance;
	
	public static synchronized Client getInstance() {
		if (instance == null) instance = new Client();
	    return instance;
	}
	
	public void setServerIp(String ip){
		this.serverIp = ip;
	}
	
	public void setChatFrame(BasicFrame frame) {
		this.chatFrame = frame;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	//Constructor
	private Client() {
		try {
			this.localIp = InetAddress.getLocalHost().getHostAddress();
			dsSend = new DatagramSocket();	
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void send(Object obj) {
		try {
			dos.writeObject(obj);
		} catch (IOException e) {
			System.out.println ("对方退出了！我从List里面去掉了！");
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
	
	public boolean connect() {
		try {
			System.out.println(serverIp);
			s = new Socket(serverIp,serverPort);
			dos = new ObjectOutputStream(s.getOutputStream());
			dis = new ObjectInputStream(new BufferedInputStream(s.getInputStream()));
			
			//send my information
			ClientInfo myInfo = new ClientInfo();
			myInfo.setIp(localIp);
			myInfo.setUserName(userName);
			send(myInfo);
			
			bConnected = true;
			
			//receive login lists of other members.
			tRecvFromServer = new Thread(new RecvFromServerThread());
			tRecvFromServer.start();
			
			//receive messages from other users
			tRecvFromUser = new Thread(new RecvFromUserThread());
			tRecvFromUser.start();
			
			System.out.println("connected!");
					
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	public void disconnect() {
		try {
			dos.close();
			dis.close();
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//Thread receiving information from server
	private class RecvFromServerThread implements Runnable {
		
		@SuppressWarnings("unchecked")
		public void run () {
			try {
				while (bConnected) {
				    infos = (ArrayList<ClientInfo>) dis.readObject();
					for (int i = 0;i < infos.size(); i++) {
						ClientInfo temp = infos.get(i);
						System.out.println(i + " " + temp.getUserName() + " " + temp.getIp());
					}
				}
			} catch (SocketException e) {
				System.out.println("退出了,bye!");
			} catch (EOFException e) {
				System.out.println("退出了,bye!");
			} catch (IOException e ) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}				
		}
	}
	
	private class RecvFromUserThread implements Runnable {
		
		public void run () {
			try {
				dsRecv = new DatagramSocket(55555);	//monitor 55555 port
				recvPacket = new DatagramPacket(buffer, buffer.length);
				while (bConnected) {
				    dsRecv.receive(recvPacket);
				    chatFrame.txtArea.setText(chatFrame.txtArea.getText() + new String(recvPacket.getData(),0,recvPacket.getLength()));
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
