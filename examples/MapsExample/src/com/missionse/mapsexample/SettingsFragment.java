package com.missionse.mapsexample;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Creates a preferences screen from XML resource.
 *
 */
public class SettingsFragment extends PreferenceFragment {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Load the preferences from an XML resource
		addPreferencesFromResource(R.xml.preferences);
	}
	
}