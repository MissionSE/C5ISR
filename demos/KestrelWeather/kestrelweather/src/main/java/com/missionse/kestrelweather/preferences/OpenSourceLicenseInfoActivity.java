package com.missionse.kestrelweather.preferences;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.missionse.kestrelweather.R;

/**
 * Provides an activity that displays the open source licenses of the project.
 */
public class OpenSourceLicenseInfoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_open_source_license_info);

		TextView openSourceTextView = (TextView) findViewById(R.id.legal_info);
		if (openSourceTextView != null) {
			openSourceTextView.setText(GooglePlayServicesUtil.getOpenSourceSoftwareLicenseInfo(this));
		}
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}
}
