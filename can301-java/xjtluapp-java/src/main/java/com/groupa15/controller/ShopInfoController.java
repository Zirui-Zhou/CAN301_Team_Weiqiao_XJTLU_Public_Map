package com.groupa15.controller;

import com.groupa15.common.Response;
import com.groupa15.entity.vo.ItemInfoVo;
import com.groupa15.entity.vo.ShopInfoVo;
import com.groupa15.entity.vo.ShopTypeVo;
import com.groupa15.service.ItemInfoService;
import com.groupa15.service.ShopInfoService;
import com.groupa15.service.ShopTypeService;
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

    @Autowired
    ShopTypeService shopTypeService;

    @Autowired
    ItemInfoService itemInfoService;

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

    @GetMapping(value = "/shopinfo", params = "type_id")
    public Response getShopInfoByShopTypeId(@RequestParam(name = "type_id") Long type_id) {
        List<ShopInfoVo> shopInfoVoList = shopInfoService.getShopInfoByShopTypeId(type_id);
        return Response.success(HttpStatus.OK, null, shopInfoVoList);
    }

    @GetMapping(value = "/shoptype")
    public Response getAllShopType() {
        List<ShopTypeVo> shopTypeVoList = shopTypeService.getAllShopType();
        return Response.success(HttpStatus.OK, null, shopTypeVoList);
    }

    @GetMapping(value = "/iteminfo", params = "shop_id")
    public Response getItemInfoByShopId(@RequestParam(name = "shop_id") Long shopId) {
        List<ItemInfoVo> itemInfoVoList = itemInfoService.getItemInfoByShopId(shopId);
        return Response.success(HttpStatus.OK, null, itemInfoVoList);
    }
}
