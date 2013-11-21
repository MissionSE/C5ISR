package com.missionse.componentexample;

import java.util.List;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class ExampleLauncherActivity extends PreferenceActivity {

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.app_name);
	}

	@Override
	public void onBuildHeaders(final List<Header> target) {
		loadHeadersFromResource(R.xml.example_launcher_headers, target);
	}
}
