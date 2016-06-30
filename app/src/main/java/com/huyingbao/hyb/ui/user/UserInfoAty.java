package com.huyingbao.hyb.ui.user;

import android.os.Bundle;

import com.huyingbao.hyb.R;
import com.huyingbao.hyb.base.BaseActivity;

public class UserInfoAty extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_user_info);
    }

    @Override
    public void initInjector() {
        mActivityComponent.inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.a_user_info;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {

    }
}
