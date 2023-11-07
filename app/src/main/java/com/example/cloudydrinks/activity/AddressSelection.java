package com.example.cloudydrinks.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.example.cloudydrinks.R;
import com.example.cloudydrinks.adapter.AddressAdapter;
import com.example.cloudydrinks.model.CartModel;
import com.example.cloudydrinks.model.Contact;
import com.example.cloudydrinks.my_interface.IAddressClickListener;
import com.example.cloudydrinks.my_interface.IEditClickListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class AddressSelection extends AppCompatActivity {
    private RecyclerView addressRecyclerView;
    private ArrayList<Contact> addressList;
    private DatabaseReference databaseReference;
    private String userPhoneNumber, addressId;
    private AddressAdapter addressAdapter;
    private LinearLayout addAddressLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_selection);

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
        addAddressLayout = findViewById(R.id.addAddressLayout);

        addAddressLayout.setOnClickListener(moveToAddAddress);

        userPhoneNumber = getIntent().getStringExtra("userPhoneNumber");
        addressId = getIntent().getStringExtra("addressId");

        generateAddressList();
    }

    public View.OnClickListener moveToAddAddress = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(AddressSelection.this, AddressActivity.class);
            intent.putExtra("userPhoneNumber", userPhoneNumber);
            startActivity(intent);
        }
    };
    public void generateAddressList() {
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(AddressSelection.this, LinearLayoutManager.VERTICAL, false);
        addressRecyclerView = findViewById(R.id.addressListRV);
        addressRecyclerView.setLayoutManager(gridLayoutManager);

        addressList = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userPhoneNumber).child("contact_address");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Contact contact = dataSnapshot.getValue(Contact.class);
                        addressList.add(contact);
                    }
                }
                addressAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        addressAdapter = new AddressAdapter(addressList, editClickListener, addressClickListener);
        addressRecyclerView.setAdapter(addressAdapter);
    }

    public IEditClickListener editClickListener = new IEditClickListener() {
        @Override
        public void onEditClickListener(Contact contact) {
            onClickEdit(contact);
        }
    };
    public IAddressClickListener addressClickListener = new IAddressClickListener() {
        @Override
        public void onAddressClickListener(Contact contact) {
            onClickAddress(contact);
        }
    };
    public void onClickAddress(Contact contact) {
        Intent intent = new Intent(AddressSelection.this, PaymentActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("contactObj", contact);
        intent.putExtras(bundle);
        intent.putExtra("userPhoneNumber", userPhoneNumber);
        startActivity(intent);
    }
    public void onClickEdit(Contact contact) {
        Intent intent = new Intent(AddressSelection.this, AddressActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("contactObj", contact);
        intent.putExtra("addressId", contact.getAddressId());
        intent.putExtras(bundle);
        intent.putExtra("userPhoneNumber", userPhoneNumber);
        startActivity(intent);
    }
}