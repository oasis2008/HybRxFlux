package com.huyingbao.hyb.ui.user;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

import com.hardsoftstudio.rxflux.action.RxError;
import com.hardsoftstudio.rxflux.dispatcher.RxViewDispatch;
import com.hardsoftstudio.rxflux.store.RxStore;
import com.hardsoftstudio.rxflux.store.RxStoreChange;
import com.huyingbao.hyb.HybApp;
import com.huyingbao.hyb.R;
import com.huyingbao.hyb.actions.Actions;
import com.huyingbao.hyb.actions.Keys;
import com.huyingbao.hyb.base.BaseActivity;
import com.huyingbao.hyb.inject.scope.PerActivity;
import com.huyingbao.hyb.model.MsgFromUser;
import com.huyingbao.hyb.stores.MsgStore;
import com.huyingbao.hyb.stores.UsersStore;
import com.huyingbao.hyb.widget.KeywordsFlow;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class UserSendMessageAty extends BaseActivity implements RxViewDispatch {
    @BindView(R.id.kf_tag)
    KeywordsFlow kfTag;
    @BindView(R.id.root_coordinator)
    CoordinatorLayout rootCoordinator;
    @Inject
    MsgStore msgStore;
    @PerActivity
    @Inject
    UsersStore usersStore;
    private double mLatitude;
    private double mLongitude;
    private StringBuffer content = new StringBuffer();


    @Override
    public void initInjector() {
        mActivityComponent.inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.a_user_send_message;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        initActionBar();
        initFlowView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setLoadingFrame(true);
        HybApp.getInstance().startLocation();
    }

    @OnClick(R.id.bt_send)
    public void onClick() {
        if (mLatitude == 0 || mLongitude == 0) {
            Snackbar.make(rootCoordinator, "请开启定位!", Snackbar.LENGTH_INDEFINITE)
                    .setAction("重试", v -> {
                        setLoadingFrame(true);
                        HybApp.getInstance().startLocation();
                    })
                    .show();
            return;
        }

        MsgFromUser msgFromUser = new MsgFromUser();
        msgFromUser.setContent(content.toString());
        msgFromUser.setLongitude(mLongitude);
        msgFromUser.setLatitude(mLatitude);
        msgFromUser.setRadius(1000);
        hybActionCreator.sendMessageByRadius(msgFromUser);
    }

    /**
     * 初始化飞入飞去控件
     */
    private void initFlowView() {
        kfTag.setDuration(800l);
        feedKeywordsFlow(kfTag, Keys.PRODUCT_TYPE);
        kfTag.go2Show(KeywordsFlow.ANIMATION_IN);
        kfTag.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = ((TextView) v).getText().toString();
                content = content.append(text);
                setChange(text);
            }
        });
    }

    /**
     * 随机飞入飞去
     *
     * @param keywordsFlow
     * @param arr
     */
    private static void feedKeywordsFlow(KeywordsFlow keywordsFlow, String[] arr) {
//        Random random = new Random();
//        for (int i = 0; i < KeywordsFlow.MAX; i++) {
//            int ran = random.nextInt(arr.length);
//            String tmp = arr[ran];
//            keywordsFlow.feedKeyword(tmp);
//        }
        for (int i = 0; i < arr.length; i++) {
            String tmp = arr[i];
            keywordsFlow.feedKeyword(tmp);
        }
    }

    /**
     * 点击之后状态改变
     *
     * @param keyword
     */
    private void setChange(String keyword) {
        kfTag.rubKeywords();
        feedKeywordsFlow(kfTag, Keys.PRODUCT_COLOR);
        kfTag.go2Show(KeywordsFlow.ANIMATION_OUT);
    }

    @Override
    public void onRxStoreChanged(@NonNull RxStoreChange change) {
        switch (change.getStoreId()) {
            case UsersStore.STORE_ID:
                switch (change.getRxAction().getType()) {
                    case Actions.A_GET_LOCATION:
                        setLoadingFrame(false);
                        mLatitude = usersStore.getBDLocation().getLatitude();
                        mLongitude = usersStore.getBDLocation().getLongitude();
                        break;
                }
                break;
            case MsgStore.STORE_ID:
                switch (change.getRxAction().getType()) {
                    case Actions.SEND_MESSAGE_BY_RADIUS:
                        msgStore.getSendStatus();
                        break;
                }
        }
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
        return Arrays.asList(msgStore);
    }

    @Nullable
    @Override
    public List<RxStore> getRxStoreListToUnRegister() {
        return Arrays.asList(msgStore);
    }
}
