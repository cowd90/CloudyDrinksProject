package com.example.cloudydrinks.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cloudydrinks.R;
import com.example.cloudydrinks.activity.PaymentActivity;
import com.example.cloudydrinks.adapter.CartAdapter;
import com.example.cloudydrinks.local_data.DataLocalManager;
import com.example.cloudydrinks.model.CartModel;
import com.example.cloudydrinks.model.Order;
import com.example.cloudydrinks.my_interface.IClickCartModel;
import com.example.cloudydrinks.my_interface.IOrderClickListener;
import com.example.cloudydrinks.utils.NumberCurrencyFormatUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyBottomSheetFragment extends BottomSheetDialogFragment {
    private final static String USER = "users";
    private final static String CART = "Cart";
    private final ArrayList<CartModel> cartList;
    private String userId;
    private DatabaseReference databaseReference;
    private int totalPrice;
    private TextView totalPriceTV;
    private RecyclerView recyclerView;

    public MyBottomSheetFragment(ArrayList<CartModel> cartList) {
        this.cartList = cartList;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        // Get user id
        userId = DataLocalManager.getUserId();

        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_bottom_sheet, null);
        bottomSheetDialog.setContentView(view);

        MaterialButton checkOutBtn = view.findViewById(R.id.checkoutBtn);
        checkOutBtn.setOnClickListener(onCheckoutListener);

        recyclerView = view.findViewById(R.id.cart_item_RV);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        totalPrice = 0;
        totalPriceTV = view.findViewById(R.id.totalPriceCartTV);

        loadData();

        BottomSheetBehavior<View> behavior = BottomSheetBehavior.from((View) view.getParent());
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        return bottomSheetDialog;
    }

    public void loadData() {
        if (cartList == null) {
            totalPriceTV.setText(NumberCurrencyFormatUtil.numberCurrencyFormat(String.valueOf(totalPrice)));
        } else {
            for (CartModel cartModel : cartList) {
                totalPrice += cartModel.getTotalPrice();
            }
            totalPriceTV.setText(NumberCurrencyFormatUtil.numberCurrencyFormat(String.valueOf(totalPrice)));
        }

        CartAdapter cartAdapter = new CartAdapter(cartList, iDelete);

        recyclerView.setAdapter(cartAdapter);
    }

    public View.OnClickListener onCheckoutListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (cartList.size() == 0) {
                Toast.makeText(getActivity(), "Vui lòng thêm sản phẩm vào giỏ hàng để tiếp tục", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(getActivity(), PaymentActivity.class);
                startActivity(intent);
            }
        }
    };

    public IClickCartModel iDelete = new IClickCartModel() {
        @Override
        public void onClickCartListener(CartModel cartModel) {
            deleteItem(cartModel);
        }
    };

    public void deleteItem(CartModel cartModel) {
        new AlertDialog.Builder(getActivity())
                .setMessage("Bạn có chắc chắn muốn xóa khỏi giỏ hàng?")
                .setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String path = cartModel.getProduct_name() + "_" + cartModel.getSize();

                        databaseReference = FirebaseDatabase.getInstance().getReference(USER).child(userId).child(CART).child(path);
                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                // remove from cart
                                if (snapshot.exists()) {
                                    databaseReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            totalPrice -= cartModel.getTotalPrice();
                                            totalPriceTV.setText(NumberCurrencyFormatUtil.numberCurrencyFormat(String.valueOf(totalPrice)));
                                            CartAdapter cartAdapter = new CartAdapter(cartList, iDelete);
                                            recyclerView.setAdapter(cartAdapter);
                                            Toast.makeText(getActivity(), "Đã xóa khỏi giỏ hàng", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {
                                    databaseReference.removeEventListener(this);
                                }

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
