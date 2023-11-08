package com.example.cloudydrinks.local_data;

import android.content.Context;

public class DataLocalManager {
    private static final String PREF_USER_ID = "PREF_USER_ID";
    private static DataLocalManager instance;
    private MySharedPreferences mySharedPreferences;

    public static void init(Context context) {
        instance = new DataLocalManager();
        instance.mySharedPreferences = new MySharedPreferences(context);
    }
    public static DataLocalManager getInstance() {
        if (instance == null) {
            instance = new DataLocalManager();
        }
        return instance;
    }

    public static void setUserId(String value) {
        DataLocalManager.getInstance().mySharedPreferences.putStringValue(PREF_USER_ID, value);
    }

    public static String getUserId() {
        return DataLocalManager.getInstance().mySharedPreferences.getStringValue(PREF_USER_ID);
    }
}
