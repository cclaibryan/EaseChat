package com.Server;

import com.Common.ClientInfo;
import com.Common.ClientInfoList;
import com.Common.Msg;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

import javax.swing.JOptionPane;


public class Server {
	boolean started = false;
	ServerSocket ss = null;
	
	private int port;
	
	ArrayList<ClientOperation> clients = new ArrayList<ClientOperation>();	//operation threads list
	ArrayList<ClientInfo> clientInfos = new ArrayList<ClientInfo>();		//user info list
	
	public static void main(String[] args) {
		
		new Server(8888);
	}
	
	public Server(int port) {
		this.port = port;
		this.start();
	}
	
	public void start() {
		try {
			ss = new ServerSocket(port);
			started = true;
			JOptionPane.showMessageDialog(null, "服务端已开启！占用8888端口。");
		} catch (BindException e) {
			System.out.println("port being used!");
			JOptionPane.showMessageDialog(null, "端口8888已被占用！服务器关闭...");
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			while (started) {
				Socket s = ss.accept();
				ClientOperation c = new ClientOperation(s);
				System.out.println("a client connected!");
				
				new Thread(c).start();
				clients.add(c);
			}		
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				ss.close();
			} catch (IOException e) {
				e.printStackTrace();
			} 	
		}
	}
	
	class ClientOperation implements Runnable {
		private Socket s;
		private ObjectInputStream dis = null;
		private ObjectOutputStream dos = null;
		private boolean bConnected = false;
		
		private String userName = null;
		private String ip = null;

		public ClientOperation(Socket s) {
			this.s = s;
			try {
				dis = new ObjectInputStream(new BufferedInputStream(s.getInputStream()));
				dos = new ObjectOutputStream(s.getOutputStream());
				bConnected = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		public void sendObject(Object obj) {
			try {
				dos.writeObject(obj);
				dos.reset();
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println ("User Logout!");
			}
		}
		
		public void groupSending(Object obj) {
			for (int i = 0; i< clients.size(); i++) {
				ClientOperation tempClientOperation = clients.get(i);
				tempClientOperation.sendObject(obj);
			}
		}
		
		public void run () {
			try {
				//receive user login information
				while (bConnected) {
					Object recvTemp = (Object) dis.readObject();
					String className = recvTemp.getClass().getName();
					
					if (className.equals("com.Common.Msg")) {
						Msg msgTemp = (Msg) recvTemp;
						//console
						String msg = msgTemp.getMsg();
						String sender = msgTemp.getMsgSender();
						
						groupSending(msgTemp);
					} 
					else if (className.equals("com.Common.ClientInfo")) {
						
						ClientInfo infoTemp = (ClientInfo)recvTemp;
						//record this user
						this.userName = infoTemp.getUserName();
						this.ip = infoTemp.getIp();
						boolean isRepeated = false;
						for(int i = 0;i<clientInfos.size();i++) {
							ClientInfo info = clientInfos.get(i);
							if (info.getUserName().equals(userName)) { //The user has loged in
								isRepeated = true;
								Msg emptyMsg = new Msg();
								sendObject(emptyMsg);
							}
						}
						if (!isRepeated) {
							clientInfos.add(infoTemp);
						
							//send this user's login info to other users
							ClientInfoList cList = new ClientInfoList();
							cList.setClientInfos(clientInfos);
						
							groupSending(cList);
						}
					}
				}
			} catch (EOFException e) {
				
				//delete the logout user
				for(int i = 0; i< clientInfos.size(); i++) {
					ClientInfo temp  = clientInfos.get(i);
					if (temp.getUserName().equals(this.userName))	
						clientInfos.remove(i);
				}
				//update the user login information
				ClientInfoList cList = new ClientInfoList();
				cList.setClientInfos(clientInfos);
				groupSending(cList);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					clients.remove(this);
					if (dis != null)	dis.close();
					if (dos != null)	dos.close();
					if (s != null) 		s.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		
	}

}
