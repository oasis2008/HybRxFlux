package com.huyingbao.hyb.ui.shop;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.hardsoftstudio.rxflux.action.RxError;
import com.hardsoftstudio.rxflux.dispatcher.RxViewDispatch;
import com.hardsoftstudio.rxflux.store.RxStore;
import com.hardsoftstudio.rxflux.store.RxStoreChange;
import com.huyingbao.hyb.R;
import com.huyingbao.hyb.actions.Actions;
import com.huyingbao.hyb.actions.Keys;
import com.huyingbao.hyb.adapter.ProductListAdapter;
import com.huyingbao.hyb.base.BaseFragment;
import com.huyingbao.hyb.model.Product;
import com.huyingbao.hyb.model.Shop;
import com.huyingbao.hyb.stores.ProdcutStore;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;

public class ProductListFrg extends BaseFragment implements RxViewDispatch, ProductListAdapter.OnProductClicked {

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    private Shop mShop;

    private ProductListAdapter adapter;

    @Inject
    ProdcutStore prodcutStore;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ProductListFrg.
     */
    public static ProductListFrg newInstance() {
        ProductListFrg fragment = new ProductListFrg();
        return fragment;
    }

    @Override
    protected void initInjector() {
        mFragmentComponent.inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.f_product_list;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        if (getArguments().containsKey(Keys.SHOP)) {
            mShop = (Shop) getArguments().getSerializable(Keys.SHOP);
            hybActionCreator.getProductByShop(mShop.getShopId(), 0);
        }
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ProductListAdapter();
        adapter.setOnProductClickCallBack(this);
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void onRxStoreChanged(@NonNull RxStoreChange change) {
        switch (change.getStoreId()) {
            case ProdcutStore.STORE_ID:
                switch (change.getRxAction().getType()) {
                    case Actions.GET_PRODUCT_BY_SHOP:
                        adapter.setProductList(prodcutStore.getProductList());
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

    @Override
    public void onClicked(Product product) {

    }
}
