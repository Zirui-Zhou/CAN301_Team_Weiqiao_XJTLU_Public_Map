package com.example.can301_2.ui.home;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.can301_2.MainViewModel;
import com.example.can301_2.adapter.CategoryItemAdapter;
import com.example.can301_2.adapter.ShopCardAdapter;
import com.example.can301_2.api.ShopInfoApi;
import com.example.can301_2.databinding.FragmentHomeBinding;
import com.example.can301_2.domain.ShopInfo;
import com.example.can301_2.domain.ShopType;
import com.example.can301_2.utils.RequestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HomeFragment extends Fragment implements CategoryItemAdapter.EventListener{

    private FragmentHomeBinding binding;
    RecyclerView recyclerViewShopInfo, recyclerViewShopType;
    ShopCardAdapter shopCardAdapter;
    CategoryItemAdapter shopTypeAdapter;
    HomeViewModel homeViewModel;
    MainViewModel mainViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        homeViewModel.getShopInfo().observe(getViewLifecycleOwner(), dataDTOS -> {
            shopCardAdapter.setAllShopInfo(dataDTOS);
            shopCardAdapter.notifyDataSetChanged();
        });

        mainViewModel.getShopTypeMap().observe(getViewLifecycleOwner(), shopTypeMap -> {
            shopTypeAdapter.setShopTypeList(new ArrayList<>(shopTypeMap.values()));
            shopCardAdapter.setShopTypeMap(shopTypeMap);
            shopTypeAdapter.notifyDataSetChanged();
        });

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerViewShopInfo = binding.shopCardList;
        shopCardAdapter = new ShopCardAdapter(mainViewModel.getShopTypeMap().getValue());
        recyclerViewShopInfo.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewShopInfo.setAdapter(shopCardAdapter);

        recyclerViewShopType = binding.homeShopTypeList;
        shopTypeAdapter = new CategoryItemAdapter(this);
        recyclerViewShopType.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewShopType.setAdapter(shopTypeAdapter);

        updateShopInfoListByType(null);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void updateShopInfoListByType(ShopType shopType) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            ShopInfoApi shopInfoService = RequestUtils.getService(ShopInfoApi.class);
            List<ShopInfo> shopInfoList;
            if (shopType == null) {
                shopInfoList = shopInfoService.getAllShopInfo().getData();
            } else {
                shopInfoList = shopInfoService.getShopInfoByShopTypeId(shopType.getShopTypeId()).getData();
            }

            handler.post(() -> {
                homeViewModel.setShopInfo(shopInfoList);
            });
        });
    }
}