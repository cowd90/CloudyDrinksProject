package com.example.cloudydrinks.fragment;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cloudydrinks.R;
import com.example.cloudydrinks.adapter.CartAdapter;
import com.example.cloudydrinks.model.CartModel;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class NotificationFragment extends Fragment {
    private RecyclerView recyclerView;
    private View mView;
    private CartAdapter cartAdapter;
    private ArrayList<CartModel> cartList;
    private DatabaseReference databaseReference;
    public NotificationFragment() {
        // Required empty public constructor
    }
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_cart, container, false);

        return mView;
    }
}