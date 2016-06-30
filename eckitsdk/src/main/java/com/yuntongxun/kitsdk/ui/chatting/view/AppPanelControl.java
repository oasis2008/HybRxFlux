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
package com.yuntongxun.kitsdk.ui.chatting.view;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import com.yuntongxun.eckitsdk.R;
import com.yuntongxun.kitsdk.core.CCPAppManager;
import com.yuntongxun.kitsdk.core.ECKitCustomProviderManager;
import com.yuntongxun.kitsdk.custom.provider.chat.ECCustomChatPlusExtendProvider;
import com.yuntongxun.kitsdk.ui.chatting.model.Capability;

public class AppPanelControl {

	private Context mContext;

	public int[] cap = new int[] { R.string.app_panel_pic,
			R.string.app_panel_tackpic, R.string.app_panel_file,

	};

	private ECCustomChatPlusExtendProvider obj;

	/**
     *
     */
	public AppPanelControl() {
		mContext = CCPAppManager.getContext();

		obj = ECKitCustomProviderManager.getCustomChatPlusExtendProvider();

	}

	/**
	 *
	 * @return
	 */
	public List<Capability> getCapability() {
		List<Capability> capabilities = new ArrayList<Capability>();

		for (int i = 0; i < cap.length; i++) {
			Capability capability = getCapability(cap[i]);
			capabilities.add(capabilities.size(), capability);
		}

		if (obj != null ) {

			String[] titleArr = obj.getCustomPlusTitleArray(mContext);
			int[] resArr = obj.getCustomPlusDrawableArray(mContext);
			
			if(titleArr!=null&&titleArr.length>0&&resArr!=null&&resArr.length>0){

			for (int i = 0; i < titleArr.length; i++) {

				Capability capability = new Capability(titleArr[i], resArr[i]);

				capabilities.add(cap.length + i, capability);

			}
			}
		}

		return capabilities;
	}

	/**
	 * @param resid
	 * @return
	 */
	private Capability getCapability(int resid) {
		Capability capability = null;

		if (resid == R.string.app_panel_pic) {
			capability = new Capability(getContext().getString(
					R.string.app_panel_pic),
					R.drawable.ytx_chattingfooter_image_selector);

		} else if (resid == R.string.app_panel_tackpic) {
			capability = new Capability(getContext().getString(
					R.string.app_panel_tackpic),
					R.drawable.ytx_chattingfooter_takephoto_selector);

		} else if (resid == R.string.app_panel_file) {
			capability = new Capability(getContext().getString(
					R.string.app_panel_file),
					R.drawable.ytx_chattingfooter_file_selector);

		}

		capability.setId(resid);
		return capability;
	}

	/**
	 * @return
	 */
	private Context getContext() {
		if (mContext == null) {
			mContext = CCPAppManager.getContext();
		}
		return mContext;
	}
}
