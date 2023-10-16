package com.example.cloudydrinks.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cloudydrinks.R;
import com.example.cloudydrinks.domain.FoodDomain;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PopularArticleAdapter extends RecyclerView.Adapter<PopularArticleAdapter.ViewHolder> {
    private ArrayList<FoodDomain> foodList;

    public PopularArticleAdapter(ArrayList<FoodDomain> foodDomains) {
        this.foodList = foodDomains;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.display_popular_food, parent, false);
        return new ViewHolder(inflate);
    }

    @NonNull
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.foodNameTV.setText(foodList.get(position).getProduct_name());
        holder.foodPriceTV.setText(String.valueOf(foodList.get(position).getProduct_price()));
        Picasso.get().load(foodList.get(position).getProduct_img_url()).resize(80, 80).centerCrop().into(holder.foodImage);
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView foodNameTV, foodPriceTV;
        ImageView foodImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            foodNameTV = itemView.findViewById(R.id.foodNameTV);
            foodPriceTV = itemView.findViewById(R.id.prizeTv);
            foodImage = itemView.findViewById(R.id.popularFoodImage);
        }
    }
}