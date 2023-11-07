package com.example.cloudydrinks.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cloudydrinks.R;
import com.example.cloudydrinks.adapter.PaymentDetailAdapter;
import com.example.cloudydrinks.model.CartModel;
import com.example.cloudydrinks.model.Contact;
import com.example.cloudydrinks.model.Order;
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

        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }

        contact = (Contact) bundle.get("contactObj");

        addressLayout.setOnClickListener(addAddressListener);

        loadAddress();
        generatePaymentDetail();
        orderBtn.setOnClickListener(orderClickListener);

    }

    public View.OnClickListener orderClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            addToOrder();
            if (!addressTV.getText().toString().trim().equals("Chọn địa chỉ")) {
                Intent intent = new Intent(PaymentActivity.this, HomeActivity.class);
                intent.putExtra("userPhoneNumber", userPhoneNumber);
                startActivity(intent);
            } else {
                Toast.makeText(PaymentActivity.this, "Vui lòng chọn địa chỉ nhận hàng", Toast.LENGTH_SHORT).show();
            }
        }
    };
    public void addToOrder() {
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userPhoneNumber);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild("Cart")) {
                    cartList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.child("Cart").getChildren()) {
                        CartModel cartModel = dataSnapshot.getValue(CartModel.class);
                        cartList.add(cartModel);
                        databaseReference.child("Cart").removeValue();
                    }

                    for (CartModel model : cartList) {
                        Order order = new Order();
                        order.setProduct_id(model.getProduct_id());
                        order.setProduct_name(model.getProduct_name());
                        order.setQuantity(model.getQuantity());
                        order.setSize(model.getSize());
                        order.setProduct_price(model.getProduct_price());
                        order.setTotalPrice(model.getTotalPrice());
                        order.setContact(contact);
                        databaseReference.child("order").child("status_delivering").child(model.getProduct_name()+"_"+model.getSize()).setValue(order).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(PaymentActivity.this, "Đặt hàng thành công!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                    }
                }
                databaseReference.removeEventListener(this);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

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
    private final View.OnClickListener addAddressListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(PaymentActivity.this, AddressSelection.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("contactObj", contact);
            intent.putExtras(bundle);
            intent.putExtra("userPhoneNumber", userPhoneNumber);
            startActivity(intent);
        }
    };
    private void loadAddress() {
        if (contact != null) {
            contactAddress = contact.getFullName() + " - " + contact.getPhoneNo() + "\n"
                    + contact.getStreet() + ", " + contact.getWard() + ", "
                    + contact.getDistrict() + ", " + contact.getCity();
            addressTV.setText(contactAddress);
            addAddressImage.setVisibility(View.GONE);
        }else {
            contactAddress = "Thêm địa chỉ";
            addressTV.setText(contactAddress);
            addAddressImage.setVisibility(View.VISIBLE);
        }

    }
}