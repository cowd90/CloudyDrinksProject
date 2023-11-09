package com.example.cloudydrinks.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.cloudydrinks.R;
import com.example.cloudydrinks.adapter.DeliveringItemAdapter;
import com.example.cloudydrinks.local_data.DataLocalManager;
import com.example.cloudydrinks.model.Order;
import com.example.cloudydrinks.my_interface.IOrderClickListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DeliveringFragment extends Fragment {
    private final static String USER = "users";
    private final static String ORDER = "order";
    private final static String DELIVERING = "status_delivering";
    private final static String STATUS_CANCEL = "status_cancel";
    private final static String DELIVERED = "status_delivered";
    private RecyclerView deliveringStatusRV;
    private ArrayList<Order> orderList;
    private Order order;
    private DatabaseReference databaseReference;
    private DeliveringItemAdapter adapter;
    private String userid;
    public DeliveringFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_delivering, container, false);

        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        deliveringStatusRV = mView.findViewById(R.id.deliveringRV);
        deliveringStatusRV.setLayoutManager(gridLayoutManager);

        generateList();

        return mView;
    }

    public void generateList() {

        userid = DataLocalManager.getUserId();

        orderList = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference(USER).child(userid).child(ORDER).child(DELIVERING);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderList.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        order = dataSnapshot.getValue(Order.class);
                        orderList.add(order);
                    }
                } else {
                    deliveringStatusRV.setVisibility(View.GONE);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        adapter = new DeliveringItemAdapter(orderList, onCancelListener, onReceivedListener);
        deliveringStatusRV.setAdapter(adapter);

    }

    public IOrderClickListener onCancelListener = new IOrderClickListener() {
        @Override
        public void onOrderClickListener(Order order) {
            onCancel(order);
        }
    };
    public IOrderClickListener onReceivedListener = new IOrderClickListener() {
        @Override
        public void onOrderClickListener(Order order) {
            onReceived(order);
        }
    };

    public void onCancel(Order order) {
        new AlertDialog.Builder(getActivity())
                .setMessage("Đơn hàng của bạn sẽ bị hủy. Bạn có chắc chắn muốn hủy đơn hàng?")
                .setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        databaseReference = FirebaseDatabase.getInstance().getReference(USER).child(userid).child(ORDER);
                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                // remove from delivering status
                                databaseReference.child(DELIVERING).child(order.getOrderId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(getActivity(), "Hủy thành công", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                // add drink to cancel fragment
                                databaseReference.child(STATUS_CANCEL).child(order.getOrderId()).setValue(order);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                })
                .setNegativeButton("Không phải bây giờ", null).show();
    }
    public void onReceived(Order order) {
        new AlertDialog.Builder(getActivity())
                .setMessage("Đã nhận được đơn hàng?")
                .setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        databaseReference = FirebaseDatabase.getInstance().getReference(USER).child(userid).child(ORDER);
                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                // remove from delivering status
                                databaseReference.child(DELIVERING).child(order.getOrderId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(getActivity(), "Đã nhận được sản phẩm", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                // add drink to cancel fragment
                                databaseReference.child(DELIVERED).child(order.getOrderId()).setValue(order);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                })
                .setNegativeButton("Hủy", null).show();
    }
}