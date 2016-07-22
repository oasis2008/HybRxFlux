package com.huyingbao.hyb.actions;

import com.hardsoftstudio.rxflux.action.RxAction;
import com.huyingbao.hyb.model.HybUser;
import com.huyingbao.hyb.model.LocalFile;
import com.huyingbao.hyb.model.Product;
import com.huyingbao.hyb.model.Shop;

import java.util.List;

import rx.subjects.BehaviorSubject;

public interface Actions {

    /**
     * action type api操作用来确定具体是哪个操作
     */
    String GET_UP_TOKEN = "get_up_token";
    String REGISTER_USER = "register_user";
    String LOGIN = "login";
    String LOGOUT = "logout";
    String GET_USER_BY_UUID = "get_user_by_uuid";
    String UPDATE_USER = "userdate_user";
    String RESET_PASSWORD = "reset_password";

    String REGISTER_SHOP = "register_shop";
    String GET_BELONG_SHOP = "get_belong_shop";
    String UPDATE_SHOP = "update_shop";
    String GET_SHOP_BY_CODE = "get_shop_by_code";
    String GET_NEARBY_SHOP = "get_nearby_shop";
    String ADD_PRODUCT = "add_product";
    String GET_PRODUCT_BY_UUID = "get_product_by_uuid";
    String UPDATE_PRODUCT = "update_product";
    String GET_PRODUCT_BY_SHOP = "get_product_by_shop";
    String UPLOAD_ONE_FILE = "upload_one_file";
    String UPLOAD_All_FILE = "upload_all_file";
    /**
     * action type 非api操作
     */
    String A_GET_LOCATION = "a_get_location";
    String A_TO_SHOP_INFO = "a_to_shop_info";

    void getUpToken(String partName);

    void registerUser(HybUser user);

    void login(HybUser user);

    void logout();

    void getUserByUuid(String uuid);

    void updateUser(String user);

    void resetPassword(String oldPassword, String newPassword);

    void registerShop(Shop shop);

    void getBelongShop(BehaviorSubject<Shop> cache);

    void updateShop(Shop shop);

    void getShopByCode(String code);

    void getNearbyShopList(double longitude, double latitude, int radius, int shopType);

    void addProduct(Product product);

    void getProductByShop(int shopId, int status);

    void uploadOneFile(LocalFile localFile, String partName);

    void uploadAllFile(List<LocalFile> list, String partName);


    boolean retry(RxAction action);


}
