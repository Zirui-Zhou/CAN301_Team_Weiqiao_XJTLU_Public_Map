package com.groupa15.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.groupa15.entity.ShopInfo;
import com.groupa15.entity.vo.ShopInfoVo;

import java.util.List;

/**
 * @author Zirui Zhou
 * @date 2022/11/6
 */
public interface ShopInfoService extends IService<ShopInfo>{

    List<ShopInfoVo> getAllShopInfo();

    ShopInfoVo getShopInfoById(Long shopId);
}
