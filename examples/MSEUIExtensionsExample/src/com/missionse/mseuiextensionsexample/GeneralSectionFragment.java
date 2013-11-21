package com.missionse.mseuiextensionsexample;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.missionse.mseuiextensions.R;

public class GeneralSectionFragment extends PreferenceFragment {

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.fragment_general_section);
	}
}
