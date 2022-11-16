package com.example.can301_2.domain;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Data
public class ShopInfo {

    @SerializedName("shopId")
    private Integer shopId;

    @SerializedName("shopName")
    private String shopName;

    @SerializedName("shopRating")
    private Double shopRating;

    @SerializedName("shopSales")
    private String shopSales;

    @SerializedName("shopCoverImage")
    private String shopCoverImage;

    @SerializedName("shopAveragePrice")
    private Double shopAveragePrice;

    @SerializedName("shopOpenTime")
    private String shopOpenTime;

    @SerializedName("shopCloseTime")
    private String shopCloseTime;

    @SerializedName("shopDescription")
    private String shopDescription;

    @SerializedName("shopLatitude")
    private Double shopLatitude;

    @SerializedName("shopLongitude")
    private Double shopLongitude;

    @SerializedName("shopDetailImages")
    private List<String> shopDetailImages;

}
