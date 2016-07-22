package com.huyingbao.hyb.inject.module;

import com.hardsoftstudio.rxflux.BuildConfig;
import com.hardsoftstudio.rxflux.RxFlux;
import com.hardsoftstudio.rxflux.util.LogLevel;
import com.huyingbao.hyb.HybApp;
import com.huyingbao.hyb.actions.HybActionCreator;
import com.huyingbao.hyb.stores.ShopStore;
import com.huyingbao.hyb.stores.UsersStore;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Flux 依赖
 *
 * @author lsxiao
 * @date 2015-11-04 00:44
 */
@Module
public class FluxModule {
    @Singleton
    @Provides
    public RxFlux provideRxFlux() {
        RxFlux.LOG_LEVEL = BuildConfig.DEBUG ? LogLevel.FULL : LogLevel.NONE;
        RxFlux rxFlux = RxFlux.init(HybApp.getInstance());
        return rxFlux;
    }

    /**
     * @param rxFlux 从当前module中调用返回值是RxFlux的方法
     * @return
     */
    @Singleton
    @Provides
    public HybActionCreator provideActionCreator(RxFlux rxFlux) {
        HybActionCreator actionCreator = new HybActionCreator(rxFlux.getDispatcher(), rxFlux.getSubscriptionManager());
        return actionCreator;
    }

    @Singleton
    @Provides
    public UsersStore provideUsersStore(RxFlux rxFlux) {
        UsersStore usersStore = new UsersStore(rxFlux.getDispatcher());
        return usersStore;
    }

    @Singleton
    @Provides
    public ShopStore provideShopStore(RxFlux rxFlux) {
        ShopStore shopStore = new ShopStore(rxFlux.getDispatcher());
        return shopStore;
    }
}
