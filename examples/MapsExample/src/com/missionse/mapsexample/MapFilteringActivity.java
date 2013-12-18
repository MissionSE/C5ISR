package com.missionse.mapsexample;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.app.SearchManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.StructuredPostal;
import android.provider.ContactsContract.Contacts;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderRequest;
import com.google.code.geocoder.model.GeocoderResult;
import com.google.code.geocoder.model.GeocoderStatus;
import com.google.gson.reflect.TypeToken;
import com.google.maps.android.MarkerManager;
import com.koushikdutta.ion.Ion;
import com.missionse.mapsexample.utils.ImageLoader;
import com.missionse.mapsexample.utils.Utils;
import com.missionse.mapviewer.MapViewerFragment;
import com.missionse.mapviewer.MiniMapFragment;

/**
 * Example use of marker management and map filtering.
 */
public class MapFilteringActivity extends Activity implements
MapViewerFragment.Callbacks, MiniMapFragment.Callbacks,
FragmentManager.OnBackStackChangedListener, LoaderCallbacks<Cursor>,
SharedPreferences.OnSharedPreferenceChangeListener,
GoogleMap.OnInfoWindowClickListener {

	public static class AddressStore {
		Map<ContactAddressKey, MarkerInfo> addressMarkers;

		public AddressStore(Map<ContactAddressKey, MarkerInfo> addressMarkers) {
			this.addressMarkers = addressMarkers;
		}
	}

	public class SomeHeavyOperation implements Runnable {
		private final Cursor mCursor;
		private final int mType;

		public SomeHeavyOperation(Cursor cursor, int type) {
			this.mCursor = cursor;
			this.mType = type;
		}

		public void run() {
			Log.d(TAG, "Started loading contacts from cursor for type=" + mType);

			if (!isNetworkConnected()) {
				MapFilteringActivity.this.runOnUiThread(new Runnable() {
					public void run() {
						toastMessage("No Network Connection!");
					}
				});
				return;
			}
			final Map<ContactAddressKey, MarkerInfo> pendingMarkers = mMarkerTypeMap
					.get(mType);

			Log.d(TAG, "Loaded " + pendingMarkers.size() + " pending markers for type=" + mType);

			final float markerHue = getMarkerTypeHue(mType);

			// Perform your heavy operation
			mCursor.moveToFirst();
			while (!mCursor.isAfterLast()) {
				// get data
				String address = mCursor.getString(mCursor
						.getColumnIndex(StructuredPostal.FORMATTED_ADDRESS));
				String urlAddress = address.replace("\n", "+");
				urlAddress = urlAddress.replace(" ", "+");
				final String name = mCursor.getString(mCursor
						.getColumnIndex(Contacts.DISPLAY_NAME));
				final long contactId = mCursor.getLong(mCursor
						.getColumnIndex(Contacts._ID));
				final String lookupKey = mCursor.getString(mCursor
						.getColumnIndex(Contacts.LOOKUP_KEY));
				ContactAddressKey addressKey = new ContactAddressKey(mType,
						name, address);

				mCursor.moveToNext();

				synchronized (mMarkerTypeMap) {
					if (pendingMarkers.containsKey(addressKey)) {
						Log.d(TAG, "Skipping id=" + contactId + ", name="
								+ name);
						continue;
					}
				}

				Geocoder geocoder = new Geocoder();
				GeocoderRequest geocoderRequest = new GeocoderRequestBuilder()
				.setAddress(urlAddress).setLanguage("en")
				.getGeocoderRequest();
				GeocodeResponse geocoderResponse = geocoder
						.geocode(geocoderRequest);

				if (geocoderResponse.getStatus().equals(GeocoderStatus.OK)) {
					List<GeocoderResult> geocoderResults = geocoderResponse
							.getResults();
					for (GeocoderResult result : geocoderResults) {
						double lat = result.getGeometry().getLocation()
								.getLat().doubleValue();
						double lng = result.getGeometry().getLocation()
								.getLng().doubleValue();
						MarkerInfo markerInfo = new MarkerInfo(address, contactId, markerHue, lat, lng, lookupKey, name, mType);
						pendingMarkers.put(addressKey, markerInfo);
					}
				} else {
					Log.e(TAG, "Geocoder request failed: status="
							+ geocoderResponse.getStatus() + "\n\taddress="
							+ address);
				}
			}

			Log.d(TAG, "Finished loading contacts from cursor for type=" + mType);

			MapFilteringActivity.this.runOnUiThread(new Runnable() {
				public void run() {
					MapFilteringActivity.this.addPendingMarkers(mType,
							pendingMarkers);
				}
			});
		}

		private float getMarkerTypeHue(int type) {
			switch (type) {
			case StructuredPostal.TYPE_HOME:
				return BitmapDescriptorFactory.HUE_ROSE;
			case StructuredPostal.TYPE_WORK:
				return BitmapDescriptorFactory.HUE_AZURE;
			case StructuredPostal.TYPE_OTHER:
				return BitmapDescriptorFactory.HUE_YELLOW;
			default:
				return BitmapDescriptorFactory.HUE_VIOLET;
			}
		}
	}

	private final class SearchQueryTextListener implements
	SearchView.OnQueryTextListener {

		@Override
		public boolean onQueryTextChange(String newText) {
			// Called when the action bar search text has changed. Updates
			// the search filter, and restarts the loader to do a new query
			// using the new search string.
			String newFilter = null;
			if (!TextUtils.isEmpty(newText)) {
				newFilter = newText;
			}

			// Don't do anything if the filter is empty
			if (mSearchTerm == null && newFilter == null) {
				return true;
			}

			// Don't do anything if the new filter is the same as the current
			// filter
			if (mSearchTerm != null && mSearchTerm.equals(newFilter)) {
				return true;
			}

			// Updates current filter to new filter
			mSearchTerm = newFilter;

			// Restarts the loader. This triggers onCreateLoader(), which builds
			// the
			// necessary content Uri from mSearchTerm.
			mSearchQueryChanged = true;
			getLoaderManager().restartLoader(ContactsQuery.QUERY_ID, null,
					MapFilteringActivity.this);
			return true;
		}

		@Override
		public boolean onQueryTextSubmit(String queryText) {
			// Nothing needs to happen when the user submits the search string
			return true;
		}
	}
	private static final Executor BACKGROUND_EXECUTOR = Executors
			.newSingleThreadExecutor();

	private static final int DEF_FILL_COLOR = Color.argb(175, 0, 0, 0);
	private static final String TAG = MapFilteringActivity.class.getSimpleName();

	// These are the Contacts rows that we will retrieve.
	static final String[] CONTACTS_SUMMARY_PROJECTION = new String[] {
		Contacts._ID,
		Contacts.DISPLAY_NAME,
		Contacts.CONTACT_STATUS,
		Contacts.LOOKUP_KEY,
		StructuredPostal.FORMATTED_ADDRESS,
	};

	/**
	 * Converts a fragment arguments bundle into an intent.
	 */
	private static Intent fragmentArgumentsToIntent(Bundle arguments) {
		Intent intent = new Intent();
		if (arguments == null) {
			return intent;
		}

		final Uri data = arguments.getParcelable("_uri");
		if (data != null) {
			intent.setData(data);
		}

		intent.putExtras(arguments);
		intent.removeExtra("_uri");
		return intent;
	}

	/**
	 * Converts an intent into a {@link Bundle} suitable for use as fragment
	 * arguments.
	 */
	private static Bundle intentToFragmentArguments(Intent intent) {
		Bundle arguments = new Bundle();
		if (intent == null) {
			return arguments;
		}

		final Uri data = intent.getData();
		if (data != null) {
			arguments.putParcelable("_uri", data);
		}

		final Bundle extras = intent.getExtras();
		if (extras != null) {
			arguments.putAll(intent.getExtras());
		}
		return arguments;
	}

	private SparseArray<Button> mFilterButtons = new SparseArray<Button>();
	private LinearLayout mFilterControls;
	private SparseBooleanArray mFilterLoaded = new SparseBooleanArray();
	private ImageLoader mImageLoader; // Handles loading the contact image in a
	private MapViewerFragment mMapViewerFragment;
	private Map<Marker, MarkerInfo> mMarkerInfoMap = new HashMap<Marker, MarkerInfo>();
	private MarkerManager mMarkerManager;
	private MarkerManager mOverviewMarkerManager;
	private SparseArray<Map<ContactAddressKey, MarkerInfo>> mMarkerTypeMap = new SparseArray<Map<ContactAddressKey, MarkerInfo>>();
	private MiniMapFragment mMiniMapFragment;
	private Menu mOptionsMenu;
	private boolean mPauseBackStackWatcher = false;
	private SharedPreferences mPrefs;

	// Stores the previously selected search item so that on a configuration
	// change the same item
	// can be reselected again
	private int mPreviouslySelectedSearchItem = 0;

	// Whether or not the search query has changed since the last time the
	// loader was refreshed
	private boolean mSearchQueryChanged;

	// background thread
	private String mSearchTerm; // Stores the current search query term

	private boolean mSessionShown = true;

	private PolygonOptions mViewPolygonOptions;

	@Override
	public void deregisterOnCameraChangeListener(OnCameraChangeListener listener) {
		// getMapViewerFragment().deregisterOnCameraChangeListener(listener);
	}

	@Override
	public double getDisplayPercentage() {
		return 0.33;
	}

	@Override
	public GoogleMap getMainMap() {
		return getMapViewerFragment().getMap();
	}

	@Override
	public PolygonOptions getViewPolygonOptions() {
		return mViewPolygonOptions;
	}

	@Override
	public void mapCreated(GoogleMap map) {
//		mMarkerManager = new MarkerManager(map);
		//TODO bad place to do this
//		mOverviewMarkerManager = new MarkerManager(mMiniMapFragment.getMap());
	}

	@Override
	public void onBackStackChanged() {
		if (mPauseBackStackWatcher) {
			return;
		}

		if (getFragmentManager().getBackStackEntryCount() == 0) {
			showDetailPane(false);
		}
	}

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

		return new CursorLoader(this, baseUri, null, select, null, null);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		mOptionsMenu = menu;
		getMenuInflater().inflate(R.menu.dual_maps_example, menu);
		MenuItem searchItem = menu.findItem(R.id.menu_search);
		if (searchItem != null) {
			setupSearchView(searchItem);
		}
		mMapViewerFragment.setMyLocationEnabled(menu.findItem(R.id.myLocation)
				.isChecked());

		return true;
	}
	
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        boolean landscape = (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE);

        LinearLayout spacerView = (LinearLayout) findViewById(R.id.map_detail_spacer);
        spacerView.setOrientation(landscape ? LinearLayout.HORIZONTAL : LinearLayout.VERTICAL);
        spacerView.setGravity(landscape ? Gravity.END : Gravity.BOTTOM);

        View popupView = findViewById(R.id.map_detail_popup);
        LinearLayout.LayoutParams popupLayoutParams = (LinearLayout.LayoutParams)
                popupView.getLayoutParams();
        popupLayoutParams.width = landscape ? 0 : ViewGroup.LayoutParams.MATCH_PARENT;
        popupLayoutParams.height = landscape ? ViewGroup.LayoutParams.MATCH_PARENT : 0;
        popupView.setLayoutParams(popupLayoutParams);

        popupView.requestLayout();

        updateMapPadding();
    }

	@Override
	public void onInfoWindowClick(Marker marker) {
		mSessionShown = true;

		MarkerInfo markerInfo = mMarkerInfoMap.get(marker);

		if (markerInfo != null) {
			final Uri uri = getLookupUri(markerInfo.getContactId(),
					markerInfo.getLookupKey());
			ContactDetailFragment fragment = ContactDetailFragment.newInstance(uri);

			showDetails(fragment, uri);
		} else {
			Log.e(TAG, "Could not find marker info for marker=" + marker);
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, final Cursor cursor) {
		if (cursor != null && cursor.getCount() > 0) {
			final int type = loader.getId();
			Log.d(TAG,
					"onLoadFinished: type=" + type + ", count="
							+ cursor.getCount());
			BACKGROUND_EXECUTOR.execute(new SomeHeavyOperation(cursor, type));
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

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {

	}

	@Override
	public void registerOnCameraChangeListener(OnCameraChangeListener listener) {
		getMapViewerFragment().registerOnCameraChangeListener(listener);
	}

	private void addPendingMarkers(int type,
			Map<ContactAddressKey, MarkerInfo> pendingMarkers) {
		Log.d(TAG, "Started adding markers for type=" + type);
		String addressType = MarkerInfo.getTypeAsString(type);
		MarkerManager.Collection markerCollection = mMarkerManager.getCollection(addressType);
		MarkerManager.Collection overviewMarkerCollection = mOverviewMarkerManager.getCollection(addressType);

		if (markerCollection == null) {
			markerCollection = mMarkerManager.newCollection(addressType);
		}
		if (overviewMarkerCollection == null) {
			overviewMarkerCollection = mOverviewMarkerManager.newCollection(addressType);
		}

		for (Entry<ContactAddressKey, MarkerInfo> markerInfo : pendingMarkers
				.entrySet()) {
			MarkerOptions options = markerInfo.getValue().getMarkerOptions();
			Marker marker = markerCollection
					.addMarker(options);
			mMarkerInfoMap.put(marker, markerInfo.getValue());
			MarkerOptions overviewOptions = markerInfo.getValue().getOverviewMarkerOptions();
			overviewMarkerCollection.addMarker(overviewOptions);
		}

		mFilterLoaded.put(type, true);

		enableMarkers();
		Log.d(TAG, "Finished adding markers for type=" + type);
	}

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

	/**
	 * Enable filter controls and display map features when all loaders have
	 * finished. This ensures that only complete data for the correct filter is
	 * shown.
	 */
	private void enableMarkers() {
		for (int i = 0; i < mFilterLoaded.size(); i++) {
			Boolean value = mFilterLoaded.valueAt(i);
			if (value == null || !value.booleanValue()) {
				return;
			}
		}

		mFilterControls.setVisibility(View.VISIBLE);
		showMarkers(StructuredPostal.TYPE_HOME);
		showMarkers(StructuredPostal.TYPE_WORK);
		showMarkers(StructuredPostal.TYPE_OTHER);
	}

	private Uri getLookupUri(long contactId, String lookupKey) {
		return Contacts.getLookupUri(contactId, lookupKey);
	}

	private MapViewerFragment getMapViewerFragment() {
		FragmentManager fm = getFragmentManager();
		mMapViewerFragment = (MapViewerFragment) fm.findFragmentByTag("map");
		if (mMapViewerFragment == null) {
			mMapViewerFragment = new MapViewerFragment();
			mMapViewerFragment
			.setArguments(intentToFragmentArguments(getIntent()));
			mMapViewerFragment.registeOnInfoWindowClickListener(this);

			fm.beginTransaction()
			.add(R.id.fragment_container_map_view, mMapViewerFragment,
					"map").commit();
		}
		return mMapViewerFragment;
	}

	private boolean isNetworkConnected() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		return (ni != null);
	}

	private void loadContactMarkers(int type) {
		Log.d(TAG, "Started loading contacts from CACHE for type=" + type);

		TypeToken<Collection<MarkerInfo>> typeToken = new TypeToken<Collection<MarkerInfo>>() { };
		Collection<MarkerInfo> markerInfos;
		try {
			markerInfos = Ion.getDefault(this).store()
					.get(MarkerInfo.getTypeAsString(type), typeToken).get();
			if (markerInfos == null) {
				markerInfos = new HashSet<MarkerInfo>();
			}

			Map<ContactAddressKey, MarkerInfo> markerMap = new HashMap<ContactAddressKey, MarkerInfo>();
			for (MarkerInfo markerInfo : markerInfos) {
				markerMap.put(new ContactAddressKey(markerInfo), markerInfo);
			}
			mMarkerTypeMap.put(type, markerMap);
		} catch (InterruptedException e) {
			Log.e(TAG, "Error reading cache.", e);
		} catch (ExecutionException e) {
			Log.e(TAG, "Error reading cache.", e);
		}
		Log.d(TAG, "Finished loading contacts from CACHE for type=" + type);
	}

	private void setUpMiniMapFragment() {
		FragmentManager fm = getFragmentManager();
		mMiniMapFragment = (MiniMapFragment) fm
				.findFragmentByTag("map_overview");
		if (mMiniMapFragment == null) {
			mMiniMapFragment = new MiniMapFragment();

			fm.beginTransaction()
			.add(R.id.fragment_container_map_overview,
					mMiniMapFragment, "map_overview").commit();
		}
	}

	private void setupSearchView(MenuItem searchItem) {
		// Retrieves the system search manager service
		final SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

		// Retrieves the SearchView from the search menu item
		final SearchView searchView = (SearchView) searchItem.getActionView();

		// Assign searchable info to SearchView
		searchView.setSearchableInfo(searchManager
				.getSearchableInfo(getComponentName()));

		// Set listeners for SearchView
		searchView.setOnQueryTextListener(new SearchQueryTextListener());

		if (Utils.hasICS()) {
			// This listener added in ICS
			searchItem
			.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
				@Override
				public boolean onMenuItemActionCollapse(
						MenuItem menuItem) {
					// When the user collapses the SearchView the
					// current search string is
					// cleared and the loader restarted.
					// if (!TextUtils.isEmpty(mSearchTerm)) {
					// onSelectionCleared();
					// }
					mSearchTerm = null;
					getLoaderManager().restartLoader(
							ContactsQuery.QUERY_ID, null,
							MapFilteringActivity.this);
					return true;
				}

				@Override
				public boolean onMenuItemActionExpand(MenuItem menuItem) {
					// Nothing to do when the action item is expanded
					return true;
				}
			});
		}

		if (mSearchTerm != null) {
			// If search term is already set here then this fragment is
			// being restored from a saved state and the search menu item
			// needs to be expanded and populated again.

			// Stores the search term (as it will be wiped out by
			// onQueryTextChange() when the menu item is expanded).
			final String savedSearchTerm = mSearchTerm;

			// Expands the search menu item
			if (Utils.hasICS()) {
				searchItem.expandActionView();
			}

			// Sets the SearchView to the previous search string
			searchView.setQuery(savedSearchTerm, false);
		}
	}

	private void showDetailPane(boolean show) {
		View detailPopup = findViewById(R.id.map_detail_spacer);
		if (show != (detailPopup.getVisibility() == View.VISIBLE)) {

			int visibility;
			if (show) {
				visibility = View.VISIBLE;
			} else {
				visibility = View.GONE;
			}
			detailPopup.setVisibility(visibility);

			updateMapPadding();
		}
	}

	private void showDetails(Fragment fragment, Uri uri) {
		// Show the session details
		clearBackStack(true);
		showDetailPane(true);
		//		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		//		intent.putExtra(ContactDetailFragment.EXTRA_VARIABLE_HEIGHT_HEADER,
		//				true);
		//		fragment.setArguments(intentToFragmentArguments(intent));
		getFragmentManager().beginTransaction()
		.replace(R.id.fragment_container_detail, fragment)
		.addToBackStack(null).commit();
	}

	private void showMarkers(int type) {
		// show all markers
		boolean visible = false;
		for (Marker m : mMarkerManager.getCollection(
				MarkerInfo.getTypeAsString(type)).getMarkers()) {
			visible = !m.isVisible();
			m.setVisible(visible);
		}
		for (Marker m : mOverviewMarkerManager.getCollection(
				MarkerInfo.getTypeAsString(type)).getMarkers()) {
			visible = !m.isVisible();
			m.setVisible(visible);
		}
		// mark button active
		int backgroundResource;
		int textColorResource;
		if (visible) {
			backgroundResource = R.drawable.map_filter_button_active_background;
			textColorResource = R.color.map_filterselect_active;
		} else {
			backgroundResource = R.drawable.map_filter_button_background;
			textColorResource = R.color.map_filterselect_inactive;
		}
		mFilterButtons.get(type).setBackgroundResource(backgroundResource);
		mFilterButtons.get(type).setTextColor(
				getResources().getColor(textColorResource));
	}

	private void storeContactMarkers() {
		TypeToken<Collection<MarkerInfo>> typeToken = new TypeToken<Collection<MarkerInfo>>() { };
		synchronized (mMarkerTypeMap) {
			for (int i = 0; i < mMarkerTypeMap.size(); i++) {

				try {
					Ion.getDefault(this)
					.store()
					.put(MarkerInfo.getTypeAsString(mMarkerTypeMap.keyAt(i)), mMarkerTypeMap.valueAt(i).values(), typeToken).get();
				} catch (InterruptedException e) {
					Log.e(TAG, "Error storing cache.", e);
				} catch (ExecutionException e) {
					Log.e(TAG, "Error storing cache.", e);
				}
			}
		}
	}

	private void toastMessage(CharSequence msg) {
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}

	private void updateMapPadding() {
		// Pan the map left or up depending on the orientation.
		boolean landscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
		boolean detailShown = findViewById(R.id.map_detail_spacer)
				.getVisibility() == View.VISIBLE;

		mMapViewerFragment.setCenterPadding(landscape ? (detailShown ? 0.25f
				: 0f) : 0, landscape ? 0 : (detailShown ? 0.25f : 0));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maps_filtering);
		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

		mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		mPrefs.registerOnSharedPreferenceChangeListener(this);

		FragmentManager fm = getFragmentManager();
		fm.addOnBackStackChangedListener(this);

		mViewPolygonOptions = new PolygonOptions().fillColor(DEF_FILL_COLOR)
				.strokeWidth(0);

		getMapViewerFragment();
		setUpMiniMapFragment();

		findViewById(R.id.close_button).setOnClickListener(
				new View.OnClickListener() {
					public void onClick(View view) {
						clearBackStack(false);
					}
				});

		mFilterControls = (LinearLayout) findViewById(R.id.map_filtercontrols);

		// setup filter button handlers
		mFilterButtons.put(StructuredPostal.TYPE_HOME,
				(Button) findViewById(R.id.map_filter1));
		mFilterButtons.put(StructuredPostal.TYPE_WORK,
				(Button) findViewById(R.id.map_filter2));
		mFilterButtons.put(StructuredPostal.TYPE_OTHER,
				(Button) findViewById(R.id.map_filter3));

		Button button = mFilterButtons.get(StructuredPostal.TYPE_HOME);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showMarkers(StructuredPostal.TYPE_HOME);
			}
		});

		button = mFilterButtons.get(StructuredPostal.TYPE_WORK);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showMarkers(StructuredPostal.TYPE_WORK);
			}
		});

		button = mFilterButtons.get(StructuredPostal.TYPE_OTHER);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showMarkers(StructuredPostal.TYPE_OTHER);
			}
		});

		mFilterLoaded.put(StructuredPostal.TYPE_HOME, false);
		mFilterLoaded.put(StructuredPostal.TYPE_WORK, false);
		mFilterLoaded.put(StructuredPostal.TYPE_OTHER, false);

		onConfigurationChanged(getResources().getConfiguration());

		loadContactMarkers(StructuredPostal.TYPE_HOME);
		loadContactMarkers(StructuredPostal.TYPE_WORK);
		loadContactMarkers(StructuredPostal.TYPE_OTHER);
	}

	@Override
	protected void onPause() {
		super.onPause();
		storeContactMarkers();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if (mMarkerManager == null) {
			mMarkerManager = new MarkerManager(getMainMap());
		}
		if (mOverviewMarkerManager == null) {
			mOverviewMarkerManager = new MarkerManager(mMiniMapFragment.getMap());
		}
	}

	@Override
	protected void onStart() {
		super.onStart();

		LoaderManager lm = getLoaderManager();
		lm.initLoader(StructuredPostal.TYPE_HOME, null, this);
		lm.initLoader(StructuredPostal.TYPE_WORK, null, this);
		lm.initLoader(StructuredPostal.TYPE_OTHER, null, this);
	}

}
