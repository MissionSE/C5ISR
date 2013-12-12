package com.missionse.mapsexample;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.app.FragmentBreadCrumbs;
import android.app.FragmentManager;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.app.SearchManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.StructuredPostal;
import android.provider.ContactsContract.Contacts;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderRequest;
import com.google.code.geocoder.model.GeocoderResult;
import com.google.code.geocoder.model.GeocoderStatus;
import com.koushikdutta.ion.Ion;
import com.missionse.mapviewer.MapViewerFragment;

/**
 * Example use of the MapViewer library.
 */
public class MapsExampleActivity extends Activity implements
MapViewerFragment.Callbacks,
FragmentManager.OnBackStackChangedListener,
LoaderCallbacks<Cursor> {

	private static final Executor BACKGROUND_EXECUTOR = Executors.newSingleThreadExecutor();

	private MapViewerFragment mMapViewerFragment;
	private Menu mOptionsMenu;

	private boolean mPauseBackStackWatcher = false;

	private FragmentBreadCrumbs mFragmentBreadCrumbs;

	private String mSelectedSupplyName;
	
	private SparseArray<Map<String, MarkerOptions>> contactLocations = new SparseArray<Map<String, MarkerOptions>>();

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
			//			mMapViewerFragment.setArguments(intentToFragmentArguments(getIntent()));

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

		if (savedInstanceState == null) {
			// load all markers
			LoaderManager lm = getLoaderManager();
			lm.initLoader(StructuredPostal.TYPE_HOME, null, this);
			lm.initLoader(StructuredPostal.TYPE_WORK, null, this);
			lm.initLoader(StructuredPostal.TYPE_OTHER, null, this);
		}
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
			String title = getString(R.string.title_supply_detail);
			mFragmentBreadCrumbs.setParentTitle(mSelectedSupplyName, mSelectedSupplyName,
					mFragmentBreadCrumbsClickListener);
			mFragmentBreadCrumbs.setTitle(title, title);
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

	@Override
	public void onLocationSelected(String locationId, String locationTitle) {
		// TODO Auto-generated method stub

	}

	// These are the Contacts rows that we will retrieve.
	static final String[] CONTACTS_SUMMARY_PROJECTION = new String[] {
		Contacts._ID,
		Contacts.DISPLAY_NAME,
		Contacts.CONTACT_STATUS,
		Contacts.LOOKUP_KEY,
		StructuredPostal.FORMATTED_ADDRESS
	};

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		// This is called when a new Loader needs to be created. This
		// sample only has one Loader, so we don't care about the ID.
		// First, pick the base URI to use depending on whether we are
		// currently filtering.
		Uri baseUri = ContactsContract.Data.CONTENT_URI;

		int type = id;
		// Now create and return a CursorLoader that will take care of
		// creating a Cursor for the data being displayed.
		String select = "((" + Contacts.DISPLAY_NAME + " NOTNULL) AND ("
				+ StructuredPostal.CITY + " NOTNULL) AND ("
				+ Contacts.DISPLAY_NAME + " != '') AND (" 
				+ StructuredPostal.TYPE + " = " + type + "))";

		return new CursorLoader(this, baseUri,
				null, select, null, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, final Cursor cursor) {
		if (cursor != null && cursor.getCount() > 0) {
			final int type = loader.getId();
			Log.d(TAG, "onLoadFinished: type=" + type + ", count=" + cursor.getCount());
			BACKGROUND_EXECUTOR.execute(new SomeHeavyOperation(cursor, type));
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub

	}

	public class SomeHeavyOperation implements Runnable {
		private final Cursor mCursor;
		private final int mType;

		public SomeHeavyOperation(Cursor cursor, int type) {
			this.mCursor = cursor;
			this.mType = type;
		}

		public void run() {
			if (!isNetworkConnected()) {
				MapsExampleActivity.this.runOnUiThread(new Runnable() {
					public void run() {
						toastMessage("No Network Connection!");
					}
				});
				return;
			}
			
			final Map<String, MarkerOptions> pendingMarkers = contactLocations.get(mType);
			

			// Perform your heavy operation
			mCursor.moveToFirst();
			while (!mCursor.isAfterLast()) {
				// get data
				String address = mCursor.getString(mCursor.getColumnIndex(StructuredPostal.FORMATTED_ADDRESS));
				address = address.replace("\n", "+");
				address = address.replace(" ", "+");
				final String name = mCursor.getString(mCursor.getColumnIndex(Contacts.DISPLAY_NAME));
				final String id = mCursor.getString(mCursor.getColumnIndex(Contacts._ID));

				Geocoder geocoder = new Geocoder();
				GeocoderRequest geocoderRequest = new GeocoderRequestBuilder().setAddress(address).setLanguage("en").getGeocoderRequest();
				GeocodeResponse geocoderResponse = geocoder.geocode(geocoderRequest);

				if (geocoderResponse.getStatus().equals(GeocoderStatus.OK)) {
					List<GeocoderResult> geocoderResults = geocoderResponse.getResults();
					for (GeocoderResult result : geocoderResults) {
						double lat = result.getGeometry().getLocation().getLat().doubleValue();
						double lng = result.getGeometry().getLocation().getLng().doubleValue();
						MarkerOptions markerOptions = new MarkerOptions()
						.draggable(false)
						.position(new LatLng(lat, lng))
						.title(name)
						.snippet(address)
						.visible(false);
						pendingMarkers.put(id, markerOptions);
					}
				} else {
					Log.e(TAG, "Geocoder request failed: status=" + geocoderResponse.getStatus()
							+ "\n\taddress=" + address);
				}
				mCursor.moveToNext();
			}

			MapsExampleActivity.this.runOnUiThread(new Runnable() {
				public void run() {
					MapsExampleActivity.this.mMapViewerFragment.addPendingMarkers(mType, pendingMarkers);
				}
			});
		}
	}

	private boolean isNetworkConnected() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		return (ni != null);
	}
	
	private void toastMessage(CharSequence msg) {
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}
	
	public static class AddressStore {
		Map<String, MarkerOptions> addressMarkers;

		public AddressStore(Map<String, MarkerOptions> addressMarkers) {
			this.addressMarkers = addressMarkers;
		}
	}
	
	private void loadContactMarkers(int type) {
		Ion.getDefault(this)
	}

}
