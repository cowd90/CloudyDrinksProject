package com.example.cloudydrinks.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cloudydrinks.R;
import com.example.cloudydrinks.activity.PaymentActivity;
import com.example.cloudydrinks.adapter.CartAdapter;
import com.example.cloudydrinks.model.CartModel;
import com.example.cloudydrinks.utils.NumberCurrencyFormatUtil;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class MyBottomSheetFragment extends BottomSheetDialogFragment {
    private ArrayList<CartModel> cartList;
    private MaterialButton checkOutBtn;
    private String userPhoneNumber;

    public MyBottomSheetFragment(ArrayList<CartModel> cartList) {
        this.cartList = cartList;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        // Get user id
        userPhoneNumber = getArguments().getString("userPhoneNumber");

        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_bottom_sheet, null);
        bottomSheetDialog.setContentView(view);

        checkOutBtn = view.findViewById(R.id.checkoutBtn);
        checkOutBtn.setOnClickListener(onCheckoutListener);

        RecyclerView recyclerView = view.findViewById(R.id.cart_item_RV);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        int totalPrice = 0;
        TextView totalPriceTV = view.findViewById(R.id.totalPriceCartTV);

        if (cartList == null) {
            totalPriceTV.setText(NumberCurrencyFormatUtil.numberCurrencyFormat(String.valueOf(totalPrice)));
        } else {
            for (CartModel cartModel : cartList) {
                totalPrice += cartModel.getTotalPrice();
            }
            totalPriceTV.setText(NumberCurrencyFormatUtil.numberCurrencyFormat(String.valueOf(totalPrice)));
        }

        CartAdapter cartAdapter = new CartAdapter(cartList);
        recyclerView.setAdapter(cartAdapter);

        BottomSheetBehavior<View> behavior = BottomSheetBehavior.from((View) view.getParent());
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        return bottomSheetDialog;
    }

    public View.OnClickListener onCheckoutListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), PaymentActivity.class);
            intent.putExtra("userPhoneNumber", userPhoneNumber);
            startActivity(intent);
        }
    };
}
