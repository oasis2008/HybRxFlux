package com.huyingbao.hyb.actions;

import com.google.gson.reflect.TypeToken;
import com.hardsoftstudio.rxflux.action.RxAction;
import com.hardsoftstudio.rxflux.action.RxActionCreator;
import com.hardsoftstudio.rxflux.dispatcher.Dispatcher;
import com.hardsoftstudio.rxflux.util.SubscriptionManager;
import com.huyingbao.hyb.core.HybApi;
import com.huyingbao.hyb.inject.component.ApplicationComponent;
import com.huyingbao.hyb.model.HybUser;
import com.huyingbao.hyb.model.LocalFile;
import com.huyingbao.hyb.model.MsgFromUser;
import com.huyingbao.hyb.model.Product;
import com.huyingbao.hyb.model.Shop;
import com.huyingbao.hyb.utils.CommonUtils;
import com.huyingbao.hyb.utils.LocalStorageUtils;
import com.huyingbao.hyb.utils.gsonhelper.GsonHelper;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;


/**
 * Action Creator responsible of creating the needed actions
 */
public class HybActionCreator extends RxActionCreator implements Actions {
    @Inject
    HybApi hybApi;
    @Inject
    LocalStorageUtils localStorageUtils;

    /**
     * If you want to give more things to the constructor like API or Preferences or any other
     * parameter you can buy make sure to call super(dispatcher, manager)
     */
    public HybActionCreator(Dispatcher dispatcher, SubscriptionManager manager) {
        super(dispatcher, manager);
        ApplicationComponent.Instance.get().inject(this);
    }

    @Override
    public void login(HybUser user) {
        user.setChannelId(localStorageUtils.getChannelId());
        user.setChannelType(3);
        //创建RxAction,传入键值对参数
        final RxAction action = newRxAction(LOGIN, Keys.USER, user);
        if (hasRxAction(action)) return;
        addRxAction(action, hybApi.login(user)
                // 指定 subscribe() 发生在 IO 线程(事件产生的线程)
                .subscribeOn(Schedulers.io())
                // 指定 Subscriber 的回调发生在主线程(事件消费的线程)
                .observeOn(AndroidSchedulers.mainThread())
                // Observable 并不是在创建的时候就立即开始发送事件，而是在它被订阅的时候，即当 subscribe() 方法执行的时候。
                // 可以看到，subscribe(Subscriber) 做了3件事：
                // 调用 Subscriber.onStart() 。这个方法在前面已经介绍过，是一个可选的准备方法。
                // 调用 Observable 中的 OnSubscribe.call(Subscriber) 。
                // 在这里，事件发送的逻辑开始运行。从这也可以看出，
                // 在 RxJava 中， Observable 并不是在创建的时候就立即开始发送事件，
                // 而是在它被订阅的时候，即当 subscribe() 方法执行的时候。
                // 将传入的 Subscriber 作为 Subscription 返回。这是为了方便 unsubscribe().
                .subscribe(userResponse -> {
                    action.getData().put(Keys.USER, userResponse);
                    postRxAction(action);
                }, throwable -> postError(action, throwable)));
    }

    @Override
    public void logout() {
        final RxAction action = newRxAction(LOGOUT);
        if (hasRxAction(action)) return;
        addRxAction(action, hybApi.logout()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(status -> {
                    action.getData().put(Keys.STATUS_LOGOUT, status);
                    postRxAction(action);
                }, throwable -> postError(action, throwable)));
    }

    @Override
    public void getUserByUuid(String uuid) {
        final RxAction action = newRxAction(GET_USER_BY_UUID, Keys.UUID, uuid);
        if (hasRxAction(action)) return;
        addRxAction(action, hybApi.getUserByUuid(uuid)
                .doOnNext(hybUser -> {

                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(status -> {
                    action.getData().put(Keys.STATUS_LOGOUT, status);
                    postRxAction(action);
                }, throwable -> postError(action, throwable)));

    }


    @Override
    public void updateUser(String user) {
        final RxAction action = newRxAction(UPDATE_USER);
        if (hasRxAction(action)) return;
        addRxAction(action, hybApi.updateUser(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userRes -> {
                    action.getData().put(Keys.USER, userRes);
                    postRxAction(action);
                }, throwable -> postError(action, throwable)));

    }

    @Override
    public void resetPassword(String oldPassword, String newPassword) {

    }


    @Override
    public void registerUser(HybUser user) {
        //创建RxAction,传入键值对参数
        //这里对应的键值对为keys.user和user
        //调用接口之后,得到对应的数据userResponse,传入keys.user
        final RxAction action = newRxAction(REGISTER_USER, Keys.USER, user);
        if (hasRxAction(action)) return;
        addRxAction(action, hybApi.registerUser(user)
                // 指定 subscribe() 发生在 IO 线程
                .subscribeOn(Schedulers.io())
                // 指定 Subscriber 的回调发生在主线程
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userResponse -> {
                    action.getData().put(Keys.USER, userResponse);
                    postRxAction(action);
                }, throwable -> postError(action, throwable)));
    }

    @Override
    public void registerShop(Shop shop) {
        final RxAction action = newRxAction(REGISTER_SHOP, Keys.SHOP, shop);
        if (hasRxAction(action)) return;
        addRxAction(action, hybApi.registerShop(shop)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(shopResponse -> {
                    action.getData().put(Keys.SHOP, shopResponse);
                    postRxAction(action);
                }, throwable -> postError(action, throwable)));
    }

    BehaviorSubject<Shop> cache;

    @Override
    public void getBelongShop() {
        final RxAction action = newRxAction(GET_BELONG_SHOP);
        if (hasRxAction(action)) return;
        addRxAction(action, subscribeData(
                shop -> {
                    action.getData().put(Keys.SHOP, shop);
                    postRxAction(action);
                },
                throwable -> postError(action, throwable)));
    }

    public Subscription subscribeData(Action1<? super Shop> onNext, Action1<Throwable> onError) {
        if (cache == null) {
            cache = BehaviorSubject.create();
            Observable.create(new Observable.OnSubscribe<Shop>() {
                @Override
                public void call(Subscriber<? super Shop> subscriber) {
                    String shopString = localStorageUtils.getShop();
                    if (shopString == null || shopString.isEmpty()) {
                        loadFromNetwork();
                    } else {
                        try {
                            Shop shop = GsonHelper.fromJson(shopString, new TypeToken<Shop>() {
                            }.getType());
                            subscriber.onNext(shop);
                        } catch (JSONException e) {
                            subscriber.onError(e);
                        }
                    }
                }
            }).subscribeOn(Schedulers.io()).subscribe(cache);
        }
        return cache.observeOn(AndroidSchedulers.mainThread()).subscribe(onNext, onError);
    }

    private void loadFromNetwork() {
        hybApi.getBelongShop()
                .doOnNext(shop -> {
                    localStorageUtils.setShop(GsonHelper.toJson(shop));
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(shop -> cache.onNext(shop)
                        , throwable -> throwable.printStackTrace());
    }

    @Override
    public void updateShop(Shop shop) {

    }

    @Override
    public void getShopByCode(String code) {

    }

    @Override
    public void getNearbyShopList(double longitude, double latitude, int radius, int shopType) {
        final RxAction action = newRxAction(GET_NEARBY_SHOP, Keys.LONGITUDE, longitude, Keys.LATITUDE, latitude, Keys.RADIUS, radius, Keys.SHOP_TYPE, shopType);
        if (hasRxAction(action)) return;
        addRxAction(action, hybApi.getShopByLocation(longitude, latitude, radius, shopType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(shopListResponse -> {
                    action.getData().put(Keys.SHOP_LIST, shopListResponse);
                    postRxAction(action);
                }, throwable -> postError(action, throwable)));
    }

    @Override
    public void addProduct(Product product) {
        final RxAction action = newRxAction(ADD_PRODUCT, Keys.PRODUCT, product);
        if (hasRxAction(action)) return;
        addRxAction(action, hybApi.addProduct(product)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(product1 -> {
                    action.getData().put(Keys.PRODUCT, product1);
                    postRxAction(action);
                }, throwable -> postError(action, throwable)));
    }

    @Override
    public void getProductByShop(int shopId, int status) {
        final RxAction action = newRxAction(GET_PRODUCT_BY_SHOP, Keys.SHOP_ID, shopId, Keys.PRODUCT_STATUS, status);
        if (hasRxAction(action)) return;
        addRxAction(action, hybApi.getEnableProductByShopCode(shopId, status)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(products -> {
                    action.getData().put(Keys.PRODUCT_LIST, products);
                    postRxAction(action);
                }, throwable -> postError(action, throwable)));
    }

    @Override
    public void getAllProduct(int belongShop, int status) {

    }

    @Override
    public void sendMessageByRadius(MsgFromUser msgFromUser) {
        final RxAction action = newRxAction(SEND_MESSAGE_BY_RADIUS, Keys.MSG_FROM_USER, msgFromUser);
        if (hasRxAction(action)) return;
        addRxAction(action, hybApi.sendMessageByRadius(msgFromUser)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(sendStatus -> {
                    action.getData().put(Keys.STATUS, sendStatus);
                    postRxAction(action);
                }, throwable -> postError(action, throwable)));
    }

    @Override
    public void getUserMessage(int belongUser) {
        final RxAction action = newRxAction(GET_USER_MESSAGE, Keys.ID, belongUser);
        if (hasRxAction(action)) return;
        addRxAction(action, hybApi.getUserMessage(belongUser)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(msgFromUserList -> {
                    action.getData().put(Keys.MSG_FROM_USER_LIST, msgFromUserList);
                    postRxAction(action);
                }, throwable -> postError(action, throwable)));
    }

    /**
     * 获取七牛token
     *
     * @param partName
     */
    @Override
    public void getUpToken(String partName) {
        final RxAction action = newRxAction(GET_UP_TOKEN, Keys.PART_NAME, partName);
        if (hasRxAction(action)) return;
        addRxAction(action, hybApi.getUpToken(partName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(products -> {
                    action.getData().put(Keys.UP_TOKEN, products);
                    postRxAction(action);
                }, throwable -> postError(action, throwable)));
    }

    /**
     * 上传文件,返回文件完整的路径
     *
     * @param localFile
     * @param upToken
     * @param partName
     * @return
     */
    private Observable<String> getUploadObservable(LocalFile localFile, String upToken, String partName) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                UploadManager uploadManager = new UploadManager();
                uploadManager.put(localFile.getLocalPath(), localFile.getFileKey(), upToken, new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject response) {
                        if (info.isOK()) {
                            subscriber.onNext(CommonUtils.getFullPath(key, partName));
                            subscriber.onCompleted();
                        } else {
                            subscriber.onError(new HttpException(Response.error(433, null)));
                        }
                    }
                }, null);
            }
        });
    }

    /**
     * 获取token,然后单个文件上传
     *
     * @param localFile
     * @param partName
     */
    @Override
    public void uploadOneFile(LocalFile localFile, String partName) {
        final RxAction action = newRxAction(UPLOAD_ONE_FILE);
        if (hasRxAction(action)) return;
        addRxAction(action, hybApi.getUpToken(partName)// 返回 Observable<String>，在上传时时请求token，并在响应后发送 token
                .flatMap(token -> getUploadObservable(localFile, token, partName))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(key -> {
                    action.getData().put(Keys.FILE_KEY, key);
                    postRxAction(action);
                }, throwable -> postError(action, throwable)));
    }


    /**
     * 获取token,然后所有文件上传
     *
     * @param list
     * @param partName
     */
    @Override
    public void uploadAllFile(List<LocalFile> list, String partName) {
        final RxAction action = newRxAction(UPLOAD_All_FILE);
        final String[] mToken = new String[1];
        if (hasRxAction(action)) return;
        addRxAction(action, hybApi.getUpToken(partName)// 返回 Observable<String>，在上传时时请求token，并在响应后发送 token
                .flatMap(token -> {
                    mToken[0] = token;
                    return Observable.from(list);
                })
                .flatMap(localFile -> getUploadObservable(localFile, mToken[0], partName))
                .collect(() -> new ArrayList<String>(), (localFiles, fullPath) -> localFiles.add(fullPath))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(fileKeyList -> {
                    action.getData().put(Keys.FILE_KEY_LIST, fileKeyList);
                    postRxAction(action);
                }, throwable -> postError(action, throwable)));
    }

    @Override
    public boolean retry(RxAction action) {
        if (hasRxAction(action)) return true;
        switch (action.getType()) {
            case LOGIN:
                login((HybUser) action.getData().get(Keys.USER));
                return true;
            case LOGOUT:
                logout();
                return true;
            case REGISTER_USER:
                registerUser((HybUser) action.getData().get(Keys.USER));
                return true;
            case REGISTER_SHOP:
                registerShop((Shop) action.getData().get(Keys.SHOP));
                return true;
            case GET_NEARBY_SHOP:
                getNearbyShopList(
                        (double) action.getData().get(Keys.LONGITUDE),
                        (double) action.getData().get(Keys.LATITUDE),
                        (int) action.getData().get(Keys.RADIUS),
                        (int) action.getData().get(Keys.SHOP_TYPE)
                );
                return true;
        }
        return false;
    }

}
