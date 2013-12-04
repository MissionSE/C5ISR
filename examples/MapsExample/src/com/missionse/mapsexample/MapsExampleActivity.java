package com.missionse.mapsexample;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup.LayoutParams;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.Switch;

import com.missionse.mapviewer.MapViewerFragment;
import com.missionse.mapviewer.MiniMapFragment;

public class MapsExampleActivity extends Activity {

	private static final String TAG_MINI_MAP = "mini_map";
	private static final String TAG_MINI_MAP_TOP_LEFT = "mini_map_top_left";
	private static final String TAG_MINI_MAP_TOP_RIGHT = "mini_map_top_right";
	private static final String TAG_MINI_MAP_BOTTOM_RIGHT = "mini_map_bottom_right";
	
	private MapViewerFragment mMainMapFragment;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.dual_maps_example, menu);
		mMainMapFragment.setMyLocationEnabled(menu.findItem(R.id.myLocation).isChecked());

		Switch miniMapSwitch = (Switch) menu.findItem(R.id.showMiniMap).getActionView().findViewById(R.id.mini_map_switch);
		miniMapSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				FragmentManager fm = getFragmentManager();
				MiniMapFragment fragment = (MiniMapFragment) fm.findFragmentByTag(TAG_MINI_MAP);
				if (fragment == null) {
					Log.d(TAG, "MimiMapFragment does not exist in fragment manager");
					mMainMapFragment.replaceBottomLeftCornerFragment(fragment, TAG_MINI_MAP);
				} else {
					Log.d(TAG, "MimiMapFragment exists in fragment manager");
					FragmentTransaction ft = getFragmentManager().beginTransaction();
					ft.setCustomAnimations(R.animator.slide_in, R.animator.slide_out);
					if (isChecked) {
						ft.attach(fragment);
					} else {
						ft.detach(fragment);
					}
					ft.commit();
				}
			}
		});
		return true;
	}

	private static final String TAG = MapsExampleActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maps_example);
		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

		FragmentManager fm = getFragmentManager();
		mMainMapFragment = (MapViewerFragment) fm.findFragmentById(R.id.fragmentContainer);

		if (mMainMapFragment == null) {
			Log.d(TAG, "Creating new MapViewerFragment");
			mMainMapFragment = new MapViewerFragment();
			fm.beginTransaction()
			.add(R.id.fragmentContainer, mMainMapFragment)
			.commit();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean enabled = !item.isChecked();
		switch (item.getItemId()) {
		case R.id.addMiniMap:
			addMiniMap();
			return true;
		case R.id.myLocation:
			item.setChecked(enabled);
			mMainMapFragment.setMyLocationEnabled(enabled);
			return true;
		case R.id.resetMaps:
			return true;
		case R.id.action_settings:
			Intent i = new Intent(this, SettingsActivity.class);
			startActivity(i);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void addMiniMap() {
		
		Fragment miniMapFragment = MiniMapFragment.newInstance(mMainMapFragment);
		if (getFragmentManager().findFragmentByTag(TAG_MINI_MAP_TOP_LEFT) == null) {
			mMainMapFragment.replaceTopLeftCornerFragment(miniMapFragment, TAG_MINI_MAP_TOP_LEFT);
		} else if (getFragmentManager().findFragmentByTag(TAG_MINI_MAP_TOP_RIGHT) == null) {
			mMainMapFragment.replaceTopRightCornerFragment(miniMapFragment, TAG_MINI_MAP_TOP_RIGHT);
		} else if (getFragmentManager().findFragmentByTag(TAG_MINI_MAP_BOTTOM_RIGHT) == null) {
			mMainMapFragment.replaceBottomRightCornerFragment(miniMapFragment, TAG_MINI_MAP_BOTTOM_RIGHT);
		} else if (getFragmentManager().findFragmentByTag(TAG_MINI_MAP) == null) {
			mMainMapFragment.replaceBottomLeftCornerFragment(miniMapFragment, TAG_MINI_MAP);
		}
	}

}
