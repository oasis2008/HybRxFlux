package com.yuntongxun.kitsdk.listener;

import com.yuntongxun.ecsdk.ECDevice;
import com.yuntongxun.ecsdk.ECError;

public interface OnConnectSDKListener {
	
	public void onConnect();
	public void onDisconnect(ECError error);
	
	public void onConnectState(ECDevice.ECConnectState state, ECError error);

}
