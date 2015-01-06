package com.Client;
import com.Common.*;
import com.sun.tools.internal.xjc.model.CElementPropertyInfo.CollectionMode;
import com.sun.tools.jdi.Packet;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

import sun.nio.cs.ext.PCK;

/*
 * TcpCLient used for user to connect to server for group chat
 */
public class TcpClient {
	
	//socket and stream for TCP connection to server
	public ObjectInputStream dis = null;
	public ObjectOutputStream dos = null;
    private Socket s = null;
    
    //client and server info
	private String serverIp = null;
	private int serverPort = 8888;
	private String userName = null;
	private String localIp;

	private ArrayList<ClientInfo> infos = null;	//store login info for other users.
    public String receivedMsgs = new String();	//received messages
	private Thread tRecvFromServer = null;	//monitor for server 
	
	boolean bConnected = false;
	
	public ArrayList<ClientInfo> getInfos() {
		return this.infos;
	}
	
	//Constructor
	public TcpClient(String userName,String serverIp) {
		try {
			//set local IP and server IP and userName
			this.infos = new ArrayList<ClientInfo>();
			this.localIp = InetAddress.getLocalHost().getHostAddress();
			this.serverIp = serverIp;
			this.userName = userName;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} 
	}
	
	//send my login in information to the server
	public void sendObject(Object obj) {
		
		try {
			dos.writeObject(obj);
		} catch (IOException e) {
			System.out.println ("对方退出了！我从List里面去掉了！");
		}
	}
	
	//send message to the group chat 
	/*public void sendMsg(String str) {
		Msg msg = new Msg(str);
		msg.setType(0);	//0 is msg
		sendObject(msg);
	}*/
	
	//connect to the server
	public boolean connect() {
		System.out.println("Connect");
		try {
			s = new Socket(serverIp,serverPort);
			dos = new ObjectOutputStream(s.getOutputStream());
			dis = new ObjectInputStream(new BufferedInputStream(s.getInputStream()));
			
			//construct and send my information
			ClientInfo myInfo = new ClientInfo(userName,localIp,s.getPort());
			myInfo.setType(1);	//1 is login info
			System.out.println("Type(tcp):" + myInfo.getType());
			sendObject(myInfo);
			
			bConnected = true;
			
			//receive login lists of other members.
			tRecvFromServer = new Thread(new RecvFromServerThread());
			tRecvFromServer.start();
			
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
					BasicInfo recvTemp = (BasicInfo) dis.readObject();
					switch (recvTemp.getType()) {
						case 0:
							Msg msgTemp = (Msg) recvTemp;
							String msg = msgTemp.getMsg();
							String sender = msgTemp.getMsgSender();
							receivedMsgs += msg;
							break;
						case 2:
							ClientInfoList listTemp = (ClientInfoList)recvTemp;
							infos = listTemp.getClientInfos();
							break;
						default:
							break;
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
	
	
	
}
