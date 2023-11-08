package com.example.cloudydrinks.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;

import com.example.cloudydrinks.R;
import com.example.cloudydrinks.adapter.PopularArticleAdapter;
import com.example.cloudydrinks.local_data.DataLocalManager;
import com.example.cloudydrinks.model.Category;
import com.example.cloudydrinks.model.Product;
import com.example.cloudydrinks.my_interface.IClickItemListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class CategoryProductActivity extends AppCompatActivity {
    private RecyclerView recycler;
    private ArrayList<Product> productList;
    private DatabaseReference databaseReference;
    private RecyclerView.Adapter adapter;
    private Category category;
    private String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        // Get items of the category
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }
        category = (Category) bundle.get("category");

        // Get current user
        userId = DataLocalManager.getUserId();

        // set up toolbar of sign up activity
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(category.getCategory_name());
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView();
    }
    private void recyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(CategoryProductActivity.this, 2, GridLayoutManager.VERTICAL, false);
        recycler = findViewById(R.id.recycler);
        recycler.setLayoutManager(gridLayoutManager);

        productList = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("products");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Product product = dataSnapshot.getValue(Product.class);
                        if (product.getCategory_id() == category.getCategory_id()) {
                            productList.add(product);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        adapter = new PopularArticleAdapter(productList, new IClickItemListener() {
            @Override
            public void onClickItemProduct(Product product) {
                onProductClickItem(product);
            }
        });
        recycler.setAdapter(adapter);
    }
    private void onProductClickItem(Product product) {
        Intent intent = new Intent(CategoryProductActivity.this, ItemViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("product object", product);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}