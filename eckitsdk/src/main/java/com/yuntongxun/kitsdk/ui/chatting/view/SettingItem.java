/*
 *  Copyright (c) 2013 The CCP project authors. All Rights Reserved.
 *
 *  Use of this source code is governed by a Beijing Speedtong Information Technology Co.,Ltd license
 *  that can be found in the LICENSE file in the root of the web site.
 *
 *   http://www.yuntongxun.com
 *
 *  An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */
package com.yuntongxun.kitsdk.ui.chatting.view;

import com.yuntongxun.eckitsdk.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;



/**
 * 设置选项
 * @author Jorstin Chan@容联•云通讯
 * @date 2014-12-27
 * @version 4.0
 */
public class SettingItem extends RelativeLayout {

    /**切换按钮*/
    public static final int ACCESSORY_TYPE_CHECKBOX = 2;
    
    /**箭头按钮*/
    public static final int ACCESSORY_TYPE_ARROW = 1;
    
    
    /**radio按钮*/
    public static final int ACCESSORY_TYPE_RADIO = 3;

    /**Item内容区域*/
    private LinearLayout mContent;
    /**标题View*/
    private TextView mTitle;
    /**概要View*/
    private TextView mSummary;
    /**切换View*/
    private CheckedTextView mCheckedTextView;
    private TextView mNewUpdate;
    /**分割线*/
    private View mDividerView;
    /**附加类型*/
    private int mAccessoryType;
    /**是否显示分割线*/
    private boolean mShowDivider;
    /**标题*/
    private String mTitleText;
    /**概要文字*/
    private String mSummaryText;
    private int[] mInsetDrawableRect = {0,0,0,0};

	private ImageView titleImg;

    /**
     * @param context
     * @param attrs
     */
    public SettingItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.detaillist_item, this, true);

        mContent = (LinearLayout) findViewById(R.id.content);
        mTitle = (TextView) findViewById(android.R.id.title);
        mSummary = (TextView) findViewById(android.R.id.summary);
        mNewUpdate = (TextView) findViewById(R.id.text_tv_one);
        mCheckedTextView = (CheckedTextView) findViewById(R.id.accessory_checked);
        mDividerView = findViewById(R.id.item_bottom_divider);
        titleImg = (ImageView) findViewById(R.id.title_iv);

        TypedArray localTypedArray = context.obtainStyledAttributes(attrs, R.styleable.setting_info);
        setTitleText(localTypedArray.getString(R.styleable.setting_info_item_titleText));
        setDetailText(localTypedArray.getString(R.styleable.setting_info_item_detailText));
        setAccessoryType(localTypedArray.getInt(R.styleable.setting_info_item_accessoryType , 0));
        setShowDivider(localTypedArray.getBoolean(R.styleable.setting_info_item_showDivider , true));
        setAvatar(localTypedArray.getDrawable(R.styleable.setting_info_item_avatar));
        localTypedArray.recycle();

        mNewUpdate.setVisibility(View.GONE);
    }


	private void setAvatar(Drawable drawable) {
    	if(drawable == null){
    		return;
    	}
    	titleImg.setVisibility(View.VISIBLE);
    	titleImg.setImageDrawable(drawable);
    	
	}

	/**
     * @param showDivider
     */
    private void setShowDivider(boolean showDivider) {
        mShowDivider = showDivider;
        View dividerView = mDividerView;
        dividerView.setVisibility(mShowDivider ? View.VISIBLE : View.GONE);
    }
    
    public void showDivider(boolean showDivider){
    	mDividerView.setVisibility(showDivider ? View.VISIBLE : View.GONE);
    }

    /**
     * 设置标题信息
     * @param text
     */
    public void setTitleText(String text) {
        mTitleText = text;
        if(text == null) {
            mTitle.setText("");
            return ;
        }
        mTitle.setText(mTitleText);
    }

    public void setCheckText(String text) {
        if(text == null) {
            mCheckedTextView.setText("");
            return ;
        }
        mCheckedTextView.setText(text);
    }

    /**
     * @param text
     *
     */
    public void setDetailText(String text) {
        mSummaryText = text;
        if(text == null) {
            mSummary.setText("");
            mSummary.setVisibility(View.GONE);
            return ;
        }
        mSummary.setText(mSummaryText);
        mSummary.setVisibility(View.VISIBLE);
    }

    @SuppressWarnings("deprecation")
    private void setSettingBackground(int resid) {
        int[] rect = new int[4];
        rect[0] = getPaddingLeft();
        rect[1] = getPaddingTop();
        rect[2] = getPaddingRight();
        rect[3] = getPaddingBottom();
        if (isInsetDrawable()) {
            setBackgroundDrawable(new InsetDrawable(getContext().getResources()
                    .getDrawable(resid), mInsetDrawableRect[0],
                    mInsetDrawableRect[1], mInsetDrawableRect[2],
                    mInsetDrawableRect[3]));
        } else {
            setBackgroundResource(resid);
        }
        setPadding(rect[0], rect[1], rect[2], rect[3]);
    }

    /**
     * 是否显示版本更新
     * @param visibility
     */
    public void setNewUpdateVisibility(boolean visibility) {
        if(mNewUpdate != null) {
            mNewUpdate.setVisibility(visibility? View.VISIBLE:View.GONE);
        }
    }

    /**
     *
     * @return
     */
    private boolean isInsetDrawable() {
        for(int i = 0; i < mInsetDrawableRect.length ; i++) {
            if(mInsetDrawableRect[i] <= 0) {
                continue;
            }
            return true;
        }
        return false;
    }

    /**
     *
     * @param accessoryType
     */
    public void setAccessoryType(int accessoryType) {
    	switch (accessoryType) {
		case ACCESSORY_TYPE_CHECKBOX:
            mAccessoryType = ACCESSORY_TYPE_CHECKBOX;
            mCheckedTextView.setCheckMarkDrawable(getContext().getResources().getDrawable(R.drawable.switch_style));
            mCheckedTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            setSettingBackground(0);
			break;
		case ACCESSORY_TYPE_ARROW:
            mAccessoryType = ACCESSORY_TYPE_ARROW;
            mCheckedTextView.setCheckMarkDrawable(getContext().getResources().getDrawable(R.drawable.enter));
            mCheckedTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            setSettingBackground(0);
			break;
		case ACCESSORY_TYPE_RADIO:
            mAccessoryType = ACCESSORY_TYPE_RADIO;
            mCheckedTextView.setCheckMarkDrawable(getContext().getResources().getDrawable(R.drawable.choose_style));
            mCheckedTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            setSettingBackground(0);
			break;
		default:
			break;
		}
    }

    /**
     * 返回切换按钮
     * @return
     */
    public CheckedTextView getCheckedTextView(){
        return mCheckedTextView;
    }

    /**
     * 是否处于选中状态
     * @return
     */
    public boolean isChecked() {
        if(mAccessoryType == ACCESSORY_TYPE_CHECKBOX ||  mAccessoryType == ACCESSORY_TYPE_RADIO) {
            return mCheckedTextView.isChecked();
        }
        return true;
    }

    /**
     * 设置状态
     * @param checked
     */
    public void setChecked(boolean checked) {
        if(mAccessoryType != ACCESSORY_TYPE_CHECKBOX &&  mAccessoryType != ACCESSORY_TYPE_RADIO) {
            return ;
        }
        mCheckedTextView.setChecked(checked);
    }


    public void toggle() {
        if(mAccessoryType != ACCESSORY_TYPE_CHECKBOX &&  mAccessoryType != ACCESSORY_TYPE_RADIO ) {
            return ;
        }
        mCheckedTextView.toggle();
    }
}
