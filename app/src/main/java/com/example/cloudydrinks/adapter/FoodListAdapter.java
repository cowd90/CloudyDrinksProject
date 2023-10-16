package com.example.cloudydrinks.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cloudydrinks.domain.FoodDomain;
import com.example.cloudydrinks.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class FoodListAdapter extends RecyclerView.Adapter<FoodListAdapter.ViewHolder> {
    private ArrayList<FoodDomain> foodList;

    public FoodListAdapter(ArrayList<FoodDomain> foodList) {
        this.foodList = foodList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.display_item_searching, parent, false);
        return new FoodListAdapter.ViewHolder(inflate);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.foodNameTV.setText(foodList.get(position).getProduct_name());
        String price = String.valueOf(foodList.get(position).getProduct_price());
        holder.foodPriceTV.setText(numberCurrencyFormat(price)+"₫");
        Picasso.get().load(foodList.get(position).getProduct_img_url()).into(holder.foodImage);
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
            foodNameTV = itemView.findViewById(R.id.searchFoodNameTV);
            foodPriceTV = itemView.findViewById(R.id.foodPriceTV);
            foodImage = itemView.findViewById(R.id.foodImage);
        }
    }
    public static String numberCurrencyFormat(String number) {
        DecimalFormat decimalFormat = new DecimalFormat("###,###");
        return decimalFormat.format(Integer.parseInt(number));
    }
}
