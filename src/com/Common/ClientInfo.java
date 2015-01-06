package com.Common;

import java.io.Serializable;

	/*clients register on the server*/
@SuppressWarnings("serial")
public class ClientInfo extends BasicInfo implements Serializable{
	private String userName;
	private String ip;
	private int port;
		
	public ClientInfo() {}
		
	public ClientInfo(String userName, String ip, int port) {
		this.userName = userName;
		this.ip = ip;
		this.port = port;
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
		
	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return this.port;
	}
	
	public String getUserName() {
		return this.userName;
	}
		
	public String getIp () {
		return this.ip;
	}
}
