package com.example.can301_2.domain;

import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ItemInfo {

    @SerializedName("itemId")
    private Long itemId;

    @SerializedName("itemName")
    private String itemName;

    @SerializedName("itemImage")
    private String itemImage;

    @SerializedName("itemPrice")
    private Float itemPrice;

    @SerializedName("itemShopId")
    private Long itemShopId;

}
