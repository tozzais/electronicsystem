package com.gaocheng.baselibrary.http;


import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 */
public class ApiManager {

    private static ApiManager mInstance;
    private static ApiManager mInstance1;

    private ApiService mApiService;
    private ApiService mApiService1;

    public ApiManager() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(HttpUrl.SERVER_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(OkHttpUtils.getInstance())
                .build();
        mApiService = retrofit.create(ApiService.class);
    }

    public ApiManager(String url) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(OkHttpUtils.getInstance())
                .build();
        mApiService1 = retrofit.create(ApiService.class);
    }

    //单例模式
    public static ApiService getService() {
        if (mInstance == null) {
            mInstance = new ApiManager();
        }
        return mInstance.mApiService;
    }

    public static ApiService getService(String url) {
        if (mInstance1 == null) {
            mInstance1 = new ApiManager(url);
        }
        return mInstance1.mApiService1;
    }

}
