package com.missionse.mapsexample;

import android.app.Activity;
import android.app.FragmentBreadCrumbs;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.SearchView;
import android.widget.Switch;

/**
 * Example use of the MapViewer library.
 */
public class MapsExampleActivity extends Activity implements
MiniMapFragment.Callbacks,
FragmentManager.OnBackStackChangedListener {

	private static final String TAG_MINI_MAP = "mini_map";
	private static final String TAG_MINI_MAP_TOP_LEFT = "mini_map_top_left";
	private static final String TAG_MINI_MAP_TOP_RIGHT = "mini_map_top_right";
	private static final String TAG_MINI_MAP_BOTTOM_RIGHT = "mini_map_bottom_right";

	private MapViewerFragment mMapViewerFragment;
	private Menu mOptionsMenu;

	private boolean mPauseBackStackWatcher = false;

	private FragmentBreadCrumbs mFragmentBreadCrumbs;
	
	private String mSelectedSupplyName;

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
		mMapViewerFragment.setMyLocationEnabled(menu.findItem(R.id.myLocation).isChecked());

		Switch miniMapSwitch = (Switch) menu.findItem(R.id.showMiniMap).getActionView().findViewById(R.id.mini_map_switch);
		miniMapSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				//TODO
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
		fm.addOnBackStackChangedListener(this);

		mFragmentBreadCrumbs = (FragmentBreadCrumbs) findViewById(R.id.breadcrumbs);
		mFragmentBreadCrumbs.setActivity(this);

		mMapViewerFragment = (MapViewerFragment) fm.findFragmentByTag("map");
		if (mMapViewerFragment == null) {
			mMapViewerFragment = new MapViewerFragment();
			mMapViewerFragment.setArguments(intentToFragmentArguments(getIntent()));

			fm.beginTransaction()
			.add(R.id.fragment_container_map, mMapViewerFragment, "map")
			.commit();
		}

		findViewById(R.id.close_button).setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				clearBackStack(false);
			}
		});

		updateBreadCrumbs();
		onConfigurationChanged(getResources().getConfiguration());
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean enabled = !item.isChecked();
		switch (item.getItemId()) {
		case R.id.menu_search:
			startSearch(null, false, Bundle.EMPTY, false);
			return true;
		case R.id.myLocation:
			item.setChecked(enabled);
			mMapViewerFragment.setMyLocationEnabled(enabled);
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

	void updateBreadCrumbs() {

		if (getFragmentManager().getBackStackEntryCount() >= 2) {
			mFragmentBreadCrumbs.setParentTitle(mSelectedSupplyName, mSelectedSupplyName,
					mFragmentBreadCrumbsClickListener);
			mFragmentBreadCrumbs.setTitle(R.string.title_supply_detail, R.string.title_supply_detail);
		} else {
			mFragmentBreadCrumbs.setParentTitle(null, null, null);
			mFragmentBreadCrumbs.setTitle(mSelectedSupplyName, mSelectedSupplyName);
		}
	}

	private View.OnClickListener mFragmentBreadCrumbsClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			getFragmentManager().popBackStack();
		}
	};

	private void clearBackStack(boolean pauseWatcher) {
		if (pauseWatcher) {
			mPauseBackStackWatcher = true;
		}

		FragmentManager fm = getFragmentManager();
		while (fm.getBackStackEntryCount() > 0) {
			fm.popBackStackImmediate();
		}

		if (pauseWatcher) {
			mPauseBackStackWatcher = false;
		}
	}

	public void onBackStackChanged() {
		if (mPauseBackStackWatcher) {
			return;
		}

		if (getFragmentManager().getBackStackEntryCount() == 0) {
			showDetailPane(false);
		}

		updateBreadCrumbs();
	}

	private void showDetailPane(boolean show) {
		View detailPopup = findViewById(R.id.map_detail_spacer);
		if (show != (detailPopup.getVisibility() == View.VISIBLE)) {
			detailPopup.setVisibility(show ? View.VISIBLE : View.GONE);

			updateMapPadding();
		}
	}

	private void updateMapPadding() {
		// Pan the map left or up depending on the orientation.
		boolean landscape = getResources().getConfiguration().orientation
		== Configuration.ORIENTATION_LANDSCAPE;
		boolean detailShown = findViewById(R.id.map_detail_spacer).getVisibility() == View.VISIBLE;

		mMapViewerFragment.setCenterPadding(
				landscape ? (detailShown ? 0.25f : 0f) : 0,
						landscape ? 0 : (detailShown ? 0.25f : 0));
	}

}
