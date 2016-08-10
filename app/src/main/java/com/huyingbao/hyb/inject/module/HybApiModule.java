package com.huyingbao.hyb.inject.module;

import com.google.gson.GsonBuilder;
import com.huyingbao.hyb.BuildConfig;
import com.huyingbao.hyb.HybApp;
import com.huyingbao.hyb.core.HybApi;
import com.huyingbao.hyb.core.PersistentCookieStore;
import com.huyingbao.hyb.utils.NetUtils;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class HybApiModule {
    private static final String BASE_URL = BuildConfig.DEBUG ? "http://52.79.131.9:1337" : "http://api.huyingbao.cc";
//    private static final String BASE_URL = BuildConfig.DEBUG ? "http://192.168.0.46:1337" : "http://api.huyingbao.cc";

    /**
     * 创建一个HybApi的实现类单例对象
     *
     * @param client OkHttpClient
     * @return HybApi
     */
    @Singleton//添加@Singleton标明该方法产生只产生一个实例
    @Provides
    public HybApi provideClientApi(OkHttpClient client) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().serializeNulls().create()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();
        return retrofit.create(HybApi.class);
    }

    /**
     * OkHttp客户端单例对象
     *
     * @param cookieJar
     * @return OkHttpClient
     */
    @Singleton//添加@Singleton标明该方法产生只产生一个实例
    @Provides
    public OkHttpClient provideClient(CookieJar cookieJar) {
        //云端响应头拦截器，用来配置缓存策略
        Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = chain -> {
            Request request = chain.request();
            if (!NetUtils.hasNetwork()) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
                Logger.w("no network");
            }
            Response originalResponse = chain.proceed(request);
            if (NetUtils.hasNetwork()) {
                //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
                String cacheControl = request.cacheControl().toString();
                return originalResponse.newBuilder()
                        .header("Cache-Control", cacheControl)
                        .removeHeader("Pragma")
                        .build();
            } else {
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=2419200")
                        .removeHeader("Pragma")
                        .build();
            }
        };
        //缓存设置
        File cacheFile = new File(HybApp.getInstance().getCacheDir(), "hybNetCache");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 100); //100Mb
        //日志拦截器单例对象,用于OkHttp层对日志进行处理
//        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//        interceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        Interceptor LoggingInterceptor = chain -> {
            Request request = chain.request();
            long t1 = System.nanoTime();
//            Logger.e(String.format("发送请求 %s on %s%n%s", request.url(), chain.connection(), request.headers()));
            Response response = chain.proceed(request);
            long t2 = System.nanoTime();
            okhttp3.MediaType mediaType = response.body().contentType();
            String content = response.body().string();
            Logger.e(String.format("接收请求 for %s in %.1fms%n%s", response.request().url(), (t2 - t1) / 1e6d, content));
            return response.newBuilder().body(okhttp3.ResponseBody.create(mediaType, content))
                    .build();
        };
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(LoggingInterceptor)
                .addNetworkInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR)
                .cookieJar(cookieJar)
                .cache(cache)
                .connectTimeout(30, TimeUnit.SECONDS)
                .build();
        return client;
    }

    /**
     * 本地cookie缓存单例对象
     *
     * @return
     */
    @Singleton//添加@Singleton标明该方法产生只产生一个实例
    @Provides
    public CookieJar provideCookieJar(PersistentCookieStore cookieStore) {
        CookieJar cookieJar = new CookieJar() {
            //Tip：這裡key必須是String
            @Override
            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                if (cookies != null && cookies.size() > 0) {
                    for (Cookie item : cookies) {
                        cookieStore.add(url, item);
                    }
                }
            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl url) {
                List<Cookie> cookies = cookieStore.get(url);
                return cookies != null ? cookies : new ArrayList<Cookie>();
            }
        };
        return cookieJar;
    }
}
