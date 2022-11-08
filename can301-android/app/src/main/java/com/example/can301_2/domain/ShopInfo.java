package com.example.can301_2.domain;

import com.google.gson.annotations.SerializedName;

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

}
