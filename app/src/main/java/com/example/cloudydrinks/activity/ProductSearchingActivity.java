package com.example.cloudydrinks.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.cloudydrinks.R;
import com.example.cloudydrinks.adapter.ProductListAdapter;
import com.example.cloudydrinks.model.Product;
import com.example.cloudydrinks.my_interface.IClickItemListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProductSearchingActivity extends AppCompatActivity {
    private final static String PRODUCT = "products";
    private TextView cancelSearchingTV;
    private ArrayList<Product> productList;
    private DatabaseReference databaseReference;
    private ProductListAdapter adapter;
    private RecyclerView recyclerViewFoodList;
    private SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_searching);

        cancelSearchingTV = findViewById(R.id.cancelSearchingTV);
        searchView = findViewById(R.id.searchBarET);

        generateList();

        cancelSearchingTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void generateList() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ProductSearchingActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerViewFoodList = findViewById(R.id.allFoodRV);
        recyclerViewFoodList.setLayoutManager(linearLayoutManager);

        productList = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference(PRODUCT);
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
        adapter = new ProductListAdapter(productList, productList, new IClickItemListener() {
            @Override
            public void onClickItemProduct(Product product) {
                onClickItem(product);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
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