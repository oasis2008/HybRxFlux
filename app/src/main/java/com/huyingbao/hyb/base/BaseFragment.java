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
        if (this instanceof RxViewDispatch) {
            RxViewDispatch viewDispatch = (RxViewDispatch) this;
            viewDispatch.onRxViewRegistered();
            rxFlux.getDispatcher().subscribeRxView(viewDispatch);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(getLayoutId(), container, false);
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        //注册RxStore
        if (this instanceof RxViewDispatch) {
            List<RxStore> rxStoreList = ((RxViewDispatch) this).getRxStoreListToRegister();
            if (rxStoreList != null) {
                for (RxStore rxStore : rxStoreList) {
                    rxStore.register();
                }
            }
        }
        //绑定view
        ButterKnife.bind(this, view);
        //绑定view之后运行
        super.onViewCreated(view, savedInstanceState);
        //view创建之后的操作
        afterCreate(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
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

    @Override
    public void onDetach() {
        super.onDetach();
        if (this instanceof RxViewDispatch) {
            rxFlux.getDispatcher().unsubscribeRxView((RxViewDispatch) this);
        }
    }


    public RxFlux getRxFlux() {
        return rxFlux;
    }

    public HybActionCreator getHybActionCreator() {
        return hybActionCreator;
    }


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

    protected abstract void initInjector();

    /**
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * @param savedInstanceState
     */
    protected abstract void afterCreate(Bundle savedInstanceState);
}
