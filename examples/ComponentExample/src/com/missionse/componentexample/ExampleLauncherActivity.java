package com.missionse.componentexample;

import java.util.List;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.Menu;
import android.view.MenuItem;

public class ExampleLauncherActivity extends PreferenceActivity {
    
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.app_name);
    }

    @Override
    public void onBuildHeaders(List<Header> target) {
    	this.loadHeadersFromResource(R.xml.example_launcher_headers, target);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_content, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    	case R.id.action_settings:
    		//TODO: handle settings
    		return true;
    	default:
    		return super.onOptionsItemSelected(item);
    	}
    }
}
