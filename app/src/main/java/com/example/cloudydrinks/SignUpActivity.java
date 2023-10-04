package com.example.cloudydrinks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class SignUpActivity extends AppCompatActivity {

    private TextView tv_signIn;
    private Toolbar toolbar;
    private Button btn_nextStep;
    private EditText mobileInput;
    private CheckBox policyInput;

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

        // Check if phone number input is filled and policy input is checked
        mobileInput.addTextChangedListener(textWatcher);
        policyInput.setOnCheckedChangeListener(onCheckedChangeListener);
        btn_nextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SignUpVerificationActivity.class);
                i.putExtra("input", mobileInput.getText().toString().trim());
                startActivity(i);
            }
        });

    }
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(!mobileInput.getText().toString().trim().isEmpty()) {
                policyInput.setEnabled(true);
            } else {
                policyInput.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    private CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (policyInput.isChecked()) {
                btn_nextStep.setEnabled(true);
                btn_nextStep.setClickable(true);
                btn_nextStep.setBackgroundColor(Color.parseColor("#FB6E64"));
                btn_nextStep.setTextColor(Color.WHITE);
            } else {
                btn_nextStep.setTextColor(Color.parseColor("#959595"));
                btn_nextStep.setBackgroundColor(Color.parseColor("#DCDBDB"));
                btn_nextStep.setEnabled(false);
                btn_nextStep.setClickable(false);
            }
        }
    };

}