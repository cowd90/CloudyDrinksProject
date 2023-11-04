package com.example.cloudydrinks.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cloudydrinks.R;
import com.example.cloudydrinks.model.CartModel;

import java.util.ArrayList;

public class PaymentDetailAdapter extends RecyclerView.Adapter<PaymentDetailAdapter.ViewHolder> {
    private ArrayList<CartModel> cartList;

    public PaymentDetailAdapter(ArrayList<CartModel> cartList) {
        this.cartList = cartList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.display_payment_detail, parent, false);
        return new ViewHolder(inflate);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final CartModel cartModel = cartList.get(position);

        if (cartModel == null) {
            return;
        }

        holder.drinkNameTV.setText(cartModel.getProduct_name());
        holder.drinkSizeTV.setText("("+cartModel.getSize()+")");
        holder.drinkQuantityTV.setText("x"+cartModel.getQuantity());
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView drinkNameTV, drinkSizeTV, drinkQuantityTV;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            drinkNameTV = itemView.findViewById(R.id.drinkNameTV);
            drinkSizeTV = itemView.findViewById(R.id.drinkSizeTV);
            drinkQuantityTV = itemView.findViewById(R.id.drinkQuantityTV);
        }
    }
}
