package com.huyingbao.hyb.stores;


import java.util.List;

/**
 * Created by marcel on 11/09/15.
 */
public interface FileStoreInterface {

    String getUpToken();

    String getFileKey();

    List<String> getFileKeyList();
}
