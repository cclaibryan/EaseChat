package com.Server;

import com.Common.BasicInfo;
import com.Common.ClientInfo;
import com.Common.ClientInfoList;
import com.Common.Msg;

import java.io.*;
import java.net.*;
import java.util.ArrayList;


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
			System.out.println("occupied port 8888");
		} catch (BindException e) {
			System.out.println("port being used!");
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
			} catch (IOException e) {
				clients.remove(this);
				System.out.println ("User Logout!");
			}
		}
		
		public void run () {
			try {
				//receive user login information
				while (bConnected) {
					BasicInfo recvTemp = (BasicInfo) dis.readObject();
					System.out.println("Type is:" + recvTemp.getType());
					switch (recvTemp.getType()) {
						case 1:
							ClientInfo infoTemp = (ClientInfo)recvTemp;
							//record this user
							this.userName = infoTemp.getUserName();
							this.ip = infoTemp.getIp();
							clientInfos.add(infoTemp);
							
							//send this user's login info to other users
							ClientInfoList cList = new ClientInfoList();
							cList.setClientInfos(clientInfos);
							cList.setType(2);
							
							for (int i = 0; i< clients.size(); i++) {
								ClientOperation tempClientOperation = clients.get(i);
								tempClientOperation.sendObject(cList);
							}
							break;
						case 0:
							Msg msgTemp = (Msg) recvTemp;
							
							//console
							String msg = msgTemp.getMsg();
							String sender = msgTemp.getMsgSender();
							System.out.println(sender + ": " + msg);
							
							for (int i = 0; i< clients.size(); i++) {
								ClientOperation tempClientOperation = clients.get(i);
								tempClientOperation.sendObject(msgTemp);
							}
							break;
						default:
							break;
					}
					System.out.println (userName + ' ' + ip + " connected!");
				}
			} catch (EOFException e) {
				
				for(int i = 0; i< clientInfos.size(); i++) {
					ClientInfo temp  = clientInfos.get(i);
					if (temp.getUserName().equals(this.userName))	
						clientInfos.remove(i);
				}
				System.out.println("Client closed!");
				
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
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
