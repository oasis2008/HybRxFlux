package com.huyingbao.hyb.ui.contacts;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.hardsoftstudio.rxflux.action.RxError;
import com.hardsoftstudio.rxflux.dispatcher.RxViewDispatch;
import com.hardsoftstudio.rxflux.store.RxStore;
import com.hardsoftstudio.rxflux.store.RxStoreChange;
import com.huyingbao.hyb.HybApp;
import com.huyingbao.hyb.R;
import com.huyingbao.hyb.actions.Actions;
import com.huyingbao.hyb.adapter.MsgFromUserListAdapter;
import com.huyingbao.hyb.adapter.ShopListAdapter;
import com.huyingbao.hyb.base.BaseFragment;
import com.huyingbao.hyb.model.MsgFromUser;
import com.huyingbao.hyb.stores.MsgStore;
import com.huyingbao.hyb.stores.UsersStore;
import com.huyingbao.hyb.utils.CommonUtils;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/5/6.
 */
public class ContactsFrg extends BaseFragment implements RxViewDispatch {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.root)
    RelativeLayout root;
    @BindView(R.id.root_coordinator)
    CoordinatorLayout rootCoordinator;

    @Inject
    UsersStore usersStore;
    @Inject
    MsgStore msgStore;
    private MsgFromUserListAdapter adapter;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ContactsFrg newInstance() {
        ContactsFrg fragment = new ContactsFrg();
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
        hybActionCreator.getUserMessage(HybApp.getUser().getUserId(),0);

        View emptyView = CommonUtils.initEmptyView(mContext,
                (ViewGroup) recyclerView.getParent(),
                R.drawable.ic_menu_camera, "您没有绑定学校!");

        adapter = new MsgFromUserListAdapter(msgStore.getMsgFromUserList());
        adapter.setEmptyView(emptyView);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }


    @OnClick({R.id.recycler_view, R.id.root, R.id.root_coordinator})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.recycler_view:
                break;
            case R.id.root:
                break;
            case R.id.root_coordinator:
                break;
        }
    }

    @Override
    public void onRxStoreChanged(@NonNull RxStoreChange change) {
        switch (change.getStoreId()) {
            case UsersStore.STORE_ID:
                switch (change.getRxAction().getType()) {
                    case Actions.A_GET_LOCATION:
                        break;
                }
                break;
            case MsgStore.STORE_ID:
                switch (change.getRxAction().getType()) {
                    case Actions.GET_USER_MESSAGE:
                        adapter.notifyDataSetChanged();
                        break;
                }
                break;
        }
    }

    @Override
    public void onRxError(@NonNull RxError error) {
        setLoadingFrame(false);
        Throwable throwable = error.getThrowable();
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
        return Arrays.asList(usersStore, msgStore);
    }

    @Nullable
    @Override
    public List<RxStore> getRxStoreListToUnRegister() {
        return null;
    }


    private void refresh() {
    }
}
