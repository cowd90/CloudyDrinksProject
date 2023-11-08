package com.example.cloudydrinks.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.cloudydrinks.R;
import com.example.cloudydrinks.activity.ConfirmPasswordActivity;
import com.example.cloudydrinks.local_data.DataLocalManager;
import com.example.cloudydrinks.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {
    private final static String USER = "users";
    private final static String USER_INFO = "user_info";
    private TextView userNameTV, userPhoneTV;
    private DatabaseReference databaseReference;
    private String userId;
    private RelativeLayout changePassword;
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
        changePassword = mView.findViewById(R.id.changePassword);

        changePassword.setOnClickListener(changePasswordListener);

        displayUserData();

        return mView;
    }

    public View.OnClickListener changePasswordListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), ConfirmPasswordActivity.class);
            startActivity(intent);
        }
    };

    public void displayUserData() {

        userId = DataLocalManager.getUserId();

        databaseReference = FirebaseDatabase.getInstance().getReference(USER).child(userId).child(USER_INFO);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);

                userNameTV.setText(user.getUsername());
                userPhoneTV.setText(user.getPhoneNumber());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}