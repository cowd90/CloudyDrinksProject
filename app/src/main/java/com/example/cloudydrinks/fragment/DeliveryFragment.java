package com.example.cloudydrinks.fragment;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cloudydrinks.R;
import com.example.cloudydrinks.adapter.CartAdapter;
import com.example.cloudydrinks.adapter.ViewPagerAdapter;
import com.example.cloudydrinks.model.CartModel;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class DeliveryFragment extends Fragment {
    private RecyclerView recyclerView;
    private View mView;
    private CartAdapter cartAdapter;
    private ArrayList<CartModel> cartList;
    private DatabaseReference databaseReference;
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private ViewPagerAdapter viewPagerAdapter;
    public DeliveryFragment() {
        // Required empty public constructor
    }
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_delivery, container, false);

        tabLayout = mView.findViewById(R.id.tabLayout);
        viewPager2 = mView.findViewById(R.id.viewPager);

        viewPagerAdapter = new ViewPagerAdapter(getActivity());
        viewPager2.setAdapter(viewPagerAdapter);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
            }
        });

        return mView;
    }
}