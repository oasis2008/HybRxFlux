package com.huyingbao.hyb.ui.shop;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import com.hardsoftstudio.rxflux.action.RxError;
import com.hardsoftstudio.rxflux.dispatcher.RxViewDispatch;
import com.hardsoftstudio.rxflux.store.RxStore;
import com.hardsoftstudio.rxflux.store.RxStoreChange;
import com.huyingbao.hyb.HybApp;
import com.huyingbao.hyb.R;
import com.huyingbao.hyb.actions.Actions;
import com.huyingbao.hyb.base.BaseActivity;
import com.huyingbao.hyb.model.Shop;
import com.huyingbao.hyb.stores.ShopStore;
import com.huyingbao.hyb.stores.UsersStore;

import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnItemSelected;

public class RegisterShopAty extends BaseActivity implements RxViewDispatch {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.et_shop_name)
    EditText etShopName;

    private int mShopTyep = 0;
    private ShopStore shopStore;
    private UsersStore usersStore;
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
        //设置toobar
        setSupportActionBar(toolbar);
        //设置标题
        toolbar.setTitle(getTitle());
        //设置返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

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
        Shop shop = new Shop();
        shop.setShopName(shopName);
        shop.setLongitude(mLatitude);
        shop.setLatitude(mLongitude);
        shop.setShopType(mShopTyep);
        getHybActionCreator().registerShop(shop);

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
                        Toast.makeText(mContext, "你点击的是:" + shopStore.getShop(), Toast.LENGTH_SHORT).show();
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
        shopStore = ShopStore.get(getRxFlux().getDispatcher());
        usersStore = UsersStore.get(getRxFlux().getDispatcher());
        return Arrays.asList(shopStore, usersStore);
    }

    @Nullable
    @Override
    public List<RxStore> getRxStoreListToUnRegister() {
        return null;
    }
}
