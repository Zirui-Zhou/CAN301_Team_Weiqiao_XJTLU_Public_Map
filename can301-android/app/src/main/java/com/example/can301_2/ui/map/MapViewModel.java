package com.example.can301_2.ui.map;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.can301_2.domain.ShopInfo;
import com.example.can301_2.domain.ShopType;

import java.util.ArrayList;
import java.util.List;

public class MapViewModel extends ViewModel {

    private final MutableLiveData<List<ShopInfo>> shopInfo;
    private final MutableLiveData<List<ShopType>> shopTypeList;

    public MapViewModel() {
        shopInfo = new MutableLiveData<>(new ArrayList<>());
        shopTypeList = new MutableLiveData<>(new ArrayList<>());
    }

    public LiveData<List<ShopInfo>> getShopInfo() {
        return shopInfo;
    }

    public void setShopInfo(List<ShopInfo> shopInfo) {
        this.shopInfo.setValue(shopInfo);
    }

    public LiveData<List<ShopType>> getShopTypeList() {
        return shopTypeList;
    }

    public void setShopTypeList(List<ShopType> shopTypeList) {
        this.shopTypeList.setValue(shopTypeList);
    }
}