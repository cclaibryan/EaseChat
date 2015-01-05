package com.Common;

import java.io.Serializable;

	/*clients register on the server*/
public class ClientInfo implements Serializable{
	private String userName;
	private String ip;
		
	public ClientInfo() {}
		
	public void setUserName(String userName) {
		this.userName = userName;
	}
		
	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getUserName() {
		return this.userName;
	}
		
	public String getIp () {
		return this.ip;
	}
}
