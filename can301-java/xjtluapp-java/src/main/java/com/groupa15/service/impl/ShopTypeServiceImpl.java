package com.groupa15.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.groupa15.entity.ShopInfo;
import com.groupa15.entity.ShopType;
import com.groupa15.entity.vo.ShopTypeVo;
import com.groupa15.mapper.ShopInfoMapper;
import com.groupa15.mapper.ShopTypeMapper;
import com.groupa15.service.ShopTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Zirui Zhou
 * @date 2022/11/25
 */
@Service
public class ShopTypeServiceImpl extends ServiceImpl<ShopTypeMapper, ShopType> implements ShopTypeService {

    @Autowired
    ShopTypeMapper shopTypeMapper;

    @Override
    public List<ShopTypeVo> getAllShopType() {
        return shopTypeMapper.selectAllShopType();
    }
}
