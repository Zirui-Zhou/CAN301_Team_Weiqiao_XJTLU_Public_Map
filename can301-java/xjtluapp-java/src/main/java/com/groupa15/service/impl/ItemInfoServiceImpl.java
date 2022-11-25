package com.groupa15.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.groupa15.entity.ItemInfo;
import com.groupa15.entity.vo.ItemInfoVo;
import com.groupa15.mapper.ItemInfoMapper;
import com.groupa15.service.ItemInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Zirui Zhou
 * @date 2022/11/25
 */
@Service
public class ItemInfoServiceImpl extends ServiceImpl<ItemInfoMapper, ItemInfo> implements ItemInfoService {

    @Autowired
    ItemInfoMapper itemInfoMapper;

    @Override
    public List<ItemInfoVo> getItemInfoByShopId(Long shopId) {
        return itemInfoMapper.selectItemInfoByShopId(shopId);
    }
}
