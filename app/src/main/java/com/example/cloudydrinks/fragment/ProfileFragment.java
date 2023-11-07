package com.example.cloudydrinks.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cloudydrinks.R;
import com.example.cloudydrinks.adapter.ProductListAdapter;
import com.example.cloudydrinks.model.Product;
import com.example.cloudydrinks.model.User;
import com.example.cloudydrinks.my_interface.IClickItemListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {
    private TextView userNameTV, userPhoneTV;
    private DatabaseReference databaseReference;
    private String userKey;
    public ProfileFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_profile, container, false);

        userNameTV = mView.findViewById(R.id.userNameTV);
        userPhoneTV = mView.findViewById(R.id.userPhoneTV);

        displayUserData();

        return mView;
    }

    public void displayUserData() {

        Bundle data = getArguments();

        if (data != null) {
            userKey = data.getString("userPhoneNo");
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userKey).child("user_info");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                Log.d("user", user.toString());

                userNameTV.setText(user.getUsername());
                userPhoneTV.setText(user.getPhoneNumber());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}