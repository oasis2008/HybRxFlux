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
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;







import com.yuntongxun.eckitsdk.R;
import com.yuntongxun.ecsdk.ECMessage;
import com.yuntongxun.ecsdk.im.ECFileMessageBody;
import com.yuntongxun.kitsdk.ui.ECChattingActivity;
import com.yuntongxun.kitsdk.ui.chatting.holder.BaseHolder;
import com.yuntongxun.kitsdk.ui.chatting.holder.FileRowViewHolder;
import com.yuntongxun.kitsdk.ui.chatting.view.ChattingItemContainer;



public class FileRxRow extends BaseChattingRow{

	public FileRxRow(int type) {
		super(type);
	}
	

	@Override
	public View buildChatView(LayoutInflater inflater, View convertView) {
        //we have a don't have a converView so we'll have to create a new one
        if (convertView == null || convertView.getTag() == null) {
            convertView = new ChattingItemContainer(inflater, R.layout.ytx_chatting_item_file_from);

            //use the view holder pattern to save of already looked up subviews
            FileRowViewHolder holder = new FileRowViewHolder(mRowType);
            convertView.setTag(holder.initBaseHolder(convertView, true));
        }
		return convertView;
	}

	@Override
	public void buildChattingData(final Context context, BaseHolder baseHolder,
			ECMessage detail, int position) {
		FileRowViewHolder holder = (FileRowViewHolder) baseHolder;
		if(detail != null) {

			ECMessage msg = detail;
			ECFileMessageBody body = (ECFileMessageBody) msg.getBody();
			holder.contentTv.setText(body.getFileName());
			ViewHolderTag holderTag = ViewHolderTag.createTag(detail, ViewHolderTag.TagType.TAG_VIEW_FILE, position);
        	holder.contentTv.setTag(holderTag);
        	holder.contentTv.setOnClickListener(((ECChattingActivity) context).getChattingAdapter().getOnClickListener());
        	
        }
	}
	
	@Override
	public int getChatViewType() {
		return ChattingRowType.FILE_ROW_RECEIVED.ordinal();
	}

	@Override
	public boolean onCreateRowContextMenu(ContextMenu contextMenu,
			View targetView, ECMessage detail) {
		// TODO Auto-generated method stub
		return false;
	}

}
