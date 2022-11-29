package com.example.can301_2;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.can301_2.domain.ShopInfo;
import com.example.can301_2.domain.ShopType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainViewModel extends ViewModel {

    private final MutableLiveData<Map<Long, ShopType>> shopTypeMap;

    public MainViewModel() {
        shopTypeMap = new MutableLiveData<>(new HashMap<>());
    }

    public LiveData<Map<Long, ShopType>> getShopTypeMap() {
        return shopTypeMap;
    }

    public void setShopTypeMap(Map<Long, ShopType> shopTypeMap) {
        this.shopTypeMap.setValue(shopTypeMap);
    }

}
