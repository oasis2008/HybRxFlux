package com.yuntongxun.kitsdk.custom.provider.chat;

import java.util.ArrayList;

import android.content.Context;

import com.yuntongxun.kitsdk.custom.provider.IBaseProvider;

/**
 * @author shan
 * @date time：2015年7月16日 上午10:31:17
 */
public interface ECCustomChatPlusExtendProvider extends IBaseProvider {

	String[] getCustomPlusTitleArray(Context context);

	int[] getCustomPlusDrawableArray(Context context);

	boolean onPlusExtendedItemClick(Context context, String title, int index);

}
