package com.huyingbao.hyb.ui.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.hardsoftstudio.rxflux.action.RxError;
import com.hardsoftstudio.rxflux.dispatcher.RxViewDispatch;
import com.hardsoftstudio.rxflux.store.RxStore;
import com.hardsoftstudio.rxflux.store.RxStoreChange;
import com.huyingbao.hyb.HybApp;
import com.huyingbao.hyb.R;
import com.huyingbao.hyb.actions.Actions;
import com.huyingbao.hyb.base.BaseFragment;
import com.huyingbao.hyb.stores.UsersStore;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/5/6.
 */
public class HomeFrg extends BaseFragment implements RxViewDispatch {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.root)
    RelativeLayout root;
    @Bind(R.id.root_coordinator)
    CoordinatorLayout rootCoordinator;

    private UsersStore usersStore;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static HomeFrg newInstance(int sectionNumber) {
        HomeFrg fragment = new HomeFrg();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
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
        //因为fragment不能像activity通过RxFlux根据生命周期在启动的时候,
        //调用getRxStoreListToRegister,注册rxstore,只能手动注册
        usersStore = UsersStore.get(getRxFlux().getDispatcher());
        usersStore.register();
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
        return null;
    }

    @Nullable
    @Override
    public List<RxStore> getRxStoreListToUnRegister() {
        return null;
    }


    private void refresh() {
        setLoadingFrame(true);
        HybApp.getInstance().startLocation();
    }
}
