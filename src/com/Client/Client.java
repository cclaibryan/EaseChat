package com.Client;
import com.Common.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

@SuppressWarnings("serial")
public class Client {
	

	public DataInputStream dis = null;
	public DataOutputStream dos = null;
    private Socket s = null;


    //server info
	private String serverIp = null;
	private int serverPort = 8888;
	
	//client info
	private String userName = null;
	private String localIp;
	
	
	boolean bConnected = false;
	
	Thread tRecv = null; 
	private static Client instance;
	
	public static synchronized Client getInstance() {
		if (instance == null) instance = new Client();
	    return instance;
	}
	
	public void setServerIp(String ip){
		this.serverIp = ip;
	}
	
	private Client() {
		try {
			this.localIp = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		connect();
	}
	
	public boolean connect() {
		try {
			s = new Socket(serverIp,serverPort);
			dos = new DataOutputStream(s.getOutputStream());
			dis = new DataInputStream(s.getInputStream());
			
			ClientInfo regisMessage = new ClientInfo();
			//regisMessage.setUserName(Client.);
			
			System.out.println("connected!");
			bConnected = true;
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
	
	
	
	
	
}
