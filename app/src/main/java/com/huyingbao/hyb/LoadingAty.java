package com.huyingbao.hyb;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.huyingbao.hyb.base.BaseActivity;
import com.huyingbao.hyb.model.HybUser;
import com.huyingbao.hyb.push.BaiduPushBase;
import com.huyingbao.hyb.ui.login.LoginAty;
import com.huyingbao.hyb.utils.gsonhelper.GsonHelper;

import org.json.JSONException;

import butterknife.Bind;

public class LoadingAty extends BaseActivity {
    private static final int DELAY_ACTION = 300;
    private final Handler mHideHandler = new Handler();
    private final Runnable mHandleActionRunnable = new Runnable() {
        public void run() {
            if (mLocalStorageUtils.isFirstTime()) {
                startActivity(LoginAty.class);
                finish();
                return;
            }
            if (!mLocalStorageUtils.isLogin()) {
                startActivity(LoginAty.class);
                finish();
                return;
            }
            try {
                HybUser user = GsonHelper.fromJson(mLocalStorageUtils.getUser(), new TypeToken<HybUser>() {
                }.getType());
                if (user == null) {
                    startActivity(LoginAty.class);
                    finish();
                    return;
                }
                if (user.getType() == 0) {
                    startActivity(MainAty.class);
                    return;
                }
                startActivity(MainShopAty.class);
                finish();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };
    @Bind(R.id.fullscreen_content)
    TextView fullscreenContent;

    @Override
    public void initInjector() {
        mActivityComponent.inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.a_loading;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        fullscreenContent.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        //开启百度推送
        BaiduPushBase.start(HybApp.getInstance());
        handleAction();
    }

    /**
     * 延时300毫秒,运行跳转逻辑
     */
    private void handleAction() {
        mHideHandler.postDelayed(mHandleActionRunnable, DELAY_ACTION);
    }


}
