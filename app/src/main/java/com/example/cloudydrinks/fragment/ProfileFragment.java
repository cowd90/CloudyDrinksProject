package com.example.cloudydrinks.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cloudydrinks.R;
import com.example.cloudydrinks.activity.ConfirmPasswordActivity;
import com.example.cloudydrinks.activity.LoginActivity;
import com.example.cloudydrinks.adapter.CartAdapter;
import com.example.cloudydrinks.local_data.DataLocalManager;
import com.example.cloudydrinks.model.User;
import com.example.cloudydrinks.utils.NumberCurrencyFormatUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {
    private final static String USER = "users";
    private final static String USER_INFO = "user_info";
    private TextView userNameTV, userPhoneTV;

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
        RelativeLayout changePassword = mView.findViewById(R.id.changePassword);
        LinearLayout logOutLayout = mView.findViewById(R.id.logOutLayout);

        displayUserData();
        changePassword.setOnClickListener(changePasswordListener);
        logOutLayout.setOnClickListener(logOutListener);


        return mView;
    }

    public void displayUserData() {

        String userId = DataLocalManager.getUserId();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(USER).child(userId).child(USER_INFO);
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
    public View.OnClickListener changePasswordListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), ConfirmPasswordActivity.class);
            startActivity(intent);
        }
    };
    public View.OnClickListener logOutListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            new AlertDialog.Builder(getActivity())
                    .setMessage("Bạn có chắc chắn muốn đăng xuất?")
                    .setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FirebaseAuth mAuth = FirebaseAuth.getInstance ();
                            mAuth.signOut();

                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("Hủy", null).show();
        }
    };
}