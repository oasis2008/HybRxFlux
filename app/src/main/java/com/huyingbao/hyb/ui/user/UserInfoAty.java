package com.huyingbao.hyb.ui.user;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hardsoftstudio.rxflux.action.RxError;
import com.hardsoftstudio.rxflux.dispatcher.RxViewDispatch;
import com.hardsoftstudio.rxflux.store.RxStore;
import com.hardsoftstudio.rxflux.store.RxStoreChange;
import com.huyingbao.hyb.HybApp;
import com.huyingbao.hyb.R;
import com.huyingbao.hyb.actions.Actions;
import com.huyingbao.hyb.actions.Keys;
import com.huyingbao.hyb.base.BaseCameraAty;
import com.huyingbao.hyb.model.HybUser;
import com.huyingbao.hyb.model.LocalFile;
import com.huyingbao.hyb.stores.FileStore;
import com.huyingbao.hyb.stores.UsersStore;
import com.huyingbao.hyb.utils.CommonUtils;
import com.huyingbao.hyb.utils.gsonhelper.GsonHelper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class UserInfoAty extends BaseCameraAty implements RxViewDispatch {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_head)
    ImageView ivHead;
    @BindView(R.id.et_user_name)
    EditText etUserName;
    private String headImg;


    @Inject
    FileStore fileStore;
    @Inject
    UsersStore usersStore;

    private String fileKey;

    private HybUser user;
    private Map<String, String> userMap;

    @Override
    public void initInjector() {
        mActivityComponent.inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.a_user_info;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        initActionBar();
        etUserName.setText(HybApp.getUser().getUserName());
        Glide.with(HybApp.getInstance()).load(HybApp.getUser().getHeadImg())
                .centerCrop().placeholder(R.mipmap.ic_launcher).crossFade()
                .into(ivHead);
        userMap = new HashMap<>();
    }


    @OnClick(R.id.iv_head)
    public void changeHeadImg() {
        mCropOption = new CropOption();
        mCropOption.outputX = 480;
        mCropOption.outputY = 480;
        showDefaultCameraMenu();
    }

    @Override
    protected void onReceiveBitmap(Uri uri) {
        Glide.with(this).load(uri)
                //不缓存到硬盘
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                //中间大小
                .centerCrop()
                //不使用内存缓存
                .skipMemoryCache(true)
                //淡入淡出动画
                .crossFade()
                .placeholder(R.mipmap.ic_launcher)
                .into(ivHead);
//        headImg = BitmapUtils.compressWebp(uri.getPath());
        headImg = uri.getPath();
    }


    @OnClick(R.id.bt_ok)
    public void onClick() {
        if (headImg != null && !headImg.isEmpty()) {
            hybActionCreator.uploadOneFile(new LocalFile(headImg, CommonUtils.getFileNameByTime(headImg)), Keys.PART_NAME_HEAD_IMAGE);
        } else {
            updateUser();
        }
    }

    @Override
    public void onRxStoreChanged(@NonNull RxStoreChange change) {
        switch (change.getStoreId()) {
            case FileStore.STORE_ID:
                switch (change.getRxAction().getType()) {
                    case Actions.UPLOAD_ONE_FILE:
                        userMap.put("headImg", fileStore.getFileKey());
                        updateUser();
                        break;
                }
                break;
            case UsersStore.STORE_ID:
                switch (change.getRxAction().getType()) {
                    case Actions.UPDATE_USER:
                        finish();
                        break;
                }
                break;
        }
    }

    private void updateUser() {
        String userName = etUserName.getText().toString();
        if (!userName.isEmpty() && !userName.equals(HybApp.getUser().getUserName())) {
            userMap.put("userName", userName);
        }
        hybActionCreator.updateUser(GsonHelper.toJson(userMap));
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
        return Arrays.asList(fileStore, usersStore);
    }

    @Nullable
    @Override
    public List<RxStore> getRxStoreListToUnRegister() {
        return Arrays.asList(fileStore);
    }
}
