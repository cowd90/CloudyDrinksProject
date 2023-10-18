package com.example.cloudydrinks.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.cloudydrinks.R;
import com.example.cloudydrinks.adapter.FoodListAdapter;
import com.example.cloudydrinks.model.Product;
import com.example.cloudydrinks.my_interface.IClickItemListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProductSearchingActivity extends AppCompatActivity {

    private TextView cancelSearchingTV;
    private ArrayList<Product> productList;
    private DatabaseReference databaseReference;
    private RecyclerView.Adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_searching);

        cancelSearchingTV = findViewById(R.id.cancelSearchingTV);

        generateFoodList();

        cancelSearchingTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProductSearchingActivity.this, HomeActivity.class));
                finish();
            }
        });
    }
    private void generateFoodList() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ProductSearchingActivity.this, LinearLayoutManager.VERTICAL, false);
        RecyclerView recyclerViewFoodList = findViewById(R.id.allFoodRV);
        recyclerViewFoodList.setLayoutManager(linearLayoutManager);

        productList = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("products");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Product product = dataSnapshot.getValue(Product.class);
                        productList.add(product);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        adapter = new FoodListAdapter(productList, new IClickItemListener() {
            @Override
            public void onClickItemProduct(Product product) {
                onClickItem(product);
            }
        });
        recyclerViewFoodList.setAdapter(adapter);
    }

    private void onClickItem(Product product) {
        Intent intent = new Intent(ProductSearchingActivity.this, ItemViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("product object", product);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        finish();
    }
}