package com.huyingbao.hyb.ui.contacts;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.hardsoftstudio.rxflux.action.RxError;
import com.hardsoftstudio.rxflux.dispatcher.RxViewDispatch;
import com.hardsoftstudio.rxflux.store.RxStore;
import com.hardsoftstudio.rxflux.store.RxStoreChange;
import com.huyingbao.hyb.HybApp;
import com.huyingbao.hyb.R;
import com.huyingbao.hyb.actions.Actions;
import com.huyingbao.hyb.adapter.MsgFromUserListAdapter;
import com.huyingbao.hyb.base.BaseFragment;
import com.huyingbao.hyb.model.MsgFromUser;
import com.huyingbao.hyb.stores.MsgStore;
import com.huyingbao.hyb.stores.UsersStore;
import com.huyingbao.hyb.utils.CommonUtils;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/5/6.
 */
public class ContactsFrg extends BaseFragment implements RxViewDispatch {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.srl_content)
    SwipeRefreshLayout srlContent;

    @Inject
    UsersStore usersStore;
    @Inject
    MsgStore msgStore;

    private MsgFromUserListAdapter adapter;
    private boolean isRefresh;
    private List<MsgFromUser> msgFromUserList;

    public static ContactsFrg newInstance() {
        ContactsFrg fragment = new ContactsFrg();
        return fragment;
    }

    @Override
    protected void initInjector() {
        mFragmentComponent.inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.f_home;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        //初始化list
        msgFromUserList = new ArrayList<MsgFromUser>();
        //获取数据
        hybActionCreator.getUserMessage(HybApp.getUser().getUserId(), 0);
        //设置刷新view
        srlContent.setRefreshing(true);
        srlContent.setOnRefreshListener(() -> {
            refresh();
        });
        //设置空数据view
        View emptyView = CommonUtils.initEmptyView(mContext,
                (ViewGroup) recyclerView.getParent(),
                R.drawable.ic_menu_camera, "暂无发送数据");
        //创建adapter
        adapter = new MsgFromUserListAdapter(msgFromUserList);
        adapter.setEmptyView(emptyView);
        //设置recyclerview
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {//上拉
                    int lastVisiblePosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                    if (lastVisiblePosition + 1 == adapter.getItemCount()) {//当前显示的数据是最后一条
                        srlContent.setRefreshing(true);
                        hybActionCreator.getUserMessage(HybApp.getUser().getUserId(), adapter.getItemCount());
                    }
                }
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    @Override
    public void onRxStoreChanged(@NonNull RxStoreChange change) {
        switch (change.getStoreId()) {
            case MsgStore.STORE_ID:
                switch (change.getRxAction().getType()) {
                    case Actions.GET_USER_MESSAGE:
                        srlContent.setRefreshing(false);
                        if (isRefresh) {//刷新
                            isRefresh = false;
                            msgFromUserList.clear();
                            msgFromUserList.addAll(msgStore.getMsgFromUserList());
                            adapter.notifyDataSetChanged();
                        } else {//添加数据
                            msgFromUserList.addAll(msgStore.getMsgFromUserList());
                            adapter.notifyDataSetChanged();
                        }
                        break;
                }
                break;
        }
    }

    @Override
    public void onRxError(@NonNull RxError error) {
    }

    @Override
    public void onRxViewRegistered() {
    }

    @Override
    public void onRxViewUnRegistered() {
    }

    @Nullable
    @Override
    public List<RxStore> getRxStoreListToRegister() {
        return Arrays.asList(usersStore, msgStore);
    }

    @Nullable
    @Override
    public List<RxStore> getRxStoreListToUnRegister() {
        return null;
    }


    /**
     * 刷新
     */
    private void refresh() {
        isRefresh = true;
        hybActionCreator.getUserMessage(HybApp.getUser().getUserId(), 0);
    }
}
