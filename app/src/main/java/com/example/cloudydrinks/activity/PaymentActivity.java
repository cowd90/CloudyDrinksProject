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
import com.example.cloudydrinks.local_data.DataLocalManager;
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
    private final static String USER = "users";
    private final static String CART = "Cart";
    private final static String ORDER = "order";
    private final static String DELIVERING = "status_delivering";
    private ArrayList<CartModel> cartList;
    private DatabaseReference databaseReference;
    private String userId, contactAddress;
    private PaymentDetailAdapter adapter;
    private TextView totalPriceTV, addressTV;
    private int totalPrice;
    private ImageView addAddressImage;
    private Contact contact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        userId = DataLocalManager.getUserId();

        totalPriceTV = findViewById(R.id.totalPriceTV);
        LinearLayout addressLayout = findViewById(R.id.addressLayout);
        MaterialButton orderBtn = findViewById(R.id.orderBtn);
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

        generatePaymentDetail();

        addressLayout.setOnClickListener(addAddressListener);
        orderBtn.setOnClickListener(orderClickListener);
        loadAddress();
    }

    public View.OnClickListener orderClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String addressField = addressTV.getText().toString().trim();
            if (addressField.equals("Chọn địa chỉ")) {
                Toast.makeText(PaymentActivity.this, "Vui lòng chọn địa chỉ nhận hàng", Toast.LENGTH_SHORT).show();
            } else {
                addToOrder();
                Intent intent = new Intent(PaymentActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        }
    };
    public void addToOrder() {
        databaseReference = FirebaseDatabase.getInstance().getReference(USER).child(userId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(CART)) {
                    cartList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.child(CART).getChildren()) {
                        CartModel cartModel = dataSnapshot.getValue(CartModel.class);
                        cartList.add(cartModel);
                        databaseReference.child(CART).removeValue();
                    }

                    for (CartModel model : cartList) {
                        String orderId = RandomKey.generateKey();

                        Order order = new Order();
                        order.setOrderId(orderId);
                        order.setProduct_id(model.getProduct_id());
                        order.setProduct_name(model.getProduct_name());
                        order.setQuantity(model.getQuantity());
                        order.setSize(model.getSize());
                        order.setProduct_price(model.getProduct_price());
                        order.setTotalPrice(model.getTotalPrice());
                        order.setContact(contact);

                        String orderDeliveringKey = RandomKey.generateKey();
                        databaseReference.child(ORDER).child(DELIVERING).child(order.getOrderId()).setValue(order).addOnCompleteListener(new OnCompleteListener<Void>() {
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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PaymentActivity.this);
        RecyclerView paymentRecyclerView = findViewById(R.id.paymentDetailRV);
        paymentRecyclerView.setLayoutManager(linearLayoutManager);

        cartList = new ArrayList<>();
        totalPrice = 0;

        databaseReference = FirebaseDatabase.getInstance().getReference(USER).child(userId).child(CART);
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
            startActivity(intent);
        }
    };
    private void loadAddress() {

        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }
        contact = (Contact) bundle.get("contactObj");

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