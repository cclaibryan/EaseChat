package com.Client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;
import javax.xml.ws.handler.MessageContext;

import org.omg.PortableServer.THREAD_POLICY_ID;

import com.Common.ClientInfo;
import com.Common.Msg;

/*
 * UdpCLient used for user to connect to other user for single chat
 */
public class UdpClient {

	//socket for UDP connection to other users
    DatagramSocket dsRecv;	//socket to receive message
    DatagramSocket dsSend;	//socket to send message
    
    DatagramPacket recvPacket;	//received package
    DatagramPacket sendPacket;	//send package
    
    //IP and port information
    String userName;
    int userPort = 0;
    String peerName;
    String peerIp;
    int peerPort = 0;
    
    SingleChatFrame singleChatFrame = null;	//for single chat
    GroupChatFrame groupChatFrame = null;	//for single chat info collection
    //received messages and buffer
    public String receivedMsgs;
    private byte[] buffer;	//received message buffer
    
    boolean bConnected;
    
    Thread tRecvFromUser;	//monitor for other user
    
    public UdpClient(String userName,GroupChatFrame chatFrame) {	//constructor for groupChatFrame
    	try {
    		this.userName = userName;
    		this.groupChatFrame = chatFrame;
			dsSend = new DatagramSocket();
			dsRecv = new DatagramSocket(55555);	//monitor 55555 port
			buffer = new byte[2048];
			recvPacket = new DatagramPacket(buffer, buffer.length);
			receivedMsgs = new String();
			
			//open monitor thread
			bConnected = true;

			tRecvFromUser = new Thread(new RecvFromPeerThread());
			tRecvFromUser.start();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
    }
    
    //constructor for singleChatFrame
    public UdpClient(String userName, int userPort, String peerName, String peerIp, int peerPort,SingleChatFrame chatFrame) {
    	try {
    		this.userName = userName;
    		this.userPort = userPort;
    		this.peerName = peerName;
    		this.peerIp = peerIp;
    		this.peerPort = peerPort;
    		this.singleChatFrame = chatFrame;
    			
			dsSend = new DatagramSocket();
			dsRecv = new DatagramSocket(userPort);	//monitor my port
			buffer = new byte[2048];
			recvPacket = new DatagramPacket(buffer, buffer.length);
			receivedMsgs = new String();
			
			//open monitor thread
			bConnected = true;
			tRecvFromUser = new Thread(new RecvFromPeerThread());
			tRecvFromUser.start();
			
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
    }
    
    //general send method
    private void sendObj(Object obj,int port) {
    	ByteArrayOutputStream bout= new ByteArrayOutputStream();
		   try {
			ObjectOutputStream oout=new ObjectOutputStream(bout);
			oout.writeObject(obj);
			oout.flush();
			buffer = bout.toByteArray();
			System.out.println(peerIp + "   " + port);
			sendPacket = new DatagramPacket(buffer, buffer.length,InetAddress.getByName(peerIp),port);
			dsSend.send(sendPacket);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
    }
    
    //send message
    public void sendMsg(String s) {
    	Msg msg = new Msg(s,userName);
		sendObj(msg,peerPort);
	}
    
    //send info 
    public void sendInfo() {
    	try {
			ClientInfo info =  new ClientInfo(userName,InetAddress.getLocalHost().getHostAddress(),userPort);
			sendObj(info, 55555);		//send to peer's 55555 port 
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void disconnect(boolean sendEndingInfo) {
    	
    	//sending ending info
    	if (sendEndingInfo) {
	    	ClientInfo endInfo = new ClientInfo(userName,"0.0.0.0",userPort);
	    	sendObj(endInfo, 55555);
    	}
    	
    	dsRecv.close();
		dsSend.close();
		tRecvFromUser.interrupt();
	}
    
    private class RecvFromPeerThread implements Runnable {
    	
    	@SuppressWarnings("unchecked")
		public void run () {
			try {
				while (bConnected && (!Thread.interrupted())) {
				    dsRecv.receive(recvPacket);
				    
				    ByteArrayInputStream bint=new ByteArrayInputStream(recvPacket.getData());  
				     ObjectInputStream oint=new ObjectInputStream(bint);
				     try {
						Object message = (Object)oint.readObject();
						String className = message.getClass().getName();
						
						if (className.equals("com.Common.Msg")) {
							Msg msgTemp = (Msg) message;
							String msg = msgTemp.getMsg();
							String sender = msgTemp.getMsgSender();
							singleChatFrame.setVisible(true);	//pop out the chat frame
							singleChatFrame.txtArea.setText(singleChatFrame.txtArea.getText() + sender + ":\n    " + msg + '\n');
						}
						else if (className.equals("com.Common.ClientInfo")) {	//only udpClient of GroupChatFrame can receive this 
							ClientInfo info = (ClientInfo)message;
							String name = info.getUserName();
							String ip = info.getIp();
							
							if (ip.equals("0.0.0.0")) {	//peer sending ending info. Dispose mine
								for (int i = 0;i< groupChatFrame.singleChatFramesList.size();i++) {
									SingleChatFrame temp = groupChatFrame.singleChatFramesList.get(i);
									if (temp.peerName.equals(name)) {
										temp.client.disconnect(false);
										temp.dispose();
										groupChatFrame.singleChatFramesList.remove(i);
										break;
									}
								}
							}
							else {
								System.out.println("Hello!");
								SingleChatFrame tempChatFrame = groupChatFrame.addSingleChatFrame(userName, name, ip,false);
								tempChatFrame.setPeerPort(info.getPort());	//set peer's port
								
								if (tempChatFrame.infoIsSent == false) {	//reply my client info
									tempChatFrame.client.sendInfo();
									tempChatFrame.infoIsSent = true;
								}
							}
						}
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} catch (SocketException e) {
				System.out.println("Exit. Bye!");
			} catch (EOFException e) {
				System.out.println("Exit. Bye!");
			} catch (IOException e ) {
				e.printStackTrace();
			} 			
		}
	}
}
