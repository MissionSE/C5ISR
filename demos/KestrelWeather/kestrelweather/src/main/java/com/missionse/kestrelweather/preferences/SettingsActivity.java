package com.missionse.kestrelweather.preferences;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.MenuItem;

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

	/**
	 * A preference value change listener that updates the preference's summary
	 * to reflect its new value.
	 */
	private static Preference.OnPreferenceChangeListener
			sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
		@Override
		public boolean onPreferenceChange(Preference preference, Object value) {
			String stringValue = value.toString();

			if (preference instanceof ListPreference) {
				ListPreference listPreference = (ListPreference) preference;
				CharSequence[] listEntries = listPreference.getEntries();
				if (listEntries != null) {
					int index = listPreference.findIndexOfValue(stringValue);
					if (index >= 0 && index < listEntries.length) {
						preference.setSummary(listEntries[index]);
					} else {
						preference.setSummary(null);
					}
				}
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

		ActionBar actionBar = getActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
		}

		mDevelopmentPreferences = getSharedPreferences(DeveloperPreferenceFragment.PREF_FILE,
				Context.MODE_PRIVATE);

		SharedPreferences.OnSharedPreferenceChangeListener developmentPreferencesListener =
				new SharedPreferences.OnSharedPreferenceChangeListener() {
					@Override
					public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
						invalidateHeaders();
					}
				};
		mDevelopmentPreferences.registerOnSharedPreferenceChangeListener(
				developmentPreferencesListener);
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

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			onBackPressed();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void startWithFragment(final String fragmentName, final Bundle args, final Fragment resultTo, final int resultRequestCode) {
		super.startWithFragment(fragmentName, args, resultTo, resultRequestCode);
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}

	@Override
	public void startWithFragment(final String fragmentName, final Bundle args, final Fragment resultTo, final int resultRequestCode,
			final int titleRes, final int shortTitleRes) {
		super.startWithFragment(fragmentName, args, resultTo, resultRequestCode, titleRes, shortTitleRes);
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}
}
