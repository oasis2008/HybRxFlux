package com.huyingbao.hyb.stores;

import com.baidu.location.BDLocation;
import com.hardsoftstudio.rxflux.RxFlux;
import com.hardsoftstudio.rxflux.action.RxAction;
import com.hardsoftstudio.rxflux.dispatcher.Dispatcher;
import com.hardsoftstudio.rxflux.store.RxStore;
import com.hardsoftstudio.rxflux.store.RxStoreChange;
import com.huyingbao.hyb.HybApp;
import com.huyingbao.hyb.actions.Actions;
import com.huyingbao.hyb.actions.Keys;
import com.huyingbao.hyb.model.HybUser;
import com.huyingbao.hyb.push.BaiduPushBase;
import com.huyingbao.hyb.utils.gsonhelper.GsonHelper;


public class UsersStore extends RxStore implements UsersStoreInterface {

    /**
     * StoreId,用来在postChange(RxStoreChange change)时,生成RxStoreChange
     * 在接受RxStoreChange的时候,区分是哪个store
     */
    public static final String STORE_ID = "UsersStore";

    /**
     * store 中存储的user
     */
    private HybUser mUser;
    /**
     * store 中存储的BDLocation
     */
    private BDLocation bdLocation;

    public UsersStore(Dispatcher dispatcher) {
        super(dispatcher);
    }

    /**
     * This callback will get all the actions, each store must react on the types he want and do
     * some logic with the model, for example add it to the list to cache it, modify
     * fields etc.. all the logic for the models should go here and then call postChange so the
     * view request the new data
     * 这个回调接收所有的actions(RxAction),每个store都必须根据action的type做出反应,,例如将其添加到列表缓存,修改字段等。
     * 所有的逻辑模型应该在这里,然后调用postChange请求新数据视图
     */
    @Override
    public void onRxAction(RxAction action) {
        switch (action.getType()) {
            case Actions.LOGIN:
                mUser = action.get(Keys.USER);
                HybApp.setUser(mUser);
                //保存登录状态
                HybApp.getInstance().getLocalSorageUtils().setLogin(true);
                //保存当前登录用户信息
                HybApp.getInstance().getLocalSorageUtils().setUser(GsonHelper.toJson(mUser));
                //开启登录成功之后也要开启百度推送
                BaiduPushBase.start(HybApp.getInstance());
                break;
            case Actions.LOGOUT:
                mUser = null;
                HybApp.setUser(null);
                //清除登陆状态
                HybApp.getInstance().getLocalSorageUtils().setLogin(false);
                //清除当前登录用户
                HybApp.getInstance().getLocalSorageUtils().setUser(null);
                //清除当前登录用户所在店铺
                HybApp.setShop(null);
                //清除当前登录用户所在店铺
                HybApp.getInstance().getLocalSorageUtils().setShop(null);
                //停止百度推送
                BaiduPushBase.stop(HybApp.getInstance());
                break;
            case Actions.REGISTER_USER:
                mUser = action.get(Keys.USER);
                break;
            case Actions.A_GET_LOCATION:
                bdLocation = action.get(Keys.LOCATION);
                break;
            case Actions.UPDATE_USER:
                mUser = action.get(Keys.USER);
                //保存当前登录用户信息
                HybApp.setUser(mUser);
                HybApp.getInstance().getLocalSorageUtils().setUser(GsonHelper.toJson(mUser));
                break;
            default: // IMPORTANT if we don't modify the store just ignore
                return;
        }
        postChange(new RxStoreChange(STORE_ID, action));
    }

    @Override
    public HybUser getUser() {
        return mUser;
    }

    @Override
    public BDLocation getBDLocation() {
        return bdLocation;
    }

    public double getLongitude(){
        if(bdLocation==null||bdLocation.getLongitude()==0){
            return 0;
        }
        return bdLocation.getLongitude();
    }

    public double getLatitude(){
        if(bdLocation==null||bdLocation.getLatitude()==0){
            return 0;
        }
        return bdLocation.getLatitude();
    }
}
