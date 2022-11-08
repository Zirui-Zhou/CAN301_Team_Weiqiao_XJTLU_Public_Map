package com.groupa15.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.groupa15.entity.ShopInfo;
import com.groupa15.entity.vo.ShopInfoVo;
import com.groupa15.mapper.ShopInfoMapper;
import com.groupa15.service.ShopInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Zirui Zhou
 * @date 2022/11/6
 */

@Service
public class ShopInfoServiceImpl extends ServiceImpl<ShopInfoMapper, ShopInfo> implements ShopInfoService {

    @Autowired
    ShopInfoMapper shopInfoMapper;

    @Override
    public List<ShopInfoVo> getAllShopInfo() {
        return shopInfoMapper.selectAllShopInfo();
    };

}
