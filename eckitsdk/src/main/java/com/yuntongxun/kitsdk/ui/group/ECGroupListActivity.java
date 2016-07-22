package com.yuntongxun.kitsdk.ui.group;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;

import com.yuntongxun.eckitsdk.R;
import com.yuntongxun.kitsdk.fragment.ConversationListFragment.OnUpdateMsgUnreadCountsListener;
import com.yuntongxun.kitsdk.ui.ECSuperActivity;
import com.yuntongxun.kitsdk.ui.chatting.view.OverflowAdapter;
import com.yuntongxun.kitsdk.ui.chatting.view.OverflowHelper;

/**
 * @author shan
 * @date time：2015年7月10日 下午2:02:39
 */
public class ECGroupListActivity extends ECSuperActivity implements
        OnUpdateMsgUnreadCountsListener, OnClickListener {


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);


        getTopBarView().setTopBarToStatus(1, R.drawable.ytx_topbar_back_bt, R.drawable.ytx_tabbar_icon_add,
                R.string.group_list, this);


    }

    @Override
    protected void onResume() {

        super.onResume();


    }


    @Override
    public void OnUpdateMsgUnreadCounts() {

    }

    @Override
    protected int getLayoutId() {

        return R.layout.ytx_layout_grouplist_activity;
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btn_left) {

            hideSoftKeyboard();
            finish();
        } else if (v.getId() == R.id.btn_right) {

            controlPlusSubMenu();
        }
    }

    private OverflowHelper mOverflowHelper;

    private void controlPlusSubMenu() {
        if (mOverflowHelper == null) {
            mOverflowHelper = new OverflowHelper(this);
        }
        if (mOverflowHelper.isOverflowShowing()) {
            mOverflowHelper.dismiss();
            return;
        }


        OverflowAdapter.OverflowItem[] mItems = new OverflowAdapter.OverflowItem[2];
        mItems[0] = new OverflowAdapter.OverflowItem("创建群组");
        mItems[1] = new OverflowAdapter.OverflowItem(
                "搜索群组");

//		mItems[0].setIcon(R.drawable.bomb_box_icon_04);
//		mItems[1].setIcon(R.drawable.bomb_box_icon_05);

        mOverflowHelper.setOverflowItems(mItems);
        mOverflowHelper.setOnOverflowItemClickListener(mOverflowItemCliclListener);
        View view = findViewById(R.id.btn_right);
        mOverflowHelper.showAsDropDown(view);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.9f;
        getWindow().setAttributes(lp);
    }

    private final AdapterView.OnItemClickListener mOverflowItemCliclListener = new AdapterView.OnItemClickListener() {


        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            if (position == 0) {

                startActivity(new Intent(ECGroupListActivity.this, CreateGroupActivity.class));

            } else if (position == 1) {
                startActivity(new Intent(ECGroupListActivity.this, BaseSearch.class));

            }

            if (mOverflowHelper.isOverflowShowing()) {
                mOverflowHelper.dismiss();
            }

        }
    };

}
