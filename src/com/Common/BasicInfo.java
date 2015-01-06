package com.Common;

public class BasicInfo {
	int type;	//0 for msg, 1 for user login info, 2 for user login info list
	
	public int getType() {
		return this.type;
	}
	public void setType(int type) {
		this.type = type;
	}
}
