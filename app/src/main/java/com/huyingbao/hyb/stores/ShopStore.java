package com.huyingbao.hyb.stores;

import com.hardsoftstudio.rxflux.action.RxAction;
import com.hardsoftstudio.rxflux.dispatcher.Dispatcher;
import com.hardsoftstudio.rxflux.store.RxStore;
import com.hardsoftstudio.rxflux.store.RxStoreChange;
import com.huyingbao.hyb.actions.Actions;
import com.huyingbao.hyb.actions.Keys;
import com.huyingbao.hyb.model.Shop;

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
            case Actions.REGISTER_SHOP:
                mShop = action.get(Keys.SHOP);
                break;
            case Actions.GET_NEARBY_SHOP:
                mShopList = action.get(Keys.SHOP_LIST);
                break;
            case Actions.A_TO_SHOP_INFO:
                mShop = action.get(Keys.SHOP);
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
