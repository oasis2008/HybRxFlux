package com.huyingbao.hyb.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.huyingbao.hyb.HybApp;
import com.huyingbao.hyb.R;
import com.huyingbao.hyb.model.Shop;

import java.util.List;


public class ShopListAdapter extends BaseQuickAdapter<Shop> {


    public ShopListAdapter(List<Shop> data) {
        super(R.layout.i_shop, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Shop shop) {
        baseViewHolder
                .setText(R.id.tv_shaop_name, shop.getShopName())
                .setText(R.id.tv_shop_des, shop.getShopId()+"")
                .setText(R.id.tv_shop_info, shop.getCode() + "");
        Glide.with(HybApp.getInstance())
                .load(shop.getHeadImg())
                .centerCrop()
                .placeholder(R.drawable.ic_menu_camera)
                .crossFade()
                .into((ImageView) baseViewHolder.getView(R.id.iv_shop_head));
    }
}