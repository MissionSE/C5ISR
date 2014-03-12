package com.missionse.kestrelweather.preferences;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.missionse.kestrelweather.R;

/**
 * This fragment shows developer preferences only.
 */
public class DeveloperPreferenceFragment extends PreferenceFragment {
	public static final String PREF_FILE = "development";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.pref_developer);
	}
}
