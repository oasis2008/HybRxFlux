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
 */package com.yuntongxun.kitsdk.ui;

import com.yuntongxun.kitsdk.core.CCPAppManager;
import com.yuntongxun.kitsdk.ui.ECSuperActivity;
import com.yuntongxun.kitsdk.view.CCPActivityBase;

import android.view.View;



public class CCPActivityImpl extends CCPActivityBase {

    final private ECSuperActivity mActivity;

    public CCPActivityImpl(ECSuperActivity activity) {
        mActivity  = activity;
    }

    @Override
    protected void onInit() {
        mActivity.onActivityInit();
    }

    @Override
    protected int getLayoutId() {
        return mActivity.getLayoutId();
    }

    @Override
    protected View getContentLayoutView() {
        return null;
    }

    @Override
    protected String getClassName() {
        return mActivity.getClass().getName();
    }

    @Override
    protected void onBaseContentViewAttach(View contentView) {
        mActivity.onBaseContentViewAttach(contentView);
    }
}
