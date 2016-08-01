package com.huyingbao.hyb.inject.component;

import android.app.Activity;
import android.content.Context;

import com.huyingbao.hyb.LoadingAty;
import com.huyingbao.hyb.MainAty;
import com.huyingbao.hyb.MainShopAty;
import com.huyingbao.hyb.base.BaseActivity;
import com.huyingbao.hyb.inject.module.ActivityModule;
import com.huyingbao.hyb.inject.qualifier.ContextLife;
import com.huyingbao.hyb.inject.scope.PerActivity;
import com.huyingbao.hyb.ui.login.LoginAty;
import com.huyingbao.hyb.ui.login.RegisterAty;
import com.huyingbao.hyb.ui.shop.ProductAddAty;
import com.huyingbao.hyb.ui.shop.ProductListFrg;
import com.huyingbao.hyb.ui.shop.RegisterShopAty;
import com.huyingbao.hyb.ui.shop.ShopDetailAty;
import com.huyingbao.hyb.ui.user.UserInfoAty;
import com.huyingbao.hyb.ui.user.UserSendMessageAty;

import dagger.Component;

/**
 * 两个Component间有依赖关系，那么它们不能使用相同的Scope
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class})
public interface ActivityComponent {
    @ContextLife("Activity")
    Context getActivityContext();

    /**
     * 从对应的ActivityModule中找不到,从dependencies的ApplicationComponent中找得到
     *
     * @return
     */
    @ContextLife("Application")
    Context getApplicationContext();

    Activity getActivity();

    void inject(BaseActivity baseActivity);

    void inject(LoginAty loginAty);

    void inject(UserSendMessageAty userSendMessageAty);

    void inject(RegisterShopAty registerShopAty);

    void inject(ShopDetailAty shopDetailAty);

    void inject(LoadingAty loadingAty);

    void inject(MainAty mainAty);

    void inject(MainShopAty mainShopAty);

    void inject(RegisterAty registerAty);

    void inject(UserInfoAty userInfoAty);

    void inject(ProductAddAty productAddAty);

}
