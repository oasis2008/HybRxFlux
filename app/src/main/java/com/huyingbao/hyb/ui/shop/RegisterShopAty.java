package com.huyingbao.hyb.ui.shop;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import com.hardsoftstudio.rxflux.action.RxError;
import com.hardsoftstudio.rxflux.dispatcher.RxViewDispatch;
import com.hardsoftstudio.rxflux.store.RxStore;
import com.hardsoftstudio.rxflux.store.RxStoreChange;
import com.huyingbao.hyb.HybApp;
import com.huyingbao.hyb.MainAty;
import com.huyingbao.hyb.R;
import com.huyingbao.hyb.actions.Actions;
import com.huyingbao.hyb.base.BaseActivity;
import com.huyingbao.hyb.model.Shop;
import com.huyingbao.hyb.stores.ShopStore;
import com.huyingbao.hyb.stores.UsersStore;
import com.huyingbao.hyb.utils.HttpCode;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import retrofit2.adapter.rxjava.HttpException;

public class RegisterShopAty extends BaseActivity implements RxViewDispatch {
    @Bind(R.id.et_shop_name)
    EditText etShopName;
    @Bind(R.id.root_coordinator)
    CoordinatorLayout rootCoordinator;
    @Bind(R.id.sv_form)
    ScrollView svForm;

    private int mShopTyep = 0;
    @Inject
    ShopStore shopStore;
    @Inject
    UsersStore usersStore;
    private double mLatitude;
    private double mLongitude;

    @Override
    public void initInjector() {
        mActivityComponent.inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.a_register_shop;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        initActionBar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setLoadingFrame(true);
        HybApp.getInstance().startLocation();
    }

    @OnClick(R.id.btn_register_shop)
    public void onClick() {
        etShopName.setError(null);
        String shopName = etShopName.getText().toString();
        if (TextUtils.isEmpty(shopName)) {
            etShopName.setError(getString(R.string.error_field_required));
            etShopName.requestFocus();
            return;
        }
        if (mLatitude == 0 || mLongitude == 0) {
            Snackbar.make(rootCoordinator, "请开启定位!", Snackbar.LENGTH_INDEFINITE)
                    .setAction("重试", v -> {
                        setLoadingFrame(true);
                        HybApp.getInstance().startLocation();
                    })
                    .show();
            return;
        }
        Shop shop = new Shop();
        shop.setShopName(shopName);
        shop.setLongitude(mLatitude);
        shop.setLatitude(mLongitude);
        shop.setShopType(mShopTyep);
        hybActionCreator.registerShop(shop);

    }

    @OnItemSelected(R.id.sp_shop_type)
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        mShopTyep = pos;
        String[] languages = getResources().getStringArray(R.array.shop_type);
        Toast.makeText(mContext, "你点击的是:" + languages[pos], Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRxStoreChanged(@NonNull RxStoreChange change) {
        switch (change.getStoreId()) {
            case ShopStore.STORE_ID:
                switch (change.getRxAction().getType()) {
                    case Actions.REGISTER_SHOP:
                        showProgress(false);
                        finish();
                        startActivity(MainAty.class);
                        break;
                }
                break;
            case UsersStore.STORE_ID:
                switch (change.getRxAction().getType()) {
                    case Actions.A_GET_LOCATION:
                        setLoadingFrame(false);
                        mLatitude = usersStore.getBDLocation().getLatitude();
                        mLongitude = usersStore.getBDLocation().getLongitude();
                        break;

                }
                break;
        }
    }

    @Override
    public void onRxError(@NonNull RxError error) {
        showProgress(false);
        Throwable throwable = error.getThrowable();
        if (throwable != null && throwable instanceof HttpException) {
            int httpCode = ((HttpException) throwable).code();
            if (httpCode == 432) {
                Snackbar.make(rootCoordinator, httpCode + HttpCode.getHttpCodeInfo(httpCode), Snackbar.LENGTH_INDEFINITE)
                        .show();
                return;
            }
            Snackbar.make(rootCoordinator, httpCode + HttpCode.getHttpCodeInfo(httpCode), Snackbar.LENGTH_INDEFINITE)
                    .setAction("重试", v -> hybActionCreator.retry(error.getAction()))
                    .show();
        } else {
            Snackbar.make(rootCoordinator, "未知错误", Snackbar.LENGTH_INDEFINITE)
                    .show();
        }
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
        return Arrays.asList(shopStore, usersStore);
    }

    @Nullable
    @Override
    public List<RxStore> getRxStoreListToUnRegister() {
        return null;
    }


    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
        svForm.setVisibility(show ? View.GONE : View.VISIBLE);
        svForm.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                svForm.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        setLoadingFrame(show);
    }

}
