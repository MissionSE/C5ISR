package com.missionse.mseuiextensionsexample;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.missionse.mseuiextensions.R;

/**
 * Displays specific headers to link to specific examples.
 */
public class GeneralSectionFragment extends PreferenceFragment {

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.example_headers);
	}
}
