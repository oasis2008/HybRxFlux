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
package com.yuntongxun.kitsdk.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;

import com.yuntongxun.eckitsdk.R;
import com.yuntongxun.ecsdk.ECMessage;
import com.yuntongxun.ecsdk.ECVoIPCallManager;
import com.yuntongxun.kitsdk.core.CCPAppManager;
import com.yuntongxun.kitsdk.db.GroupNoticeSqlManager;
import com.yuntongxun.kitsdk.ui.voip.ECVoIPBaseActivity;


/**
 * 状态栏通知
 * @author Jorstin Chan@容联•云通讯
 * @date 2015-1-4
 * @version 4.0
 */
public class ECNotificationManager {

    public static final int CCP_NOTIFICATOIN_ID_CALLING = 0x1;

    public static final int NOTIFY_ID_PUSHCONTENT = 35;

    private Context mContext;

    private static NotificationManager mNotificationManager;

    public static ECNotificationManager mInstance;
    public static ECNotificationManager getInstance() {
        if(mInstance == null) {
            mInstance = new ECNotificationManager(CCPAppManager.getContext());
        }

        return mInstance;
    }


    private ECNotificationManager(Context context){
        mContext = context;
    }

   


    /**
     *
     * @param contex
     * @param fromUserName
     * @param msgType
     * @return
     */
    public final String getTickerText(Context contex ,String fromUserName ,int msgType) {
        if(msgType == ECMessage.Type.TXT.ordinal()) {
            return contex.getResources().getString(R.string.notification_fmt_one_txttype, fromUserName);
        } else if (msgType == ECMessage.Type.IMAGE.ordinal()) {
            return contex.getResources().getString(R.string.notification_fmt_one_imgtype, fromUserName);
        } else if (msgType == ECMessage.Type.VOICE.ordinal()) {
            return contex.getResources().getString(R.string.notification_fmt_one_voicetype, fromUserName);
        } else if (msgType == ECMessage.Type.FILE.ordinal()) {
            return contex.getResources().getString(R.string.notification_fmt_one_filetype, fromUserName);
        } else if (msgType == GroupNoticeSqlManager.NOTICE_MSG_TYPE) {
            return contex.getResources().getString(R.string.str_system_message_group_notice);
        } else {
            //return contex.getResources().getString(R.string.app_name);
            return contex.getPackageManager().getApplicationLabel(contex.getApplicationInfo()).toString();
        }

    }

    public final String getContentTitle(Context context ,int sessionUnreadCount, String fromUserName) {
        if(sessionUnreadCount > 1) {
            return context.getString(R.string.app_name);
        }

        return fromUserName;
    }

    /**
     *
     * @param context
     * @return
     */
    public final String getContentText(Context context , int sessionCount , int sessionUnread , String pushContent ,int lastMsgType) {

        if (sessionCount > 1) {

            return context.getResources().getQuantityString(
                    R.plurals.notification_fmt_multi_msg_and_talker,1,
                    sessionCount, sessionUnread);
        }

        if(sessionUnread > 1) {
            return context.getResources().getQuantityString(
                    R.plurals.notification_fmt_multi_msg_and_one_talker, sessionUnread,sessionUnread);
        }

        if(lastMsgType == ECMessage.Type.TXT.ordinal()) {
            return pushContent;
        } else if (lastMsgType == ECMessage.Type.FILE.ordinal()) {
            return context.getResources().getString(R.string.app_file);
        } else if (lastMsgType == ECMessage.Type.VOICE.ordinal()) {
            return context.getResources().getString(R.string.app_voice);
        } else if (lastMsgType == ECMessage.Type.IMAGE.ordinal()) {
            return context.getResources().getString(R.string.app_pic);
        } else {
            return pushContent;
        }

    }

    private void cancel() {
        NotificationManager notificationManager = (NotificationManager) CCPAppManager
                .getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager == null) {
            return;
        }
        notificationManager.cancel(0);
    }

    /**
     * 取消所有的状态栏通知
     */
    public final void forceCancelNotification() {
        cancel();
        NotificationManager notificationManager = (NotificationManager) CCPAppManager
                .getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager == null) {
            return;
        }
        notificationManager.cancel(NOTIFY_ID_PUSHCONTENT);

    }

    public final Looper getLooper() {
        return Looper.getMainLooper();
    }

   
    
    /**
     * 后台呈现音视频呼叫Notification
     * @param callType 呼叫类型
     */
    public static void showCallingNotification(ECVoIPCallManager.CallType callType) {
        try {
            getInstance().checkNotification();
//            String topic = getInstance().mContext.getString(R.string.ec_voip_is_talking_tip);
            String topic = " 正在通话中, 轻击以继续";
            Intent intent;
            if(callType == ECVoIPCallManager.CallType.VIDEO) {
                intent = new Intent(ECVoIPBaseActivity.ACTION_VIDEO_CALL);
            } else {
                intent = new Intent(ECVoIPBaseActivity.ACTION_VOICE_CALL);
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent contentIntent = PendingIntent.getActivity(getInstance().mContext,
                    R.string.app_name,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            Notification.Builder builder = new Notification.Builder(getInstance().mContext);
            builder.setLights(0xFFFFFF, 300, 1000)
                    .setAutoCancel(true)
                    .setTicker(topic)
                    .setContentIntent(contentIntent);
            builder.setDefaults(Notification.DEFAULT_VIBRATE);
            mNotificationManager.notify(CCP_NOTIFICATOIN_ID_CALLING, builder.getNotification());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void checkNotification() {
        if(mNotificationManager == null) {
            mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        }
    }
    
    public static void cancelCCPNotification(int id) {
        getInstance().checkNotification();
        mNotificationManager.cancel(id);
    }

}
