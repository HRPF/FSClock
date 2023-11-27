package com.hrpf.fsclock.fragment;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.hrpf.fsclock.R;

public class AppSettingsFragment extends PreferenceFragmentCompat {

    public static AppSettingsFragment newInstance(){
        return new AppSettingsFragment();
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
    }
}