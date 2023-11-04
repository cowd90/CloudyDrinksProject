package com.example.cloudydrinks.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cloudydrinks.R;
import com.example.cloudydrinks.model.CartModel;
import com.example.cloudydrinks.model.Contact;
import com.example.cloudydrinks.model.Product;
import com.example.cloudydrinks.utils.NumberCurrencyFormatUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class AddressActivity extends AppCompatActivity {
    private String selectedDistrict, selectedWard;
    private Spinner districtSpinner, wardSpinner;
    private ArrayAdapter<CharSequence> districtAdapter, wardAdapter;
    private MaterialButton submitBtn;
    private EditText fullNameET, phoneNumberET, streetET;
    private String userPhoneNumber;
    private DatabaseReference databaseReference;
    private Contact contact;
    private int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        districtSpinner = findViewById(R.id.district);
        wardSpinner = findViewById(R.id.ward);
        submitBtn = findViewById(R.id.submitBtn);
        fullNameET = findViewById(R.id.fullNameET);
        phoneNumberET = findViewById(R.id.phoneNumberET);
        streetET = findViewById(R.id.streetET);

        // get user
        userPhoneNumber = getIntent().getStringExtra("userPhoneNumber");

        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }

        contact = (Contact) bundle.get("contactObj");

        districtAdapter = ArrayAdapter.createFromResource(this, R.array.district_arr, R.layout.spinner_layout);
        districtAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        districtSpinner.setAdapter(districtAdapter);

        districtSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDistrict = districtSpinner.getSelectedItem().toString();

                int parentId = parent.getId();
                if (parentId == R.id.district) {
                    switch (selectedDistrict) {
                        case "Chọn Quận/Huyện":
                            wardAdapter = ArrayAdapter.createFromResource(parent.getContext(), R.array.arr_default_ward, R.layout.spinner_layout);
                            break;
                        case "TP Thủ Đức":
                            wardAdapter = ArrayAdapter.createFromResource(parent.getContext(), R.array.arr_thu_duc, R.layout.spinner_layout);
                            break;
                        case "Quận 1":
                            wardAdapter = ArrayAdapter.createFromResource(parent.getContext(), R.array.arr_district_1, R.layout.spinner_layout);
                            break;
                        case "Quận 3":
                            wardAdapter = ArrayAdapter.createFromResource(parent.getContext(), R.array.arr_district_3, R.layout.spinner_layout);
                            break;
                        case "Quận 4":
                            wardAdapter = ArrayAdapter.createFromResource(parent.getContext(), R.array.arr_district_4, R.layout.spinner_layout);
                            break;
                        case "Quận 5":
                            wardAdapter = ArrayAdapter.createFromResource(parent.getContext(), R.array.arr_district_5, R.layout.spinner_layout);
                            break;
                        case "Quận 6":
                            wardAdapter = ArrayAdapter.createFromResource(parent.getContext(), R.array.arr_district_6, R.layout.spinner_layout);
                            break;
                        case "Quận 7":
                            wardAdapter = ArrayAdapter.createFromResource(parent.getContext(), R.array.arr_district_7, R.layout.spinner_layout);
                            break;
                        case "Quận 8":
                            wardAdapter = ArrayAdapter.createFromResource(parent.getContext(), R.array.arr_district_8, R.layout.spinner_layout);
                            break;
                        case "Quận 10":
                            wardAdapter = ArrayAdapter.createFromResource(parent.getContext(), R.array.arr_district_10, R.layout.spinner_layout);
                            break;
                        case "Quận 11":
                            wardAdapter = ArrayAdapter.createFromResource(parent.getContext(), R.array.arr_district_11, R.layout.spinner_layout);
                            break;
                        case "Quận 12":
                            wardAdapter = ArrayAdapter.createFromResource(parent.getContext(), R.array.arr_district_12, R.layout.spinner_layout);
                            break;
                        case "Quận Bình Tân":
                            wardAdapter = ArrayAdapter.createFromResource(parent.getContext(), R.array.arr_district_binh_tan, R.layout.spinner_layout);
                            break;
                        case "Quận Bình Thạnh":
                            wardAdapter = ArrayAdapter.createFromResource(parent.getContext(), R.array.arr_district_binh_thanh, R.layout.spinner_layout);
                            break;
                        case "Quận Gò Vấp":
                            wardAdapter = ArrayAdapter.createFromResource(parent.getContext(), R.array.arr_district_go_vap, R.layout.spinner_layout);
                            break;
                        case "Quận Phú Nhuận":
                            wardAdapter = ArrayAdapter.createFromResource(parent.getContext(), R.array.arr_district_phu_nhuan, R.layout.spinner_layout);
                            break;
                        case "Quận Tân Bình":
                            wardAdapter = ArrayAdapter.createFromResource(parent.getContext(), R.array.arr_district_tan_binh, R.layout.spinner_layout);
                            break;
                        case "Quận Tân Phú":
                            wardAdapter = ArrayAdapter.createFromResource(parent.getContext(), R.array.arr_district_tan_phu, R.layout.spinner_layout);
                            break;
                        case "Huyện Bình Chánh":
                            wardAdapter = ArrayAdapter.createFromResource(parent.getContext(), R.array.arr_district_can_gio, R.layout.spinner_layout);
                            break;
                        case "Huyện Cần Giờ":
                            wardAdapter = ArrayAdapter.createFromResource(parent.getContext(), R.array.arr_district_tan_phu, R.layout.spinner_layout);
                            break;
                        case "Huyện Củ Chi":
                            wardAdapter = ArrayAdapter.createFromResource(parent.getContext(), R.array.arr_district_cu_chi, R.layout.spinner_layout);
                            break;
                        case "Huyện Hóc Môn":
                            wardAdapter = ArrayAdapter.createFromResource(parent.getContext(), R.array.arr_district_hoc_mon, R.layout.spinner_layout);
                            break;
                        case "Huyện Nhà Bè":
                            wardAdapter = ArrayAdapter.createFromResource(parent.getContext(), R.array.arr_district_nha_be, R.layout.spinner_layout);
                            break;
                        default: break;
                    }
                    wardAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
                    wardSpinner.setAdapter(wardAdapter);
                    position = wardAdapter.getPosition(contact.getWard());

                    wardSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            selectedWard = wardSpinner.getSelectedItem().toString();

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        fullNameET.addTextChangedListener(textWatcher);
        phoneNumberET.addTextChangedListener(textWatcher);
        streetET.addTextChangedListener(textWatcher);

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

        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userPhoneNumber);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                loadAddressData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        submitBtn.setOnClickListener(onSubmitListener);
    }
    public void loadAddressData() {
        fullNameET.setText(contact.getFullName());
        phoneNumberET.setText(contact.getPhoneNo());
        streetET.setText(contact.getStreet());

        // set data for district spinner
        String districtValue = contact.getDistrict();
        int districtPosition = districtAdapter.getPosition(districtValue);
        districtSpinner.setSelection(districtPosition);

        // set data for ward spinner
        String wardValue = contact.getWard();
        int wardPosition = wardAdapter.getPosition(wardValue);
        wardSpinner.setSelection(position);
    }
    public void createOrUpdateAddress() {
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userPhoneNumber);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Contact contact = new Contact();
                contact.setFullName(fullNameET.getText().toString().trim());
                contact.setPhoneNo(phoneNumberET.getText().toString().trim());
                contact.setStreet(streetET.getText().toString().trim());
                contact.setCity("TP Hồ Chí Minh");
                contact.setDistrict(selectedDistrict);
                contact.setWard(selectedWard);
                databaseReference.child("contact_address").setValue(contact).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(AddressActivity.this, "Thêm địa chỉ thành công", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AddressActivity.this, PaymentActivity.class);
                        intent.putExtra("userPhoneNumber", userPhoneNumber);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public View.OnClickListener onSubmitListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (selectedDistrict.equals("Chọn Quận/Huyện") && selectedWard.equals("Chọn Phường/Xã")) {
                Toast.makeText(AddressActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            } else {
                createOrUpdateAddress();
            }
        }
    };
    public TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!fullNameET.getText().toString().isEmpty() && !phoneNumberET.getText().toString().isEmpty() && !streetET.getText().toString().isEmpty()) {
                submitBtn.setEnabled(true);
                submitBtn.setClickable(true);
                submitBtn.setBackgroundColor(Color.parseColor("#FB6E64"));
                submitBtn.setTextColor(Color.WHITE);
            } else {
                submitBtn.setEnabled(false);
                submitBtn.setClickable(false);
                submitBtn.setTextColor(Color.parseColor("#BCBCBC"));
                submitBtn.setBackgroundColor(Color.parseColor("#DCDCDC"));
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}