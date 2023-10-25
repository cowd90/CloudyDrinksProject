package com.example.cloudydrinks.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cloudydrinks.R;
import com.example.cloudydrinks.model.Product;
import com.example.cloudydrinks.my_interface.IClickItemListener;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class PopularArticleAdapter extends RecyclerView.Adapter<PopularArticleAdapter.ViewHolder> {
    private ArrayList<Product> productList;
    private IClickItemListener iClickItemListener;

    public PopularArticleAdapter(ArrayList<Product> productDomains, IClickItemListener iClickItemListener) {
        this.productList = productDomains;
        this.iClickItemListener = iClickItemListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.display_popular_food, parent, false);
        return new ViewHolder(inflate);
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Product product = productList.get(position);

        if (product == null) {
            return;
        }

        holder.foodNameTV.setText(productList.get(position).getProduct_name());
        String price = String.valueOf(productList.get(position).getProduct_price());
        holder.foodPriceTV.setText(numberCurrencyFormat(price)+"₫");
        Picasso.get().load(productList.get(position).getProduct_img_url()).into(holder.foodImage);
        holder.layoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickItemListener.onClickItemProduct(product);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView foodNameTV, foodPriceTV;
        ImageView foodImage;
        CardView layoutItem;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            foodNameTV = itemView.findViewById(R.id.foodNameTV);
            foodPriceTV = itemView.findViewById(R.id.prizeTv);
            foodImage = itemView.findViewById(R.id.popularFoodImage);
            layoutItem = itemView.findViewById(R.id.layout_item);
        }
    }
    public static String numberCurrencyFormat(String number) {
        DecimalFormat decimalFormat = new DecimalFormat("###,###");
        return decimalFormat.format(Integer.parseInt(number));
    }
}