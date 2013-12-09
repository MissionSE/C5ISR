package com.missionse.mapsexample;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.SearchView;
import android.widget.Switch;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.missionse.mapviewer.MapViewerFragment;
import com.missionse.mapviewer.MiniMapFragment;

/**
 * Example use of the MapViewer library.
 */
public class MapsExampleActivity extends Activity implements MiniMapFragment.Callbacks {

	private static final String TAG_MINI_MAP = "mini_map";
	private static final String TAG_MINI_MAP_TOP_LEFT = "mini_map_top_left";
	private static final String TAG_MINI_MAP_TOP_RIGHT = "mini_map_top_right";
	private static final String TAG_MINI_MAP_BOTTOM_RIGHT = "mini_map_bottom_right";

	private MapViewerFragment mMainMapFragment;
	private Menu mOptionsMenu;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		mOptionsMenu = menu;
		getMenuInflater().inflate(R.menu.dual_maps_example, menu);
		MenuItem searchItem = menu.findItem(R.id.menu_search);
		if (searchItem != null) {
			SearchView searchView = (SearchView) searchItem.getActionView();
			if (searchView != null) {
				SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
				searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
				searchView.setQueryRefinementEnabled(true);
			}
		}
		mMainMapFragment.setMyLocationEnabled(menu.findItem(R.id.myLocation).isChecked());

		Switch miniMapSwitch = (Switch) menu.findItem(R.id.showMiniMap).getActionView().findViewById(R.id.mini_map_switch);
		miniMapSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				FragmentManager fm = getFragmentManager();
				MiniMapFragment fragment = (MiniMapFragment) fm.findFragmentByTag(TAG_MINI_MAP);
				if (fragment == null) {
					Log.d(TAG, "MimiMapFragment does not exist in fragment manager");
					replaceBottomLeftCornerFragment(fragment, TAG_MINI_MAP);
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
		Log.d(TAG, "mMainMapFragement=" + mMainMapFragment);
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
		case R.id.menu_search:
			startSearch(null, false, Bundle.EMPTY, false);
			return true;
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

		Fragment miniMapFragment = new MiniMapFragment();
		if (getFragmentManager().findFragmentByTag(TAG_MINI_MAP_TOP_LEFT) == null) {
			replaceTopLeftCornerFragment(miniMapFragment, TAG_MINI_MAP_TOP_LEFT);
		} else if (getFragmentManager().findFragmentByTag(TAG_MINI_MAP_TOP_RIGHT) == null) {
			replaceTopRightCornerFragment(miniMapFragment, TAG_MINI_MAP_TOP_RIGHT);
		} else if (getFragmentManager().findFragmentByTag(TAG_MINI_MAP_BOTTOM_RIGHT) == null) {
			replaceBottomRightCornerFragment(miniMapFragment, TAG_MINI_MAP_BOTTOM_RIGHT);
		} else if (getFragmentManager().findFragmentByTag(TAG_MINI_MAP) == null) {
			replaceBottomLeftCornerFragment(miniMapFragment, TAG_MINI_MAP);
		}
	}

	@Override
	public void registerOnCameraChangeListener(OnCameraChangeListener listener) {
		mMainMapFragment.registerOnCameraChangeListener(listener);
	}

	@Override
	public void deregisterOnCameraChangeListener(OnCameraChangeListener listener) {
		mMainMapFragment.deregisterOnCameraChangeListener(listener);
	}

	@Override
	public GoogleMap getMainMap() {
		return mMainMapFragment.getMainMap();
	}

	private void replaceTopLeftCornerFragment(Fragment fragment, String tag) {
		replaceCornerFragment(R.id.top_left_container, fragment, tag, android.R.animator.fade_in, android.R.animator.fade_out);
	}

	private void replaceTopRightCornerFragment(Fragment fragment, String tag) {
		replaceCornerFragment(R.id.top_right_container, fragment, tag, android.R.animator.fade_in, android.R.animator.fade_out);
	}

	private void replaceBottomLeftCornerFragment(Fragment fragment, String tag) {
		replaceCornerFragment(R.id.bottom_left_container, fragment, tag, android.R.animator.fade_in, android.R.animator.fade_out);
	}

	private void replaceBottomRightCornerFragment(Fragment fragment, String tag) {
		replaceCornerFragment(R.id.bottom_right_container, fragment, tag, android.R.animator.fade_in, android.R.animator.fade_out);
	}

	private void replaceCornerFragment(int containerViewId, Fragment fragment, String tag, int enter, int exit) {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(containerViewId, fragment, tag);
		ft.commit();
	}

	private void removeCornerFragment(Fragment fragment, int enter, int exit) {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.remove(fragment);
		ft.commit();
	}

}
