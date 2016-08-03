package com.huyingbao.hyb.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.hardsoftstudio.rxflux.RxFlux;
import com.hardsoftstudio.rxflux.dispatcher.RxViewDispatch;
import com.hardsoftstudio.rxflux.store.RxStore;
import com.huyingbao.hyb.actions.HybActionCreator;
import com.huyingbao.hyb.inject.component.ApplicationComponent;
import com.huyingbao.hyb.inject.component.DaggerFragmentComponent;
import com.huyingbao.hyb.inject.component.FragmentComponent;
import com.huyingbao.hyb.inject.module.FragmentModule;
import com.huyingbao.hyb.inject.qualifier.ContextLife;
import com.huyingbao.hyb.utils.LocalStorageUtils;
import com.huyingbao.hyb.utils.ViewUtils;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment extends Fragment {
    @Inject
    protected HybActionCreator hybActionCreator;
    @Inject
    protected RxFlux rxFlux;
    @Inject
    @ContextLife("Activity")
    protected Context mContext;
    @Inject
    protected LocalStorageUtils mLocalStorageUtils;
    protected FragmentComponent mFragmentComponent;
    private View mRootView;
    private ProgressBar loadingProgress;
    private Unbinder unbinder;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //初始化注入器
        mFragmentComponent = DaggerFragmentComponent.builder()
                .fragmentModule(new FragmentModule(this))
                .applicationComponent(ApplicationComponent.Instance.get())
                .build();
        //注入Injector
        initInjector();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(getLayoutId(), container, false);
        return mRootView;
    }

    /**
     * 注册RxStore
     * 因为fragment不能像activity通过RxFlux根据生命周期在启动的时候,
     * 调用getRxStoreListToRegister,注册RxStore,只能手动注册
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        //注册rxstore
        if (this instanceof RxViewDispatch) {
            List<RxStore> rxStoreList = ((RxViewDispatch) this).getRxStoreListToRegister();
            if (rxStoreList != null) {
                for (RxStore rxStore : rxStoreList) {
                    rxStore.register();
                }
            }
        }
        //绑定view
        unbinder=ButterKnife.bind(this, view);
        //绑定view之后运行
        super.onViewCreated(view, savedInstanceState);
        //view创建之后的操作
        afterCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        //注册view
        if (this instanceof RxViewDispatch) {
            RxViewDispatch viewDispatch = (RxViewDispatch) this;
            rxFlux.getDispatcher().subscribeRxView( (RxViewDispatch) this);
            ((RxViewDispatch) this).onRxViewRegistered();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        //解除view注册
        if (this instanceof RxViewDispatch) {
            rxFlux.getDispatcher().unsubscribeRxView((RxViewDispatch) this);
            ((RxViewDispatch) this).onRxViewUnRegistered();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        //解除RxStore注册
        if (this instanceof RxViewDispatch) {
            List<RxStore> rxStoreList = ((RxViewDispatch) this).getRxStoreListToUnRegister();
            if (rxStoreList != null) {
                for (RxStore rxStore : rxStoreList) {
                    rxStore.unregister();
                }
            }
        }
    }

    protected abstract void initInjector();

    protected abstract int getLayoutId();

    protected abstract void afterCreate(Bundle savedInstanceState);

    /**
     * 是否显示进度条
     *
     * @param show
     */
    public void setLoadingFrame(boolean show) {
        if (loadingProgress == null) {
            loadingProgress = ViewUtils.createProgressBar((Activity) mContext, null);
        }
        loadingProgress.setVisibility(show ? View.VISIBLE : View.GONE);
    }

}
