package com.missionse.kestrelweather.preferences;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.missionse.kestrelweather.R;

import java.util.List;
import java.util.ListIterator;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p/>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends PreferenceActivity {
	private SharedPreferences mDevelopmentPreferences;
	private SharedPreferences.OnSharedPreferenceChangeListener mDevelopmentPreferencesListener;

	/**
	 * A preference value change listener that updates the preference's summary
	 * to reflect its new value.
	 */
	private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
		@Override
		public boolean onPreferenceChange(Preference preference, Object value) {
			String stringValue = value.toString();

			if (preference instanceof ListPreference) {
				// For list preferences, look up the correct display value in
				// the preference's 'entries' list.
				ListPreference listPreference = (ListPreference) preference;
				int index = listPreference.findIndexOfValue(stringValue);

				// Set the summary to reflect the new value.
				preference.setSummary(
						index >= 0
								? listPreference.getEntries()[index]
								: null);
			} else {
				// For all other preferences, set the summary to the value's
				// simple string representation.
				preference.setSummary(stringValue);
			}
			return true;
		}
	};

	/**
	 * Binds a preference's summary to its value. More specifically, when the
	 * preference's value is changed, its summary (line of text below the
	 * preference title) is updated to reflect the value. The summary is also
	 * immediately updated upon calling this method. The exact display format is
	 * dependent on the type of preference.
	 * @see #sBindPreferenceSummaryToValueListener
	 */
	private static void bindPreferenceSummaryToValue(Preference preference) {
		// Set the listener to watch for value changes.
		preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

		// Trigger the listener immediately with the preference's
		// current value.
		sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
				PreferenceManager
						.getDefaultSharedPreferences(preference.getContext())
						.getString(preference.getKey(), ""));
	}

	@Override
	public void onBuildHeaders(List<Header> target) {
		mDevelopmentPreferences = getSharedPreferences(DeveloperPreferenceFragment.PREF_FILE,
				Context.MODE_PRIVATE);
		loadHeadersFromResource(R.xml.pref_headers, target);
		updateHeaderList(target);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getActionBar().setDisplayHomeAsUpEnabled(true);

		mDevelopmentPreferences = getSharedPreferences(DeveloperPreferenceFragment.PREF_FILE,
				Context.MODE_PRIVATE);

		mDevelopmentPreferencesListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
			@Override
			public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
				invalidateHeaders();
			}
		};
		mDevelopmentPreferences.registerOnSharedPreferenceChangeListener(
				mDevelopmentPreferencesListener);
	}

	/**
	 * Subclasses should override this method and verify that the given fragment is a valid type
	 * to be attached to this activity. The default implementation returns <code>true</code> for
	 * apps built for <code>android:targetSdkVersion</code> older than
	 * {@link android.os.Build.VERSION_CODES#KITKAT}. For later versions, it will throw an exception.
	 * @param fragmentName the class name of the Fragment about to be attached to this activity.
	 * @return true if the fragment class name is valid for this Activity and false otherwise.
	 */
	@Override
	protected boolean isValidFragment(String fragmentName) {
		return true;  //FIXME
	}

	private void updateHeaderList(List<Header> target) {
		final boolean showDev = mDevelopmentPreferences.getBoolean(getString(R.string.key_developer), false);

		ListIterator<Header> iterator = target.listIterator();
		while (iterator.hasNext()) {
			Header header = iterator.next();
			int id = (int) header.id;

			if (id == R.id.developer_settings && !showDev) {
				iterator.remove();
			}
		}
	}

	/**
	 * This fragment shows units preferences only.
	 */
	public static class UnitsPreferenceFragment extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.pref_units);

			// Bind the summaries of EditText/List/Dialog/Ringtone preferences
			// to their values. When their values change, their summaries are
			// updated to reflect the new value, per the Android Design
			// guidelines.
			bindPreferenceSummaryToValue(findPreference(getString(R.string.key_units_temperature)));
			bindPreferenceSummaryToValue(findPreference(getString(R.string.key_units_pressure)));
			bindPreferenceSummaryToValue(findPreference(getString(R.string.key_units_wind_speed)));
			bindPreferenceSummaryToValue(findPreference(getString(R.string.key_units_wind_direction)));
		}
	}

	/**
	 * This fragment shows data and sync preferences only. It is used when the
	 * activity is showing a two-pane settings UI.
	 */
	public static class DataSyncPreferenceFragment extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.pref_data_sync);

			// Bind the summaries of EditText/List/Dialog/Ringtone preferences
			// to their values. When their values change, their summaries are
			// updated to reflect the new value, per the Android Design
			// guidelines.
			bindPreferenceSummaryToValue(findPreference(getString(R.string.key_sync_frequency)));
		}
	}

	/**
	 * This fragment shows developer preferences only.
	 */
	public static class DeveloperPreferenceFragment extends PreferenceFragment {
		public static final String PREF_FILE = "development";

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.pref_developer);
		}
	}

	/**
	 * This fragment shows about preferences only.
	 */
	public static class AboutPreferenceFragment extends PreferenceFragment {
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
										Toast.LENGTH_SHORT);
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
						getActivity().startActivity(new Intent(getActivity(), OpenSourceLicenseInfoActivity.class));
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
}
