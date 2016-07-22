package com.yuntongxun.kitsdk.ui.chatting.view;

import com.yuntongxun.kitsdk.ut
ls.Log til;

import android.content.Conte
t;
imp rt android.support.v4.view
ViewPa er;
import android.util.A

ribute et;
import android.view.MotionEvent;



public class HackyViewPager extends ViewPager {
    private static final String TAG = "HackyViewPager";

    public HackyViewPager(Context context) {
        super(context);
    }

    public HackyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            LogUtil.e(TAG, "hacky viewpager error1");
            return false;
        } catch (ArrayIndexOutOfBoundsException e) {
            LogUtil.e(TAG, "hacky viewpager error2");
            return false;
        }
    }

}
