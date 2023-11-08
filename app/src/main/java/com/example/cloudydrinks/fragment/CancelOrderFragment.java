package com.example.cloudydrinks.fragment;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cloudydrinks.R;
import com.example.cloudydrinks.adapter.ReiceivedAndCancelAdapter;
import com.example.cloudydrinks.local_data.DataLocalManager;
import com.example.cloudydrinks.model.Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CancelOrderFragment extends Fragment {
    private RecyclerView recyclerView;
    private ArrayList<Order> orderList;
    private Order order;
    private DatabaseReference databaseReference;
    private ReiceivedAndCancelAdapter adapter;
    private String userid;
    public CancelOrderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_cancel_order, container, false);

        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView = mView.findViewById(R.id.cancelRV);
        recyclerView.setLayoutManager(gridLayoutManager);

        generateList();

        return mView;
    }
    public void generateList() {

        userid = DataLocalManager.getUserId();

        orderList = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userid).child("order").child("status_cancel");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderList.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        order = dataSnapshot.getValue(Order.class);
                        orderList.add(order);
                    }
                } else {
                    recyclerView.setVisibility(View.GONE);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        adapter = new ReiceivedAndCancelAdapter(orderList);
        recyclerView.setAdapter(adapter);

    }
}