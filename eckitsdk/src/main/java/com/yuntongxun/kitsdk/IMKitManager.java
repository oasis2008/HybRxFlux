package com.yuntongxun.kitsdk;

import android.content.Intent;

import com.yuntongxun.kitsdk.core.CCPAppManager;
import com.yuntongxun.kitsdk.core.ECKitConstant;
import com.yuntongxun.kitsdk.ui.ECChattingActivity;
import com.yuntongxun.kitsdk.ui.ECConversationListActivity;
import com.yuntongxun.kitsdk.ui.chatting.model.IMChattingHelper;
import com.yuntongxun.kitsdk.ui.group.ECGroupListActivity;

public class IMKitManager {

	protected static IMKitManager sInstance;

	protected static IMKitManager getInstance() {
		if (sInstance == null) {
			synchronized (IMKitManager.class) {
				sInstance = new IMKitManager();
			}
		}

		return sInstance;
	}

	public void startConversationActivity(String target) {

		Intent intent = new Intent(CCPAppManager.getContext(),
				ECChattingActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra(ECKitConstant.KIT_CONVERSATION_TARGET, target);
		CCPAppManager.getContext().startActivity(intent);

	}

	public void startConversationListActivity() {

		Intent intent = new Intent(CCPAppManager.getContext(),
				ECConversationListActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		CCPAppManager.getContext().startActivity(intent);

	}

	public static void setAutoReceiverOfflineMsg(boolean isAuto) {

		IMChattingHelper.isAutoGetOfflineMsg = isAuto;
	}
	
	public void startGroupListActivity() {

		Intent intent = new Intent(CCPAppManager.getContext(),
				ECGroupListActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		CCPAppManager.getContext().startActivity(intent);

	}

}
