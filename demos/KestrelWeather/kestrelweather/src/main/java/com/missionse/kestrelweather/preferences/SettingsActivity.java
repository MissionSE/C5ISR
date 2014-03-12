package com.missionse.kestrelweather.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

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
								: null
				);
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
	static void bindPreferenceSummaryToValue(Preference preference) {
		// Set the listener to watch for value changes.
		preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

		// Trigger the listener immediately with the preference's
		// current value.
		sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
				PreferenceManager
						.getDefaultSharedPreferences(preference.getContext())
						.getString(preference.getKey(), "")
		);
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

}
