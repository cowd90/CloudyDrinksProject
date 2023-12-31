package com.example.cloudydrinks.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.cloudydrinks.R;
import com.example.cloudydrinks.local_data.DataLocalManager;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SetNewPassword extends AppCompatActivity {
    private final static String USER = "users";
    private final static String USER_INFO = "user_info";
    private final static String PASSWORD = "password";
    private EditText passwordET, conPasswordET;
    private ImageView passwordIcon, conPasswordIcon;
    private Button setPassWordBtn;
    private boolean passwordShowing = false;
    private String newPassword, conNewPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_new_password);

        passwordET = findViewById(R.id.newPasswordET);
        conPasswordET = findViewById(R.id.conNewPasswordET);
        passwordIcon = findViewById(R.id.passwordIcon);
        conPasswordIcon = findViewById(R.id.conPasswordIcon);
        setPassWordBtn = findViewById(R.id.setPasswordBtn);

        passwordET.addTextChangedListener(textWatcher);
        conPasswordET.addTextChangedListener(textWatcher);

        String userId = DataLocalManager.getUserId();

        setPassWordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.equals(newPassword, conNewPassword)) {

                    FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
                    DatabaseReference reference = rootNode.getReference(USER);

                    reference.child(userId).child(USER_INFO).child(PASSWORD).setValue(newPassword);

                    Toast.makeText(SetNewPassword.this, "Cập nhật mật khẩu thành công!", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                } else {
                    conPasswordET.setError("Mật khẩu không khớp");
                    conPasswordET.requestFocus();
                }
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

    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(!s.toString().trim().isEmpty()) {
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
            newPassword = s.toString();
            conNewPassword = s.toString();
        }
    };
}