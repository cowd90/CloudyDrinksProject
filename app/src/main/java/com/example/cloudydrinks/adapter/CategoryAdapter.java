package com.example.cloudydrinks.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cloudydrinks.R;
import com.example.cloudydrinks.model.Category;
import com.example.cloudydrinks.model.Product;
import com.example.cloudydrinks.my_interface.ICategoryClickListener;
import com.example.cloudydrinks.my_interface.IClickItemListener;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private ArrayList<Product> productList;
    private IClickItemListener iClickItemListener;

    public CategoryAdapter(ArrayList<Product> productList, IClickItemListener iClickItemListener) {
        this.productList = productList;
        this.iClickItemListener = iClickItemListener;
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.display_item_gridview, parent, false);
        return new CategoryAdapter.ViewHolder(inflate);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
        final Product product = productList.get(position);

        if (product == null) {
            return;
        }

        holder.productNameTV.setText(productList.get(position).getProduct_name());
        String price = String.valueOf(productList.get(position).getProduct_price());
        holder.productPriceTV.setText(numberCurrencyFormat(price)+"₫");
        Picasso.get().load(productList.get(position).getProduct_img_url()).into(holder.productImage);

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
        TextView productNameTV, productPriceTV;
        ImageView productImage;
        LinearLayout layoutItem;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layoutItem = itemView.findViewById(R.id.layout_item);
            productNameTV = itemView.findViewById(R.id.productNameTV);
            productPriceTV = itemView.findViewById(R.id.priceTv);
            productImage = itemView.findViewById(R.id.productImage);
        }
    }
    public static String numberCurrencyFormat(String number) {
        DecimalFormat decimalFormat = new DecimalFormat("###,###");
        return decimalFormat.format(Integer.parseInt(number));
    }
}
