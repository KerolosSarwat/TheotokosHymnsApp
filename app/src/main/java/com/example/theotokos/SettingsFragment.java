package com.example.theotokos;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.viewmodel.CreationExtras;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
//import java.util.prefs.PreferenceChangeListener;

public class SettingsFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener {

    private static final String KEY_FONT_SIZE = "font_size";

    private SharedPreferences sharedPreferences;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.setting, rootKey);

        sharedPreferences = requireContext().getSharedPreferences("your_preference_file_name", Context.MODE_PRIVATE);

        PreferenceScreen preferenceScreen = getPreferenceScreen();
        //preferenceScreen.getSharedPreferences().registerOnSharedPreferenceChangeListener(this);


        // Initialize preferences to their default values if not set
        int defaultFontSize = 16; // Replace with your desired default font size
        int fontSize = sharedPreferences.getInt(KEY_FONT_SIZE, defaultFontSize);
        preferenceScreen.findPreference(KEY_FONT_SIZE).setSummary(String.format("Font Size: %d", fontSize));
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference.getKey().equals(KEY_FONT_SIZE)) {
            int fontSize = (int) newValue;
            // Apply the font size to your app's views
            //applyFontSize(fontSize);

            // Update the preference summary
            preference.setSummary(String.format("Font Size: %d", fontSize));

            return true;
        }
        return false;
    }


    @NonNull
    @Override
    public CreationExtras getDefaultViewModelCreationExtras() {
        return super.getDefaultViewModelCreationExtras();
    }
}