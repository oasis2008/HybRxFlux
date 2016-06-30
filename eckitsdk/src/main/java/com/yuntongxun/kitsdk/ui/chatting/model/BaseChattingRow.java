/*
 *  Copyright (c) 2013 The CCP project authors. All Rights Reserved.
 *
 *  Use of this source code is governed by a Beijing Speedtong Information Technology Co.,Ltd license
 *  that can be found in the LICENSE file in the root of the web site.
 *
 *   http://www.cloopen.com
 *
 *  An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */
package com.yuntongxun.kitsdk.ui.chatting.model;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.View;

import com.yuntongxun.eckitsdk.R;
import com.yuntongxun.ecsdk.ECMessage;
import com.yuntongxun.kitsdk.core.ECKitCustomProviderManager;
import com.yuntongxun.kitsdk.custom.provider.chat.ECCustomChatActionProvider;
import com.yuntongxun.kitsdk.ui.ECChattingActivity;
import com.yuntongxun.kitsdk.ui.chatting.holder.BaseHolder;
import com.yuntongxun.kitsdk.utils.LogUtil;

import java.util.HashMap;

public abstract class BaseChattingRow implements IChattingRow {

	public static final String TAG = LogUtil
			.getLogUtilsTag(BaseChattingRow.class);
	private HashMap<String, String> hashMap = new HashMap<String, String>();
	int mRowType;

	public BaseChattingRow(int type) {
		mRowType = type;
	}

	/**
	 * 处理消息的发送状态设置
	 * 
	 * @param position
	 *            消息的列表所在位置
	 * @param holder
	 *            消息ViewHolder
	 * @param l
	 */
	protected static void getMsgStateResId(int position, BaseHolder holder,
			ECMessage msg, View.OnClickListener l) {
		if (msg != null && msg.getDirection() == ECMessage.Direction.SEND) {
			ECMessage.MessageStatus msgStatus = msg.getMsgStatus();
			if (msgStatus == ECMessage.MessageStatus.FAILED) {
				holder.getUploadState().setImageResource(
						R.drawable.ytx_msg_state_failed_resend);
				holder.getUploadState().setVisibility(View.VISIBLE);
				if (holder.getUploadProgressBar() != null) {
					holder.getUploadProgressBar().setVisibility(View.GONE);
				}
			} else if (msgStatus == ECMessage.MessageStatus.SUCCESS
					|| msgStatus == ECMessage.MessageStatus.RECEIVE) {
				holder.getUploadState().setImageResource(0);
				holder.getUploadState().setVisibility(View.GONE);
				if (holder.getUploadProgressBar() != null) {
					holder.getUploadProgressBar().setVisibility(View.GONE);
				}

			} else if (msgStatus == ECMessage.MessageStatus.SENDING) {
				holder.getUploadState().setImageResource(0);
				holder.getUploadState().setVisibility(View.GONE);
				if (holder.getUploadProgressBar() != null) {
					holder.getUploadProgressBar().setVisibility(View.VISIBLE);
				}

			} else {
				if (holder.getUploadProgressBar() != null) {
					holder.getUploadProgressBar().setVisibility(View.GONE);
				}
				LogUtil.d(TAG, "getMsgStateResId: not found this state");
			}

			ViewHolderTag holderTag = ViewHolderTag.createTag(msg,
					ViewHolderTag.TagType.TAG_RESEND_MSG, position);
			holder.getUploadState().setTag(holderTag);
			holder.getUploadState().setOnClickListener(l);
		}
	}

	/**
	 *
	 * @param contextMenu
	 * @param targetView
	 * @param detail
	 * @return
	 */
	public abstract boolean onCreateRowContextMenu(ContextMenu contextMenu,
			View targetView, ECMessage detail);

	/**
	 *
	 * @param baseHolder
	 * @param displayName
	 */
	public static void setDisplayName(BaseHolder baseHolder, String displayName) {
		if (baseHolder == null || baseHolder.getChattingUser() == null) {
			return;
		}
		System.out.println(displayName);
		if (TextUtils.isEmpty(displayName)) {
			baseHolder.getChattingUser().setVisibility(View.GONE);
			return;
		}

		baseHolder.getChattingUser().setText(displayName);
		baseHolder.getChattingUser().setVisibility(View.VISIBLE);
	}

	protected abstract void buildChattingData(Context context,
			BaseHolder baseHolder, ECMessage detail, int position);

	public void buildChattingBaseData(Context context, BaseHolder baseHolder,
			ECMessage detail, int position) {

		// 处理其他使用逻辑
		buildChattingData(context, baseHolder, detail, position);
		setContactPhoto(baseHolder, detail);
		if (((ECChattingActivity) context).isPeerChat()
				&& detail.getDirection() == ECMessage.Direction.RECEIVE) {//群组昵称修改处

//			ECContacts contact = ContactSqlManager.getContact(detail.getForm());
//			if (contact != null) {
//				if (TextUtils.isEmpty(contact.getNickname())) {
//					contact.setNickname(contact.getContactid());
//				}
//				setDisplayName(baseHolder, contact.getNickname());
//			} else {
//				setDisplayName(baseHolder, detail.getForm());
//			}
			setDisplayName(baseHolder,
					detail.getForm());
		}
		setContactPhotoClickListener(context, baseHolder, detail);
	}

	private void setContactPhotoClickListener(final Context context,
			BaseHolder baseHolder, final ECMessage detail) {
		if (baseHolder.getChattingAvatar() != null && detail != null) {
			baseHolder.getChattingAvatar().setOnClickListener(
					new View.OnClickListener() {
						@Override
						public void onClick(View v) {

							ECCustomChatActionProvider obj = ECKitCustomProviderManager
									.getCustomChatActionProvider();

							if (obj != null
									) {

								
								
							boolean result=	obj.onMessagePortRaitClick(context, detail);
							if(result){
								return;
							}
							}

						}
					});
		}
	}

	/**
	 * 添加用户头像
	 * 
	 * @param baseHolder
	 * @param detail
	 */
	private void setContactPhoto(BaseHolder baseHolder, ECMessage detail) {
		if (baseHolder.getChattingAvatar() != null) {
			try {
				if (TextUtils.isEmpty(detail.getForm())) {
					return;
				}
				String userUin = "";
				if (hashMap.containsKey(detail.getForm())) {
					userUin = hashMap.get(detail.getForm());
				} else {
					// userUin = ContactSqlManager.getContact(detail.getForm())
					// .getRemark();？？
				}
				// baseHolder.getChattingAvatar().setImageBitmap(
				// ContactLogic.getPhoto(null));？？
			} catch (Exception e) {
			}
		}
	}

}
