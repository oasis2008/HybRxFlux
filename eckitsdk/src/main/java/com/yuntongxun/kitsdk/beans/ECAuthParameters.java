package com.yuntongxun.kitsdk.beans;

import com.yuntongxun.ecsdk.ECInitParams;

public class ECAuthParameters {
	
	
	
	private String userId;
	private String pwd;
	
	private String appKey;
	private String appToken;
	
	private ECInitParams.LoginMode loginMode;
	private ECInitParams.LoginAuthType loginType;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getAppKey() {
		return appKey;
	}
	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}
	public String getAppToken() {
		return appToken;
	}
	public void setAppToken(String appToken) {
		this.appToken = appToken;
	}
	public ECInitParams.LoginMode getLoginMode() {
		return loginMode;
	}
	public void setLoginMode(ECInitParams.LoginMode loginMode) {
		this.loginMode = loginMode;
	}
	public ECInitParams.LoginAuthType getLoginType() {
		return loginType;
	}
	public void setLoginType(ECInitParams.LoginAuthType loginType) {
		this.loginType = loginType;
	}
	
	
	
	
	

}
