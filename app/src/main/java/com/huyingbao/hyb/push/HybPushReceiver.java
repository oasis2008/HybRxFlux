package com.huyingbao.hyb.push;

import android.content.Context;

import com.baidu.android.pushservice.PushMessageReceiver;
import com.huyingbao.hyb.HybApp;
import com.orhanobut.logger.Logger;

import java.util.List;

/**
 * Created by Administrator on 2016/6/8.
 */
public class HybPushReceiver extends PushMessageReceiver {
    @Override
    public void onBind(Context context, int errorCode, String appid, String userId, String channelId, String requestId) {
        if (errorCode == 0) {
            HybApp.getInstance().getLocalSorageUtils().setChannelId(channelId);
        }

    }

    @Override
    public void onUnbind(Context context, int i, String s) {

    }

    @Override
    public void onSetTags(Context context, int i, List<String> list, List<String> list1, String s) {

    }

    @Override
    public void onDelTags(Context context, int i, List<String> list, List<String> list1, String s) {

    }

    @Override
    public void onListTags(Context context, int i, List<String> list, String s) {

    }

    @Override
    public void onMessage(Context context, String s, String s1) {
        Logger.e(s);
    }

    @Override
    public void onNotificationClicked(Context context, String s, String s1, String s2) {

    }

    @Override
    public void onNotificationArrived(Context context, String s, String s1, String s2) {

    }
}
