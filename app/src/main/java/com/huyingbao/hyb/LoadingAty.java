package com.huyingbao.hyb;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.hardsoftstudio.rxflux.action.RxError;
import com.hardsoftstudio.rxflux.dispatcher.RxViewDispatch;
import com.hardsoftstudio.rxflux.store.RxStore;
import com.hardsoftstudio.rxflux.store.RxStoreChange;
import com.huyingbao.hyb.base.BaseActivity;
import com.huyingbao.hyb.push.BaiduPushBase;
import com.huyingbao.hyb.stores.UsersStore;
import com.huyingbao.hyb.ui.login.LoginAty;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class LoadingAty extends BaseActivity implements RxViewDispatch{
    private static final int DELAY_ACTION = 300;
    private final Handler mHideHandler = new Handler();
    private final Runnable mHandleActionRunnable = new Runnable() {
        public void run() {
            if (mLocalStorageUtils.isFirstTime() || !mLocalStorageUtils.isLogin() || HybApp.getUser() == null) {
                startActivity(LoginAty.class);
                finish();
                return;
            }
            if (HybApp.getUser().getType() == 0) {
                startActivity(MainAty.class);
            } else {
                startActivity(MainShopAty.class);
            }
            finish();

        }
    };
    @BindView(R.id.fullscreen_content)
    TextView fullscreenContent;
    @Inject
    UsersStore userStore;

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
        HybApp.getInstance().startLocation();
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


    @Override
    public void onRxStoreChanged(@NonNull RxStoreChange change) {

    }

    @Override
    public void onRxError(@NonNull RxError error) {

    }

    @Override
    public void onRxViewRegistered() {

    }

    @Override
    public void onRxViewUnRegistered() {

    }

    @Nullable
    @Override
    public List<RxStore> getRxStoreListToRegister() {
        return Arrays.asList(userStore);
    }

    @Nullable
    @Override
    public List<RxStore> getRxStoreListToUnRegister() {
        return null;
    }
}
