package com.groupa15.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.groupa15.entity.ShopInfo;
import com.groupa15.entity.User;
import com.groupa15.entity.vo.ShopInfoVo;
import com.groupa15.entity.vo.UserInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Zirui Zhou
 * @date 2022/11/6
 */

@Mapper
public interface ShopInfoMapper extends BaseMapper<ShopInfo> {

    List<ShopInfoVo> selectAllShopInfo();

}
