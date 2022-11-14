package com.example.can301_2.api;

import com.example.can301_2.domain.CommonResponse;
import com.example.can301_2.domain.ShopInfo;

import java.util.List;

import retrofit2.http.GET;

public interface ShopInfoApi {

    @GET("/shopinfo")
    CommonResponse<List<ShopInfo>> getAllShopInfo();

}
