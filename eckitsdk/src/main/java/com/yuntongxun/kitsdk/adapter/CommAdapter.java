package com.yuntongxun.kitsdk.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class CommAdapter extends BaseAdapter {

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int itemViewType = getItemViewType(position);
        if(isNullContentView(convertView, itemViewType)) {
            convertView = buildViewByType(position, parent, itemViewType);
        }
        bindData(convertView, position, itemViewType);
        return convertView;
    }

    /**
     * 判断是否为空，重复使用
     * @param convertView
     * @param itemViewType
     * @return
     */
    protected boolean isNullContentView(View convertView , int itemViewType) {
        return convertView == null;
    }

    /**
     * 需要实现方法，子类根据提供的Item类型返回对应的View
     * @param position
     * @param parent
     * @param itemViewType
     * @return
     */
    protected abstract View buildViewByType( int position ,ViewGroup parent , int itemViewType);

    /**
     * 绑定数据
     * @param convertView
     * @param position
     * @param itemViewType
     */
    protected abstract void bindData(View convertView , int position , int itemViewType);
}