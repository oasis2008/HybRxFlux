/*
 *  Copyright (c) 2015 The CCP project authors. All Rights Reserved.
 *
 *  Use of this source code is governed by a Beijing Speedtong Information Technology Co.,Ltd license
 *  that can be found in the LICENSE file in the root of the web site.
 *
 *   http://www.yuntongxun.com
 *
 *  An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */package com.yuntongxun.kitsdk.core;

import java.io.File;







import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.Intents;
import android.text.TextUtils;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.yuntongxun.ecsdk.ECDevice;
import com.yuntongxun.ecsdk.ECVoIPCallManager;
import com.yuntongxun.kitsdk.beans.ClientUser;
import com.yuntongxun.kitsdk.beans.ImageMsgInfoEntry;
import com.yuntongxun.kitsdk.beans.ViewImageInfo;
import com.yuntongxun.kitsdk.setting.ECPreferenceSettings;
import com.yuntongxun.kitsdk.setting.ECPreferences;
import com.yuntongxun.kitsdk.ui.ECChattingActivity;
import com.yuntongxun.kitsdk.ui.ECSuperActivity;
import com.yuntongxun.kitsdk.ui.ECImageGalleryActivity;
import com.yuntongxun.kitsdk.ui.ECImageGralleryPagerActivity;
import com.yuntongxun.kitsdk.ui.chatting.view.ChatFooterPanel;
import com.yuntongxun.kitsdk.ui.voip.VideoActivity;
import com.yuntongxun.kitsdk.ui.voip.VoIPCallActivity;
import com.yuntongxun.kitsdk.ui.voip.VoIPCallHelper;
import com.yuntongxun.kitsdk.utils.LogUtil;
import com.yuntongxun.kitsdk.utils.MimeTypesTools;


/**
 * 存储SDK一些全局性的常量
 * Created by Jorstin on 2015/3/17.
 */
public class CCPAppManager {

    public static Md5FileNameGenerator md5FileNameGenerator = new Md5FileNameGenerator();
    /**Android 应用上下文*/
    private static Context mContext = null;
    /**包名*/
    public static String pkgName = "com.yuntongxun.kitsdk";
    /**SharedPreferences 存储名字前缀*/
    public static final String PREFIX = "com.yuntongxun.kitsdk_";
    public static final int FLAG_RECEIVER_REGISTERED_ONLY_BEFORE_BOOT = 0x10000000;
    /**IM功能UserData字段默认文字*/
    public static final String USER_DATA = "yuntongxun.kitsdk";
    public static HashMap<String, Integer> mPhotoCache = new HashMap<String, Integer>();
    public static ArrayList<ECSuperActivity> activities = new ArrayList<ECSuperActivity>();
    /**IM聊天更多功能面板*/
    private static ChatFooterPanel mChatFooterPanel;
    public static String getPackageName() {
        return pkgName;
    }
    private static ClientUser mClientUser;
    /**
     * 返回SharePreference配置文件名称
     * @return
     */
    public static String getSharePreferenceName() {
        return pkgName + "_preferences";
    }

    /**
     * 返回上下文对象
     * @return
     */
    public static Context getContext(){
        return mContext;
    }

    /**
     * 设置上下文对象
     * @param context
     */
    public static void setContext(Context context) {
        mContext = context;
        pkgName = context.getPackageName();
        LogUtil.d(LogUtil.getLogUtilsTag(CCPAppManager.class),
                "setup application context for package: " + pkgName);
    }

    public static ChatFooterPanel getChatFooterPanel(Context context) {
        return mChatFooterPanel;
    }

    /**
     * 缓存账号注册信息
     * @param user
     */
    public static void setClientUser(ClientUser user) {
        mClientUser = user;
    }

    /**
     * 保存注册账号信息
     * @return
     */
    public static ClientUser getClientUser() {
        if(mClientUser != null) {
            return mClientUser;
        }
        String registAccount = getAutoRegistAccount();
        if(!TextUtils.isEmpty(registAccount)) {
            mClientUser = new ClientUser("");
            return mClientUser.from(registAccount);
        }
        return null;
    }

    private static String getAutoRegistAccount() {
        SharedPreferences sharedPreferences = ECPreferences.getSharedPreferences();
        ECPreferenceSettings registAuto = ECPreferenceSettings.SETTINGS_REGIST_AUTO;
        String registAccount = sharedPreferences.getString(registAuto.getId(), (String) registAuto.getDefaultValue());
        return registAccount;
    }

    /**
     * @param context
     * @param path
     */
    public static void doViewFilePrevieIntent(Context context ,String path) {
        try {
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(android.content.Intent.ACTION_VIEW);
            String type = MimeTypesTools.getMimeType(context, path);
            File file = new File(path);
            intent.setDataAndType(Uri.fromFile(file), type);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e(LogUtil.getLogUtilsTag(CCPAppManager.class), "do view file error " + e.getLocalizedMessage());
        }
    }

    /**
     *
     * @param cotnext
     * @param value
     */
    public static void startChattingImageViewAction(Context cotnext, ImageMsgInfoEntry value) {
        Intent intent = new Intent(cotnext, ECImageGralleryPagerActivity.class);
        intent.putExtra(ECImageGalleryActivity.CHATTING_MESSAGE, value);
        cotnext.startActivity(intent);
    }

    /**
     * 批量查看图片
     * @param ctx
     * @param position
     * @param session
     */
    public static void startChattingImageViewAction(Context ctx , int position , ArrayList<ViewImageInfo> session) {
        Intent intent = new Intent(ctx, ECImageGralleryPagerActivity.class);
        intent.putExtra(ECImageGralleryPagerActivity.EXTRA_IMAGE_INDEX, position);
        intent.putParcelableArrayListExtra(ECImageGralleryPagerActivity.EXTRA_IMAGE_URLS, session);
        ctx.startActivity(intent);
    }

    /**
     * 获取应用程序版本名称
     * @return
     */
    public static String getVersion() {
        String version = "0.0.0";
        if(mContext == null) {
            return version;
        }
        try {
            PackageInfo packageInfo = mContext.getPackageManager().getPackageInfo(
                    getPackageName(), 0);
            version = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return version;
    }

    /**
     * 获取应用版本号
     * @return 版本号
     */
    public static int getVersionCode() {
        int code = 1;
        if(mContext == null) {
            return code;
        }
        try {
            PackageInfo packageInfo = mContext.getPackageManager().getPackageInfo(
                    getPackageName(), 0);
            code = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return code;
    }


    public static void addActivity(ECSuperActivity activity) {
        activities.add(activity);
    }

    public static void clearActivity() {
        for(ECSuperActivity activity : activities) {
            if(activity != null) {
                activity.finish();
                activity = null;
            }
            activities.clear();
        }
    }

   

    public static HashMap<String, Object> prefValues = new HashMap<String, Object>();

    /**
     *
     * @param key
     * @param value
     */
    public static void putPref(String key , Object value) {
        prefValues.put(key, value);
    }

    public static Object getPref(String key) {
        return prefValues.remove(key);
    }

    public static void removePref(String key) {
        prefValues.remove(key);
    }
    
    
    public static void startChattingAction(Context context , String contactid , String username) {
        startChattingAction(context , contactid , username , false);
    }

    /**
     *
     * @param context
     * @param contactid
     * @param username
     * @param clearTop
     */
    public static void startChattingAction(Context context , String contactid , String username , boolean clearTop) {
        Intent intent = new Intent(context, ECChattingActivity.class);
        if(clearTop) {
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        intent.putExtra(ECKitConstant.KIT_CONVERSATION_TARGET, contactid);
        intent.putExtra(ECChattingActivity.CONTACT_USER, username);
        context.startActivity(intent);
    }
    
    
    /**
     * VoIP呼叫
     * @param nickname 昵称
     * @param contactId 呼出号码
     */
    public static void callVoIPAction(Context ctx , String nickname, String contactId, String voipFrom,boolean isCallBack) {
        // VoIP呼叫
        callVoIPAction(ctx , ECVoIPCallManager.CallType.VOICE , nickname , contactId,voipFrom,isCallBack );
    }

    /**
     * 根据呼叫类型通话
     * @param ctx 上下文
     * @param callType 呼叫类型
     * @param nickname 昵称
     * @param contactId 号码
     */
    public static void callVoIPAction(Context ctx , ECVoIPCallManager.CallType callType ,String nickname, String contactId,String voipFrom,boolean isCallBack) {
        // VoIP呼叫
        Intent callAction = new Intent(ctx , VoIPCallActivity.class);
        if(callType == ECVoIPCallManager.CallType.VIDEO) {
            callAction = new Intent(ctx , VideoActivity.class);
            VoIPCallHelper.mHandlerVideoCall = true;
        } else {
            VoIPCallHelper.mHandlerVideoCall = false;
        }
        callAction.putExtra(VoIPCallActivity.EXTRA_CALL_NAME , nickname);
        callAction.putExtra(VoIPCallActivity.EXTRA_CALL_NUMBER , contactId);
        callAction.putExtra(VoIPCallActivity.EXTRA_CALL_FROM, voipFrom);
        callAction.putExtra(ECDevice.CALLTYPE , callType);
        callAction.putExtra(VoIPCallActivity.EXTRA_OUTGOING_CALL , true);
        callAction.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if(isCallBack){
        	callAction.putExtra(VoIPCallActivity.ACTION_CALLBACK_CALL, true);	
        }
        ctx.startActivity(callAction);
    }
	
  
	
	
}
