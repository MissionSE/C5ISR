package com.missionse.kestrelweather.preferences;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.missionse.kestrelweather.R;

/**
 * This fragment shows units preferences only.
 */
public class SimulatedValuesPreferenceFragment extends PreferenceFragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.pref_simulated_values);

		SettingsActivity.bindPreferenceSummaryToValue(findPreference(getString(R.string.key_simulated_temperature)));
		SettingsActivity.bindPreferenceSummaryToValue(findPreference(getString(R.string.key_simulated_humidity)));
		SettingsActivity.bindPreferenceSummaryToValue(findPreference(getString(R.string.key_simulated_pressure)));
		SettingsActivity.bindPreferenceSummaryToValue(findPreference(getString(R.string.key_simulated_pressure_trend)));
		SettingsActivity.bindPreferenceSummaryToValue(findPreference(getString(R.string.key_simulated_heat_index)));
		SettingsActivity.bindPreferenceSummaryToValue(findPreference(getString(R.string.key_simulated_wind_speed)));
		SettingsActivity.bindPreferenceSummaryToValue(findPreference(getString(R.string.key_simulated_wind_dir)));
		SettingsActivity.bindPreferenceSummaryToValue(findPreference(getString(R.string.key_simulated_wind_chill)));
		SettingsActivity.bindPreferenceSummaryToValue(findPreference(getString(R.string.key_simulated_dew_pt)));
	}
}
