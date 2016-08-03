package com.huyingbao.hyb.inject.component;

import android.app.Activity;
import android.content.Context;

import com.huyingbao.hyb.base.BaseFragment;
import com.huyingbao.hyb.inject.module.FragmentModule;
import com.huyingbao.hyb.inject.qualifier.ContextLife;
import com.huyingbao.hyb.inject.scope.PerFragment;
import com.huyingbao.hyb.ui.contacts.ContactsFrg;
import com.huyingbao.hyb.ui.home.HomeFrg;
import com.huyingbao.hyb.ui.shop.ProductListFrg;
import com.huyingbao.hyb.ui.shop.ShopListBearbyFrg;

import dagger.Component;

@PerFragment
@Component(dependencies = ApplicationComponent.class, modules = FragmentModule.class)
public interface FragmentComponent {
    @ContextLife("Application")
    Context getApplicationContext();

    @ContextLife("Activity")
    Context getActivityContext();

    Activity getActivity();

//    void inject(BaseFragment baseFragment);

    void inject(HomeFrg homeFrg);

    void inject(ContactsFrg contactsFrg);

    void inject(ShopListBearbyFrg shopListBearbyFrg);

    void inject(ProductListFrg productListFrg);
}
