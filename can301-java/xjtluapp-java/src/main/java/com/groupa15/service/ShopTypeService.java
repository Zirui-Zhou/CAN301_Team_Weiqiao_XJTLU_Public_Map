package com.groupa15.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.groupa15.entity.ShopType;
import com.groupa15.entity.vo.ShopTypeVo;

import java.util.List;

/**
 * @author Zirui Zhou
 * @date 2022/11/25
 */

public interface ShopTypeService extends IService<ShopType> {

    List<ShopTypeVo> getAllShopType();

}
