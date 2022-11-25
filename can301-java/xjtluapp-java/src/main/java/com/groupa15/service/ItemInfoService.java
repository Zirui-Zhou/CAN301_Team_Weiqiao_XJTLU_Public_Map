package com.groupa15.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.groupa15.entity.ItemInfo;
import com.groupa15.entity.vo.ItemInfoVo;

import java.util.List;

/**
 * @author Zirui Zhou
 * @date 2022/11/25
 */
public interface ItemInfoService extends IService<ItemInfo> {

    List<ItemInfoVo> getItemInfoByShopId(Long shopId);

}
