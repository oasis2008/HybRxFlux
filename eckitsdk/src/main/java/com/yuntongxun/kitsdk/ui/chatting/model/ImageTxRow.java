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


import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yuntongxun.eckitsdk.R;
import com.yuntongxun.ecsdk.ECMessage;
import com.yuntongxun.ecsdk.im.ECFileMessageBody;
import com.yuntongxun.kitsdk.db.ImgInfoSqlManager;
import com.yuntongxun.kitsdk.ui.ECChattingActivity;
import com.yuntongxun.kitsdk.ui.chatting.holder.BaseHolder;
import com.yuntongxun.kitsdk.ui.chatting.holder.ImageRowViewHolder;
import com.yuntongxun.kitsdk.ui.chatting.view.ChattingItemContainer;
import com.yuntongxun.kitsdk.utils.DemoUtils;
import com.yuntongxun.kitsdk.utils.FileAccessor;
import com.yuntongxun.kitsdk.utils.ResourceHelper;


public class ImageTxRow extends BaseChattingRow {

    public ImageTxRow(int type){
        super(type);
    }

    @Override
    public View buildChatView(LayoutInflater inflater, View convertView) {
        //we have a don't have a converView so we'll have to create a new one
        if (convertView == null) {
            convertView = new ChattingItemContainer(inflater, R.layout.ytx_chatting_item_to_picture);

            //use the view holder pattern to save of already looked up subviews
            ImageRowViewHolder holder = new ImageRowViewHolder(mRowType);
            convertView.setTag(holder.initBaseHolder(convertView, false));

        }
        return convertView;
    }

    @Override
    public void buildChattingData(Context context, BaseHolder baseHolder,
                                  ECMessage detail, int position) {
        ImageRowViewHolder holder = (ImageRowViewHolder) baseHolder;
        ECFileMessageBody body = (ECFileMessageBody) detail.getBody();
        String userData = detail.getUserData();
        if(TextUtils.isEmpty(userData)) {
            return ;
        }
        int start = userData.indexOf("THUMBNAIL://");
        if(start != -1) {
            String thumbnail = userData.substring(start);
            // holder.chattingContentIv.setImageBitmap(ImgInfoSqlManager.getInstance().getThumbBitmap(thumbnail, 2));
            ImgInfo imgInfo = ImgInfoSqlManager.getInstance().getImgInfo(detail.getMsgId());
            if(imgInfo != null && !TextUtils.isEmpty(imgInfo.getBigImgPath())) {

                String uri = "file://" + FileAccessor.getImagePathName() + "/" + imgInfo.getBigImgPath();
                ImageLoader.getInstance().displayImage(uri, holder.chattingContentIv, DemoUtils.getChatDisplayImageOptions());
            }
        } else {
            holder.chattingContentIv.setImageBitmap(null);
        }
        int startWidth = userData.indexOf("outWidth://");
        int startHeight = userData.indexOf(",outHeight://");
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) holder.chattingContentIv.getLayoutParams();
        if(startWidth != -1 && startHeight != -1 && start != -1) {
            int imageMinWidth = /*DemoUtils.getImageMinWidth(context)*/ ResourceHelper.fromDPToPix(context , 100);
            int width = DemoUtils.getInt(userData.substring(startWidth + "outWidth://".length(), startHeight), imageMinWidth);
            int height = DemoUtils.getInt(userData.substring(startHeight + ",outHeight://".length(), start - 1) , imageMinWidth);
            holder.chattingContentIv.setMinimumWidth(imageMinWidth);
            params.width = imageMinWidth;
            int _height = height * imageMinWidth /width;
            if(_height > ResourceHelper.fromDPToPix(context , 230)) {
                _height = ResourceHelper.fromDPToPix(context , 230);
                holder.chattingContentIv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
            if(width != 0) {
                holder.chattingContentIv.setMinimumHeight(_height);
                params.height = _height;
            } else {
                holder.chattingContentIv.setMinimumHeight(imageMinWidth);
                params.height = imageMinWidth;
            }
            holder.chattingContentIv.setLayoutParams(params);
        }
        ViewHolderTag holderTag = ViewHolderTag.createTag(detail, ViewHolderTag.TagType.TAG_VIEW_PICTURE ,position);
        OnClickListener onClickListener = ((ECChattingActivity) context).getChattingAdapter().getOnClickListener();
        holder.chattingContentIv.setTag(holderTag);
        holder.chattingContentIv.setOnClickListener(onClickListener);
        getMsgStateResId(position, holder, detail, onClickListener);
    }

    @Override
    public int getChatViewType() {
        return ChattingRowType.IMAGE_ROW_TRANSMIT.ordinal();
    }


    @Override
    public boolean onCreateRowContextMenu(ContextMenu contextMenu,
                                          View targetView, ECMessage detail) {
        // TODO Auto-generated method stub
        return false;
    }

}
