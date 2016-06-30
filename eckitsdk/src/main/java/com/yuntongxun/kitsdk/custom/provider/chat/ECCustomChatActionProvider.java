package com.yuntongxun.kitsdk.custom.provider.chat;

import android.content.Context;

import com.yuntongxun.ecsdk.ECMessage;
import com.yuntongxun.kitsdk.custom.provider.IBaseProvider;
import com.yuntongxun.kitsdk.ui.chatting.model.ECConversation;

/** @author shan
@date time：2015年7月16日 上午10:28:22*/
public interface ECCustomChatActionProvider extends IBaseProvider {
	
	
	boolean onCustomChatMessageItemLongClick(Context context,ECMessage message);
	
	
	boolean onMessagePortRaitClick(Context context,ECMessage message);
	
	
	boolean onRightavigationBarClick(Context context,String sessionId);
	

}
