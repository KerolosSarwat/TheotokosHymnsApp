package com.example.theotokos;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.preference.PreferenceFragmentCompat;

import java.util.Objects;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String PREF_FONT_SIZE = "font_size";
    private static int fontSize;

    public SettingsFragment(){
        fontSize = getFontSize();
    }
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.setting, rootKey);

        // Register the listener for preference changes
        Objects.requireNonNull(getPreferenceScreen().getSharedPreferences()).registerOnSharedPreferenceChangeListener(this);

        // Retrieve and apply the saved font size
        fontSize = Objects.requireNonNull(getPreferenceManager().getSharedPreferences()).getInt(PREF_FONT_SIZE, 23);
        Log.e( "onCreatePreferences: ", ""+ fontSize);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(PREF_FONT_SIZE)) {
            fontSize = sharedPreferences.getInt(PREF_FONT_SIZE, 23);
        }
    }
    public static int getFontSize(){
        //fontSize = Objects.requireNonNull(getPreferenceManager().getSharedPreferences()).getInt(PREF_FONT_SIZE, 23);

        return fontSize == 0 ? 23 : fontSize;
    }

}