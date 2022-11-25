package com.example.can301_2.api;

import com.example.can301_2.domain.CommonResponse;
import com.example.can301_2.domain.ItemInfo;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ItemInfoApi {

    @GET("/iteminfo")
    CommonResponse<List<ItemInfo>> getItemInfoByShopId(@Query("shop_id") Long shop_id);

}
