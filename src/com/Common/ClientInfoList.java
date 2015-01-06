package com.Common;

import java.io.Serializable;
import java.util.ArrayList;

public class ClientInfoList extends BasicInfo implements Serializable {
	private ArrayList<ClientInfo> clientInfos;

	public ArrayList<ClientInfo> getClientInfos() {
		return clientInfos;
	}

	public void setClientInfos(ArrayList<ClientInfo> clientInfos) {
		this.clientInfos = clientInfos;
	}
}
