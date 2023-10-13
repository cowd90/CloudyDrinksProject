package com.example.cloudydrinks;

import androidx.appcompat.app.AppCompatActivity;

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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SetNewPassword extends AppCompatActivity {
    private EditText passwordET, conPasswordET;
    private ImageView passwordIcon, conPasswordIcon;
    private Button setPassWordBtn;
    private boolean passwordShowing = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_new_password);

        passwordET = findViewById(R.id.passwordET);
        conPasswordET = findViewById(R.id.conPasswordET);
        passwordIcon = findViewById(R.id.passwordIcon);
        conPasswordIcon = findViewById(R.id.conPasswordIcon);
        setPassWordBtn = findViewById(R.id.setPasswordBtn);

        String newPassword = passwordET.getText().toString().trim();
        String conNewPassword = conPasswordET.getText().toString().trim();

        setPassWordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (newPassword.length() < 8) {
                    passwordET.setError("Mật khẩu phải dài ít nhất 8 ký tự");
                    passwordET.requestFocus();
                } else {
                    passwordET.setError(null);
                }

                if (conNewPassword.length() < 8) {
                    conPasswordET.setError("Mật khẩu phải dài ít nhất 8 ký tự");
                    conPasswordET.requestFocus();
                } else {
                    conPasswordET.setError(null);
                }

                if (newPassword.equals(conNewPassword)) {
                    Toast.makeText(SetNewPassword.this, "Cập nhật mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                    conPasswordET.setError(null);
                    String phoneNo = getIntent().getStringExtra("phoneNo");

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
                    reference.child(phoneNo).child("password").setValue(newPassword);


                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                } else {
                    conPasswordET.setError("Mật khẩu không khớp");
                    conPasswordET.requestFocus();
                }
            }
        });

        passwordET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!passwordET.getText().toString().trim().isEmpty()) {
                    setPassWordBtn.setEnabled(true);
                    setPassWordBtn.setClickable(true);
                    setPassWordBtn.setBackgroundColor(Color.parseColor("#FB6E64"));
                    setPassWordBtn.setTextColor(Color.WHITE);
                } else {
                    setPassWordBtn.setTextColor(Color.parseColor("#BCBCBC"));
                    setPassWordBtn.setBackgroundColor(Color.parseColor("#DCDCDC"));
                    setPassWordBtn.setEnabled(false);
                    setPassWordBtn.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // check password showing or not
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
    }
}