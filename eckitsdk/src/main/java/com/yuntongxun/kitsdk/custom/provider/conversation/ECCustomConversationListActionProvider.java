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
 * @date time：2015年7月16日 上午10:19:55
 */
public interface ECCustomConversationListActionProvider extends IBaseProvider {

	boolean onCustomConversationItemClick(Context context,
			ECConversation conversation);

	boolean onCustonConversationLongClick(Context context,
			ECConversation conversation);

	boolean onCustomConversationMenuItemClick(Context context,
			ECConversation conversation, int position);

	boolean onCustomConversationListRightavigationBarClick(Context context);

}
