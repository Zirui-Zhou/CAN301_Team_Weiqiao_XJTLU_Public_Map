package com.example.can301_2.ui.detail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.can301_2.domain.ShopInfo;

import java.util.ArrayList;
import java.util.List;

public class DetailViewModel extends ViewModel {
    private final MutableLiveData<ShopInfo> shopInfo;

    public DetailViewModel() {
        shopInfo = new MutableLiveData<>();
        shopInfo.setValue(new ShopInfo());
    }

    public LiveData<ShopInfo> getShopInfo() {
        return shopInfo;
    }

    public void setShopInfo(ShopInfo shopInfo) {
        this.shopInfo.setValue(shopInfo);
    }
}