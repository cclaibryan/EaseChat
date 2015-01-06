package com.Common;

import java.io.Serializable;

import sun.security.action.GetBooleanAction;

public class Msg extends BasicInfo implements Serializable {
	private String msg;
	private String msgSender;
	
	public Msg() {}
	
	public Msg(String msg) {
		this.msg = msg;
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
