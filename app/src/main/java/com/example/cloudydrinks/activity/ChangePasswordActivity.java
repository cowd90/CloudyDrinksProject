package com.example.cloudydrinks.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.cloudydrinks.R;
import com.example.cloudydrinks.local_data.DataLocalManager;
import com.example.cloudydrinks.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class ChangePasswordActivity extends AppCompatActivity {
    private final static String USER = "users";
    private final static String USER_INFO = "user_info";
    private EditText passwordET, conPasswordET;
    private ImageView passwordIcon, conPasswordIcon;
    private boolean passwordShowing = false;
    private String password, conPassword;
    private Button doneBtn;
    private DatabaseReference databaseReference;
    private String userId;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        userId = DataLocalManager.getUserId();

        // set up toolbar of sign up activity
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        passwordET = findViewById(R.id.passwordET);
        conPasswordET = findViewById(R.id.conPasswordET);
        passwordIcon = findViewById(R.id.passwordIcon);
        conPasswordIcon = findViewById(R.id.conPasswordIcon);
        doneBtn = findViewById(R.id.doneBtn);

        password = passwordET.getText().toString().trim();
        conPassword = conPasswordET.getText().toString().trim();

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
        conPasswordIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passwordShowing) {
                    passwordShowing = false;

                    conPasswordET.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    conPasswordIcon.setImageResource(R.drawable.show_pwd);
                } else {
                    passwordShowing = true;

                    conPasswordET.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    conPasswordIcon.setImageResource(R.drawable.hide_pwd);
                }
                // move the cursor at last of the text
                conPasswordET.setSelection(conPasswordET.length());
            }
        });

        passwordET.addTextChangedListener(pwdTextWatcher);
        conPasswordET.addTextChangedListener(conPwdTextWatcher);

        doneBtn.setOnClickListener(changeNewPasswordListener);
    }

    public View.OnClickListener changeNewPasswordListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (password.equals(conPassword)) {
                databaseReference = FirebaseDatabase.getInstance().getReference(USER).child(userId).child(USER_INFO);
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        user = snapshot.getValue(User.class);
                        Log.d("password", password);
                        user.setPassword(password);

                        databaseReference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(ChangePasswordActivity.this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ChangePasswordActivity.this, HomeActivity.class));
                                finish();
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            } else if (password.length() < 8) {
                passwordET.setError("Mật khẩu phải dài ít nhất 8 ký tự");
                passwordET.requestFocus();
                return;
            } else if (conPassword.isEmpty()) {
                conPasswordET.setError("Không được bỏ trống");
                conPasswordET.requestFocus();
            } else {
                conPasswordET.setError("Mật khẩu không trùng khớp");
                conPasswordET.requestFocus();
            }
        }
    };

    public TextWatcher pwdTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!password.isEmpty()) {
                conPasswordET.setEnabled(true);
            } else {
                conPasswordET.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            password = s.toString();
        }
    };

    public TextWatcher conPwdTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!s.toString().isEmpty()) {
                doneBtn.setEnabled(true);
                doneBtn.setBackgroundColor(Color.parseColor("#FB6E64"));
                doneBtn.setTextColor(Color.WHITE);
            } else {
                doneBtn.setTextColor(Color.parseColor("#BCBCBC"));
                doneBtn.setBackgroundColor(Color.parseColor("#DCDCDC"));
                doneBtn.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            conPassword = s.toString();
        }
    };
}