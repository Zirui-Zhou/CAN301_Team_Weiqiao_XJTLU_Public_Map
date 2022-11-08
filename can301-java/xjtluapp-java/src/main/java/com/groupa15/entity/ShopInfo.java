package com.groupa15.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

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

}
