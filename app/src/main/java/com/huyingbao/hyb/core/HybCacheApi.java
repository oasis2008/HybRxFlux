package com.huyingbao.hyb.core;

import com.huyingbao.hyb.model.Shop;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.rx_cache.DynamicKey;
import io.rx_cache.EvictDynamicKey;
import io.rx_cache.LifeCache;
import io.rx_cache.Reply;
import rx.Observable;

/**
 * Created by Administrator on 2016/7/25.
 */
public interface HybCacheApi {
    //这里设置缓存失效时间为2分钟。
    @LifeCache(duration = 2, timeUnit = TimeUnit.MINUTES)
    Observable<Reply<List<Shop>>> getShops(Observable<List<Shop>> oShops, DynamicKey userName, EvictDynamicKey evictDynamicKey);
}
