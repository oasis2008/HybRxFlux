package com.yuntongxun.kitsdk.custom.provider.conversation;

import java.util.List;


import com.yuntongxun.kitsdk.custom.provider.IBaseProvider;
import com.yuntongxun.kitsdk.ui.chatting.model.ECConversation;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;

/**
 * @author shan
 * @date time：2015年7月16日 上午10:18:10
 */
public interface ECCustomConversationListUIProvider extends IBaseProvider {

	List<String> getCustomConversationItemLongClickMenu(Fragment f,
			ECConversation conversation);


	

}
