package com.groupa15.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.groupa15.entity.ShopInfo;
import com.groupa15.entity.ShopType;
import com.groupa15.entity.vo.ShopInfoVo;
import com.groupa15.entity.vo.ShopTypeVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Zirui Zhou
 * @date 2022/11/25
 */
@Mapper
public interface ShopTypeMapper extends BaseMapper<ShopType> {

    List<ShopTypeVo> selectAllShopType();

}