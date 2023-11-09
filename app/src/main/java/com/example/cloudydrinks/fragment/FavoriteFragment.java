package com.example.cloudydrinks.fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.cloudydrinks.R;
import com.example.cloudydrinks.adapter.FavoriteListAdapter;
import com.example.cloudydrinks.adapter.RecyclerViewItemTouchHelper;
import com.example.cloudydrinks.local_data.DataLocalManager;
import com.example.cloudydrinks.model.Product;
import com.example.cloudydrinks.my_interface.ItemTouchHelperListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class FavoriteFragment extends Fragment implements ItemTouchHelperListener {
    private final static String USER = "users";
    private final static String WISH_LIST = "wishlist";
    private ArrayList<Product> productList;
    private RecyclerView favRecyclerview;
    private FavoriteListAdapter adapter;
    private DatabaseReference databaseReference;
    private String myKey;
    private FrameLayout rootView;
    public FavoriteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_favorite, container, false);

        rootView = mView.findViewById(R.id.root_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        favRecyclerview = mView.findViewById(R.id.favListRV);
        favRecyclerview.setLayoutManager(linearLayoutManager);

        generateDrinkList();
        favRecyclerview.setAdapter(adapter);

        ItemTouchHelper.SimpleCallback simpleCallback = new RecyclerViewItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(favRecyclerview);

        return mView;
    }
    private void generateDrinkList() {

        myKey = DataLocalManager.getUserId();

        productList = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference(USER).child(myKey).child(WISH_LIST);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    productList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Product product = dataSnapshot.getValue(Product.class);
                        productList.add(product);
                    }
                } else {
                    favRecyclerview.setVisibility(View.GONE);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        adapter = new FavoriteListAdapter(productList);
        favRecyclerview.setAdapter(adapter);
    }

    @Override
    public void onSwipe(RecyclerView.ViewHolder viewHolder) {
        if (viewHolder instanceof FavoriteListAdapter.ViewHolder) {
            String productName = productList.get(viewHolder.getAdapterPosition()).getProduct_name();

            final Product productDelete = productList.get(viewHolder.getAdapterPosition());
            final int indexDelete = viewHolder.getAdapterPosition();

            // remove item
            adapter.removeItem(indexDelete);
            databaseReference = FirebaseDatabase.getInstance().getReference(USER).child(myKey).child(WISH_LIST).child(productName);
            databaseReference.removeValue();

            Snackbar snackbar = Snackbar.make(rootView, "Đã xóa " + productName + " khỏi danh mục yêu thích", Snackbar.LENGTH_LONG);
            snackbar.setAction("Khôi phục", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapter.restoreItem(productDelete, indexDelete);
                    if (indexDelete == 0 || indexDelete == productList.size() - 1) {
                        favRecyclerview.scrollToPosition(indexDelete);
                    }
                    databaseReference = FirebaseDatabase.getInstance().getReference(USER).child(myKey).child(WISH_LIST).child(productName);
                    databaseReference.setValue(productDelete);
                }
            });
            snackbar.setActionTextColor(Color.parseColor("#BA704F"));
            snackbar.show();
        }
    }
}