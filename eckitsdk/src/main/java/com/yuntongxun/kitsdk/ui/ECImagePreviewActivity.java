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

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

import com.yuntongxun.eckitsdk.R;
import com.yuntongxun.ecsdk.platformtools.ECHandlerHelper;
import com.yuntongxun.kitsdk.core.CoreHandler;
import com.yuntongxun.kitsdk.setting.ECPreferenceSettings;
import com.yuntongxun.kitsdk.setting.ECPreferences;
import com.yuntongxun.kitsdk.ui.photoview.PhotoView;
import com.yuntongxun.kitsdk.utils.DemoUtils;
import com.yuntongxun.kitsdk.view.TopBarView;

import java.io.InvalidClassException;


public class ECImagePreviewActivity extends ECSuperActivity implements
		View.OnClickListener {

	private static final String TAG = "ECDemo.ImagePreviewActivity";
	private TopBarView mTopBarView;
	private PhotoView mImageView;
	private Bitmap bitmap;

	private CoreHandler mCoreHandler = new CoreHandler(
			new CoreHandler.HandlerCallbck() {
				@Override
				public boolean dispatchMessage() {
					mImageView.setImageBitmap(bitmap);
					mImageView.invalidate();
					mTopBarView.setRightBtnEnable(true);
					return false;
				}
			}, false);

	@Override
	protected int getLayoutId() {
		return R.layout.ytx_image_preview_activity;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mTopBarView = getTopBarView();
		mTopBarView.setTopBarToStatus(1, R.drawable.ytx_topbar_back_bt,
				R.drawable.ytx_btn_style_green, null,
				getString(R.string.dialog_ok_button),
				getString(R.string.app_title_image_preview), null, this);
		mTopBarView.setRightBtnEnable(false);

		initViewUI();
	}

	private void initViewUI() {
		final String path = ECPreferences.getSharedPreferences().getString(
				ECPreferenceSettings.SETTINGS_CROPIMAGE_OUTPUTPATH.getId(),
				(String) ECPreferenceSettings.SETTINGS_CROPIMAGE_OUTPUTPATH
						.getDefaultValue());

		mImageView = (PhotoView) findViewById(R.id.image);
		ECHandlerHelper handlerHelper = new ECHandlerHelper();
		handlerHelper.postRunnOnThead(new Runnable() {

			@Override
			public void run() {
				bitmap = DemoUtils.getSuitableBitmap(path);
				mCoreHandler.sendEmptyMessageDelayed(200);
			}
		});

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (bitmap != null && !bitmap.isRecycled()) {
			bitmap.recycle();
			bitmap = null;
		}
	}

	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.btn_left) {
			hideSoftKeyboard();
			finish();

		} else if (v.getId() == R.id.text_right) {
			try {
				ECPreferences.savePreference(
						ECPreferenceSettings.SETTINGS_PREVIEW_SELECTED,
						Boolean.TRUE, true);
				setResult(RESULT_OK);
			} catch (InvalidClassException e) {
				e.printStackTrace();
			}
			finish();

		}
	}

}
