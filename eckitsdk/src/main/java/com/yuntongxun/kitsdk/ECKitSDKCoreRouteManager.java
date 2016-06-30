package com.yuntongxun.kitsdk;


import android.content.Context;

import com.yuntongxun.ecsdk.ECDevice;
import com.yuntongxun.ecsdk.ECError;
import com.yuntongxun.ecsdk.ECInitParams;
import com.yuntongxun.ecsdk.platformtools.ECHandlerHelper;
import com.yuntongxun.kitsdk.core.CCPAppManager;
import com.yuntongxun.kitsdk.listener.OnConnectSDKListener;
import com.yuntongxun.kitsdk.listener.OnInitSDKListener;
import com.yuntongxun.kitsdk.listener.OnLogoutSDKListener;
import com.yuntongxun.kitsdk.utils.LogUtil;


public class ECKitSDKCoreRouteManager implements ECDevice.InitListener,
		ECDevice.OnECDeviceConnectListener, ECDevice.OnLogoutListener {

	private static final String TAG = "ECKitSDKCoreRouteManager";
	public static final String ACTION_LOGOUT = "com.yuntongxun.ECDemo_logout";
	public static final String ACTION_SDK_CONNECT = "com.yuntongxun.Intent_Action_SDK_CONNECT";
	public static final String ACTION_KICK_OFF = "com.yuntongxun.Intent_ACTION_KICK_OFF";
	private static ECKitSDKCoreRouteManager sInstance;
	private ECInitParams.LoginMode mMode = ECInitParams.LoginMode.FORCE_LOGIN;

	private static OnInitSDKListener onInitSDKListener;
	private static OnConnectSDKListener onConnectSDKListener;
	private static OnLogoutSDKListener onLogoutSDKListener;

	public static void setOnLogoutSDKListener(
			OnLogoutSDKListener onLogoutSDKListener) {
		ECKitSDKCoreRouteManager.onLogoutSDKListener = onLogoutSDKListener;
	}

	public static void setOnInitSDKListener(OnInitSDKListener onInitSDKListener) {
		ECKitSDKCoreRouteManager.onInitSDKListener = onInitSDKListener;
	}

	public static void setOnConnectSDKListener(
			OnConnectSDKListener onConnectSDKListener) {
		ECKitSDKCoreRouteManager.onConnectSDKListener = onConnectSDKListener;
	}

	private ECKitSDKCoreRouteManager() {
	}

	public static ECKitSDKCoreRouteManager getInstance() {
		if (sInstance == null) {
			sInstance = new ECKitSDKCoreRouteManager();
		}
		return sInstance;
	}

	public static void init(Context ctx, OnInitSDKListener l) {
		init(ctx, ECInitParams.LoginMode.AUTO, l);
	}

	@SuppressWarnings("static-access")
	public static void init(Context ctx, ECInitParams.LoginMode mode,
			OnInitSDKListener l) {

		LogUtil.d(TAG, "[init] start regist..");
		ctx = CCPAppManager.getContext();
		getInstance().mMode = mode;
		getInstance().onInitSDKListener = l;
		// 判断SDK是否已经初始化，没有初始化则先初始化SDK
		if (!ECDevice.isInitialized()) {
			ECDevice.initial(ctx, getInstance());
			return;
		}
		LogUtil.d(TAG, " SDK has inited , then regist..");
		// 已经初始化成功，直接进行注册
		getInstance().onInitialized();
	}

	@Override
	public void onInitialized() {
		LogUtil.d(TAG, "ECSDK is ready");
		ECHandlerHelper.postRunnOnUI(new Runnable() {

			@Override
			public void run() {
				if (onInitSDKListener != null) {
					onInitSDKListener.onInitialized();
				}

			}
		});

	}

	@Override
	public void onConnect() {

		if (onConnectSDKListener != null) {

			onConnectSDKListener.onConnect();
			return;
		}
		LogUtil.e("onConnectSDKListener is null ,callback onlogout failed");
	}

	@Override
	public void onDisconnect(ECError error) {
		if (onConnectSDKListener != null) {

			onConnectSDKListener.onDisconnect(error);
			return;
		}
		LogUtil.e("onConnectSDKListener is null ,callback onlogout failed");

	}

	@Override
	public void onConnectState(ECDevice.ECConnectState state, ECError error) {

		if (onConnectSDKListener != null) {

			onConnectSDKListener.onConnectState(state, error);
			return;
		}
		LogUtil.e("onConnectSDKListener is null ,callback onlogout failed");
	}

	@Override
	public void onLogout() {

		if (onLogoutSDKListener != null) {
			onLogoutSDKListener.onLogout();
			return;
		}
		LogUtil.e("onLogoutSDKListener is null ,callback onlogout failed");

	}

	@Override
	public void onError(final Exception exception) {
		LogUtil.e(TAG,
				"ECSDK couldn't start: " + exception.getLocalizedMessage());

		ECDevice.unInitial();
		ECHandlerHelper.postRunnOnUI(new Runnable() {

			@Override
			public void run() {

				if (onInitSDKListener != null) {
					onInitSDKListener.onError(exception);
				}
			}
		});

	}

	public static void logout() {
		ECDevice.logout(getInstance());
		release();
	}

	public static void release() {

		
	}

}
