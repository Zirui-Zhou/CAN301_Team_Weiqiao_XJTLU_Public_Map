package com.groupa15.controller;

import com.groupa15.common.Response;
import com.groupa15.entity.vo.ShopInfoVo;
import com.groupa15.service.ShopInfoService;
import com.groupa15.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Zirui Zhou
 * @date 2022/11/6
 */

@RestController
public class ShopInfoController {

    @Autowired
    ShopInfoService shopInfoService;

    @GetMapping(value = "/shopinfo")
    public Response getArticleList() {
        List<ShopInfoVo> shopInfoVoList = shopInfoService.getAllShopInfo();
        return Response.success(HttpStatus.OK, "Get the shop info list", shopInfoVoList);
    }

    @GetMapping(value = "/shopinfo", params = "id")
    public Response getArticle(@RequestParam(name = "id") Long id) {
        ShopInfoVo shopInfoVo = shopInfoService.getShopInfoById(id);
        return Response.success(HttpStatus.OK, null, shopInfoVo);
    }
}
