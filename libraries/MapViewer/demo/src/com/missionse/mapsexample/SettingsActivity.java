package com.missionse.mapsexample;

import android.app.Activity;
import android.os.Bundle;

/**
 * Activity for customizing app settings.
 */
public class SettingsActivity extends Activity {
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }

}