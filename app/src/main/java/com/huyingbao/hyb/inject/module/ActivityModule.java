package com.huyingbao.hyb.inject.module;

import android.app.Activity;
import android.content.Context;

import com.hardsoftstudio.rxflux.RxFlux;
import com.huyingbao.hyb.inject.qualifier.ContextLife;
import com.huyingbao.hyb.inject.scope.PerActivity;
import com.huyingbao.hyb.inject.scope.PerFragment;
import com.huyingbao.hyb.stores.FileStore;
import com.huyingbao.hyb.stores.ProdcutStore;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {
    private Activity mActivity;

    public ActivityModule(Activity activity) {
        mActivity = activity;
    }

    @Provides
    @PerActivity
    @ContextLife("Activity")
    public Context provideContext() {
        return mActivity;
    }

    @Provides
    @PerActivity
    public Activity provideActivity() {
        return mActivity;
    }

    @Provides
    @PerActivity
    public FileStore provideFileStore(RxFlux rxFlux) {
        return new FileStore(rxFlux.getDispatcher());
    }
}
