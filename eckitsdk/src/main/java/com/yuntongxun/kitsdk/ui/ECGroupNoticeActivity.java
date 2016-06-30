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
package com.yuntongxun.kitsdk.ui;

import android.content.Context;



import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
















import com.yuntongxun.eckitsdk.R;
import com.yuntongxun.ecsdk.ECDevice;
import com.yuntongxun.ecsdk.ECError;
import com.yuntongxun.ecsdk.ECGroupManager;
import com.yuntongxun.ecsdk.SdkErrorCode;
import com.yuntongxun.ecsdk.im.ECAckType;
import com.yuntongxun.ecsdk.im.group.ECGroupNoticeMessage;
import com.yuntongxun.kitsdk.adapter.CCPListAdapter;
import com.yuntongxun.kitsdk.beans.DemoGroupNotice;
import com.yuntongxun.kitsdk.beans.NoticeSystemMessage;
import com.yuntongxun.kitsdk.db.GroupNoticeSqlManager;
import com.yuntongxun.kitsdk.group.GroupNoticeHelper;
import com.yuntongxun.kitsdk.utils.DateUtil;
import com.yuntongxun.kitsdk.utils.LogUtil;
import com.yuntongxun.kitsdk.utils.ToastUtil;
import com.yuntongxun.kitsdk.view.ECProgressDialog;


/**
 * 群组通知列表界面
 * @author Jorstin Chan@容联•云通讯
 * @date 2014-12-31
 * @version 4.0
 */
public class ECGroupNoticeActivity extends ECSuperActivity implements
        View.OnClickListener {

    private static final String TAG = "ECDemo.GroupNoticeActivity";

    /**会话消息列表ListView*/
    private ListView mListView;
    private GroupNoticeAdapter mAdapter;
    private ECProgressDialog mPostingdialog;

    @Override
    protected int getLayoutId() {
        return R.layout.ytx_group_notice_activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();

        getTopBarView().setTopBarToStatus(1, R.drawable.ytx_topbar_back_bt, getString(R.string.app_clear), getString(R.string.app_title_notice), this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        GroupNoticeSqlManager.setAllSessionRead();
        GroupNoticeSqlManager.registerMsgObserver(mAdapter);
        mAdapter.notifyChange();
    }

    @Override
    protected void onPause() {
        super.onPause();
        GroupNoticeSqlManager.unregisterMsgObserver(mAdapter);
    }


    /**
     *
     */
    private void initView() {
        if(mListView != null) {
            mListView.setAdapter(null);
        }

        mListView = (ListView) findViewById(R.id.group_notice_lv);
        View mCallEmptyView = findViewById(R.id.empty_conversation_tv);
        mListView.setEmptyView(mCallEmptyView);
        mListView.setDrawingCacheEnabled(false);
        mListView.setScrollingCacheEnabled(false);

        mListView.setOnItemClickListener(null);

        mAdapter = new GroupNoticeAdapter(this);
        mListView.setAdapter(mAdapter);
    }
    
    public static interface OnAckGroupServiceListener {
        void onAckGroupService(boolean success);
    }


    public class GroupNoticeAdapter extends CCPListAdapter<DemoGroupNotice> {

        /**
         * @param ctx
         */
        public GroupNoticeAdapter(Context ctx) {
            super(ctx, new DemoGroupNotice());
        }

        @Override
        public void initCursor() {
            notifyChange();
        }

        @Override
        public DemoGroupNotice getItem(DemoGroupNotice t,
                                          Cursor cursor) {
            DemoGroupNotice message = new DemoGroupNotice();
            message.setCursor(cursor);
            return message;
        }

        public final CharSequence getContent(NoticeSystemMessage message) {
            if(message.getType() == ECGroupNoticeMessage.ECGroupMessageType.QUIT) {

            }
            return message.getContent();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view;
            ViewHolder mViewHolder;
            if (convertView == null || convertView.getTag() == null) {
                view = View.inflate(mContext, R.layout.ytx_group_notice_list_item, null);

                mViewHolder = new ViewHolder();
                mViewHolder.msgType = (TextView) view.findViewById(R.id.msg_type);
                mViewHolder.nickname = (TextView) view.findViewById(R.id.user_nickname);
                mViewHolder.ImageViewHeader = (ImageView) view.findViewById(R.id.ImageViewHeader);
                mViewHolder.msgTime = (TextView) view.findViewById(R.id.msg_time);
                mViewHolder.sysMsgFrom = (TextView) view.findViewById(R.id.sysMsg_from);
                mViewHolder.resultShow = (TextView) view.findViewById(R.id.result_show);
                mViewHolder.resultSummary = (TextView) view.findViewById(R.id.result_summary);
                mViewHolder.acceptBtn = (Button) view.findViewById(R.id.accept_btn);
                mViewHolder.refuseBtn = (Button) view.findViewById(R.id.Refuse_btn);
                mViewHolder.operationLy = (LinearLayout) view.findViewById(R.id.operation_ly);
                view.setTag(mViewHolder);
            } else {
                view = convertView;
                mViewHolder = (ViewHolder) view.getTag();
            }

            final DemoGroupNotice item = getItem(position);
            mViewHolder.nickname.setText(item.getGroupName());
            mViewHolder.resultSummary.setText(item.getContent());
            mViewHolder.sysMsgFrom.setText(getString(R.string.str_system_come_from, item.getGroupName()));
            mViewHolder.sysMsgFrom.setVisibility(View.GONE);
            if(!TextUtils.isEmpty(item.getDeclared())) {
                mViewHolder.sysMsgFrom.setText("附加消息：" + item.getDeclared());
                mViewHolder.sysMsgFrom.setVisibility(View.VISIBLE);
            }
            if(item.getDateCreate() > 0) {
                mViewHolder.msgTime.setText(DateUtil.getDateString(item.getDateCreate() , DateUtil.SHOW_TYPE_CALL_LOG));
            } else  {
                mViewHolder.msgTime.setText("");
            }

            if(item.getConfirm() == GroupNoticeHelper.SYSTEM_MESSAGE_NEED_REPLAY) {

                // System information about the invitation to join the group
                // or join the group needs to operate, Whether is it right? Read or unread,
                // as long as the state has not operation can display the operating button
                mViewHolder.operationLy.setVisibility(View.VISIBLE);
                mViewHolder.resultShow.setVisibility(View.GONE);

            } else {
                // Other notice about information, only need to display
                // without the need to have relevant operation
                mViewHolder.operationLy.setVisibility(View.GONE);
                mViewHolder.resultShow.setVisibility(View.VISIBLE);
                if (item.getConfirm() == GroupNoticeHelper.SYSTEM_MESSAGE_REFUSE) {
                    mViewHolder.resultShow.setText(R.string.str_system_message_operation_result_refuse);
                } else if (item.getConfirm() == GroupNoticeHelper.SYSTEM_MESSAGE_THROUGH) {
                    mViewHolder.resultShow.setText(R.string.str_system_message_operation_result_through);

                } else{
                    mViewHolder.resultShow.setVisibility(View.GONE);
                }
            }


            mViewHolder.acceptBtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //
                    OperationGroupSystemMsg(true , item);
                }
            });
            mViewHolder.refuseBtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    OperationGroupSystemMsg(false, item);
                }
            });
            return view;
        }

        @Override
        public void notifyChange() {
            Cursor cursor = GroupNoticeSqlManager.getCursor();
            setCursor(cursor);
            super.notifyDataSetChanged();
        }
        
        private  boolean isSuccess(ECError error) {
            if(error.errorCode == SdkErrorCode.REQUEST_SUCCESS)  {
                return true;
            }
            return false;
        }
        
        
        
        
        public void operationGroupApplyOrInvite(boolean inviteAck ,String groupId , String member , ECAckType ackType , final OnAckGroupServiceListener listener) {
            if(!inviteAck) {
                ECDevice.getECGroupManager().ackJoinGroupRequest(groupId , member , ackType , new ECGroupManager.OnAckJoinGroupRequestListener() {
                    @Override
                    public void onAckJoinGroupRequestComplete(ECError error, String groupId, String member) {
                        if(isSuccess(error)) {

                            if(listener != null) {
                                listener.onAckGroupService(true);
                            }
                            return ;
                        }
                        ToastUtil.showMessage("操作失败");
                    }

                    public void onComplete(ECError error) {

                    }
                });
                return ;
            }


            ECDevice.getECGroupManager().ackInviteJoinGroupRequest(groupId, ackType,member, new ECGroupManager.OnAckInviteJoinGroupRequestListener() {
                @Override
                public void onAckInviteJoinGroupRequestComplete(ECError error, String groupId) {
                    if(isSuccess(error)) {

                        if(listener != null) {
                            listener.onAckGroupService(true);
                        }
                        return ;
                    }
                    ToastUtil.showMessage("操作失败");
                }

                public void onComplete(ECError error) {

                }
            });


        } 
        
        
        
        


        /**
         * 处理接受或者拒绝邀请
         * @param isAccept
         * @param imSystemMessage
         */
        protected void OperationGroupSystemMsg(final boolean isAccept,final  DemoGroupNotice imSystemMessage) {
            showProcessDialog(getString(R.string.login_posting_submit));
            synchronized (ECGroupNoticeActivity.class) {

                boolean isInvite = imSystemMessage.getAuditType() == ECGroupNoticeMessage.ECGroupMessageType.INVITE.ordinal();
                ECAckType ackType = isAccept ? ECAckType.AGREE : ECAckType.REJECT;
                
                
                operationGroupApplyOrInvite(isInvite ,imSystemMessage.getGroupId(), isInvite?imSystemMessage.getAdmin():imSystemMessage.getMember(), ackType, new OnAckGroupServiceListener() {
                    public void onAckGroupService(boolean success) {
                        long rows = GroupNoticeSqlManager.updateNoticeOperation(imSystemMessage.getId(), isAccept);
                        notifyChange();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dismissPostingDialog();
                            }
                        });
                    }
                });
            }
        }

    }


    static class ViewHolder {
        LinearLayout operationLy;
        TextView msgType;
        TextView resultShow;
        TextView nickname;
        TextView sysMsgFrom;
        TextView msgTime;
        ImageView ImageViewHeader;
        TextView resultSummary;
        Button acceptBtn; // accetp
        Button refuseBtn;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mAdapter != null) {
            mAdapter.closeCursor();
        }
        System.gc();
    }

    @Override
    public void onClick(View v) {
        
        
        if(v.getId()==R.id.btn_left){
        	hideSoftKeyboard();
            finish();
        	
        }else if(v.getId()==R.id.text_right){
        	GroupNoticeSqlManager.delSessions();
            mAdapter.notifyChange();
        }
        
        
        
    }


    void showProcessDialog(String tips) {
        mPostingdialog = new ECProgressDialog(ECGroupNoticeActivity.this, R.string.login_posting_submit);
        mPostingdialog.show();
    }

    /**
     * 关闭对话框
     */
    private void dismissPostingDialog() {
        if(mPostingdialog == null || !mPostingdialog.isShowing()) {
            return ;
        }
        mPostingdialog.dismiss();
        mPostingdialog = null;
    }
}
