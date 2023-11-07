package com.example.cloudydrinks.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cloudydrinks.R;
import com.example.cloudydrinks.activity.ItemViewActivity;
import com.example.cloudydrinks.activity.ProductSearchingActivity;
import com.example.cloudydrinks.adapter.ProductListAdapter;
import com.example.cloudydrinks.model.Product;
import com.example.cloudydrinks.my_interface.IClickItemListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FavoriteFragment extends Fragment {
    private ArrayList<Product> productList;
    private RecyclerView favRecyclerview;
    private ProductListAdapter adapter;
    private DatabaseReference databaseReference;
    private String myKey;
    public FavoriteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_favorite, container, false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        favRecyclerview = mView.findViewById(R.id.favListRV);
        favRecyclerview.setLayoutManager(linearLayoutManager);

        generateDrinkList();
        favRecyclerview.setAdapter(adapter);

        return mView;
    }
    private void generateDrinkList() {
        Bundle data = getArguments();

        if (data != null) {
            myKey = data.getString("userPhoneNo");
        }

        productList = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(myKey).child("wishlist");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    productList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Product product = dataSnapshot.getValue(Product.class);
                        Log.d("drink", snapshot.toString());
                        productList.add(product);
                    }
                } else {
                    favRecyclerview.setVisibility(View.GONE);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        adapter = new ProductListAdapter(productList, new IClickItemListener() {
            @Override
            public void onClickItemProduct(Product product) {
                onClickItem(product);
            }
        });
    }

    private void onClickItem(Product product) {
        Intent intent = new Intent(getActivity(), ItemViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("product object", product);
        intent.putExtra("userPhoneNumber", myKey);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}