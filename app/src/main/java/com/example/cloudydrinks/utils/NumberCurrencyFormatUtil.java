package com.example.cloudydrinks.utils;

import java.text.DecimalFormat;

public class NumberCurrencyFormatUtil {
    public static String numberCurrencyFormat(String number) {
        DecimalFormat decimalFormat = new DecimalFormat("###,###");
        return decimalFormat.format(Integer.parseInt(number)) + "₫";
    }
}
