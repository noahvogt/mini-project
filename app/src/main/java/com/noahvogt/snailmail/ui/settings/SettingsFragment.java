package com.noahvogt.snailmail.ui.settings;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.noahvogt.snailmail.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings);
    }
}
