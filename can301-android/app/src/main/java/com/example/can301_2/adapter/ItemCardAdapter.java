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
import com.example.can301_2.domain.ItemInfo;
import com.example.can301_2.domain.ShopInfo;
import com.example.can301_2.utils.RequestUtils;

import java.util.ArrayList;
import java.util.List;

public class ItemCardAdapter extends RecyclerView.Adapter<ItemCardAdapter.ItemCardViewHolder> {
    List<ItemInfo> itemInfoList = new ArrayList<>();

    public void setItemInfoList(List<ItemInfo> itemInfoList) {
        this.itemInfoList = itemInfoList;
    }

    @NonNull
    @Override
    public ItemCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.recycleitem_item_card, parent, false);
        return new ItemCardViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemCardViewHolder holder, int position) {
        ItemInfo itemInfo = itemInfoList.get(position);
        holder.textViewItemName.setText(itemInfo.getItemName());
        holder.textViewItemPrice.setText(holder.itemView.getContext().getString(R.string.item_price, itemInfo.getItemPrice()));
        Glide.with(holder.itemView).load(RequestUtils.baseStaticUrl + itemInfo.getItemImage()).into(holder.imageViewItemImage);
    }

    @Override
    public int getItemCount() {
        return this.itemInfoList.size();
    }

    static class ItemCardViewHolder extends RecyclerView.ViewHolder{
        TextView textViewItemName, textViewItemPrice;
        ImageView imageViewItemImage;
        public ItemCardViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewItemName = itemView.findViewById(R.id.item_card_name);
            textViewItemPrice = itemView.findViewById(R.id.item_card_price);
            imageViewItemImage = itemView.findViewById(R.id.item_card_image);
        }
    }
}
