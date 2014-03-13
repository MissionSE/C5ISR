package com.missionse.kestrelweather.preferences;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.widget.Toast;

import com.missionse.kestrelweather.R;

/**
 * This fragment shows about preferences only.
 */
public class AboutPreferenceFragment extends PreferenceFragment {
	private int mDeveloperClickCount;
	private Toast mDeveloperToast;
	private static final int TAPS_TO_BE_A_DEVELOPER = 7;

	@Override
	public void onResume() {
		super.onResume();
		if (getActivity() != null) {
			if (getActivity().getSharedPreferences(DeveloperPreferenceFragment.PREF_FILE, Context.MODE_PRIVATE)
					.getBoolean(getString(R.string.key_developer), false)) {
				mDeveloperClickCount = -1;
			} else {
				mDeveloperClickCount = TAPS_TO_BE_A_DEVELOPER;
			}
		}

		mDeveloperToast = null;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.pref_about);

		Preference version = findPreference(getString(R.string.key_about_version));
		if (version != null) {
			try {
				PackageInfo packageInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
				version.setSummary(packageInfo.versionName + " (" + packageInfo.versionCode + ")");
			} catch (PackageManager.NameNotFoundException e) {
				e.printStackTrace();
			}

			version.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
				@Override
				public boolean onPreferenceClick(final Preference preference) {
					if (mDeveloperClickCount > 0) {
						mDeveloperClickCount--;
						if (mDeveloperClickCount == 0) {
							getActivity().getSharedPreferences(DeveloperPreferenceFragment.PREF_FILE,
									Context.MODE_PRIVATE).edit().putBoolean(
									getString(R.string.key_developer), true).apply();
							if (mDeveloperToast != null) {
								mDeveloperToast.cancel();
							}
							mDeveloperToast = Toast.makeText(getActivity(), R.string.developer_mode_enabled, Toast.LENGTH_SHORT);
							mDeveloperToast.show();
						} else if (mDeveloperClickCount > 0 && mDeveloperClickCount < (TAPS_TO_BE_A_DEVELOPER - 2)) {
							if (mDeveloperToast != null) {
								mDeveloperToast.cancel();
							}
							mDeveloperToast = Toast.makeText(getActivity(), getResources().getQuantityString(
											R.plurals.developer_mode_toast, mDeveloperClickCount, mDeveloperClickCount),
									Toast.LENGTH_SHORT
							);
							mDeveloperToast.show();
						}
					} else if (mDeveloperClickCount < 0) {
						if (mDeveloperToast != null) {
							mDeveloperToast.cancel();
						}
						mDeveloperToast = Toast.makeText(getActivity(), R.string.developer_mode_already,
								Toast.LENGTH_LONG);
						mDeveloperToast.show();
					}
					return true;
				}
			});
		}

		Preference licenseInfo = findPreference(getString(R.string.key_about_open_source_licenses));
		if (licenseInfo != null) {
			licenseInfo.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
				@Override
				public boolean onPreferenceClick(Preference preference) {
					Activity activity = getActivity();
					Intent intent = new Intent(activity, OpenSourceLicenseInfoActivity.class);
					Bundle activityOptions = ActivityOptions.makeCustomAnimation(
							activity, R.anim.fade_in, R.anim.fade_out).toBundle();
					activity.startActivity(intent, activityOptions);
					return true;
				}
			});
		}

		Preference company = findPreference(getString(R.string.key_about_company_name));
		if (company != null) {
			company.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
				@Override
				public boolean onPreferenceClick(Preference preference) {
					Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://asrcfederal.com/mse"));
					startActivity(browserIntent);
					return true;
				}
			});
		}

	}
}