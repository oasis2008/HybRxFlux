package com.huyingbao.hyb.stores;

import com.hardsoftstudio.rxflux.action.RxAction;
import com.hardsoftstudio.rxflux.dispatcher.Dispatcher;
import com.hardsoftstudio.rxflux.store.RxStore;
import com.hardsoftstudio.rxflux.store.RxStoreChange;
import com.huyingbao.hyb.actions.Actions;
import com.huyingbao.hyb.actions.Keys;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.yuntongxun.kitsdk.utils.LogUtil;

import org.json.JSONObject;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by marcel on 09/10/15.
 */
public class FileStore extends RxStore implements FileStoreInterface {

    /**
     * StoreId,用来在postChange(RxStoreChange change)时,生成RxStoreChange
     * 在接受RxStoreChange的时候,区分是哪个store
     */
    public static final String STORE_ID = "FileStore";

    private static FileStore instance;

    private String upToken;

    private FileStore(Dispatcher dispatcher) {
        super(dispatcher);
    }

    public static synchronized FileStore get(Dispatcher dispatcher) {
        if (instance == null) instance = new FileStore(dispatcher);
        return instance;
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
            default: // IMPORTANT if we don't modify the store just ignore
                return;
        }
        postChange(new RxStoreChange(STORE_ID, action));
    }

    @Override
    public String getUpToken() {
        return null;
    }

    public Observable upload(final String path, final String key) {
        return Observable.create(new Observable.OnSubscribe<Subscriber>() {
            @Override
            public void call(Subscriber subscriber) {
                UploadManager uploadManager = new UploadManager();
                uploadManager.put(path, key, upToken, new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject response) {
                        if (info.isOK()) {
                            subscriber.onNext(key);
                            subscriber.onCompleted();
                        } else {
//                            APIError apiError=newAPIError(info.statusCode,info.error);
                            subscriber.onError();
                        }
                    }
                });
            }
        }).retryWhen(new HttpTokenExpireFunc());

    }

    /**
     * qiniu token 为空或者过期 重新获取
     */
    private class HttpTokenExpireFunc implements Func1<Observable<? extends Throwable>, Observable<?>> {
        @Override
        public Observable<?> call(Observable<? extends Throwable> observable) {
            return observable.flatMap(new Func1<Throwable, Observable<?>>() {
                @Override
                public Observable<?> call(Throwable throwable) {
                    APIError apiError = (APIError) throwable;
                    if (apiError.getCode() == -4 || apiError.getCode() == -5) {
                        LogUtil.e("token过期或失效，重新获取");
                        return HttpApiUtils.getInstance().getQiNiuToken().doOnNext(new Action1<QiNiuToken>() {
                            @Override
                            public void call(QiNiuToken qiNiuToken) {
                                LogUtil.e("获取七牛token成功");
                                YebaConstants.QINIUTOKEN = qiNiuToken.getUpload_token();
                            }
                        });
                    }
                    return Observable.error(throwable);
                }
            });
        }
    }

}
