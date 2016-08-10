package com.huyingbao.hyb.ui.shop;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hardsoftstudio.rxflux.action.RxAction;
import com.hardsoftstudio.rxflux.action.RxError;
import com.hardsoftstudio.rxflux.dispatcher.RxViewDispatch;
import com.hardsoftstudio.rxflux.store.RxStore;
import com.hardsoftstudio.rxflux.store.RxStoreChange;
import com.huyingbao.hyb.HybApp;
import com.huyingbao.hyb.R;
import com.huyingbao.hyb.actions.Actions;
import com.huyingbao.hyb.actions.Keys;
import com.huyingbao.hyb.adapter.ShopListAdapter;
import com.huyingbao.hyb.adapter.ShopListAdapter;
import com.huyingbao.hyb.base.BaseFragment;
import com.huyingbao.hyb.inject.scope.PerFragment;
import com.huyingbao.hyb.model.Shop;
import com.huyingbao.hyb.model.Shop;
import com.huyingbao.hyb.stores.ShopStore;
import com.huyingbao.hyb.stores.UsersStore;
import com.huyingbao.hyb.utils.CommonUtils;
import com.huyingbao.hyb.utils.HttpCode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.adapter.rxjava.HttpException;

/**
 * Created by Administrator on 2016/5/6.
 */
public class ShopListBearbyFrg extends BaseFragment implements RxViewDispatch, BaseQuickAdapter.OnRecyclerViewItemClickListener {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.srl_content)
    SwipeRefreshLayout srlContent;
    @BindView(R.id.root_coordinator)
    CoordinatorLayout rootCoordinator;

    @Inject
    ShopStore shopStore;
    @Inject
    UsersStore usersStore;

    private ShopListAdapter adapter;
    private boolean isRefresh;
    private List<Shop> shopList;
    private int index = 0;


    public static ShopListBearbyFrg newInstance() {
        ShopListBearbyFrg fragment = new ShopListBearbyFrg();
        return fragment;
    }

    @Override
    protected void initInjector() {
        mFragmentComponent.inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.f_home;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        //初始化list
        shopList = new ArrayList<Shop>();
        //获取数据
        hybActionCreator.getUserMessage(HybApp.getUser().getUserId(), 0);
        //设置刷新view
        srlContent.setRefreshing(true);
        srlContent.setOnRefreshListener(() -> {
            refresh();
        });
        //设置空数据view
        View emptyView = CommonUtils.initEmptyView(mContext,
                (ViewGroup) recyclerView.getParent(),
                R.drawable.ic_menu_camera, "暂无发送数据");
        //创建adapter
        adapter = new ShopListAdapter(shopList);
        adapter.setEmptyView(emptyView);
        adapter.setOnRecyclerViewItemClickListener(this);
        //设置recyclerview
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {//上拉
                    int lastVisiblePosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                    if (lastVisiblePosition + 1 == adapter.getItemCount()) {//当前显示的数据是最后一条
                        srlContent.setRefreshing(true);
                        getShopList();
                    }
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }


    @Override
    public void onRxStoreChanged(@NonNull RxStoreChange change) {
        switch (change.getStoreId()) {
            case ShopStore.STORE_ID:
                switch (change.getRxAction().getType()) {
                    case Actions.GET_NEARBY_SHOP:
                        srlContent.setRefreshing(false);
                        index++;//页面索引+1
                        if (isRefresh) {//刷新
                            isRefresh = false;
                            shopList.clear();
                            shopList.addAll(shopStore.getShopList());
                            adapter.notifyDataSetChanged();
                        } else {//添加数据
                            shopList.addAll(shopStore.getShopList());
                            adapter.notifyDataSetChanged();
                        }
                        break;
                    case Actions.A_TO_SHOP_INFO:
                        Intent intent = new Intent(getContext(), ShopDetailAty.class);
                        intent.putExtra(Keys.SHOP, shopStore.getShop());
                        startActivity(intent);
                }
                break;

        }
    }

    @Override
    public void onRxError(@NonNull RxError error) {
        setLoadingFrame(false);
        Throwable throwable = error.getThrowable();
        if (throwable != null) {
            if (throwable instanceof HttpException) {
                int httpCode = ((HttpException) throwable).code();
                Snackbar.make(rootCoordinator, httpCode + HttpCode.getHttpCodeInfo(httpCode), Snackbar.LENGTH_INDEFINITE)
                        .setAction("重试", v -> hybActionCreator.retry(error.getAction()))
                        .show();
            }
        } else {
            Toast.makeText(getContext(), "未知错误!", Toast.LENGTH_LONG).show();
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
        return Arrays.asList(usersStore, shopStore);
    }

    @Nullable
    @Override
    public List<RxStore> getRxStoreListToUnRegister() {
        return null;
    }


    /**
     * 刷新
     */
    private void refresh() {
        index = 0;
        isRefresh = true;
        getShopList();
    }

    private void getShopList() {
        Shop shop = new Shop();
        shop.setLongitude(usersStore.getLongitude());
        shop.setLatitude(usersStore.getLatitude());
        shop.setRadius(10000);
        shop.setShopType(0);

        Map<String, String> options = new HashMap<>();
        options.put("index", index + "");
        options.put("sort", "distance ASC");
        options.put("limit", "5");

        hybActionCreator.getNearbyShopList(shop, options);
    }

    @Override
    public void onItemClick(View view, int i) {
        RxAction action = hybActionCreator.newRxAction(Actions.A_TO_SHOP_INFO, Keys.SHOP, shopList.get(i));
        hybActionCreator.postRxAction(action);
    }
}
