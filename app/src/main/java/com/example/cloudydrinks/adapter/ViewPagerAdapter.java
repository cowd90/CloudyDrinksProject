package com.example.cloudydrinks.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.cloudydrinks.fragment.CancelOrderFragment;
import com.example.cloudydrinks.fragment.DeliveredFragment;
import com.example.cloudydrinks.fragment.DeliveringFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new DeliveringFragment();
            case 1:
                return new DeliveredFragment();
            case 2:
                return new CancelOrderFragment();
            default:
                return new DeliveringFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}