package com.huyingbao.hyb.base;

import android.content.Context;
import android.view.Gravity;

import com.huyingbao.hyb.R;


public class DialogFactory {
    /**
     * 创建Dialog，默认显示在中间，无弹出动画
     *
     * @param context
     * @return
     */
    public static BaseDialog createDialog(Context context) {
        BaseDialog dialog = new BaseDialog(context, R.style.DialogNoTItleTheme);
        return dialog;
    }

    /**
     * 创建一个位置在底部，并且从底部弹出的Dialog
     *
     * @param context
     * @return
     */
    public static BaseDialog createBottomDialog(Context context) {
        BaseDialog dialog = (BaseDialog) createDialog(context);
        dialog.setAnimations(R.style.AnimBottomStyle);
        dialog.setGravity(Gravity.BOTTOM);
        return dialog;
    }
}
