package com.example.can301_2.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.can301_2.R;
import com.example.can301_2.utils.RequestUtils;

import java.util.ArrayList;
import java.util.List;

public class ImageGalleryAdapter extends RecyclerView.Adapter<ImageGalleryAdapter.ShopCardViewHolder> {
    List<String> imageUrls = new ArrayList<>();

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    @NonNull
    @Override
    public ShopCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.recycleitem_image_gallery, parent, false);
        return new ShopCardViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopCardViewHolder holder, int position) {
        Glide
                .with(holder.itemView)
                .load(RequestUtils.baseStaticUrl + imageUrls.get(position))
                .into(holder.imageViewGalleryImage);
    }

    @Override
    public int getItemCount() {
        return this.imageUrls.size();
    }

    static class ShopCardViewHolder extends RecyclerView.ViewHolder{
        ImageView imageViewGalleryImage;
        public ShopCardViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewGalleryImage = itemView.findViewById(R.id.gallery_image);
        }
    }
}
