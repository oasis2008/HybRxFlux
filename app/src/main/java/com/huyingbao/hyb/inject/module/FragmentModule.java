package com.huyingbao.hyb.inject.module;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

import com.hardsoftstudio.rxflux.RxFlux;
import com.huyingbao.hyb.HybApp;
import com.huyingbao.hyb.inject.qualifier.ContextLife;
import com.huyingbao.hyb.inject.scope.PerFragment;
import com.huyingbao.hyb.stores.ProdcutStore;

import dagger.Module;
import dagger.Provides;

@Module
public class FragmentModule {
    private Fragment mFragment;

    public FragmentModule(Fragment fragment) {
        mFragment = fragment;
    }

    @Provides
    @PerFragment
    @ContextLife("Activity")
    public Context provideContext() {
        return mFragment.getActivity();
    }

    @Provides
    @PerFragment
    public Activity provideActivity() {
        return mFragment.getActivity();
    }

    @Provides
    @PerFragment
    public Fragment provideFragment() {
        return mFragment;
    }

    @Provides
    @PerFragment
    public ProdcutStore provideLocalProdcutStore() {
        return new ProdcutStore(HybApp.getInstance().getRxFlux().getDispatcher());
    }
}
