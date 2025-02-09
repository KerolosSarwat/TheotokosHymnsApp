package com.example.theotokos;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsFragment extends PreferenceFragmentCompat
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String PREF_FONT_SIZE = "font_size";
    private static final String TAG = "SettingsFragment";

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        // Load preferences from XML
        setPreferencesFromResource(R.xml.setting, rootKey);

        // Register the listener for preference changes
        SharedPreferences sharedPreferences = getPreferenceManager().getSharedPreferences();
        if (sharedPreferences != null) {
            sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        }

        // Initialize the summary for the font size preference
        updateFontSizeSummary();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Register the listener for preference changes
        SharedPreferences sharedPreferences = getPreferenceManager().getSharedPreferences();
        if (sharedPreferences != null) {
            sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (PREF_FONT_SIZE.equals(key)) {
            // Update the summary when the font size changes
            updateFontSizeSummary();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Register the listener for preference changes
        SharedPreferences sharedPreferences = getPreferenceManager().getSharedPreferences();
        if (sharedPreferences != null) {
            sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // Unregister the listener for preference changes
        SharedPreferences sharedPreferences = getPreferenceManager().getSharedPreferences();
        if (sharedPreferences != null) {
            sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
        }
    }

    // Helper method to update the summary of the font size preference
    private void updateFontSizeSummary() {
        SharedPreferences sharedPreferences = getPreferenceManager().getSharedPreferences();
        if (sharedPreferences != null) {
            int fontSize = sharedPreferences.getInt(PREF_FONT_SIZE, 25); // Default to 25sp
            Preference fontSizePreference = findPreference(PREF_FONT_SIZE);
            if (fontSizePreference != null) {
                fontSizePreference.setSummary(String.valueOf(fontSize));
            }
        }
    }

    // Static method to get the font size from SharedPreferences
    public static int getFontSize(SharedPreferences sharedPreferences) {
        if (sharedPreferences != null) {
            return sharedPreferences.getInt(PREF_FONT_SIZE, 25); // Default to 25sp
        }
        return 25; // Fallback value
    }
}