package com.groupa15.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author Zirui Zhou
 * @date 2022/11/25
 */
@Data
@Accessors(chain = true)
public class ItemInfo {

    @TableId
    private Long itemId;

    private String itemName;

    private String itemImage;

    private Float itemPrice;

    private Long itemShopId;

}
