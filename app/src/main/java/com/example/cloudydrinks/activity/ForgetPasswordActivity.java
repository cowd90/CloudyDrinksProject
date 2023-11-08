package com.example.cloudydrinks.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.cloudydrinks.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class ForgetPasswordActivity extends AppCompatActivity {
    private final static String USER = "users";
    private MaterialButton button;
    private EditText phoneNumberET;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        button = findViewById(R.id.btn_nextForgetPwd);
        phoneNumberET = findViewById(R.id.et_forgetPwdInput);
        progressBar = findViewById(R.id.progressbar);

        // set up toolbar of sign up activity
        Toolbar toolbar = findViewById(R.id.forget_pwd_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String userPhoneNumber = phoneNumberET.getText().toString().trim();

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference(USER);
                Query username = reference.orderByChild("phoneNumber").equalTo(userPhoneNumber);

                username.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //Check if user exists
                        if (snapshot.hasChild(userPhoneNumber)){
                            phoneNumberET.setError(null);

                            Intent intent = new Intent(getApplicationContext(), VerifyOTP.class);
                            intent.putExtra("phoneNo", userPhoneNumber);
                            intent.putExtra("whatToDo", "Update user data");
                            startActivity(intent);

                            progressBar.setVisibility(View.GONE);

                        } else {
                            progressBar.setVisibility(View.GONE);
                            phoneNumberET.setError("Số điện thoại chưa đăng ký");
                            phoneNumberET.requestFocus();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(ForgetPasswordActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        phoneNumberET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!phoneNumberET.getText().toString().trim().isEmpty()) {
                    button.setClickable(true);
                    button.setEnabled(true);
                    button.setTextColor(Color.WHITE);
                    button.setBackgroundColor(Color.parseColor("#FB6E64"));
                } else {
                    button.setTextColor(Color.parseColor("#BCBCBC"));
                    button.setBackgroundColor(Color.parseColor("#DCDCDC"));
                    button.setEnabled(false);
                    button.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}