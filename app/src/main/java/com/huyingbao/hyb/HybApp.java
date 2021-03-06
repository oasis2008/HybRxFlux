package com.huyingbao.hyb;

import android.app.Application;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.google.gson.reflect.TypeToken;
import com.hardsoftstudio.rxflux.RxFlux;
import com.hardsoftstudio.rxflux.action.RxAction;
import com.huyingbao.hyb.actions.Actions;
import com.huyingbao.hyb.actions.HybActionCreator;
import com.huyingbao.hyb.actions.Keys;
import com.huyingbao.hyb.inject.component.ApplicationComponent;
import com.huyingbao.hyb.inject.component.DaggerApplicationComponent;
import com.huyingbao.hyb.inject.module.ApplicationModule;
import com.huyingbao.hyb.model.HybUser;
import com.huyingbao.hyb.model.Shop;
import com.huyingbao.hyb.utils.LocalStorageUtils;
import com.huyingbao.hyb.utils.gsonhelper.GsonHelper;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.tbruyelle.rxpermissions.RxPermissions;

import org.json.JSONException;

import javax.inject.Inject;

public class HybApp extends Application {

    @Inject
    HybActionCreator hybActionCreator;
    @Inject
    RxFlux rxFlux;
    @Inject
    RxPermissions rxPermissions;
    @Inject
    LocalStorageUtils mLocalStorageUtils;
    /**
     * 定位配置类
     */
    private static LocationClientOption mLocationClientOption;
    private static BDLocationListener mBDLocationListener;
    private LocationClient mLocationClient;

    private static HybApp intantce;
    private static HybUser mUser;
    private static Shop mShop;

    public static HybApp getInstance() {
        if (intantce == null) {
            intantce = new HybApp();
        }
        return intantce;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initDagger();
        intantce = this;
        ApplicationComponent.Instance.get().inject(this);
        initLocationClient();
        Logger.init("Hyb").hideThreadInfo().logLevel(LogLevel.FULL);
    }

    /**
     * Module实例的创建,如果Module只有有参构造器，则必须显式传入Module实例
     * 单例的有效范围随着其依附的Component，为了使得@Singleton的作用范围是整个Application，你需要添加以下代码
     */
    private void initDagger() {
        ApplicationComponent applicationComponet = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
        ApplicationComponent.Instance.init(applicationComponet);
    }


    /**
     * 初始化百度定位配置和监听器
     */
    private void initLocationClient() {
        //初始化LocationClientOption
        mLocationClientOption = new LocationClientOption();
        mLocationClientOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        mLocationClientOption.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        mLocationClientOption.setScanSpan(1000);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        mLocationClientOption.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        mLocationClientOption.setOpenGps(true);//可选，默认false,设置是否使用gps
        mLocationClientOption.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        mLocationClientOption.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        mLocationClientOption.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        mLocationClientOption.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        mLocationClientOption.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        mLocationClientOption.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        //初始化监听器
        mBDLocationListener = new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                RxAction action = getHybActionCreator().newRxAction(Actions.A_GET_LOCATION, Keys.LOCATION, bdLocation);
                getHybActionCreator().postRxAction(action);
//                getHybActionCreator().postLocation(bdLocation);
                //接收到位置信息之后,LocationClient取消位置监听器
                mLocationClient.unRegisterLocationListener(this);
                mLocationClient.stop();
            }
        };
    }

    /**
     * 开始定位
     */
    public void startLocation() {
        rxPermissions
                .request(android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe(granted -> {
                    if (granted) {
                        //百度定位Client,使用的时候使用一次就重新生成一次
                        mLocationClient = new LocationClient(getApplicationContext());
                        //设置配置参数
                        mLocationClient.setLocOption(mLocationClientOption);
                        //注册位置监听器
                        mLocationClient.registerLocationListener(mBDLocationListener);
                        //开始定位
                        mLocationClient.start();
                    }
                });
    }

    public LocalStorageUtils getLocalSorageUtils() {
        return mLocalStorageUtils;
    }

    public RxFlux getRxFlux() {
        return rxFlux;
    }

    public HybActionCreator getHybActionCreator() {
        return hybActionCreator;
    }

    public static HybUser getUser() {
        if (mUser != null) {
            return mUser;
        }
        try {
            mUser = GsonHelper.fromJson(intantce.getLocalSorageUtils().getUser(), new TypeToken<HybUser>() {
            }.getType());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mUser;
    }

    public static void setUser(HybUser user) {
        mUser = user;
    }

    public static Shop getShop() {
        if (mShop != null) {
            return mShop;
        }
        try {
            mShop = GsonHelper.fromJson(intantce.getLocalSorageUtils().getShop(), new TypeToken<Shop>() {
            }.getType());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mShop;
    }

    public static void setShop(Shop shop) {
        mShop = shop;
    }
}
