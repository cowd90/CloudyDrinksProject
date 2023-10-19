package com.example.cloudydrinks.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRadioButton;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.cloudydrinks.R;
import com.example.cloudydrinks.model.Product;
import com.example.cloudydrinks.utils.MySpannable;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

public class ItemViewActivity extends AppCompatActivity {
    private ImageView productImage;
    private AppCompatRadioButton largeSizeRB, mediumSizeRB, smallSizeRB;
    private TextView productNameTV, productPriceTV, productDescriptionTV;
    private MaterialButton addToCartBtn;
    private Product product;
    private String priceText, btnText;
    private int priceInt;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_view);

        productNameTV = findViewById(R.id.productNameTV);
        productPriceTV = findViewById(R.id.productPriceTV);
        productDescriptionTV = findViewById(R.id.productDescriptionTV);
        productImage = findViewById(R.id.productImage);

        largeSizeRB = findViewById(R.id.largeRB);
        mediumSizeRB = findViewById(R.id.mediumRB);
        smallSizeRB = findViewById(R.id.smallRB);

        addToCartBtn = findViewById(R.id.addToCartBtn);

        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }

        product = (Product) bundle.get("product object");

        priceInt = product.getProduct_price();
        String priceFromOBj = String.valueOf(priceInt);
        priceText = numberCurrencyFormat(priceFromOBj)+"₫";

        Picasso.get().load(product.getProduct_img_url()).into(productImage);
        productNameTV.setText(product.getProduct_name());
        productPriceTV.setText(priceText);
        productDescriptionTV.setText(product.getProduct_description());

        btnText = String.valueOf(addToCartBtn.getText());

        addToCartBtn.setText(btnText+priceText);

        // Set up product description (view more and show less function)
        makeTextViewResizable(productDescriptionTV, 2, "Xem thêm", true);

    }

    public static String numberCurrencyFormat(String number) {
        DecimalFormat decimalFormat = new DecimalFormat("###,###");
        return decimalFormat.format(Integer.parseInt(number));
    }

    public static void makeTextViewResizable(final TextView tv, final int maxLine, final String expandText, final boolean viewMore) {

        if (tv.getTag() == null) {
            tv.setTag(tv.getText());
        }
        ViewTreeObserver vto = tv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
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
        boolean isSelected = ((AppCompatRadioButton)view).isChecked();
        int id = view.getId();

        if (id == R.id.smallRB) {
            if (isSelected) {
                smallSizeRB.setTextColor(Color.WHITE);
                mediumSizeRB.setTextColor(Color.parseColor("#BA704F"));
                largeSizeRB.setTextColor(Color.parseColor("#BA704F"));

                productPriceTV.setText(priceText);
                addToCartBtn.setText(btnText+priceText);
            }
        } else if (id == R.id.mediumRB) {
            if (isSelected) {
                mediumSizeRB.setTextColor(Color.WHITE);
                smallSizeRB.setTextColor(Color.parseColor("#BA704F"));
                largeSizeRB.setTextColor(Color.parseColor("#BA704F"));

                productPriceTV.setText(numberCurrencyFormat(String.valueOf(priceInt+10000))+"₫");
                addToCartBtn.setText(btnText+numberCurrencyFormat(String.valueOf(priceInt+10000))+"₫");
            }
        } else if (id == R.id.largeRB) {
            if (isSelected) {
                largeSizeRB.setTextColor(Color.WHITE);
                smallSizeRB.setTextColor(Color.parseColor("#BA704F"));
                mediumSizeRB.setTextColor(Color.parseColor("#BA704F"));

                productPriceTV.setText(numberCurrencyFormat(String.valueOf(priceInt+20000))+"₫");
                addToCartBtn.setText(btnText+numberCurrencyFormat(String.valueOf(priceInt+20000))+"₫");
            }
        }

    }
}