package com.example.can301_2.ui.detail;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.Rating;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.can301_2.R;
import com.example.can301_2.adapter.ImageGalleryAdapter;
import com.example.can301_2.api.ShopInfoApi;
import com.example.can301_2.databinding.FragmentAnotherDetailBinding;
import com.example.can301_2.databinding.FragmentDetailBinding;
import com.example.can301_2.databinding.FragmentHomeBinding;
import com.example.can301_2.domain.ShopInfo;
import com.example.can301_2.ui.home.HomeFragment;
import com.example.can301_2.ui.home.HomeViewModel;
import com.example.can301_2.utils.RequestUtils;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

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

        MyAsyncTasks myAsyncTasks = new MyAsyncTasks();
        try {
            ShopInfo shopInfo = myAsyncTasks.execute(shopId).get();
            Glide
                    .with(getContext())
                    .load(RequestUtils.baseStaticUrl + shopInfo.getShopCoverImage())
                    .into(imageViewShopCover);
            textViewShopName.setText(shopInfo.getShopName());
            ratingBarShopRating.setRating(shopInfo.getShopRating().floatValue());
            imageGalleryAdapter.setImageUrls(shopInfo.getShopDetailImages());
            imageGalleryAdapter.notifyDataSetChanged();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return root;
    }



    @Override
    public void onStop() {
        Log.e(TAG, "onStop: ");
//        Navigation.findNavController(getView()).navigateUp();
        super.onStop();
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