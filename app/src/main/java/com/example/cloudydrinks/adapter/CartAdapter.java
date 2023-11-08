package com.example.cloudydrinks.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cloudydrinks.R;
import com.example.cloudydrinks.model.CartModel;
import com.example.cloudydrinks.my_interface.IClickCartModel;
import com.example.cloudydrinks.utils.NumberCurrencyFormatUtil;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private ArrayList<CartModel> cartList;
    private IClickCartModel iDelete;

    public CartAdapter(ArrayList<CartModel> cartList, IClickCartModel iDelete) {
        this.cartList = cartList;
        this.iDelete = iDelete;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.display_cart_item, parent, false);
        return new ViewHolder(inflate);
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final CartModel cartModel = cartList.get(position);

        if (cartModel == null) {
            return;
        }

        holder.productNameTV.setText(cartModel.getProduct_name());
        String price = String.valueOf(cartModel.getTotalPrice());
        holder.productTotalPriceTV.setText(NumberCurrencyFormatUtil.numberCurrencyFormat(price));
        holder.quantityTV.setText("x" + cartModel.getQuantity());
        holder.sizeTV.setText(cartModel.getSize());
        Picasso.get().load(cartModel.getProduct_img_url()).into(holder.productImage);

        holder.removeCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iDelete.onClickCartListener(cartModel);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (cartList != null) {
            return cartList.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView productNameTV, productTotalPriceTV, quantityTV, sizeTV;
        ImageView productImage, removeCartBtn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productNameTV = itemView.findViewById(R.id.productNameCartTV);
            productTotalPriceTV = itemView.findViewById(R.id.productPriceCartTV);
            quantityTV = itemView.findViewById(R.id.quantityTV);
            sizeTV = itemView.findViewById(R.id.productStats);
            productImage = itemView.findViewById(R.id.productImageCart);
            removeCartBtn = itemView.findViewById(R.id.removeCartBtn);
        }
    }
}