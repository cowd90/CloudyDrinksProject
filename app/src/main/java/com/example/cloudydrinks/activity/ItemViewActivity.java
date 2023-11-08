package com.example.cloudydrinks.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRadioButton;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cloudydrinks.R;
import com.example.cloudydrinks.local_data.DataLocalManager;
import com.example.cloudydrinks.model.CartModel;
import com.example.cloudydrinks.model.Product;
import com.example.cloudydrinks.model.Size;
import com.example.cloudydrinks.utils.MySpannable;
import com.example.cloudydrinks.utils.NumberCurrencyFormatUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ItemViewActivity extends AppCompatActivity {
    private ImageView productImage, plusBtn, minusBtn;
    private AppCompatRadioButton largeSizeRB, mediumSizeRB, smallSizeRB;
    private TextView productNameTV, productPriceTV, productDescriptionTV, quantityTV;
    private MaterialButton addToCartBtn;
    private Product product;
    private DatabaseReference databaseReference;
    private String priceText, btnText;
    private ArrayList<Size> sizeList;
    private int price;
    private int upsizeMPrice;
    private int upsizeLPrice;
    private int quantity, currentPrice;
    private String sizeTxt;
    private String userId;
    private CheckBox favoriteCheckbox;
    private ImageView closeIV;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_view);

        sizeList = new ArrayList<>();

        // Get elements from xml files
        productNameTV = findViewById(R.id.productNameTV);
        productPriceTV = findViewById(R.id.productPriceTV);
        productDescriptionTV = findViewById(R.id.productDescriptionTV);
        productImage = findViewById(R.id.productImage);

        largeSizeRB = findViewById(R.id.largeRB);
        mediumSizeRB = findViewById(R.id.mediumRB);
        smallSizeRB = findViewById(R.id.smallRB);

        plusBtn = findViewById(R.id.plusBtn);
        minusBtn = findViewById(R.id.minusBtn);
        quantityTV = findViewById(R.id.quantityTV);
        addToCartBtn = findViewById(R.id.addToCartBtn);
        favoriteCheckbox = findViewById(R.id.favCheckbox);

        closeIV = findViewById(R.id.closeIV);
        closeIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Get user phone number
        userId = DataLocalManager.getUserId();

        // Get size attributes
        databaseReference = FirebaseDatabase.getInstance().getReference("sizes");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    sizeList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Size size = dataSnapshot.getValue(Size.class);
                        sizeList.add(size);
                    }
                    // Set up attributes
                    for (int i = 0; i < sizeList.size(); i++) {
                        if (sizeList.get(i).getSize_id() == 0) {
                            smallSizeRB.setText(sizeList.get(i).getSize_name());
                            sizeTxt = smallSizeRB.getText().toString();
                        } else if (sizeList.get(i).getSize_id() == 1) {
                            mediumSizeRB.setText(sizeList.get(i).getSize_name());
                            upsizeMPrice = price + sizeList.get(i).getUpsize_price();
                        } else if (sizeList.get(i).getSize_id() == 2) {
                            largeSizeRB.setText(sizeList.get(i).getSize_name());
                            upsizeLPrice = price + sizeList.get(i).getUpsize_price();
                        }
                    }
                }
            }

            @Override

            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // Retrieve product object from previous activity
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }

        product = (Product) bundle.get("product object");

        setFavoriteCheckbox(product);

        // price of product
        price = product.getProduct_price();

        String priceFromOBj = String.valueOf(price);
        priceText = NumberCurrencyFormatUtil.numberCurrencyFormat(priceFromOBj);

        // Upload image, name, price, and description of product
        Picasso.get().load(product.getProduct_img_url()).into(productImage);
        productNameTV.setText(product.getProduct_name());
        productPriceTV.setText(priceText);
        productDescriptionTV.setText(product.getProduct_description());

        btnText = String.valueOf(addToCartBtn.getText());

        addToCartBtn.setText(btnText+priceText);

        // Set up product description (view more and show less function)
        makeTextViewResizable(productDescriptionTV, 2, "Xem thêm", true);

        plusBtn.setOnClickListener(plusButtonOnClickListener);

        minusBtn.setOnClickListener(minusButtonOnClickListener);

        addToCartBtn.setOnClickListener(addToCartOnClickListener);

    }

    public View.OnClickListener addToCartOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            addToCart(product);
        }
    };

    private void addToCart(Product product) {
        String path = product.getProduct_name() + "_" + sizeTxt;
        int quantity = Integer.parseInt(quantityTV.getText().toString().trim());

        DatabaseReference userCart = FirebaseDatabase.getInstance().getReference("users").child(userId).child("Cart");
        userCart.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(path)) { // if user already have item in cart
                    // Just update quantity and total price of that item
                    CartModel cartModel = snapshot.child(path).getValue(CartModel.class);
                    cartModel.setQuantity(cartModel.getQuantity() + quantity);
                    Map<String, Object> map = new HashMap<>();
                    map.put("quantity", cartModel.getQuantity());
                    map.put("totalPrice", cartModel.getQuantity() * cartModel.getProduct_price());

                    userCart.child(path).updateChildren(map, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {

                            Toast.makeText(ItemViewActivity.this, "Đã cập nhật giỏ hàng!", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else { // If item doesn't not exist in cart, add new one
                    CartModel cartModel = new CartModel();

                    cartModel.setProduct_id(product.getProduct_id());
                    cartModel.setProduct_name(product.getProduct_name());
                    cartModel.setProduct_img_url(product.getProduct_img_url());

                    if (smallSizeRB.isChecked()) {
                        cartModel.setSize(smallSizeRB.getText().toString());
                        cartModel.setProduct_price(price);
                    } else if (mediumSizeRB.isChecked()) {
                        cartModel.setSize(mediumSizeRB.getText().toString());
                        cartModel.setProduct_price(upsizeMPrice);
                    } else if (largeSizeRB.isChecked()) {
                        cartModel.setSize(largeSizeRB.getText().toString());
                        cartModel.setProduct_price(upsizeLPrice);
                    }

                    cartModel.setQuantity(quantity);

                    cartModel.setTotalPrice(cartModel.getProduct_price() * cartModel.getQuantity());
                    userCart.child(path).setValue(cartModel, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                            Toast.makeText(ItemViewActivity.this, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public View.OnClickListener plusButtonOnClickListener = new View.OnClickListener() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onClick(View v) {
            quantity = Integer.parseInt(String.valueOf(quantityTV.getText()));
            quantity++;

            // Check customer choose which size
            if (smallSizeRB.isChecked()) {
                currentPrice = quantity * price;
            } else if (mediumSizeRB.isChecked()) {
                currentPrice = quantity * upsizeMPrice;
            } else if (largeSizeRB.isChecked()) {
                currentPrice = quantity * upsizeLPrice;
            }

            addToCartBtn.setText(btnText+NumberCurrencyFormatUtil.numberCurrencyFormat(String.valueOf(currentPrice)));
            quantityTV.setText(String.valueOf(quantity));
        }
    };

    public View.OnClickListener minusButtonOnClickListener = new View.OnClickListener() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onClick(View v) {
            quantity = Integer.parseInt(String.valueOf(quantityTV.getText()));
            if (quantity == 1) {
                quantity = 1;
            } else {
                quantity--;
            }
            // Check customer choose which size
            if (smallSizeRB.isChecked()) {
                currentPrice = quantity * price;
            } else if (mediumSizeRB.isChecked()) {
                currentPrice = quantity * upsizeMPrice;
            } else if (largeSizeRB.isChecked()) {
                currentPrice = quantity * upsizeLPrice;
            }

            addToCartBtn.setText(btnText+ NumberCurrencyFormatUtil.numberCurrencyFormat(String.valueOf(currentPrice)));
            quantityTV.setText(String.valueOf(quantity));
        }
    };

    public static void makeTextViewResizable(final TextView tv, final int maxLine, final String expandText, final boolean viewMore) {

        if (tv.getTag() == null) {
            tv.setTag(tv.getText());
        }
        ViewTreeObserver vto = tv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                String text;
                int lineEndIndex;
                ViewTreeObserver obs = tv.getViewTreeObserver();
                obs.removeOnGlobalLayoutListener(this);

                if (maxLine == 0) {
                    lineEndIndex = tv.getLayout().getLineEnd(0);
                    text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                } else if (maxLine > 0 && tv.getLineCount() >= maxLine) {
                    lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
                    text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                } else {
                    lineEndIndex = tv.getLayout().getLineEnd(tv.getLayout().getLineCount() - 1);
                    text = tv.getText().subSequence(0, lineEndIndex) + " " + expandText;
                }
                tv.setText(text);
                tv.setMovementMethod(LinkMovementMethod.getInstance());
                tv.setText(
                        addClickablePartTextViewResizable(new SpannableString(tv.getText().toString()), tv, lineEndIndex, expandText,
                                viewMore), TextView.BufferType.SPANNABLE);
            }
        });
    }

    private static SpannableStringBuilder addClickablePartTextViewResizable(final Spanned strSpanned, final TextView tv,
                                                                            final int maxLine, final String spanableText, final boolean viewMore) {
        String str = strSpanned.toString();
        SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);

        if (str.contains(spanableText)) {
            ssb.setSpan(new MySpannable(false){
                @Override
                public void onClick(View widget) {
                    tv.setLayoutParams(tv.getLayoutParams());
                    tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                    tv.invalidate();
                    if (viewMore) {
                        makeTextViewResizable(tv, -1, "Ẩn bớt", false);
                    } else {
                        makeTextViewResizable(tv, 2, "Xem thêm", true);
                    }
                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length(), 0);

        }
        return ssb;
    }
    @SuppressLint("SetTextI18n")
    public void onRadioButtonClicked(View view) {
        boolean isSelected = ((AppCompatRadioButton) view).isChecked();
        int id = view.getId();

        if (id == R.id.smallRB) {
            if (isSelected) {
                sizeTxt = smallSizeRB.getText().toString();
                quantityTV.setText(String.valueOf(1));

                smallSizeRB.setTextColor(Color.WHITE);
                mediumSizeRB.setTextColor(Color.parseColor("#BA704F"));
                largeSizeRB.setTextColor(Color.parseColor("#BA704F"));

                productPriceTV.setText(priceText);

                quantity = Integer.parseInt(String.valueOf(quantityTV.getText()));
                currentPrice = quantity * price;

                addToCartBtn.setText(btnText+NumberCurrencyFormatUtil.numberCurrencyFormat(String.valueOf(currentPrice)));
            }
        } else if (id == R.id.mediumRB) {
            if (isSelected) {
                sizeTxt = mediumSizeRB.getText().toString();
                quantityTV.setText(String.valueOf(1));

                mediumSizeRB.setTextColor(Color.WHITE);
                smallSizeRB.setTextColor(Color.parseColor("#BA704F"));
                largeSizeRB.setTextColor(Color.parseColor("#BA704F"));

                productPriceTV.setText(NumberCurrencyFormatUtil.numberCurrencyFormat(String.valueOf(upsizeMPrice)));

                quantity = Integer.parseInt(String.valueOf(quantityTV.getText()));
                currentPrice = quantity * upsizeMPrice;
                addToCartBtn.setText(btnText+NumberCurrencyFormatUtil.numberCurrencyFormat(String.valueOf(currentPrice)));
            }
        } else if (id == R.id.largeRB) {
            if (isSelected) {
                sizeTxt = largeSizeRB.getText().toString();
                quantityTV.setText(String.valueOf(1));

                largeSizeRB.setTextColor(Color.WHITE);
                smallSizeRB.setTextColor(Color.parseColor("#BA704F"));
                mediumSizeRB.setTextColor(Color.parseColor("#BA704F"));

                productPriceTV.setText(NumberCurrencyFormatUtil.numberCurrencyFormat(String.valueOf(upsizeLPrice)));

                quantity = Integer.parseInt(String.valueOf(quantityTV.getText()));
                currentPrice = quantity * upsizeLPrice;
                addToCartBtn.setText(btnText+NumberCurrencyFormatUtil.numberCurrencyFormat(String.valueOf(currentPrice)));
            }
        }

    }
    public void setFavoriteCheckbox(Product product) {
        DatabaseReference wishlistRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("wishlist").child(product.getProduct_name());
        wishlistRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Product item = snapshot.getValue(Product.class);
                    favoriteCheckbox.setChecked(product.getProduct_name().equals(item.getProduct_name()));
                } else {
                    databaseReference.removeEventListener(this);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void onItemClick(View view) {
        CheckBox checkBox = (CheckBox)view;
        if(checkBox.isChecked()){
            addToFavorite(product);
        } else {
            removeFromFavorite(product);
        }
    }

    private void removeFromFavorite(Product product) {
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId).child("wishlist").child(product.getProduct_name());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Product item = snapshot.getValue(Product.class);
                if (Objects.equals(Objects.requireNonNull(item).getProduct_name(), product.getProduct_name())) {
                    databaseReference.removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addToFavorite(Product product) {
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId).child("wishlist");
        databaseReference.child(product.getProduct_name()).setValue(product);
    }
}