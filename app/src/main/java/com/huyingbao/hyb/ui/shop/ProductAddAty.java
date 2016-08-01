package com.huyingbao.hyb.ui.shop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.huyingbao.hyb.R;
import com.huyingbao.hyb.base.BaseActivity;

public class ProductAddAty extends BaseActivity {


    @Override
    public void initInjector() {
        mActivityComponent.inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.a_product_add;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        initActionBar();
    }
}
