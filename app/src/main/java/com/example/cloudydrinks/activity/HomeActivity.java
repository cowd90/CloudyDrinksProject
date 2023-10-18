package com.example.cloudydrinks.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.cloudydrinks.R;
import com.example.cloudydrinks.adapter.CategoryAdapter;
import com.example.cloudydrinks.adapter.PopularArticleAdapter;
import com.example.cloudydrinks.model.Categories;
import com.example.cloudydrinks.model.Product;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    private BottomNavigationView bottomNav;
    private RecyclerView.Adapter adapterCategory, adapterPopular;
    private DatabaseReference databaseReference;
    private RecyclerView recyclerViewCategoryList, recyclerViewPopularList;
    private MaterialButton productSearchingBtn;
    private ArrayList<Product> popularProductList;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();

        bottomNav = findViewById(R.id.bottomNav);
        productSearchingBtn = findViewById(R.id.productSearchingBtn);

        recyclerViewCategory();
        recyclerViewPopular();

        productSearchingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ProductSearchingActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void recyclerViewCategory() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewCategoryList = findViewById(R.id.categoryRV);
        recyclerViewCategoryList.setLayoutManager(linearLayoutManager);

        ArrayList<Categories> categoryList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("categories");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoryList.clear();
                if(snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Categories categories = dataSnapshot.getValue(Categories.class);
                        categoryList.add(categories);
                    }
                }
                adapterCategory.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        adapterCategory = new CategoryAdapter(categoryList);
        recyclerViewCategoryList.setAdapter(adapterCategory);
    }

    private void recyclerViewPopular() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(HomeActivity.this, 2, GridLayoutManager.VERTICAL, false);
        recyclerViewPopularList = findViewById(R.id.popularFoodRV);
        recyclerViewPopularList.setLayoutManager(gridLayoutManager);

        popularProductList = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("products");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                popularProductList.clear();
                if(snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Log.d("data", dataSnapshot.toString());
                        Product productDomain = dataSnapshot.getValue(Product.class);
                        popularProductList.add(productDomain);
                    }
                }
                adapterPopular.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        adapterPopular = new PopularArticleAdapter(popularProductList);
        recyclerViewPopularList.setAdapter(adapterPopular);
    }
}