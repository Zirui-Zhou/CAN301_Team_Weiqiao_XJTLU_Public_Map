package com.example.can301_2.adapter;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.can301_2.R;
import com.example.can301_2.domain.ShopInfo;
import com.example.can301_2.domain.ShopType;
import com.example.can301_2.utils.RequestUtils;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

public class CategoryItemAdapter extends RecyclerView.Adapter<CategoryItemAdapter.CategoryItemViewHolder>{
    EventListener listener;

    List<ShopType> shopTypeList = new ArrayList<>();
    int selectedPosition = RecyclerView.NO_POSITION;

    @NonNull
    @Override
    public CategoryItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.recycleitem_shop_type, parent, false);
        return new CategoryItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryItemViewHolder holder, int position) {
        ShopType shopType = shopTypeList.get(position);
        holder.textViewTypeName.setText(shopType.getShopTypeName());
        Glide.with(holder.itemView).load(RequestUtils.baseStaticUrl + shopType.getShopTypeImage()).into(holder.imageViewTypeImage);
        holder.itemView.setForeground(position == selectedPosition ? new ColorDrawable(0x80000000) : new ColorDrawable(0x00000000));
    }

    @Override
    public int getItemCount() {
        return this.shopTypeList.size();
    }

    public void setShopTypeList(List<ShopType> shopTypeList) {
        this.shopTypeList = shopTypeList;
    }

    class CategoryItemViewHolder extends RecyclerView.ViewHolder{
        TextView textViewTypeName;
        ImageView imageViewTypeImage;
        public CategoryItemViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTypeName = itemView.findViewById(R.id.recycleritem_shop_type_name);
            imageViewTypeImage = itemView.findViewById(R.id.recycleritem_shop_type_image);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (getAdapterPosition() == RecyclerView.NO_POSITION)
                        return;
                    if (getAdapterPosition() == selectedPosition) {
                        notifyItemChanged(selectedPosition);
                        selectedPosition = RecyclerView.NO_POSITION;
                        listener.updateShopInfoListByType(null);
                        return;
                    }
                    notifyItemChanged(selectedPosition);
                    selectedPosition = getAdapterPosition();
                    notifyItemChanged(selectedPosition);
                    listener.updateShopInfoListByType(shopTypeList.get(selectedPosition));
                }
            });
        }
    }

    public interface EventListener {
        void updateShopInfoListByType(ShopType shopType);
    }

    public CategoryItemAdapter(EventListener listener) {
        this.listener = listener;
    }

}
