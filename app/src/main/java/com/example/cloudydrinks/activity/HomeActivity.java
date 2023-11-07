package com.example.cloudydrinks.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;

import com.andremion.counterfab.CounterFab;
import com.example.cloudydrinks.R;
import com.example.cloudydrinks.adapter.BannerAdapter;
import com.example.cloudydrinks.adapter.CategoryIconAdapter;
import com.example.cloudydrinks.adapter.PopularArticleAdapter;
import com.example.cloudydrinks.adapter.ViewPagerAdapter;
import com.example.cloudydrinks.fragment.DeliveringFragment;
import com.example.cloudydrinks.fragment.DeliveryFragment;
import com.example.cloudydrinks.fragment.FavoriteFragment;
import com.example.cloudydrinks.fragment.HomeFragment;
import com.example.cloudydrinks.fragment.MyBottomSheetFragment;
import com.example.cloudydrinks.fragment.ProfileFragment;
import com.example.cloudydrinks.model.Banner;
import com.example.cloudydrinks.model.CartModel;
import com.example.cloudydrinks.model.Category;
import com.example.cloudydrinks.model.Product;
import com.example.cloudydrinks.my_interface.ICategoryClickListener;
import com.example.cloudydrinks.my_interface.IClickItemListener;
import com.example.cloudydrinks.utils.TranslateAnimation;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationBarView;
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
    private RecyclerView.Adapter adapterCategory, adapterPopular;
    private DatabaseReference databaseReference;
    private ArrayList<Product> popularProductList;
    private ArrayList<Category> categoryList;
    private ViewPager viewPager;
    private CircleIndicator circleIndicator;
    private BannerAdapter bannerAdapter;
    private List<Banner> bannerList;
    private Timer mTimmer;
    private CounterFab fab;
    private ArrayList<CartModel> cartList;
    private String userPhoneNumber;
    private BadgeDrawable badgeDrawable;
    private int countFavorite;
    private final Fragment homeFragment = new HomeFragment();
    private final Fragment favFragment = new FavoriteFragment();
    private final Fragment deliveryFragment = new DeliveryFragment();
    private final Fragment profileFragment = new ProfileFragment();
    public static final String PREFERENCE_USERID = "com.example.cloudydrinks";
    @SuppressLint({"MissingInflatedId", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        MaterialButton productSearchingBtn = findViewById(R.id.productSearchingBtn);
        viewPager = findViewById(R.id.viewPager);
        circleIndicator = findViewById(R.id.circle_indicator);
        BottomNavigationView bottomNavigation = findViewById(R.id.bottomNav);
        fab = findViewById(R.id.fab);

        badgeDrawable = bottomNavigation.getOrCreateBadge(R.id.itFavorite);

        loadFragment(homeFragment);
        bottomNavigation.getMenu().findItem(R.id.itHome).setChecked(true);

        // Get user phone number
        userPhoneNumber = getIntent().getStringExtra("userPhoneNumber");

        // Save user id to shared preferences
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("userid", userPhoneNumber);
        editor.apply();

        // pass current user to favorite fragment
        Bundle data = new Bundle();
        data.putString("userPhoneNo", userPhoneNumber);
        favFragment.setArguments(data);
        profileFragment.setArguments(data);

        // Get data from firebase
        cartList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userPhoneNumber).child("Cart");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartList.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        CartModel cartModel = dataSnapshot.getValue(CartModel.class);
                        cartList.add(cartModel);
                    }
                    setCartItemQuantity(cartList);
                } else {
                    setCartItemQuantity(cartList);
                }
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openBottomSheetFragment(cartList);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

//         Hide/show floating action button when scroll
        NestedScrollView nsv = findViewById(R.id.nestedScroll);
        nsv.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(@NonNull NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > oldScrollY) {
                    fab.setVisibility(View.GONE);
                } else {
                    fab.setVisibility(View.VISIBLE);
                }
            }
        });

        // Hide/show bottom navigation when scroll
        nsv.setOnTouchListener(new TranslateAnimation(this, bottomNavigation));

        recyclerViewBanner();
        recyclerViewCategory();
        recyclerViewPopular();
        loadFavoriteQuantityItem();

        productSearchingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ProductSearchingActivity.class);
                intent.putExtra("userPhoneNumber", userPhoneNumber);
                startActivity(intent);
                finish();
            }
        });

        bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.itHome) {
                    fab.setVisibility(View.VISIBLE);
                    loadFragment(homeFragment);
                } else if (item.getItemId() == R.id.itFavorite) {
                    fab.setVisibility(View.GONE);
                    loadFragment(favFragment);
                } else if (item.getItemId() == R.id.itNotification) {
                    fab.setVisibility(View.GONE);
                    loadFragment(deliveryFragment);
                } else if (item.getItemId() == R.id.itProfile) {
                    fab.setVisibility(View.GONE);
                    loadFragment(profileFragment);
                }

                return true;
            }
        });
    }

    public void openBottomSheetFragment(ArrayList<CartModel> cartList) {
        MyBottomSheetFragment myBottomSheetFragment = new MyBottomSheetFragment(cartList);
        Bundle bundle = new Bundle();
        bundle.putString("userPhoneNumber", userPhoneNumber);
        myBottomSheetFragment.setArguments(bundle);
        myBottomSheetFragment.show(getSupportFragmentManager(), myBottomSheetFragment.getTag());
    }
    private void setCartItemQuantity(ArrayList<CartModel> cartList) {
        if (cartList == null) {
            fab.setCount(0);
        } else {
            fab.setCount(cartList.size());
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(R.id.frame_layout, fragment);
        transaction.commit();

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
        RecyclerView recyclerViewCategoryList = findViewById(R.id.categoryRV);
        recyclerViewCategoryList.setLayoutManager(linearLayoutManager);
        recyclerViewCategoryList.setFocusable(false);
        recyclerViewCategoryList.setNestedScrollingEnabled(false);

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
        intent.putExtra("userPhoneNumber", userPhoneNumber);
        intent.putExtras(bundle);
        startActivity(intent);
    }
    private void recyclerViewPopular() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(HomeActivity.this, 2, GridLayoutManager.VERTICAL, false);
        RecyclerView recyclerViewPopularList = findViewById(R.id.popularFoodRV);
        recyclerViewPopularList.setLayoutManager(gridLayoutManager);
        recyclerViewPopularList.setFocusable(false);
        recyclerViewPopularList.setNestedScrollingEnabled(false);

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
        intent.putExtra("userPhoneNumber", userPhoneNumber);
        startActivity(intent);
    }
    private void loadFavoriteQuantityItem() {
        countFavorite = 0;
        DatabaseReference wishList = FirebaseDatabase.getInstance().getReference("users").child(userPhoneNumber).child("wishlist");
        wishList.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        countFavorite++;
                    }
                    badgeDrawable.setVisible(true);
                    badgeDrawable.setNumber(countFavorite);
                } else {
                    badgeDrawable.setVisible(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}