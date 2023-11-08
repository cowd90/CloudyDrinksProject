package com.example.cloudydrinks.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cloudydrinks.R;
import com.example.cloudydrinks.adapter.CartAdapter;
import com.example.cloudydrinks.local_data.DataLocalManager;
import com.example.cloudydrinks.model.CartModel;
import com.example.cloudydrinks.model.Contact;
import com.example.cloudydrinks.model.Product;
import com.example.cloudydrinks.utils.NumberCurrencyFormatUtil;
import com.example.cloudydrinks.utils.RandomKey;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.LongFunction;

public class AddressActivity extends AppCompatActivity {
    private String selectedDistrict, selectedWard;
    private Spinner districtSpinner, wardSpinner;
    private ArrayAdapter<CharSequence> districtAdapter, wardAdapter;
    private MaterialButton submitBtn, deleteAddressBtn;
    private EditText fullNameET, phoneNumberET, streetET;
    private String userId;
    private DatabaseReference databaseReference;
    private Contact address;
    private String fullName, phoneNo, district, ward, street;
    private String addressId;
    private int wardPosition;
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
        deleteAddressBtn = findViewById(R.id.deleteAddressBtn);

        // get user
        userId = DataLocalManager.getUserId();

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

        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId);
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
        deleteAddressBtn.setOnClickListener(deleteAddressListener);
        submitBtn.setOnClickListener(onSubmitListener);
    }
    public void loadAddressData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }

        address = (Contact) bundle.get("contactObj");

        if (address != null) {
            fullName = address.getFullName();
            phoneNo = address.getPhoneNo();
            district = address.getDistrict();
            ward = address.getWard();
            street = address.getStreet();

            getWardAdapterResource();

            fullNameET.setText(fullName);
            phoneNumberET.setText(phoneNo);
            streetET.setText(street);

            // set data for district spinner
            int districtPosition = districtAdapter.getPosition(district);
            districtSpinner.setSelection(districtPosition);

            // set data for ward spinner
            wardPosition = wardAdapter.getPosition(ward);
            wardSpinner.setSelection(wardPosition);
        }
    }
    public void getWardAdapterResource() {
        districtSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDistrict = districtSpinner.getSelectedItem().toString();

                int parentId = parent.getId();
                if (parentId == R.id.district) {
                    switch (district) {
                        case "Chọn Quận/Huyện":
                            wardAdapter = ArrayAdapter.createFromResource(parent.getContext(), R.array.arr_default_ward, R.layout.spinner_layout);
                            wardPosition = wardAdapter.getPosition(ward);
                            break;
                        case "TP Thủ Đức":
                            wardAdapter = ArrayAdapter.createFromResource(parent.getContext(), R.array.arr_thu_duc, R.layout.spinner_layout);
                            wardPosition = wardAdapter.getPosition(ward);
                            break;
                        case "Quận 1":
                            wardAdapter = ArrayAdapter.createFromResource(parent.getContext(), R.array.arr_district_1, R.layout.spinner_layout);
                            wardPosition = wardAdapter.getPosition(ward);
                            break;
                        case "Quận 3":
                            wardAdapter = ArrayAdapter.createFromResource(parent.getContext(), R.array.arr_district_3, R.layout.spinner_layout);
                            wardPosition = wardAdapter.getPosition(ward);
                            break;
                        case "Quận 4":
                            wardAdapter = ArrayAdapter.createFromResource(parent.getContext(), R.array.arr_district_4, R.layout.spinner_layout);
                            wardPosition = wardAdapter.getPosition(ward);
                            break;
                        case "Quận 5":
                            wardAdapter = ArrayAdapter.createFromResource(parent.getContext(), R.array.arr_district_5, R.layout.spinner_layout);
                            wardPosition = wardAdapter.getPosition(ward);
                            break;
                        case "Quận 6":
                            wardAdapter = ArrayAdapter.createFromResource(parent.getContext(), R.array.arr_district_6, R.layout.spinner_layout);
                            wardPosition = wardAdapter.getPosition(ward);
                            break;
                        case "Quận 7":
                            wardAdapter = ArrayAdapter.createFromResource(parent.getContext(), R.array.arr_district_7, R.layout.spinner_layout);
                            wardPosition = wardAdapter.getPosition(ward);
                            break;
                        case "Quận 8":
                            wardAdapter = ArrayAdapter.createFromResource(parent.getContext(), R.array.arr_district_8, R.layout.spinner_layout);
                            wardPosition = wardAdapter.getPosition(ward);
                            break;
                        case "Quận 10":
                            wardAdapter = ArrayAdapter.createFromResource(parent.getContext(), R.array.arr_district_10, R.layout.spinner_layout);
                            wardPosition = wardAdapter.getPosition(ward);
                            break;
                        case "Quận 11":
                            wardAdapter = ArrayAdapter.createFromResource(parent.getContext(), R.array.arr_district_11, R.layout.spinner_layout);
                            wardPosition = wardAdapter.getPosition(ward);
                            break;
                        case "Quận 12":
                            wardAdapter = ArrayAdapter.createFromResource(parent.getContext(), R.array.arr_district_12, R.layout.spinner_layout);
                            wardPosition = wardAdapter.getPosition(ward);
                            break;
                        case "Quận Bình Tân":
                            wardAdapter = ArrayAdapter.createFromResource(parent.getContext(), R.array.arr_district_binh_tan, R.layout.spinner_layout);
                            wardPosition = wardAdapter.getPosition(ward);
                            break;
                        case "Quận Bình Thạnh":
                            wardAdapter = ArrayAdapter.createFromResource(parent.getContext(), R.array.arr_district_binh_thanh, R.layout.spinner_layout);
                            wardPosition = wardAdapter.getPosition(ward);
                            break;
                        case "Quận Gò Vấp":
                            wardAdapter = ArrayAdapter.createFromResource(parent.getContext(), R.array.arr_district_go_vap, R.layout.spinner_layout);
                            wardPosition = wardAdapter.getPosition(ward);
                            break;
                        case "Quận Phú Nhuận":
                            wardAdapter = ArrayAdapter.createFromResource(parent.getContext(), R.array.arr_district_phu_nhuan, R.layout.spinner_layout);
                            wardPosition = wardAdapter.getPosition(ward);
                            break;
                        case "Quận Tân Bình":
                            wardAdapter = ArrayAdapter.createFromResource(parent.getContext(), R.array.arr_district_tan_binh, R.layout.spinner_layout);
                            wardPosition = wardAdapter.getPosition(ward);
                            break;
                        case "Quận Tân Phú":
                            wardAdapter = ArrayAdapter.createFromResource(parent.getContext(), R.array.arr_district_tan_phu, R.layout.spinner_layout);
                            wardPosition = wardAdapter.getPosition(ward);
                            break;
                        case "Huyện Bình Chánh":
                            wardAdapter = ArrayAdapter.createFromResource(parent.getContext(), R.array.arr_district_can_gio, R.layout.spinner_layout);
                            wardPosition = wardAdapter.getPosition(ward);
                            break;
                        case "Huyện Cần Giờ":
                            wardAdapter = ArrayAdapter.createFromResource(parent.getContext(), R.array.arr_district_tan_phu, R.layout.spinner_layout);
                            wardPosition = wardAdapter.getPosition(ward);
                            break;
                        case "Huyện Củ Chi":
                            wardAdapter = ArrayAdapter.createFromResource(parent.getContext(), R.array.arr_district_cu_chi, R.layout.spinner_layout);
                            wardPosition = wardAdapter.getPosition(ward);
                            break;
                        case "Huyện Hóc Môn":
                            wardAdapter = ArrayAdapter.createFromResource(parent.getContext(), R.array.arr_district_hoc_mon, R.layout.spinner_layout);
                            wardPosition = wardAdapter.getPosition(ward);
                            break;
                        case "Huyện Nhà Bè":
                            wardAdapter = ArrayAdapter.createFromResource(parent.getContext(), R.array.arr_district_nha_be, R.layout.spinner_layout);
                            wardPosition = wardAdapter.getPosition(ward);
                            break;
                        default:
                            break;
                    }
                    wardAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
                    wardSpinner.setAdapter(wardAdapter);
                    wardSpinner.setSelection(wardPosition);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    public void createOrUpdateAddress() {
        addressId = RandomKey.generateKey();

        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId).child("contact_address");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (address != null) {
                    String path = address.getAddressId();

                    if (snapshot.hasChild(path)) { // update address
                        Contact contact = snapshot.child(path).getValue(Contact.class);
                        contact.setAddressId(address.getAddressId());
                        contact.setFullName(fullNameET.getText().toString().trim());
                        contact.setPhoneNo(phoneNumberET.getText().toString().trim());
                        contact.setStreet(streetET.getText().toString().trim());
                        contact.setCity("TP Hồ Chí Minh");
                        contact.setDistrict(selectedDistrict);
                        contact.setWard(selectedWard);

                        databaseReference.child(path).setValue(contact).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(AddressActivity.this, "Cập nhật chỉ thành công", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(AddressActivity.this, AddressSelection.class);
                                intent.putExtra("addressId", address.getAddressId());
                                startActivity(intent);
                            }
                        });
                    }
                } else { // add new address
                    Contact contact = new Contact();
                    contact.setAddressId(addressId);
                    contact.setFullName(fullNameET.getText().toString().trim());
                    contact.setPhoneNo(phoneNumberET.getText().toString().trim());
                    contact.setStreet(streetET.getText().toString().trim());
                    contact.setCity("TP Hồ Chí Minh");
                    contact.setDistrict(selectedDistrict);
                    contact.setWard(selectedWard);

                    databaseReference.child(addressId).setValue(contact).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(AddressActivity.this, "Thêm địa chỉ thành công", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AddressActivity.this, AddressSelection.class);
                            intent.putExtra("addressId", addressId);
                            startActivity(intent);
                        }
                    });
                }

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
    public View.OnClickListener deleteAddressListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new AlertDialog.Builder(AddressActivity.this)
                    .setMessage("Bạn có chắc chắn muốn địa chỉ này?")
                    .setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId).child("contact_address");
                            databaseReference.child(address.getAddressId()).removeValue();
                            finish();
                        }
                    })
                    .setNegativeButton("Hủy", null).show();
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