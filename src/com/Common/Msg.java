package com.Common;

import java.io.Serializable;


public class Msg implements Serializable {
	private String msg;
	private String msgSender;
	
	public Msg() {}
	
	public Msg(String msg,String msgSender) {
		this.msg = msg;
		this.msgSender = msgSender;
	}
	
	public String getMsg() {
		return this.msg;
	}
	
	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getMsgSender() {
		return msgSender;
	}

	public void setMsgSender(String msgSender) {
		this.msgSender = msgSender;
	}
}
