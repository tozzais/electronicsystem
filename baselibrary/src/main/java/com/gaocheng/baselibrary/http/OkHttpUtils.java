package com.gaocheng.baselibrary.http;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by lizetong on 15/8/27.
 */
public class OkHttpUtils {

    private final static String RESPONSE_CACHE = "alex_http_cache";
    private final static long RESPONSE_CACHE_SIZE = 10 * 1024 * 1024;
    private final static long HTTP_CONNECT_TIMEOUT = 10;
    private final static long HTTP_READ_TIMEOUT = 30;

    private static OkHttpClient singleton;

    public static OkHttpClient getInstance() {
        if (singleton == null) {
            synchronized (OkHttpUtils.class) {
                if (singleton == null) {

                    //定义logging
                    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                    OkHttpClient.Builder builder = new OkHttpClient.Builder();
                    builder.connectTimeout(HTTP_CONNECT_TIMEOUT, TimeUnit.SECONDS)
                            .readTimeout(HTTP_READ_TIMEOUT, TimeUnit.SECONDS)
//                            .cookieJar(new CookieJar() {
//
//                                private HashMap<String,List<Cookie>> cookieStore = new HashMap();
//                                @Override
//                                public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
//                                    if (url.encodedPath().contains(com.ipd.jumpbox.leshangstore.http.HttpUrl.GET_CODE)){
//                                        cookieStore.put("smsCode",cookies);
//                                    }
//
//                                }
//
//                                @Override
//                                public List<Cookie> loadForRequest(HttpUrl url) {
//                                    List<Cookie> cookies = cookieStore.get("smsCode");
//                                    return cookies != null ? cookies : new ArrayList<Cookie>();
//                                }
//                            })
                            .addInterceptor(interceptor);


                    singleton = builder.build();
                }
            }
        }
        return singleton;
    }
}
