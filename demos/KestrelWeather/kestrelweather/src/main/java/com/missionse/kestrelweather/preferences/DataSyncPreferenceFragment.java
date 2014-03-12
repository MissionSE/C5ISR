package com.missionse.kestrelweather.preferences;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.missionse.kestrelweather.R;

/**
 * This fragment shows data and sync preferences only. It is used when the
 * activity is showing a two-pane settings UI.
 */
public class DataSyncPreferenceFragment extends PreferenceFragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.pref_data_sync);

		// Bind the summaries of EditText/List/Dialog/Ringtone preferences
		// to their values. When their values change, their summaries are
		// updated to reflect the new value, per the Android Design
		// guidelines.
		SettingsActivity.bindPreferenceSummaryToValue(findPreference(getString(R.string.key_sync_frequency)));
	}
}