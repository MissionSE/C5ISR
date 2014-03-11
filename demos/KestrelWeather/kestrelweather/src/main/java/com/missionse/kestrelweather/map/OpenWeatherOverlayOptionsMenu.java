package com.missionse.kestrelweather.map;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.missionse.kestrelweather.R;

/**
 * Provides an options menu for the OpenWeather overlays.
 */
public class OpenWeatherOverlayOptionsMenu implements OptionsMenuListener {
	private TiledMap mTiledMap;

	/**
	 * Constructor.
	 * @param tiledMap The map that contains the overlays.
	 */
	public OpenWeatherOverlayOptionsMenu(final TiledMap tiledMap) {
		mTiledMap = tiledMap;
	}

	@Override
	public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
		inflater.inflate(R.menu.openweather_overlay, menu);
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		boolean selectionConsumed = false;
		if (OpenWeatherOverlayFactory.isValidOverlay(item.getItemId())) {
			String overlayName = item.getTitle().toString();
			mTiledMap.setOverlayVisibility(overlayName, !item.isChecked());
			item.setChecked(mTiledMap.isOverlayVisible(overlayName));
			selectionConsumed = true;
		}
		return selectionConsumed;
	}
}
