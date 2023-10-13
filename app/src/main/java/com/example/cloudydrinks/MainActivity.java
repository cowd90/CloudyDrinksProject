package com.example.cloudydrinks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private EditText passwordET, phoneNumberET;
    private boolean passwordShowing = false;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private MaterialButton loginBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tv_signUp = findViewById(R.id.sign_up);
        TextView tv_forgetpwd = findViewById(R.id.forget_pwd);
        ImageView passwordIcon = findViewById(R.id.passwordIcon);
        passwordET = findViewById(R.id.passwordET);
        phoneNumberET = findViewById(R.id.phoneNumberET);
        loginBtn = findViewById(R.id.loginBtn);
        progressBar = findViewById(R.id.progressbar);

        mAuth = FirebaseAuth.getInstance();

        phoneNumberET.addTextChangedListener(textWatcher);
        passwordET.addTextChangedListener(textWatcher);

        passwordIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Check if password is showing or not
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

        tv_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });

        tv_forgetpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ForgetPasswordActivity.class));
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String username = phoneNumberET.getText().toString().trim();
                String password = passwordET.getText().toString().trim();

                if (TextUtils.isEmpty(username)) {
                    phoneNumberET.setError("Vui lòng không bỏ trống");
                    phoneNumberET.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    passwordET.setError("Vui lòng xác nhận laị mật khẩu");
                    passwordIcon.requestFocus();
                    return;
                }

                if (password.length() < 8) {
                    passwordET.setError("Mật khẩu phải dài ít nhất 8 ký tự");
                    passwordET.requestFocus();
                    return;
                }

//            mAuth.signInWithEmailAndPassword(username, password)
//                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//                            if (task.isSuccessful()) {
//                                progressBar.setVisibility(View.GONE);
//                                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
//                                finish();
//                            } else {
//                                progressBar.setVisibility(View.GONE);
//                                Toast.makeText(MainActivity.this, "Tên đăng nhập hoặc mật khẩu không đúng", Toast.LENGTH_LONG).show();
//                            }
//                    });
//                        }
                if (validateUsername() && validatePassword()){
                    checkUser();
                }
            }
        });
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        FirebaseUser user = mAuth.getCurrentUser();
//        if (user != null) {
//            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
//            finish();
//        }
//
//    }
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!phoneNumberET.getText().toString().isEmpty() && !passwordET.getText().toString().isEmpty()) {
                loginBtn.setEnabled(true);
                loginBtn.setClickable(true);
                loginBtn.setBackgroundColor(Color.parseColor("#FB6E64"));
                loginBtn.setTextColor(Color.WHITE);
            } else {
                loginBtn.setTextColor(Color.parseColor("#BCBCBC"));
                loginBtn.setBackgroundColor(Color.parseColor("#DCDCDC"));
                loginBtn.setEnabled(false);
                loginBtn.setClickable(false);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    public Boolean validateUsername(){
        String val = phoneNumberET.getText().toString();
        if (val.isEmpty()){
            phoneNumberET.setError("Vui lòng nhập tên đăng nhập");
            return false;
        } else {
            phoneNumberET.setError(null);
            return true;
        }
    }

    public Boolean validatePassword(){
        String val = passwordET.getText().toString();
        if (val.isEmpty()){
            passwordET.setError("Vui lòng nhập mật khẩu");
            return false;
        } else {
            passwordET.setError(null);
            return true;
        }
    }

    public void checkUser(){
        String userPhoneNumber = phoneNumberET.getText().toString().trim();
        String userPassword = passwordET.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query username = reference.orderByChild("phoneNumber").equalTo(userPhoneNumber);

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Check if username, email or phone number is exist in firebase database
                if (snapshot.hasChild(userPhoneNumber)){
                    // get password of user from firebase database and match it with user password input
                    phoneNumberET.setError(null);
                    String passwordFromDB = snapshot.child(userPhoneNumber).child("password").getValue(String.class);

                    if (passwordFromDB.equals(userPassword)){
                        phoneNumberET.setError(null);

                        //Pass the data using intent

                        String usernameFromDB = snapshot.child(userPhoneNumber).child("username").getValue(String.class);

                        progressBar.setVisibility(View.GONE);
                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);

                        intent.putExtra("username", usernameFromDB);

                        startActivity(intent);
                    } else {
                        passwordET.setError("Mật khẩu không đúng");
                        passwordET.requestFocus();
                    }
                } else {
                    progressBar.setVisibility(View.GONE);
                    phoneNumberET.setError("Tên đăng nhập không tồn tại");
                    phoneNumberET.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        username.addListenerForSingleValueEvent(valueEventListener);
    }
}