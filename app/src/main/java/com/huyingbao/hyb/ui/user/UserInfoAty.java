package com.huyingbao.hyb.ui.user;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.huyingbao.hyb.HybApp;
import com.huyingbao.hyb.R;
import com.huyingbao.hyb.base.BaseActivity;

import butterknife.Bind;
import butterknife.OnClick;

public class UserInfoAty extends BaseActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.iv_head)
    ImageView ivHead;
    @Bind(R.id.et_user_name)
    EditText etUserName;

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
        etUserName.setText(HybApp.getUser().getUserName());
        Glide.with(HybApp.getInstance()).load(HybApp.getUser().getHeadImg())
                .centerCrop().placeholder(R.mipmap.ic_launcher).crossFade()
                .into(ivHead);
    }


    @OnClick(R.id.iv_head)
    public void changeHeadImg() {

    }
}
