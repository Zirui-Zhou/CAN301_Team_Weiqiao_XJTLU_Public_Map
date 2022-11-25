package com.example.can301_2.domain;

import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ShopType {

    @SerializedName("shopTypeId")
    private Long shopTypeId;

    @SerializedName("shopTypeName")
    private String shopTypeName;

    @SerializedName("shopTypeImage")
    private String shopTypeImage;

}

