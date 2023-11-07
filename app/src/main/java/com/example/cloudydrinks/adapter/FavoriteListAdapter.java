package com.example.cloudydrinks.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cloudydrinks.R;
import com.example.cloudydrinks.model.Product;
import com.example.cloudydrinks.my_interface.IClickItemListener;
import com.example.cloudydrinks.utils.NumberCurrencyFormatUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FavoriteListAdapter extends RecyclerView.Adapter<FavoriteListAdapter.ViewHolder> {
    private ArrayList<Product> productList;

    public FavoriteListAdapter(ArrayList<Product> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.display_favourite_item, parent, false);
        return new ViewHolder(inflate);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Product product = productList.get(position);

        if (product == null) {
            return;
        }

        holder.productNameTV.setText(productList.get(position).getProduct_name());
        String price = String.valueOf(productList.get(position).getProduct_price());
        holder.productPriceTV.setText(NumberCurrencyFormatUtil.numberCurrencyFormat(price));
        Picasso.get().load(productList.get(position).getProduct_img_url()).into(holder.productImage);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView productNameTV, productPriceTV;
        ImageView productImage;
        LinearLayout layoutForeground;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productNameTV = itemView.findViewById(R.id.ProductNameTV);
            productPriceTV = itemView.findViewById(R.id.productPriceTV);
            productImage = itemView.findViewById(R.id.productImage);
            layoutForeground = itemView.findViewById(R.id.layoutForeground);
        }
    }

    public void removeItem(int index) {
        productList.remove(index);
        notifyItemRemoved(index);
    }

    public void restoreItem(Product product, int index) {
        productList.add(index, product);
        notifyItemInserted(index);
    }
}
