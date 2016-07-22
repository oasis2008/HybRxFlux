package com.yuntongxun.kitsdk.ui.chatting.view;

import com.yuntongxun.eckits
k.R;
        i port com.yuntongxun.kits
        k.util.DensityUtil;

import
        ndroid app.Activity;
import android
conten.Context;
import android.
        iew.Ke Event;
import andr
id.vie.LayoutInflater;
import and
oid.vi w.MotionEvent;
import andro
d.view View;
import android.view.Wi
dowMan ger;
import android.widget.
        dapter iew;
import android.widget.LinearLayout;
impo

        andro d.widget.PopupWindow;
        impo
        t andr id.widget.PopupWindow.OnDismissListener;

/**
 * 自定义Overflow ActionBar菜单，显示下拉更多选项
 * Created by Jorstin on 2015/3/18.
 */
public class OverflowHelper {

    private Context mContext;
    /**
     * 菜单承载区域View
     */
    private PopupWindow mPopupWindow;
    /**
     * 菜单选项
     */
    private LinearLayout mPopupLayout;
    /**
     * 下拉菜单显示区域
     */
    private PopupMenuListView mListView;
    /**
     * 下拉菜单数据适配器
     */
    private OverflowAdapter mAdapter;
    /**
     * 菜单颜色状态
     */
    private int mNormalColor;
    private int mDisabledColor;

    public OverflowHelper(Context ctx) {
        mContext = ctx;
        mNormalColor = mContext.getResources().getColor(R.color.white);
        mDisabledColor = mContext.getResources().getColor(R.color.text_disabled);
        mPopupLayout = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.comm_popup_menu, null, true);
        mListView = (PopupMenuListView) mPopupLayout.findViewById(R.id.comm_popup_list);
        mAdapter = new OverflowAdapter(this, ctx);
        mListView.setAdapter(mAdapter);
        mListView.setOnKeyListener(mOnKeyListener);
        mPopupWindow = new PopupWindow(mPopupLayout, -2, -2, true);

        mPopupWindow.setContentView(mPopupLayout);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setWidth(414);
        mPopupWindow.getContentView().setOnTouchListener(mOnTouchListener);
        mPopupWindow.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                backgroundAlpha(1f);

            }
        });
        mPopupWindow.update();
    }

    OnDismissListener listener;

    public void setOnCloseListener(OnDismissListener listener) {
        this.listener = listener;
    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0  
        ((Activity) mContext).getWindow().setAttributes(lp);
    }


    /**
     * 显示菜单
     *
     * @param anchor
     * @param xoff
     * @param yoff
     */
    public void showAsDropDown(View anchor, float xoff, float yoff) {
        mPopupWindow.showAsDropDown(anchor, DensityUtil.dip2px(xoff), DensityUtil.dip2px(yoff));
    }

    public void showAsDropDown(View anchor) {
        mPopupWindow.showAsDropDown(anchor, DensityUtil.dip2px(-82.0F), DensityUtil.dip2px(11.0F));
    }

    /**
     * 这是点击事件监听
     *
     * @param itemClickListener
     */
    public void setOnOverflowItemClickListener(AdapterView.OnItemClickListener itemClickListener) {
        mListView.setOnItemClickListener(itemClickListener);
    }

    /**
     * 设置菜单显示数据
     *
     * @param items
     */
    public void setOverflowItems(OverflowAdapter.OverflowItem[] items) {
        mAdapter.setOverflowItem(items);
        mAdapter.notifyDataSetChanged();
        mListView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int measuredWidth = mListView.getMeasuredWidth();
        mPopupWindow.setWidth(measuredWidth);
        mPopupWindow.update();
    }

    /**
     * 是否菜单显示
     *
     * @return
     */
    public boolean isOverflowShowing() {
        return mPopupWindow.isShowing();
    }

    /**
     * 关闭菜单
     */
    public void dismiss() {
        if (!mPopupWindow.isShowing()) {
            return;
        }
        mPopupWindow.dismiss();
    }

    /**
     * @return the mNormalColor
     */
    public int getNormalColor() {
        return mNormalColor;
    }

    /**
     * @return the mDisabledColor
     */
    public int getDisabledColor() {
        return mDisabledColor;
    }


    private final View.OnKeyListener mOnKeyListener
            = new View.OnKeyListener() {

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (event.getKeyCode() == KeyEvent.KEYCODE_MENU && event.getAction() != KeyEvent.ACTION_DOWN) {
                return false;
            }
            dismiss();
            return true;
        }

    };

    private final View.OnTouchListener mOnTouchListener
            = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            dismiss();
            return false;
        }
    };

}