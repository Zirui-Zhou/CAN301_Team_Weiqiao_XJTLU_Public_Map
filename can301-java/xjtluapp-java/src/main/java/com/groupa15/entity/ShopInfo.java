package com.groupa15.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author Zirui Zhou
 * @date 2022/11/6
 */

@Data
@Accessors(chain = true)
public class ShopInfo {

    @TableId
    private Long shopId;

    private String shopName;

    private float shopRating;

    private String shopSales;

    private float shopAveragePrice;

    private Date shopOpenTime;

    private Date shopCloseTime;

    private String shopDescription;

    private Double shopLatitude;

    private Double shopLongitude;

    private Long shopTypeId;

}
