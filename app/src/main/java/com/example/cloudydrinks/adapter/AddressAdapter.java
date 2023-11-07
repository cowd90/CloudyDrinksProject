package com.example.cloudydrinks.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cloudydrinks.R;
import com.example.cloudydrinks.model.CartModel;
import com.example.cloudydrinks.model.Contact;
import com.example.cloudydrinks.my_interface.IAddressClickListener;
import com.example.cloudydrinks.my_interface.IEditClickListener;

import java.util.ArrayList;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {
    private ArrayList<Contact> addressList;
    private IEditClickListener iEditClickListener;
    private IAddressClickListener iAddressClickListener;

    public AddressAdapter(ArrayList<Contact> addressList, IEditClickListener iEditClickListener, IAddressClickListener iAddressClickListener) {
        this.addressList = addressList;
        this.iEditClickListener = iEditClickListener;
        this.iAddressClickListener = iAddressClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.display_address_info, parent, false);
        return new ViewHolder(inflate);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Contact contact = addressList.get(position);

        if (contact == null) {
            return;
        }

        holder.customerNameTV.setText(contact.getFullName());
        holder.customerPhoneTV.setText("  |  " + contact.getPhoneNo());
        holder.streetTV.setText(contact.getStreet());
        holder.addressTV.setText(contact.getWard() + ", " + contact.getDistrict() + ", " + contact.getCity());

        holder.editTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iEditClickListener.onEditClickListener(contact);
            }
        });

        holder.addressLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iAddressClickListener.onAddressClickListener(contact);
            }
        });
    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView customerNameTV, customerPhoneTV, streetTV, addressTV, editTV;
        LinearLayout addressLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            customerNameTV = itemView.findViewById(R.id.customerNameTV);
            customerPhoneTV = itemView.findViewById(R.id.customerPhoneTV);
            streetTV = itemView.findViewById(R.id.streetTV);
            addressTV = itemView.findViewById(R.id.addressTV);
            editTV = itemView.findViewById(R.id.editTV);
            addressLayout = itemView.findViewById(R.id.addressLayout);
        }
    }
}
