package com.groupa15.entity.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author Zirui Zhou
 * @date 2022/11/6
 */

@Data
public class ShopInfoVo {

    private Long shopId;

    private String shopName;

    private float shopRating;

    private String shopSales;

    private String shopCoverImage;

    private float shopAveragePrice;

    private Date shopOpenTime;

    private Date shopCloseTime;

    private String shopDescription;

    private Double shopLatitude;

    private Double shopLongitude;

    private List<String> shopDetailImages;

}
