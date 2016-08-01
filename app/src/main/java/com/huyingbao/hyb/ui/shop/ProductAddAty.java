package com.huyingbao.hyb.ui.shop;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.widget.EditText;
import android.widget.ImageView;

import com.hardsoftstudio.rxflux.action.RxError;
import com.hardsoftstudio.rxflux.dispatcher.RxViewDispatch;
import com.hardsoftstudio.rxflux.store.RxStore;
import com.hardsoftstudio.rxflux.store.RxStoreChange;
import com.huyingbao.hyb.HybApp;
import com.huyingbao.hyb.MainAty;
import com.huyingbao.hyb.R;
import com.huyingbao.hyb.actions.Actions;
import com.huyingbao.hyb.base.BaseActivity;
import com.huyingbao.hyb.model.Product;
import com.huyingbao.hyb.stores.ProdcutStore;
import com.huyingbao.hyb.stores.ProductStoreInterface;
import com.huyingbao.hyb.stores.ShopStore;
import com.huyingbao.hyb.stores.UsersStore;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProductAddAty extends BaseActivity implements RxViewDispatch{

    @Inject
    ProdcutStore prodcutStore;
    @Bind(R.id.iv_head)
    ImageView ivHead;
    @Bind(R.id.et_product_name)
    EditText etProductName;
    @Bind(R.id.root_coordinator)
    CoordinatorLayout rootCoordinator;

    @Override
    public void initInjector() {
        mActivityComponent.inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.a_product_add;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        initActionBar();
    }

    @OnClick(R.id.bt_ok)
    public void onClick() {
        String productName = etProductName.getText().toString();
        if (productName == null || productName.isEmpty()) {
            Snackbar.make(rootCoordinator, "请输入", Snackbar.LENGTH_INDEFINITE).show();
            return;
        }
        Product product =new Product();
        product.setProductName(productName);
        product.setBelongShop(HybApp.getShop().getShopId());
        hybActionCreator.addProduct(product);
    }

    @Override
    public void onRxStoreChanged(@NonNull RxStoreChange change) {
        switch (change.getStoreId()) {
            case ProdcutStore.STORE_ID:
                switch (change.getRxAction().getType()) {
                    case Actions.ADD_PRODUCT:
                        Snackbar.make(rootCoordinator, "请输入", Snackbar.LENGTH_INDEFINITE).show();
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
        return Arrays.asList(prodcutStore);
    }

    @Nullable
    @Override
    public List<RxStore> getRxStoreListToUnRegister() {
        return Arrays.asList(prodcutStore);
    }
}
