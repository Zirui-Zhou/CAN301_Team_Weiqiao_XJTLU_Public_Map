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
public class ShopType {

    @TableId
    private Long shopTypeId;

    private String shopTypeName;

    private String shopTypeImage;

    private String shopTypeMarkerIcon;
}