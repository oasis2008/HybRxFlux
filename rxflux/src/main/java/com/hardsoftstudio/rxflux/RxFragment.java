package com.hardsoftstudio.rxflux;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.hardsoftstudio.rxflux.dispatcher.Dispatcher;
import com.hardsoftstudio.rxflux.dispatcher.RxBus;
import com.hardsoftstudio.rxflux.dispatcher.RxViewDispatch;
import com.hardsoftstudio.rxflux.store.RxStore;
import com.hardsoftstudio.rxflux.util.LogLevel;
import com.hardsoftstudio.rxflux.util.SubscriptionManager;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by marcel on 09/09/15.
 * Main class, the init method of this class must be called onCreate of the Application and must
 * be called just once. This class will automatically track the lifecycle of the application and
 * unregister all the remaining subscriptions for each activity.
 * 主类,必须在application创建的时候调用该类的实例方法,并仅调用一次.
 * 这个类会自动跟踪应用程序的生命周期,并且注销每个activity剩余的订阅subscriptions
 */
public abstract class RxFragment extends Fragment {
    protected RxFlux rxFlux;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        rxFlux = RxFlux.init((Application) context.getApplicationContext());
        if (this instanceof RxViewDispatch) {
            RxViewDispatch viewDispatch = (RxViewDispatch) this;
            viewDispatch.onRxViewRegistered();
            rxFlux.getDispatcher().subscribeRxView(viewDispatch);
        }
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
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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
}
