package com.example.cloudydrinks.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cloudydrinks.R;
import com.example.cloudydrinks.adapter.PaymentDetailAdapter;
import com.example.cloudydrinks.adapter.PopularArticleAdapter;
import com.example.cloudydrinks.model.CartModel;
import com.example.cloudydrinks.model.Contact;
import com.example.cloudydrinks.model.Product;
import com.example.cloudydrinks.my_interface.IClickItemListener;
import com.example.cloudydrinks.utils.NumberCurrencyFormatUtil;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class PaymentActivity extends AppCompatActivity {
    private RecyclerView paymentRecyclerView;
    private ArrayList<CartModel> cartList;
    private DatabaseReference databaseReference;
    private String userPhoneNumber, contactAddress;
    private PaymentDetailAdapter adapter;
    private TextView totalPriceTV, addressTV;
    private int totalPrice;
    private LinearLayout addressLayout;
    private MaterialButton orderBtn;
    private ImageView addAddressImage;
    private Contact contact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        userPhoneNumber = getIntent().getStringExtra("userPhoneNumber");
        totalPriceTV = findViewById(R.id.totalPriceTV);
        addressLayout = findViewById(R.id.addressLayout);
        orderBtn = findViewById(R.id.orderBtn);
        addressTV = findViewById(R.id.addressTV);
        addAddressImage = findViewById(R.id.addAddressImage);

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

        addressLayout.setOnClickListener(addAddressListener);

        loadAddress();
        generatePaymentDetail();
        orderBtn.setOnClickListener(orderClickListener);

    }

    public View.OnClickListener orderClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    private void generatePaymentDetail() {
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(PaymentActivity.this);
        paymentRecyclerView = findViewById(R.id.paymentDetailRV);
        paymentRecyclerView.setLayoutManager(gridLayoutManager);

        cartList = new ArrayList<>();
        totalPrice = 0;

        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userPhoneNumber).child("Cart");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartList.clear();
                if(snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        CartModel cartModel = dataSnapshot.getValue(CartModel.class);
                        totalPrice += cartModel.getTotalPrice();
                        cartList.add(cartModel);
                    }
                    totalPriceTV.setText(NumberCurrencyFormatUtil.numberCurrencyFormat(String.valueOf(totalPrice)));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        adapter = new PaymentDetailAdapter(cartList);
        paymentRecyclerView.setAdapter(adapter);
    }
    private View.OnClickListener addAddressListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(PaymentActivity.this, AddressActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("contactObj", contact);
            intent.putExtras(bundle);
            intent.putExtra("userPhoneNumber", userPhoneNumber);
            startActivity(intent);
        }
    };
    private void loadAddress() {
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userPhoneNumber).child("contact_address");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    contact = snapshot.getValue(Contact.class);
                    contactAddress = contact.getFullName() + " - " + contact.getPhoneNo() + "\n"
                            + contact.getStreet() + ", " + contact.getWard() + ", "
                            + contact.getDistrict() + ", " + contact.getCity();
                    addressTV.setText(contactAddress);
                    addAddressImage.setVisibility(View.GONE);
                } else {
                    contactAddress = "Thêm địa chỉ";
                    addressTV.setText(contactAddress);
                    addAddressImage.setVisibility(View.VISIBLE);

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}