package com.example.cloudydrinks.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cloudydrinks.R;
import com.example.cloudydrinks.model.Category;
import com.example.cloudydrinks.my_interface.ICategoryClickListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CategoryIconAdapter extends RecyclerView.Adapter<CategoryIconAdapter.ViewHolder> {
    private ArrayList<Category> categories;
    private ICategoryClickListener iCategoryClickListener;

    public CategoryIconAdapter(ArrayList<Category> categories, ICategoryClickListener iCategoryClickListener) {
        this.categories = categories;
        this.iCategoryClickListener = iCategoryClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_category, parent, false);
        return new CategoryIconAdapter.ViewHolder(view);
    }

    @NonNull
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Category category = categories.get(position);

        if (category == null) {
            return;
        }

        Picasso.get().load(categories.get(position).getCategory_img()).into(holder.categoryPic);
        holder.categoryName.setText(categories.get(position).getCategory_name());
        holder.layoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iCategoryClickListener.onClickCategoryItem(category);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName;
        ImageView categoryPic;
        CardView layoutItem;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.categoryNameTV);
            categoryPic = itemView.findViewById(R.id.categoryImageView);
            layoutItem = itemView.findViewById(R.id.categoryIconLayout);
        }
    }
}
