package com.android.settings.clover.misc;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import androidx.preference.Preference;

import com.android.internal.logging.nano.MetricsProto;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

import com.android.settings.preferences.KeyboxDataPreference;
import com.android.settings.preferences.PifDataPreference;

public class MiscSettings extends SettingsPreferenceFragment {

    private static final String KEYBOX_DATA_KEY = "keybox_data_setting";
    private ActivityResultLauncher<Intent> mKeyboxFilePickerLauncher;
    private KeyboxDataPreference mKeyboxDataPreference;

    // Pif preference integration
    private static final String PIF_DATA_KEY = "pif_data_setting";
    private ActivityResultLauncher<Intent> mPifFilePickerLauncher;
    private PifDataPreference mPifDataPreference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.misc_settings);

        mKeyboxFilePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    if (uri != null) { 
                        Preference pref = findPreference(KEYBOX_DATA_KEY);
                        if (pref instanceof KeyboxDataPreference) {
                            ((KeyboxDataPreference) pref).handleFileSelected(uri);
                        }
                    }
                }
            }
        );

        // Setup Pif file picker
        mPifFilePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    Preference pref = findPreference(PIF_DATA_KEY);
                    if (pref instanceof PifDataPreference) {
                        ((PifDataPreference) pref).handleFileSelected(uri);
                    }
                }
            }
        );
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mKeyboxDataPreference = findPreference(KEYBOX_DATA_KEY);
        mPifDataPreference = findPreference(PIF_DATA_KEY);
        if (mKeyboxDataPreference != null) {
            mKeyboxDataPreference.setFilePickerLauncher(mKeyboxFilePickerLauncher);
        }
        if (mPifDataPreference != null) {
            mPifDataPreference.setFilePickerLauncher(mPifFilePickerLauncher);
        }
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.CLOVER;
    }
}