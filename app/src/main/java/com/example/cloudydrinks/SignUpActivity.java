package com.example.cloudydrinks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cloudydrinks.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    private Button btn_nextStep;
    private EditText passwordET, conPasswordET, usernameET, phoneNumberET;
    private CheckBox policyInput;
    private ImageView passwordIcon, conPasswordIcon;
    private boolean passwordShowing = false;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private static final String USER = "users";
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        TextView tv_signIn = findViewById(R.id.sign_in);
        btn_nextStep = findViewById(R.id.btn_next_verify_phone_number);
        policyInput = findViewById(R.id.cb_policy);
        passwordET = findViewById(R.id.passwordET);
        conPasswordET = findViewById(R.id.conPasswordET);
        passwordIcon = findViewById(R.id.passwordIcon);
        conPasswordIcon = findViewById(R.id.conPasswordIcon);
        usernameET = findViewById(R.id.usernameET);
        phoneNumberET = findViewById(R.id.phoneNumberET);
        progressBar = findViewById(R.id.progressbar);

        // set up toolbar of sign up activity
        Toolbar toolbar = findViewById(R.id.sign_up_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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

        // switch to the sign in screen
        tv_signIn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.putExtra("phoneNumberInput", phoneNumberET.getText().toString().trim());
                startActivity(i);
            }
        });

        // Check if all the input fields are filled
        usernameET.addTextChangedListener(textWatcher);
        phoneNumberET.addTextChangedListener(textWatcher);
        passwordET.addTextChangedListener(textWatcher);
        conPasswordET.addTextChangedListener(textWatcher);
        policyInput.setOnCheckedChangeListener(onCheckedChangeListener);

        btn_nextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                registerUser();
            }
        });

    }
    private void registerUser() {

        database = FirebaseDatabase.getInstance();
        reference = database.getReference(USER);

        String username = usernameET.getText().toString().trim();
        String phoneNumber = phoneNumberET.getText().toString().trim();
        String password = passwordET.getText().toString().trim();
        String conPassword = conPasswordET.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            usernameET.setError("Vui lòng điền tên đăng nhập");
            usernameET.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(phoneNumber)) {
            phoneNumberET.setError("Vui lòng nhập số điện thoại của bạn");
            phoneNumberET.requestFocus();
            return;
        }

        if (phoneNumber.length() < 9) {
            passwordET.setError("Số điện thoại không hợp lệ");
            passwordET.requestFocus();
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

        if (TextUtils.isEmpty(conPassword)) {
            conPasswordET.setError("Vui lòng xác nhận laị mật khẩu");
            conPasswordET.requestFocus();
            return;
        }

        if (!conPassword.equals(password)) {
            conPasswordET.setError("Mật khẩu không trùng khớp");
            conPasswordET.requestFocus();
            return;
        }

        reference.child(USER).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(phoneNumber)) {
                    phoneNumberET.setError("Số điện thoại đã được đăng ký");
                    phoneNumberET.requestFocus();
                } else {
                    Intent intent = new Intent(SignUpActivity.this, VerifyOTP.class);
                    intent.putExtra("username", username);
                    intent.putExtra("phoneNo", phoneNumber);
                    intent.putExtra("password", password);
                    intent.putExtra("whatToDo", "Create new user");
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!usernameET.getText().toString().isEmpty() && !phoneNumberET.getText().toString().isEmpty()
                && !passwordET.getText().toString().isEmpty() && !conPasswordET.getText().toString().isEmpty()) {
                    policyInput.setEnabled(true);
            } else {
                policyInput.setChecked(false);
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
                btn_nextStep.setTextColor(Color.parseColor("#BCBCBC"));
                btn_nextStep.setBackgroundColor(Color.parseColor("#DCDCDC"));
                btn_nextStep.setEnabled(false);
                btn_nextStep.setClickable(false);
            }
        }
    };

}