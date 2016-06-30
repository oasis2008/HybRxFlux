package com.huyingbao.hyb.ui.user;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import com.huyingbao.hyb.R;
import com.huyingbao.hyb.base.BaseActivity;

import butterknife.Bind;

public class UserInfoAty extends BaseActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;

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
        //设置toobar
        setSupportActionBar(toolbar);
        //设置标题
        toolbar.setTitle(getTitle());
        //设置返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
}
