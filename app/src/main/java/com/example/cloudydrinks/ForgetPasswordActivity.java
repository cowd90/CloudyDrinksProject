package com.example.cloudydrinks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.button.MaterialButton;

import java.util.Objects;

public class ForgetPasswordActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private MaterialButton button;
    private EditText input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        button = findViewById(R.id.btn_nextForgetPwd);
        input = findViewById(R.id.et_forgetPwdInput);

        // set up toolbar of sign up activity
        toolbar = findViewById(R.id.forget_pwd_toolbar);
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
                Intent i = new Intent(getApplicationContext(), SignUpVerificationActivity.class);
                i.putExtra("input", input.getText().toString().trim());
                startActivity(i);
            }
        });

        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!input.getText().toString().trim().isEmpty()) {
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