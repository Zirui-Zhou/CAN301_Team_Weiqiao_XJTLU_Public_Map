package com.example.can301_2.ui.detail;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.can301_2.R;
import com.example.can301_2.adapter.ImageGalleryAdapter;
import com.example.can301_2.adapter.ItemCardAdapter;
import com.example.can301_2.api.ItemInfoApi;
import com.example.can301_2.api.ShopInfoApi;
import com.example.can301_2.databinding.FragmentAnotherDetailBinding;
import com.example.can301_2.domain.ItemInfo;
import com.example.can301_2.domain.ShopInfo;
import com.example.can301_2.utils.RequestUtils;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DetailFragment extends Fragment {
    String TAG = "detail";
    private FragmentAnotherDetailBinding binding;
    private DetailViewModel detailViewModel;

    public static DetailFragment newInstance() {
        return new DetailFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ImageGalleryAdapter imageGalleryAdapter = new ImageGalleryAdapter();

        detailViewModel = new ViewModelProvider(this).get(DetailViewModel.class);


        binding = FragmentAnotherDetailBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ImageView imageViewShopCover = binding.imageView;
        TextView textViewShopName = binding.detailShopName;
        RatingBar ratingBarShopRating = binding.detailShopRating;
        RecyclerView recyclerViewShopDetailImage = binding.detailShopDetailImageList;
        RecyclerView recyclerViewItemInfo = binding.detailShopItemInfoList;

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerViewShopDetailImage.getContext(), LinearLayoutManager.HORIZONTAL) {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int position = parent.getChildAdapterPosition(view);
                // hide the divider for the last child
                if (position == state.getItemCount() - 1) {
                    outRect.setEmpty();
                } else {
                    super.getItemOffsets(outRect, view, parent, state);
                }
            }
        };
        dividerItemDecoration.setDrawable(getContext().getResources().getDrawable(R.drawable.divider_image_gallery));
        recyclerViewShopDetailImage.addItemDecoration(dividerItemDecoration);
        recyclerViewShopDetailImage.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false) {
            @Override
            public boolean checkLayoutParams(RecyclerView.LayoutParams lp) {
                // force height of viewHolder here, this will override layout_height from xml
                lp.width = (int) (getWidth() * 0.4);
                return true;
            }
        });
        recyclerViewShopDetailImage.setAdapter(imageGalleryAdapter);

        assert getArguments() != null;
        long shopId = getArguments().getLong("shop_id");

        ItemCardAdapter itemCardAdapter = new ItemCardAdapter();
        recyclerViewItemInfo.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewItemInfo.setAdapter(itemCardAdapter);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            ShopInfoApi shopInfoService = RequestUtils.getService(ShopInfoApi.class);
            ShopInfo shopInfo = shopInfoService.getShopInfoById(shopId).getData();
//            detailViewModel.setShopInfo(shopInfo);

            ItemInfoApi itemInfoService = RequestUtils.getService(ItemInfoApi.class);
            List<ItemInfo> itemInfoList = itemInfoService.getItemInfoByShopId(shopId).getData();
//            detailViewModel.setItemInfoList(itemInfoList);

            handler.post(() -> {
                Glide.with(getContext())
                        .load(RequestUtils.baseStaticUrl + shopInfo.getShopCoverImage())
                        .into(imageViewShopCover);
                textViewShopName.setText(shopInfo.getShopName());
                ratingBarShopRating.setRating(shopInfo.getShopRating().floatValue());
                imageGalleryAdapter.setImageUrls(shopInfo.getShopDetailImages());
                imageGalleryAdapter.notifyDataSetChanged();
                itemCardAdapter.setItemInfoList(itemInfoList);
                itemCardAdapter.notifyDataSetChanged();
            });
        });

        return root;
    }


    @Override
    public void onPause() {
        Log.e(TAG, "onStop: ");
//        Navigation.findNavController(getView()).clearBackStack();
//        super.onDestroy();
        super.onPause();
    }

}