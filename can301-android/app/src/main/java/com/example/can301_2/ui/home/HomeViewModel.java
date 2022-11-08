package com.example.can301_2.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.can301_2.domain.ShopInfo;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<List<ShopInfo>> shopInfo;

    public HomeViewModel() {
        shopInfo = new MutableLiveData<>();
        shopInfo.setValue(new ArrayList<>());
    }

    public LiveData<List<ShopInfo>> getShopInfo() {
        return shopInfo;
    }

    public void setShopInfo(List<ShopInfo> shopInfo) {
        this.shopInfo.setValue(shopInfo);
    }
}