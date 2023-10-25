package com.example.cloudydrinks.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import com.example.cloudydrinks.R;
import com.example.cloudydrinks.adapter.BannerAdapter;
import com.example.cloudydrinks.adapter.CategoryIconAdapter;
import com.example.cloudydrinks.adapter.PopularArticleAdapter;
import com.example.cloudydrinks.model.Banner;
import com.example.cloudydrinks.model.Category;
import com.example.cloudydrinks.model.Product;
import com.example.cloudydrinks.my_interface.ICategoryClickListener;
import com.example.cloudydrinks.my_interface.IClickItemListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

public class HomeActivity extends AppCompatActivity {
    private BottomNavigationView bottomNav;
    private RecyclerView.Adapter adapterCategory, adapterPopular;
    private DatabaseReference databaseReference;
    private RecyclerView recyclerViewCategoryList, recyclerViewPopularList, recyclerViewBanner;
    private MaterialButton productSearchingBtn;
    private ArrayList<Product> popularProductList;
    private FirebaseAuth mAuth;
    private ArrayList<Category> categoryList;
    private ViewPager viewPager;
    private CircleIndicator circleIndicator;
    private BannerAdapter bannerAdapter;
    private List<Banner> bannerList;
    private Timer mTimmer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();

        bottomNav = findViewById(R.id.bottomNav);
        productSearchingBtn = findViewById(R.id.productSearchingBtn);
        viewPager = findViewById(R.id.viewPager);
        circleIndicator = findViewById(R.id.circle_indicator);

        recyclerViewBanner();
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
    private void recyclerViewBanner() {

        bannerList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("banners");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Banner banner = dataSnapshot.getValue(Banner.class);
                        bannerList.add(banner);
                    }

                    Log.d("data", String.valueOf(bannerList.size()));
                    bannerAdapter = new BannerAdapter(HomeActivity.this, bannerList);
                    viewPager.setAdapter(bannerAdapter);

                    circleIndicator.setViewPager(viewPager);
                    bannerAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());

                    autoSlideImage();
                }
                bannerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    private void autoSlideImage() {
        if (bannerList == null || bannerList.isEmpty() || viewPager == null) {
            return;
        }

        // Init timer
        if (mTimmer == null) {
            mTimmer = new Timer();
        }

        mTimmer.schedule(new TimerTask() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        int currentItem = viewPager.getCurrentItem();
                        int totalItem = bannerList.size() - 1;
                        if (currentItem < totalItem) {
                            currentItem++;
                            viewPager.setCurrentItem(currentItem);
                        } else {
                            viewPager.setCurrentItem(0);
                        }
                    }
                });
            }
        }, 500, 3000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTimmer != null) {
            mTimmer.cancel();
            mTimmer = null;
        }
    }

    private void recyclerViewCategory() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewCategoryList = findViewById(R.id.categoryRV);
        recyclerViewCategoryList.setLayoutManager(linearLayoutManager);

        categoryList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("categories");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Category category = dataSnapshot.getValue(Category.class);
                        categoryList.add(category);
                    }
                }
                adapterCategory.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        adapterCategory = new CategoryIconAdapter(categoryList, new ICategoryClickListener() {
            @Override
            public void onClickCategoryItem(Category category) {
                onCategoryClickItem(category);
            }
        });
        recyclerViewCategoryList.setAdapter(adapterCategory);
    }

    private void onCategoryClickItem(Category category) {
        Intent intent = new Intent(HomeActivity.this, CategoryProductActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("category", category);
        intent.putExtras(bundle);
        startActivity(intent);
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
        adapterPopular = new PopularArticleAdapter(popularProductList, new IClickItemListener() {
            @Override
            public void onClickItemProduct(Product product) {
                onProductClickItem(product);
            }
        });
        recyclerViewPopularList.setAdapter(adapterPopular);
    }

    private void onProductClickItem(Product product) {
        Intent intent = new Intent(HomeActivity.this, ItemViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("product object", product);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}