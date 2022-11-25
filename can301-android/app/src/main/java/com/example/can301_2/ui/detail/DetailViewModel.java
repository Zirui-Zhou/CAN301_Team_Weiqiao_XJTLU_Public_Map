package com.example.can301_2.ui.detail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.can301_2.domain.ItemInfo;
import com.example.can301_2.domain.ShopInfo;

import java.util.ArrayList;
import java.util.List;

public class DetailViewModel extends ViewModel {
    private final MutableLiveData<ShopInfo> shopInfo;
    private final MutableLiveData<List<ItemInfo>> itemInfoList;

    public DetailViewModel() {
        shopInfo = new MutableLiveData<>();
        itemInfoList = new MutableLiveData<>();
        shopInfo.setValue(new ShopInfo());
        itemInfoList.setValue(new ArrayList<>());
    }

    public LiveData<ShopInfo> getShopInfo() {
        return shopInfo;
    }

    public void setShopInfo(ShopInfo shopInfo) {
        this.shopInfo.setValue(shopInfo);
    }

    public LiveData<List<ItemInfo>> getItemInfoList() {
        return itemInfoList;
    }

    public void setItemInfoList(List<ItemInfo> itemInfoList) {
        this.itemInfoList.setValue(itemInfoList);
    }

}