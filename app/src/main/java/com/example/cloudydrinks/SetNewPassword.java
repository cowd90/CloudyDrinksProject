package com.example.cloudydrinks;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SetNewPassword extends AppCompatActivity {
    private EditText passwordInput;
    private Button confirmPasswordBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_new_password);

        passwordInput = findViewById(R.id.passwordET);
        confirmPasswordBtn = findViewById(R.id.conPasswordET);
        confirmPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                finish();
            }
        });

        passwordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!passwordInput.getText().toString().trim().isEmpty()) {
                    confirmPasswordBtn.setEnabled(true);
                    confirmPasswordBtn.setClickable(true);
                    confirmPasswordBtn.setBackgroundColor(Color.parseColor("#FB6E64"));
                    confirmPasswordBtn.setTextColor(Color.WHITE);
                } else {
                    confirmPasswordBtn.setTextColor(Color.parseColor("#BCBCBC"));
                    confirmPasswordBtn.setBackgroundColor(Color.parseColor("#DCDCDC"));
                    confirmPasswordBtn.setEnabled(false);
                    confirmPasswordBtn.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}