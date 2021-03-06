package com.huyingbao.hyb.stores;

import com.hardsoftstudio.rxflux.action.RxAction;
import com.hardsoftstudio.rxflux.dispatcher.Dispatcher;
import com.hardsoftstudio.rxflux.store.RxStore;
import com.hardsoftstudio.rxflux.store.RxStoreChange;
import com.huyingbao.hyb.HybApp;
import com.huyingbao.hyb.actions.Actions;
import com.huyingbao.hyb.actions.Keys;
import com.huyingbao.hyb.model.Shop;
import com.huyingbao.hyb.utils.gsonhelper.GsonHelper;

import java.util.ArrayList;

public class ShopStore extends RxStore implements ShopStoreInterface {

    public static final String STORE_ID = "ShopStore";

    private Shop mShop;
    private ArrayList<Shop> mShopList;

    public ShopStore(Dispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void onRxAction(RxAction action) {
        switch (action.getType()) {
            case Actions.GET_NEARBY_SHOP:
                mShopList = action.get(Keys.SHOP_LIST);
                break;
            case Actions.A_TO_SHOP_INFO:
                mShop = action.get(Keys.SHOP);
                break;
            case Actions.REGISTER_SHOP:
            case Actions.GET_BELONG_SHOP:
                mShop = action.get(Keys.SHOP);
                HybApp.setShop(mShop);
                //保存当前登录用户所属店铺
                HybApp.getInstance().getLocalSorageUtils().setShop(GsonHelper.toJson(mShop));
                break;
            default://若是接收到的action中type不是需要处理的type,则直接返回,不调用postChange()
                return;
        }
        postChange(new RxStoreChange(STORE_ID, action));
    }

    @Override
    public Shop getShop() {
        return mShop;
    }

    @Override
    public ArrayList<Shop> getShopList() {
        return mShopList;
    }
}
