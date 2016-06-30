/*
 *  Copyright (c) 2013 The CCP project authors. All Rights Reserved.
 *
 *  Use of this source code is governed by a Beijing Speedtong Information Technology Co.,Ltd license
 *  that can be found in the LICENSE file in the root of the web site.
 *
 *   http://www.yuntongxun.com
 *
 *  An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */
package com.yuntongxun.kitsdk.adapter;

import android.content.Context;

import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yuntongxun.eckitsdk.R;
import com.yuntongxun.ecsdk.ECMessage;


import com.yuntongxun.kitsdk.db.ConversationSqlManager;
import com.yuntongxun.kitsdk.db.GroupNoticeSqlManager;
import com.yuntongxun.kitsdk.db.GroupSqlManager;
import com.yuntongxun.kitsdk.group.GroupNoticeHelper;
import com.yuntongxun.kitsdk.ui.chatting.model.ECConversation;
import com.yuntongxun.kitsdk.ui.chatting.view.CCPTextView;
import com.yuntongxun.kitsdk.utils.DateUtil;
import com.yuntongxun.kitsdk.utils.DemoUtils;


/**
 * 
 * @author luhuashan
 * 会话列表界面适配器
 *
 */
public class ConversationAdapter extends CCPListAdapter <ECConversation> {

    private OnListAdapterCallBackListener mCallBackListener;
    int padding;
    
    public ConversationAdapter(Context ctx , OnListAdapterCallBackListener listener) {
        super(ctx, new ECConversation());
        mCallBackListener = listener;
        padding = ctx.getResources().getDimensionPixelSize(R.dimen.OneDPPadding);
    }


    @Override
    public ECConversation getItem(ECConversation t, Cursor cursor) {
        ECConversation conversation = new ECConversation();
        conversation.setCursor(cursor);
        
        if(conversation.getSessionId().equals("10089")){
        	conversation.setUsername("系统通知");
        	return conversation;
        }

        if(conversation.getUsername() != null && conversation.getUsername().endsWith("@priategroup.com")) {
        	
            
            conversation.setUsername(conversation.getSessionId());//??
        } else if(conversation.getUsername() != null && conversation.getUsername().toUpperCase().startsWith("G")){
        	if(GroupSqlManager.getECGroup(conversation.getUsername())!=null){
        		conversation.setUsername(GroupSqlManager.getECGroup(conversation.getUsername()).getName());
        	}else{
        		conversation.setUsername("未知");
        	}
        } else {
        	conversation.setUsername(conversation.getSessionId());
        }
        return conversation;
    }

    /**
     * 会话时间
     * @param conversation
     * @return
     */
    protected final CharSequence getConversationTime(ECConversation conversation) {
        if(conversation.getSendStatus() == ECMessage.MessageStatus.SENDING.ordinal()) {
            return mContext.getString(R.string.conv_msg_sending);
        }
        if(conversation.getDateTime() <= 0) {
            return "";
        }
        return DateUtil.getDateString(conversation.getDateTime(),
                DateUtil.SHOW_TYPE_CALL_LOG).trim();
    }

    /**
     * 根据消息类型返回相应的主题描述
     * @param conversation
     * @return
     */
    protected final CharSequence getConversationSnippet(ECConversation conversation) {
        if(conversation == null) {
            return "";
        }
        if(GroupNoticeSqlManager.CONTACT_ID.equals(conversation.getSessionId())) {
            return GroupNoticeHelper.getNoticeContent(conversation.getContent());
        }
        if(conversation.getMsgType() == ECMessage.Type.VOICE.ordinal()) {
            return mContext.getString(R.string.app_voice);
        } else if(conversation.getMsgType() == ECMessage.Type.FILE.ordinal()) {
            return mContext.getString(R.string.app_file);
        } else if(conversation.getMsgType() == ECMessage.Type.IMAGE.ordinal()) {
            return mContext.getString(R.string.app_pic);
        } else if(conversation.getMsgType() == ECMessage.Type.VIDEO.ordinal()) {
            return mContext.getString(R.string.app_video);
        }
        return conversation.getContent();
    }

    /**
     * 根据消息发送状态处理
     * @param context
     * @param conversation
     * @return
     */
    public static Drawable getChattingSnippentCompoundDrawables(Context context ,ECConversation conversation) {
        if(conversation.getSendStatus() == ECMessage.MessageStatus.FAILED.ordinal()) {
            return DemoUtils.getDrawables(context, R.drawable.msg_state_failed);
        } else if (conversation.getSendStatus() == ECMessage.MessageStatus.SENDING.ordinal()) {
            return DemoUtils.getDrawables(context, R.drawable.msg_state_sending);
        } else {
            return null;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;
        ViewHolder mViewHolder;
        if(convertView == null || convertView.getTag() == null) {
            view = View.inflate(mContext , R.layout.ytx_conversation_item, null);

            mViewHolder = new ViewHolder();
            mViewHolder.user_avatar = (ImageView) view.findViewById(R.id.avatar_iv);
            mViewHolder.prospect_iv = (ImageView) view.findViewById(R.id.avatar_prospect_iv);
            mViewHolder.nickname_tv = (TextView) view.findViewById(R.id.nickname_tv);
            mViewHolder.tipcnt_tv = (TextView) view.findViewById(R.id.tipcnt_tv);
            mViewHolder.update_time_tv = (TextView) view.findViewById(R.id.update_time_tv);
            mViewHolder.last_msg_tv = (CCPTextView) view.findViewById(R.id.last_msg_tv);
            mViewHolder.image_input_text = (ImageView) view.findViewById(R.id.image_input_text);
            view.setTag(mViewHolder);
        } else {
            view = convertView;
            mViewHolder = (ViewHolder) view.getTag();
        }

        ECConversation conversation = getItem(position);
        if(conversation != null) {
            if(TextUtils.isEmpty(conversation.getUsername())) {
                mViewHolder.nickname_tv.setText(conversation.getSessionId());
            } else {
                mViewHolder.nickname_tv.setText(conversation.getUsername());
            }
            mViewHolder.last_msg_tv.setEmojiText(getConversationSnippet(conversation));
            mViewHolder.last_msg_tv.setCompoundDrawables(getChattingSnippentCompoundDrawables(mContext, conversation), null, null, null);
            String msgCount = conversation.getUnreadCount() > 100 ? "..." : String.valueOf(conversation.getUnreadCount());
            mViewHolder.tipcnt_tv.setText(msgCount);
            mViewHolder.tipcnt_tv.setVisibility(conversation.getUnreadCount() == 0 ? View.GONE : View.VISIBLE);
            mViewHolder.image_input_text.setVisibility(View.GONE);
            mViewHolder.update_time_tv.setText(getConversationTime(conversation));
            if(conversation.getSessionId().toUpperCase().startsWith("G")) {
            	mViewHolder.user_avatar.setImageResource(R.drawable.group_head);


            } else {
                mViewHolder.user_avatar.setImageResource(R.drawable.default_avatar);
                if(conversation.getSessionId().equals(GroupNoticeSqlManager.CONTACT_ID)) {
                } else {
                }
            }
        }

        return view;
    }

    static class ViewHolder {
        ImageView user_avatar;
        TextView tipcnt_tv;
        ImageView prospect_iv;
        TextView nickname_tv;
        TextView update_time_tv;
        CCPTextView last_msg_tv;
        ImageView image_input_text;
    }

    @Override
    public void initCursor() {
        notifyChange();
    }

    @Override
    public void notifyChange() {
        if(mCallBackListener != null) {
            mCallBackListener.OnListAdapterCallBack();
        }
        Cursor cursor = ConversationSqlManager.getConversationCursor();
        setCursor(cursor);
        super.notifyDataSetChanged();
    }


}
