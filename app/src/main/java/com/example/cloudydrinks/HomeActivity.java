package com.example.cloudydrinks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.cloudydrinks.adapter.AdapterViewPager;
import com.example.cloudydrinks.fragment.FragmentFavorite;
import com.example.cloudydrinks.fragment.FragmentHome;
import com.example.cloudydrinks.fragment.FragmentProfile;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    private ViewPager2 pagerMain;
    private BottomNavigationView bottomNav;
    private ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        pagerMain = findViewById(R.id.pagerMain);
        bottomNav = findViewById(R.id.bottomNav);

        fragmentArrayList.add(new FragmentHome());
        fragmentArrayList.add(new FragmentFavorite());
        fragmentArrayList.add(new FragmentProfile());

        AdapterViewPager adapterViewPager = new AdapterViewPager(HomeActivity.this, fragmentArrayList);
        // set Adapter
        pagerMain.setAdapter(adapterViewPager);
        pagerMain.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position == 0) {
                    bottomNav.setSelectedItemId(R.id.itHome);
                } else if (position == 1) {
                    bottomNav.setSelectedItemId(R.id.itFavorite);
                } else if (position == 2) {
                    bottomNav.setSelectedItemId(R.id.itProfile);
                }
            }
        });
        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.itHome) {
                    pagerMain.setCurrentItem(0);
                } else if (item.getItemId() == R.id.itFavorite) {
                    pagerMain.setCurrentItem(1);
                } else if (item.getItemId() == R.id.itProfile) {
                    pagerMain.setCurrentItem(2);
                }
                return true;
            }
        });
    }
}