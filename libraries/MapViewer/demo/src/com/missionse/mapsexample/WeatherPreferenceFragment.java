package com.missionse.mapsexample;


import android.os.Bundle;
import android.preference.PreferenceFragment;

public class WeatherPreferenceFragment extends PreferenceFragment {

    public WeatherPreferenceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences_weather);
    }
}