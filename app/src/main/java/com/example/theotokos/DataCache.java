package com.example.theotokos;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.*;
public class DataCache {

    private static final String PREF_FILE_NAME = "my_prefs";
    private static final String KEY_USER = "user";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    DataCache(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static DataCache getInstance(Context context) {
        // Singleton pattern
        DataCache instance = null;
        instance = new DataCache(context);
        return instance;
    }

    public void saveUser(User user) {
        String json = new Gson().toJson(user);
        editor.putString(KEY_USER, json);
        editor.apply();
    }

    public User getUser() {
        String json = sharedPreferences.getString(KEY_USER, null);
        if (json != null) {
            return new Gson().fromJson(json, User.class);
        }
        return null;
    }
    public void clearCache(){
        editor.putString(KEY_USER, null);
        editor.apply();
    }
}