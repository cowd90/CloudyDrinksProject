package com.example.cloudydrinks.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.example.cloudydrinks.R;
import com.example.cloudydrinks.model.Banner;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class BannerAdapter extends PagerAdapter {
    private Context mContext;;
    private List<Banner> mListBanner;

    public BannerAdapter(Context mContext, List<Banner> mListBanner) {
        this.mContext = mContext;
        this.mListBanner = mListBanner;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.display_banner_image, container, false);
        ImageView bannerImg = view.findViewById(R.id.bannerImage);

        Banner banner = mListBanner.get(position);
        if (banner != null) {
            Picasso.get().load(banner.getImg_url()).into(bannerImg);
        }

        // Add view to view group
        container.addView(view);

        return view;
    }

    @Override
    public int getCount() {
        if (mListBanner != null) {
            return mListBanner.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        // Remove view from view group
        container.removeView((View) object);
    }
}
