package com.example.can301_2.utils;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RequestUtils {

    public final static String baseUrl = "http://106.14.123.152:8081";
    public final static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(baseUrl)
            .addCallAdapterFactory(new SyncCallAdapter.Factory())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static <T> T getService(Class<T> serviceClass) {
        return retrofit.create(serviceClass);
    }

}
