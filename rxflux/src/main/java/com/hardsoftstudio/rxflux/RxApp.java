package com.hardsoftstudio.rxflux;

import android.app.Application;

/**
 * Created by Administrator on 2016/7/18.
 */
public class RxApp extends Application{
    private static RxFlux rxFlux;

    @Override
    public void onCreate() {
        super.onCreate();
        rxFlux=RxFlux.init(this);
    }

    public static RxFlux getRxFlux() {
        return rxFlux;
    }
}
