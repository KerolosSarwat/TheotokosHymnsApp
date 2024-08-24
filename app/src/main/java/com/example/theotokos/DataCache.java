package com.example.theotokos;

import android.content.Context;
import android.content.SharedPreferences;

public class DataCache {

    private static final String SHARED_PREFS_NAME = "secretData";

    public static void saveData(Context context, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getData(Context context, String key, String defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, defaultValue);
    }
}
