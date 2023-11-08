package com.example.cloudydrinks.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.cloudydrinks.R;
import com.example.cloudydrinks.local_data.DataLocalManager;
import com.example.cloudydrinks.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class ConfirmPasswordActivity extends AppCompatActivity {
    private final static String USER = "users";
    private final static String USER_INFO = "user_info";
    private ImageView passwordIcon;
    private EditText passwordET;
    private boolean passwordShowing = false;
    private DatabaseReference databaseReference;
    private String userId;
    private String currentPassword;
    private Button confirmOldPwdBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_password);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        userId = DataLocalManager.getUserId();

        passwordIcon = findViewById(R.id.passwordIcon);
        passwordET = findViewById(R.id.passwordET);
        confirmOldPwdBtn = findViewById(R.id.confirmOldPwdBtn);

        // hide/show password listener
        passwordIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passwordShowing) {
                    passwordShowing = false;

                    passwordET.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    passwordIcon.setImageResource(R.drawable.show_pwd);
                } else {
                    passwordShowing = true;

                    passwordET.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    passwordIcon.setImageResource(R.drawable.hide_pwd);
                }
                // move the cursor at last of the text
                passwordET.setSelection(passwordET.length());
            }
        });
        passwordET.addTextChangedListener(textWatcher);

        confirmOldPwdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference = FirebaseDatabase.getInstance().getReference(USER).child(userId).child(USER_INFO);
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);
                        if (user != null) {
                            if (user.getPassword().equals(currentPassword)) {
                                Intent intent = new Intent(ConfirmPasswordActivity.this, ChangePasswordActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(ConfirmPasswordActivity.this, "Mật khẩu không đúng. Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    public TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            currentPassword = s.toString();
            if (!currentPassword.isEmpty()) {
                confirmOldPwdBtn.setEnabled(true);
                confirmOldPwdBtn.setBackgroundColor(Color.parseColor("#FB6E64"));
                confirmOldPwdBtn.setTextColor(Color.WHITE);
            } else {
                confirmOldPwdBtn.setTextColor(Color.parseColor("#BCBCBC"));
                confirmOldPwdBtn.setBackgroundColor(Color.parseColor("#DCDCDC"));
                confirmOldPwdBtn.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}