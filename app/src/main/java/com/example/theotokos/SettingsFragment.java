package com.example.theotokos;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.viewmodel.CreationExtras;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
//import java.util.prefs.PreferenceChangeListener;

public class SettingsFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener {//implements PreferenceChangeListener {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.setting, rootKey);
        findPreference("font_size").setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference.getKey().equals("font_size")) {
            int fontSize = (int) newValue;
            // Apply the font size to your app's views
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