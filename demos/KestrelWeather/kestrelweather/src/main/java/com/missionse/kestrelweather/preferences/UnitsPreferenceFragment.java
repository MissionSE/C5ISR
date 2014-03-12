package com.missionse.kestrelweather.preferences;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.missionse.kestrelweather.R;

/**
 * This fragment shows units preferences only.
 */
public class UnitsPreferenceFragment extends PreferenceFragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.pref_units);

		// Bind the summaries of EditText/List/Dialog/Ringtone preferences
		// to their values. When their values change, their summaries are
		// updated to reflect the new value, per the Android Design
		// guidelines.
		SettingsActivity.bindPreferenceSummaryToValue(findPreference(getString(R.string.key_units_temperature)));
		SettingsActivity.bindPreferenceSummaryToValue(findPreference(getString(R.string.key_units_pressure)));
		SettingsActivity.bindPreferenceSummaryToValue(findPreference(getString(R.string.key_units_wind_speed)));
		SettingsActivity.bindPreferenceSummaryToValue(findPreference(getString(R.string.key_units_wind_direction)));
	}
}
