package com.huyingbao.hyb;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hardsoftstudio.rxflux.action.RxError;
import com.hardsoftstudio.rxflux.dispatcher.RxViewDispatch;
import com.hardsoftstudio.rxflux.store.RxStore;
import com.hardsoftstudio.rxflux.store.RxStoreChange;
import com.huyingbao.hyb.actions.Actions;
import com.huyingbao.hyb.base.BaseActivity;
import com.huyingbao.hyb.stores.UsersStore;
import com.huyingbao.hyb.ui.contacts.ContactsFrg;
import com.huyingbao.hyb.ui.home.HomeFrg;
import com.huyingbao.hyb.ui.login.LoginAty;
import com.huyingbao.hyb.ui.shop.ShopListBearbyFrg;
import com.huyingbao.hyb.ui.user.UserInfoAty;
import com.huyingbao.hyb.ui.user.UserSendMessageAty;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class MainAty extends BaseActivity
        implements RxViewDispatch, NavigationView.OnNavigationItemSelectedListener {

    /**
     * 页面个数
     */
    private static final int COUNT_FRAGMENT = 3;

    @BindView(R.id.container)
    ViewPager mViewPager;
    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.a_main_nvMain)
    NavigationView navView;
    @BindView(R.id.a_main_dlMain)
    DrawerLayout drawerLayout;

    /**
     * 当前fragment的位置信息
     */
    private int mCurrentPosition;
    private Fragment[] mFragments;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private FragmentManager mFragmentManager;

    @Inject
    UsersStore usersStore;

    @Override
    public void initInjector() {
        mActivityComponent.inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.a_main;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        initActionBar(null, false);
        //侧滑菜单控件
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        //右侧导航视图
        navView.setNavigationItemSelectedListener(this);



        //初始化fragment数组
        mFragments = new Fragment[COUNT_FRAGMENT];
        //初始化fragmentmanger
        mFragmentManager = getSupportFragmentManager();
        //左右滑动fremge适配器
        mSectionsPagerAdapter = new SectionsPagerAdapter(mFragmentManager);
        mViewPager.setOffscreenPageLimit(COUNT_FRAGMENT + 1);
        //viewpager设置page变化监听器
        // mViewPager.addOnPageChangeListener(onPagechangeListener);
        // Set up the ViewPager with the sections adapter.
        mViewPager.setAdapter(mSectionsPagerAdapter);
        //底部tab跟随viewpager
        tabs.setupWithViewPager(mViewPager);
        recover(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initNavView();
    }

    /**
     * 初始化左侧导航
     */
    private void initNavView() {
        View headerView = navView.getHeaderView(navView.getHeaderCount() - 1);
        ImageView nHeaderIvUserHeadImg = (ImageView) headerView.findViewById(R.id.n_header_ivUserHeadImg);
        TextView nHeaderTvUserName = (TextView) headerView.findViewById(R.id.n_header_tvUserName);
        TextView nHeaderTvPhone = (TextView) headerView.findViewById(R.id.n_header_tvPhone);
        LinearLayout nHeaderLlUserInfo = (LinearLayout) headerView.findViewById(R.id.n_header_llUserInfo);

        nHeaderTvUserName.setText(HybApp.getUser().getUserName());
        nHeaderTvPhone.setText(HybApp.getUser().getPhone());
        Glide.with(HybApp.getInstance()).load(HybApp.getUser().getHeadImg())
                .centerCrop().placeholder(R.mipmap.ic_launcher).crossFade()
                .into(nHeaderIvUserHeadImg);
        nHeaderLlUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(UserInfoAty.class);
            }
        });
    }

    /**
     * 恢复数据
     *
     * @param savedInstanceState
     */
    private void recover(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            int position = savedInstanceState.getInt("mCurrentPosition");
            this.setPosition(position);
        } else {
            this.setPosition(0);
        }
    }

    /**
     * 恢复数据
     *
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("mCurrentPosition", this.mCurrentPosition);
        if (this.mFragments[this.mCurrentPosition] != null) {
            this.mFragmentManager.putFragment(outState, Integer.toString(this.mCurrentPosition), this.mFragments[this.mCurrentPosition]);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        //回退键先关闭左侧导航view
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.a_main_dlMain);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main_aty, menu);
//        return true;

//        menu.add("探索")
//                .setIcon(R.drawable.ic_menu_camera)
//                .setOnMenuItemClickListener(item -> false)
//                .setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
//        menu.add("消息")
//                .setIcon(R.drawable.ic_menu_camera)
//                .setOnMenuItemClickListener(item -> false)
//                .setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
//        return super.onCreateOptionsMenu(menu);

        SubMenu subMenu = menu.addSubMenu("");
        subMenu.add("探索")
                .setIcon(R.drawable.ic_menu_camera)
                .setOnMenuItemClickListener(item -> false);
        subMenu.add("消息")
                .setIcon(R.drawable.ic_menu_camera)
                .setOnMenuItemClickListener(item -> false);
        MenuItem item = subMenu.getItem();
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {
            hybActionCreator.logout();
        }
        //点击之后关闭左侧导航
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.a_main_dlMain);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @OnClick({R.id.toolbar, R.id.container, R.id.tabs, R.id.fab, R.id.a_main_nvMain, R.id.a_main_dlMain})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar:
                break;
            case R.id.container:
                break;
            case R.id.tabs:
                break;
            case R.id.fab:
                startActivity(UserSendMessageAty.class);
                break;
            case R.id.a_main_nvMain:
                break;
            case R.id.a_main_dlMain:
                break;
        }
    }

    /**
     * 设置当前位置
     *
     * @param position
     */
    public void setPosition(int position) {
        if (position < 0 || position > COUNT_FRAGMENT - 1) {// 位置非法
            return;
        }
        mCurrentPosition = position;
        mViewPager.setCurrentItem(position, false);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (mFragments[position] != null) {
                return mFragments[position];
            }
            switch (position) {
                case 0:
                    mFragments[position] = ContactsFrg.newInstance();
                    break;
                case 1:
                    mFragments[position] = ShopListBearbyFrg.newInstance();
                    break;
                case 2:
                    mFragments[position] = HomeFrg.newInstance();
                    break;
                case 3:
                    mFragments[position] = HomeFrg.newInstance();
                    break;
                case 4:
                    mFragments[position] = HomeFrg.newInstance();
                    break;
            }
            return mFragments[position];
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return COUNT_FRAGMENT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "呼";
                case 1:
                    return "应";
                case 2:
                    return "望";
                case 3:
                    return "SECTION 3";
                case 4:
                    return "SECTION 3";
            }
            return null;
        }
    }

    @Override
    public void onRxStoreChanged(@NonNull RxStoreChange change) {
        switch (change.getStoreId()) {
            case UsersStore.STORE_ID:
                switch (change.getRxAction().getType()) {
                    case Actions.LOGOUT:
                        startActivity(LoginAty.class);
                        finish();
                        break;
                }
                break;
        }
    }

    @Override
    public void onRxError(@NonNull RxError error) {

    }


    /**
     * 将activity拥有的fragment注册到dispatcher
     */
    @Override
    public void onRxViewRegistered() {
    }

    /**
     * 从dispatcher中解除fragment的注册
     */
    @Override
    public void onRxViewUnRegistered() {
    }

    /**
     * 需要注册rxstore list 在activity创建的时候调用该方法,
     * 注册rxstore list 到 dispatcher
     *
     * @return
     */
    @Nullable
    @Override
    public List<RxStore> getRxStoreListToRegister() {
        return Arrays.asList(usersStore);
    }

    /**
     * 需要解除注册rxstore list 在activity创建的时候调用该方法,
     * 从 dispatcher 解除注册rxstore list
     *
     * @return
     */
    @Nullable
    @Override
    public List<RxStore> getRxStoreListToUnRegister() {
        return null;
    }

}
