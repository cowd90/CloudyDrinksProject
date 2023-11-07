package com.example.cloudydrinks.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cloudydrinks.R;
import com.example.cloudydrinks.model.Order;
import com.example.cloudydrinks.my_interface.IOrderClickListener;
import com.example.cloudydrinks.utils.NumberCurrencyFormatUtil;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class DeliveringItemAdapter extends RecyclerView.Adapter<DeliveringItemAdapter.ViewHolder> {
    private ArrayList<Order> orderList;
    private IOrderClickListener iCancel, iReceived;

    public DeliveringItemAdapter(ArrayList<Order> orderList, IOrderClickListener iCancel, IOrderClickListener iReceived) {
        this.orderList = orderList;
        this.iCancel = iCancel;
        this.iReceived = iReceived;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.display_delivering_order, parent, false);
        return new ViewHolder(inflate);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Order model = orderList.get(position);

        if (model == null) {
            return;
        }

        holder.orderItemNameTV.setText(model.getProduct_name());
        holder.orderItemSizeTV.setText("(" + model.getSize() +")");
        holder.orderItemQuantityTV.setText("x" + model.getQuantity());
        holder.customerInfoTV.setText(model.getContact().getFullName() + " - " + model.getContact().getPhoneNo());
        holder.addressTV.setText(model.getContact().getWard() + ", " + model.getContact().getDistrict() + ", " + model.getContact().getCity());
        holder.orderItemPriceTV.setText(NumberCurrencyFormatUtil.numberCurrencyFormat(String.valueOf(model.getTotalPrice())));

        holder.cancelOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iCancel.onOrderClickListener(model);
            }
        });

        holder.receivedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iReceived.onOrderClickListener(model);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView orderItemNameTV, orderItemSizeTV, orderItemQuantityTV, customerInfoTV, addressTV, orderItemPriceTV;
        MaterialButton cancelOrderBtn, receivedBtn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderItemNameTV = itemView.findViewById(R.id.orderItemNameTV);
            orderItemSizeTV = itemView.findViewById(R.id.orderItemSizeTV);
            orderItemQuantityTV = itemView.findViewById(R.id.orderItemQuantityTV);
            addressTV = itemView.findViewById(R.id.deliveringAddressTV);
            customerInfoTV = itemView.findViewById(R.id.customerInfoTV);
            orderItemPriceTV = itemView.findViewById(R.id.orderItemPriceTV);
            cancelOrderBtn = itemView.findViewById(R.id.cancelOrderBtn);
            receivedBtn = itemView.findViewById(R.id.receivedBtn);
        }
    }
}
