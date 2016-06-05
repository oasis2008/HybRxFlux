package com.huyingbao.hyb.stores;

import com.hardsoftstudio.rxflux.action.RxAction;
import com.hardsoftstudio.rxflux.dispatcher.Dispatcher;
import com.hardsoftstudio.rxflux.store.RxStore;
import com.hardsoftstudio.rxflux.store.RxStoreChange;
import com.huyingbao.hyb.actions.Actions;
import com.huyingbao.hyb.actions.Keys;
import com.huyingbao.hyb.model.Product;

import java.util.ArrayList;

public class ProdcutStore extends RxStore implements ProductStoreInterface {

    public static final String STORE_ID = "ProductStore";

    private ArrayList<Product> mProductList;

    /**
     * 构造方法,传入dispatcher
     *
     * @param dispatcher
     */
    public ProdcutStore(Dispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void onRxAction(RxAction action) {
        switch (action.getType()) {
            case Actions.GET_PRODUCT_BY_SHOP:
                mProductList = action.get(Keys.PRODUCT_LIST);
                break;
            default://若是接收到的action中type不是需要处理的type,则直接返回,不调用postChange()
                return;
        }
        postChange(new RxStoreChange(STORE_ID, action));
    }

    @Override
    public ArrayList<Product> getProductList() {
        return mProductList;
    }
}
