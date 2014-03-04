package com.missionse.kestrelweather.preferences;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.missionse.kestrelweather.R;


public class OpenSourceLicenseInfoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_open_source_license_info);

		((TextView) findViewById(R.id.legal_info)).setText(GooglePlayServicesUtil.getOpenSourceSoftwareLicenseInfo(this));
	}
}
