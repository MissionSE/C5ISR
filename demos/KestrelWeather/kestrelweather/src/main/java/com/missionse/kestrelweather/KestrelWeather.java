package com.missionse.kestrelweather;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.missionse.kestrelweather.drawer.KestrelWeatherDrawerFactory;
import com.missionse.uiextensions.navigationdrawer.DrawerActivity;
import com.missionse.uiextensions.navigationdrawer.configuration.DrawerConfigurationContainer;

public class KestrelWeather extends DrawerActivity {

    private KestrelWeatherDrawerFactory mDrawerFactory;

    public KestrelWeather() {
        mDrawerFactory = new KestrelWeatherDrawerFactory(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_kestrel_weather);
    }

    @Override
    protected DrawerConfigurationContainer getDrawerConfigurations() {
        return mDrawerFactory.createDrawers();
    }

    @Override
    protected void onDrawerConfigurationComplete() {
        mDrawerFactory.addNavigationMenuItems(getLeftDrawerAdapter());

        //selectItem(getLeftDrawerAdapter().getPosition(KestrelWeatherDrawerFactory.MAP_OVERVIEW), getLeftDrawerList());
    }

    @Override
    protected void onNavigationItemSelected(int i) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.kestrel_weather, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
