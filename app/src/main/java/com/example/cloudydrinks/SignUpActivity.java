package com.example.cloudydrinks;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    private TextView tv_signIn;
    private Toolbar toolbar;
    private Button btn_nextStep;
    private EditText mobileInput;
    private CheckBox policyInput;
    String phoneNumber;
    boolean isTick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        tv_signIn = findViewById(R.id.sign_in);
        btn_nextStep = findViewById(R.id.btn_next_verify_phone_number);
        mobileInput = findViewById(R.id.et_mobile_input);
        policyInput = findViewById(R.id.cb_policy);

        // set up toolbar of sign up activity
        toolbar = findViewById(R.id.sign_up_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        tv_signIn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });

        // Check if phone number input, terms of service and policies is filled

        btn_nextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignUpActivity.this, SignUpVerificationActivity.class);
                i.putExtra("mobile", mobileInput.getText().toString());
                startActivity(i);
            }
        });
    }
   private CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
       @Override
       public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
           if (isChecked) {
               
           }
       }
   };
}