package com.example.can301_2.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ShopInfoApi {

    @GET("/shopinfo")
    Call<ResponseBody> getAllShopInfo();

}
