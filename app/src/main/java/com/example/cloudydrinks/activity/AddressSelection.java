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
import android.widget.LinearLayout;

import com.example.cloudydrinks.R;
import com.example.cloudydrinks.adapter.AddressAdapter;
import com.example.cloudydrinks.local_data.DataLocalManager;
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
    private final static String USER = "users";
    private final static String ADDRESS_PATH = "contact_address";
    private ArrayList<Contact> addressList;
    private String userId;
    private AddressAdapter addressAdapter;

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

        LinearLayout addAddressLayout = findViewById(R.id.addAddressLayout);

        addAddressLayout.setOnClickListener(moveToAddAddress);

        userId = DataLocalManager.getUserId();

        generateAddressList();
    }

    public View.OnClickListener moveToAddAddress = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(AddressSelection.this, AddressActivity.class);
            intent.putExtra("whatToDo", "Add new address");
            startActivity(intent);
        }
    };
    public void generateAddressList() {
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(AddressSelection.this, LinearLayoutManager.VERTICAL, false);
        RecyclerView addressRecyclerView = findViewById(R.id.addressListRV);
        addressRecyclerView.setLayoutManager(gridLayoutManager);

        addressList = new ArrayList<>();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(USER).child(userId).child(ADDRESS_PATH);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                addressList.clear();
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
        startActivity(intent);
        finish();
    }
    public void onClickEdit(Contact contact) {
        Intent intent = new Intent(AddressSelection.this, AddressActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("contactObj", contact);
        intent.putExtra("addressId", contact.getAddressId());
        intent.putExtras(bundle);
        startActivity(intent);
    }
}