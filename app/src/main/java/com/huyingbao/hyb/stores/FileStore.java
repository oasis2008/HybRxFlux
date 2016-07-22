package com.huyingbao.hyb.stores;

import com.hardsoftstudio.rxflux.action.RxAction;
import com.hardsoftstudio.rxflux.dispatcher.Dispatcher;
import com.hardsoftstudio.rxflux.store.RxStore;
import com.hardsoftstudio.rxflux.store.RxStoreChange;
import com.huyingbao.hyb.actions.Actions;
import com.huyingbao.hyb.actions.Keys;

import java.util.List;

public class FileStore extends RxStore implements FileStoreInterface {

    /**
     * StoreId,用来在postChange(RxStoreChange change)时,生成RxStoreChange
     * 在接受RxStoreChange的时候,区分是哪个store
     */
    public static final String STORE_ID = "FileStore";


    private String upToken;
    private String fileKey;
    private List<String> fileKeyList;

    public FileStore(Dispatcher dispatcher) {
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
            case Actions.GET_UP_TOKEN:
                upToken = action.get(Keys.UP_TOKEN);
                break;
            case Actions.UPLOAD_ONE_FILE:
                fileKey = action.get(Keys.FILE_KEY);
                break;
            case Actions.UPLOAD_All_FILE:
                fileKeyList = action.get(Keys.FILE_KEY_LIST);
            default: // IMPORTANT if we don't modify the store just ignore
                return;
        }
        postChange(new RxStoreChange(STORE_ID, action));
    }

    @Override
    public String getUpToken() {
        return upToken;
    }

    @Override
    public String getFileKey() {
        return fileKey;
    }

    @Override
    public List<String> getFileKeyList() {
        return fileKeyList;
    }
}
