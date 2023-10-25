package com.example.cloudydrinks.model;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.GridView;

import androidx.annotation.NonNull;

import com.example.cloudydrinks.adapter.CategoryAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

//public class ProductData extends AsyncTask<String, String, String> {
//    private Context context;
//    private GridView gridView;
//    private CategoryAdapter adapter;
//    private static ArrayList<Product> data;
//
//    public ProductData(Context context, GridView gridView) {
//        this.context = context;
//        this.gridView = gridView;
//    }
//
//    @Override
//    protected String doInBackground(String... strings) {
//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("products");
//        data = new ArrayList<>();
//
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists()) {
//                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                        Product product = dataSnapshot.getValue(Product.class);
//                        if (product.getCategory_id() == 0) {
//                            data.add(product);
//                        }
//                    }
//                }
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//        return null;
//    }
//
//    @Override
//    protected void onPostExecute(String s) {
//        super.onPostExecute(s);
//        adapter = new CategoryAdapter(data, context);
//        gridView.setAdapter(adapter);
//    }
//}
