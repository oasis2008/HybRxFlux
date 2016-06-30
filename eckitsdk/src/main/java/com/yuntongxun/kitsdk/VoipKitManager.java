package com.yuntongxun.kitsdk;

import com.yuntongxun.ecsdk.ECVoIPCallManager;
import com.yuntongxun.kitsdk.core.CCPAppManager;

public class VoipKitManager {

	protected static VoipKitManager sInstance;

	protected static VoipKitManager getInstance() {
		if (sInstance == null) {
			synchronized (VoipKitManager.class) {
				sInstance = new VoipKitManager();
			}
		}

		return sInstance;
	}

	public static void makeVoiceCall(String nickName, String phonenum) {

		CCPAppManager
				.callVoIPAction(ECDeviceKit.getmContext(),
						ECVoIPCallManager.CallType.VOICE, nickName, phonenum,
						"", false);

	}

	public static void makeVideoCall(String nickName, String phonenum) {

		CCPAppManager
				.callVoIPAction(ECDeviceKit.getmContext(),
						ECVoIPCallManager.CallType.VIDEO, nickName, phonenum,
						"", false);
	}

}
