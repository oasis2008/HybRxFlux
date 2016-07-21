package com.hardsoftstudio.rxflux;

import android.app.Application;

import com.hardsoftstudio.rxflux.util.LogLevel;

public class RxApp extends Application {
    private RxFlux rxFlux;

    @Override
    public void onCreate() {
        super.onCreate();
        RxFlux.LOG_LEVEL = BuildConfig.DEBUG ? LogLevel.FULL : LogLevel.NONE;
        rxFlux = RxFlux.init(this);
    }

    public RxFlux getRxFlux() {
        return rxFlux;
    }
}
