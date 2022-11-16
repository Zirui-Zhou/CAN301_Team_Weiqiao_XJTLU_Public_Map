package com.example.can301_2.ui.home;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.can301_2.adapter.ShopCardAdapter;
import com.example.can301_2.api.ShopInfoApi;
import com.example.can301_2.databinding.FragmentHomeBinding;
import com.example.can301_2.domain.ShopInfo;
import com.example.can301_2.utils.RequestUtils;

import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    RecyclerView recyclerView;
    ShopCardAdapter shopCardAdapter;
    ProgressDialog progressDialog;
    HomeViewModel homeViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        homeViewModel.getShopInfo().observe(getViewLifecycleOwner(), new Observer<List<ShopInfo>>() {
            @Override
            public void onChanged(List<ShopInfo> dataDTOS) {
                shopCardAdapter.setAllShopInfo(dataDTOS);
                shopCardAdapter.notifyDataSetChanged();
            }
        });

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = binding.shopCardList;
        shopCardAdapter = new ShopCardAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(shopCardAdapter);

        MyAsyncTasks myAsyncTasks = new MyAsyncTasks();
        myAsyncTasks.execute();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public class MyAsyncTasks extends AsyncTask<Void, Void, List<ShopInfo>> {

        @Override
        protected List<ShopInfo> doInBackground(Void... params) {
            ShopInfoApi shopInfoService = RequestUtils.getService(ShopInfoApi.class);
            return shopInfoService.getAllShopInfo().getData();
        }

        @Override
        protected void onPostExecute(List<ShopInfo> shopInfo) {
            homeViewModel.setShopInfo(shopInfo);
        }
    }

}