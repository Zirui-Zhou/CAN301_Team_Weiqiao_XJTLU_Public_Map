package com.example.can301_2.api;

import com.example.can301_2.domain.CommonResponse;
import com.example.can301_2.domain.ItemInfo;
import com.example.can301_2.domain.ShopInfo;
import com.example.can301_2.domain.ShopType;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ShopInfoApi {

    @GET("/shopinfo")
    CommonResponse<List<ShopInfo>> getAllShopInfo();

    @GET("/shopinfo")
    CommonResponse<ShopInfo> getShopInfoById(@Query("id") Long shop_id);

    @GET("/shoptype")
    CommonResponse<List<ShopType>> getAllShopType();

}
