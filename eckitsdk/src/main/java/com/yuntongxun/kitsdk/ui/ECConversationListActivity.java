package com.yuntongxun.kitsdk.ui;

import com.yuntongxun.eckitsdk.R;
import com.yuntongxun.kitsdk.core.ECKitCustomProviderManager;
import com.yuntongxun.kitsdk.custom.provider.conversation.ECCustomConversationListActionProvider;
import com.yuntongxun.kitsdk.fragment.ConversationListFragment;
import com.yuntongxun.kitsdk.fragment.ConversationListFragment.OnUpdateMsgUnreadCountsListener;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

/**
 * @author shan
 * @date time：2015年7月10日 下午2:02:39
 */
public class ECConversationListActivity extends ECSuperActivity implements
		OnUpdateMsgUnreadCountsListener, OnClickListener {

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);

		getTopBarView().setTopBarToStatus(1, R.drawable.ytx_topbar_back_bt, R.drawable.ytx_tabbar_icon_add,
				R.string.app_conmusicate, this);

	}

	@Override
	public void OnUpdateMsgUnreadCounts() {

	}

	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.ytx_layout_conversationlist_activity;
	}

	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.btn_left) {

			hideSoftKeyboard();
			finish();
		}else if(v.getId()==R.id.btn_right){
			
			ECCustomConversationListActionProvider obj=	ECKitCustomProviderManager.getCustomConversationAction();
			
			if(obj!=null){
				obj.onCustomConversationListRightavigationBarClick(ECConversationListActivity.this);
			}
		}
	}

}
