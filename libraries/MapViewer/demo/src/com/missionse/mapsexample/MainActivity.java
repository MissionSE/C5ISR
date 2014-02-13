package com.missionse.mapsexample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.maps.model.Marker;

public class MainActivity extends Activity implements View.OnClickListener {
    private ViewGroup mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mListView = (ViewGroup) findViewById(R.id.list);

        addDemo("Filtering Demo", MapFilteringActivity.class);
        addDemo("Map Overview Demo", MapOverviewActivity.class);
        addDemo("Clustering Demo", MarkerClusterActivity.class);
        addDemo("Custom Marker Info Window Demo", CustomMarkerInfoWindowActivity.class);
        addDemo("Settings Demo", MapSettingsActivity.class);
    }

    private void addDemo(String demoName, Class<? extends Activity> activityClass) {
        Button b = new Button(this);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        b.setLayoutParams(layoutParams);
        b.setText(demoName);
        b.setTag(activityClass);
        b.setOnClickListener(this);
        mListView.addView(b);
    }

    @Override
    public void onClick(View view) {
        Class activityClass = (Class) view.getTag();
        startActivity(new Intent(this, activityClass));
    }
}
