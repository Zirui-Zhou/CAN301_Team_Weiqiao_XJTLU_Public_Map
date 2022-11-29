package com.example.can301_2.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.can301_2.R;
import com.example.can301_2.domain.ShopInfo;
import com.example.can301_2.domain.ShopType;
import com.example.can301_2.utils.RequestUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ShopCardAdapter extends RecyclerView.Adapter<ShopCardAdapter.ShopCardViewHolder> {
    List<ShopInfo> allShopInfo = new ArrayList<>();
    Map<Long, ShopType> shopTypeMap;

    public ShopCardAdapter(Map<Long, ShopType> shopTypeMap) {
        this.shopTypeMap = shopTypeMap;
    }

    public void setAllShopInfo(List<ShopInfo> allShopInfo) {
        this.allShopInfo = allShopInfo;
    }

    public void setShopTypeMap(Map<Long, ShopType> shopTypeMap) {
        this.shopTypeMap = shopTypeMap;
    }

    @NonNull
    @Override
    public ShopCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.recycleitem_shop_card, parent, false);
        return new ShopCardViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopCardViewHolder holder, int position) {
        ShopInfo shopInfo = allShopInfo.get(position);
        holder.textViewShopName.setText(shopInfo.getShopName());
        holder.textViewShopSales.setText(holder.itemView.getContext().getString(R.string.shop_sales, shopInfo.getShopSales()));
        if (!shopTypeMap.isEmpty()) {
            holder.textViewShopType.setText(Objects.requireNonNull(shopTypeMap.get(shopInfo.getShopTypeId())).getShopTypeName());
        }
        holder.ratingBarShopRating.setRating(shopInfo.getShopRating().floatValue());
        Glide.with(holder.itemView).load(RequestUtils.baseStaticUrl + shopInfo.getShopCoverImage()).into(holder.imageViewShopCoverImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putLong("shop_id", shopInfo.getShopId());
                Navigation.findNavController(view).navigate(R.id.action_navigation_home_to_navigation_detail2, bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.allShopInfo.size();
    }

    static class ShopCardViewHolder extends RecyclerView.ViewHolder{
        TextView textViewShopName, textViewShopSales, textViewShopType;
        RatingBar ratingBarShopRating;
        ImageView imageViewShopCoverImage;
        public ShopCardViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewShopName = itemView.findViewById(R.id.shopName);
            textViewShopSales = itemView.findViewById(R.id.shopSales);
            ratingBarShopRating = itemView.findViewById(R.id.shopRating);
            imageViewShopCoverImage = itemView.findViewById(R.id.shopCoverImage);
            textViewShopType = itemView.findViewById(R.id.recycleitem_shop_card_shop_type);
        }
    }
}
