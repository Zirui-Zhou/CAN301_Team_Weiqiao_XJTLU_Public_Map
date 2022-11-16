package com.example.can301_2.ui.detail;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.can301_2.R;
import com.example.can301_2.api.ShopInfoApi;
import com.example.can301_2.databinding.FragmentDetailBinding;
import com.example.can301_2.databinding.FragmentHomeBinding;
import com.example.can301_2.domain.ShopInfo;
import com.example.can301_2.ui.home.HomeFragment;
import com.example.can301_2.ui.home.HomeViewModel;
import com.example.can301_2.utils.RequestUtils;

import java.util.List;

public class DetailFragment extends Fragment {
    private FragmentDetailBinding binding;
    private DetailViewModel detailViewModel;

    public static DetailFragment newInstance() {
        return new DetailFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        detailViewModel = new ViewModelProvider(this).get(DetailViewModel.class);




        binding = FragmentDetailBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        TextView textView = binding.shop;
        ImageView imageView = binding.detailImageFirst;

        assert getArguments() != null;
        long shopId = getArguments().getLong("shop_id");

        detailViewModel.getShopInfo().observe(getViewLifecycleOwner(), new Observer<ShopInfo>() {
            @Override
            public void onChanged(ShopInfo shopInfo) {
                textView.setText(shopInfo.getShopName());
                Glide.with(root).load(RequestUtils.baseStaticUrl + shopInfo.getShopCoverImage()).into(imageView);
            }
        });

        MyAsyncTasks myAsyncTasks = new MyAsyncTasks();
        myAsyncTasks.execute(shopId);

        return root;
    }

    public class MyAsyncTasks extends AsyncTask<Long, Void, ShopInfo> {

        @Override
        protected ShopInfo doInBackground(Long... id) {
            ShopInfoApi shopInfoService = RequestUtils.getService(ShopInfoApi.class);
            return shopInfoService.getShopInfoById(id[0]).getData();
        }

        @Override
        protected void onPostExecute(ShopInfo shopInfo) {
            detailViewModel.setShopInfo(shopInfo);
        }
    }
}