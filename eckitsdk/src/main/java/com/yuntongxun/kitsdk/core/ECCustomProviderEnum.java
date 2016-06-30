package com.yuntongxun.kitsdk.core;

import com.yuntongxun.kitsdk.ui.ECChattingActivity;
import com.yuntongxun.kitsdk.ui.ECConversationListActivity;

/**
 * @author shan
 * @date time：2015年7月16日 上午10:36:59
 */
public enum ECCustomProviderEnum {

	CHATTING_PROVIDER(ECChattingActivity.class.getSimpleName()), CONVERSATION_PROVIDER(
			ECConversationListActivity.class.getSimpleName());

	private String name;

	private ECCustomProviderEnum(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
