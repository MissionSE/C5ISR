package com.missionse.mseuiextensionsexample;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.missionse.mseuiextensions.R;

/**
 * Acts as the entry point to the MSE UI Extensions Example application, creating the menu headers for access to the
 * sub-Activities.
 */
public class ExampleLauncherActivity extends PreferenceActivity {

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.app_name);

		addPreferencesFromResource(R.xml.example_headers);
	}
}
