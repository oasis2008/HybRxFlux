package com.yuntongxun.kitsdk;


import com.yuntongxun.ecsdk.ECDevice;

import com.yuntongxun.ecsdk.ECInitParams;
import com.yuntongxun.kitsdk.beans.ECAuthParameters;
import com.yuntongxun.kitsdk.core.CCPAppManager;
import com.yuntongxun.kitsdk.core.ECKitCustomProviderManager;
import com.yuntongxun.kitsdk.db.ConversationSqlManager;
import com.yuntongxun.kitsdk.db.GroupNoticeSqlManager;
import com.yuntongxun.kitsdk.db.GroupSqlManager;
import com.yuntongxun.kitsdk.db.IMessageSqlManager;
import com.yuntongxun.kitsdk.db.ImgInfoSqlManager;
import com.yuntongxun.kitsdk.listener.OnConnectSDKListener;
import com.yuntongxun.kitsdk.listener.OnInitSDKListener;
import com.yuntongxun.kitsdk.listener.OnLogoutSDKListener;
import com.yuntongxun.kitsdk.ui.chatting.model.IMChattingHelper;
import com.yuntongxun.kitsdk.ui.voip.VoIPCallActivity;
import com.yuntongxun.kitsdk.utils.FileAccessor;
import com.yuntongxun.kitsdk.utils.LogUtil;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

public class ECDeviceKit {

	protected static ECDeviceKit sInstance;

	private static String userId;
	private static ECInitParams mInitParams;
	private ECInitParams.LoginMode mMode = ECInitParams.LoginMode.FORCE_LOGIN;

	private static Context mContext;

	public static ECDeviceKit getInstance() {
		if (sInstance == null) {
			synchronized (ECDeviceKit.class) {
				sInstance = new ECDeviceKit();

			}
		}

		return sInstance;
	}

	private ECDeviceKit() {

	}

	public static Context getmContext() {
		return mContext;
	}

	public static void setmContext(Context mContext) {
		ECDeviceKit.mContext = mContext;
	}

	/**
	 * 
	 * @param user
	 * @param context
	 * @param l
	 */
	public static void init(String user, Context context, OnInitSDKListener l) {
		try {
			userId = user;
			FileAccessor.initFileAccess();
			CCPAppManager.setContext(context);
			setmContext(context);

			initSql();
		} catch (Exception e) {

			LogUtil.e("please check your sdcard is mounted");
		}

		ECKitSDKCoreRouteManager.init(context, l);

	}

	public static IMKitManager getIMKitManager() {

		return IMKitManager.getInstance();
	}
	public static VoipKitManager getVoipKitManager() {
		
		return VoipKitManager.getInstance();
	}

	public String getUserId() {
		return userId;
	}

	public static void login(ECAuthParameters initParams, OnConnectSDKListener l) {

		if (initParams == null) {
			LogUtil.e("initparams cannot be null,please check it");
			return;
		}

		ECKitSDKCoreRouteManager.setOnConnectSDKListener(l);

		if (mInitParams == null || mInitParams.getInitParams() == null
				|| mInitParams.getInitParams().isEmpty()) {
			mInitParams = ECInitParams.createParams();
		}
		mInitParams.reset();
		// 如：VoIP账号/手机号码/..
		mInitParams.setUserid(initParams.getUserId());
		// appkey
		mInitParams.setAppKey(initParams.getAppKey());
		mInitParams.setToken(initParams.getAppToken());
		mInitParams.setMode(initParams.getLoginMode());
		// 如果有密码（VoIP密码，对应的登陆验证模式是）
		// ECInitParams.LoginAuthType.PASSWORD_AUTH
		if (!TextUtils.isEmpty(initParams.getPwd())) {
			mInitParams.setPwd(initParams.getPwd());
		}

		// 设置登陆验证模式（是否验证密码/如VoIP方式登陆）
		if (initParams.getLoginType() != null) {
			mInitParams.setAuthType(initParams.getLoginType());
		}
		mInitParams.setOnChatReceiveListener(IMChattingHelper.getInstance());
		mInitParams.setOnDeviceConnectListener(ECKitSDKCoreRouteManager
				.getInstance());
		
		
        Intent intent = new Intent(getInstance().mContext, VoIPCallActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity( getInstance().mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mInitParams.setPendingIntent(pendingIntent);


		ECDevice.login(mInitParams);
	}

	public static void logout(OnLogoutSDKListener l) {

		ECKitSDKCoreRouteManager.logout();
		release();
	}

	public static void unInitial() {

		release();
		ECDevice.unInitial();
	}

	public static void release() {
		IMChattingHelper.getInstance().destory();
		ECKitCustomProviderManager.release();
		ConversationSqlManager.reset();
		GroupNoticeSqlManager.reset();
		GroupSqlManager.reset();
		IMessageSqlManager.reset();
		ImgInfoSqlManager.reset();

	}

	public static void initSql() {

		ConversationSqlManager.getInstance();
		GroupNoticeSqlManager.getInstance();
		GroupSqlManager.getInstance();
	}

}
