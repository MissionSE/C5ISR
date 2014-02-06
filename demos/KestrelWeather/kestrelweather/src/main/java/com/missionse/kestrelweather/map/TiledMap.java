package com.missionse.kestrelweather.map;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages the overlays of the Google Map.
 */
public class TiledMap implements MapLoadedListener {
	private Map<String, TileProvider> mTileProviders;
	private Map<String, TileOverlay> mTileOverlays;

	/**
	 * Constructor.
	 */
	public TiledMap() {
		mTileOverlays = new HashMap<String, TileOverlay>();
		mTileProviders = new HashMap<String, TileProvider>();
	}

	@Override
	public void mapLoaded(final GoogleMap map) {
		map.setMyLocationEnabled(true);
		map.setMapType(GoogleMap.MAP_TYPE_HYBRID);

		for (String overlayName : mTileProviders.keySet()) {
			TileProvider tileProvider = mTileProviders.get(overlayName);
			TileOverlay tileOverlay = map.addTileOverlay(
					new TileOverlayOptions()
							.tileProvider(tileProvider)
							.visible(false));

			mTileOverlays.put(overlayName, tileOverlay);
		}
	}

	/**
	 * Add a TileProvider to the map.
	 * @param overlayName The name of the overlay the provider is providing.
	 * @param tileProvider The TileProvider that provides the overlay.
	 */
	public void addTileProvider(final String overlayName, final TileProvider tileProvider) {
		mTileProviders.put(overlayName, tileProvider);
	}

	/**
	 * Checks whether an overlay is visible.
	 * @param overlayName The name of the overlay.
	 * @return Whether the overlay is visible.
	 */
	public boolean isOverlayVisible(final String overlayName) {
		boolean visible = false;
		if (mTileOverlays.containsKey(overlayName)) {
			TileOverlay tileOverlay = mTileOverlays.get(overlayName);
			if (tileOverlay != null) {
				visible = tileOverlay.isVisible();
			}
		}

		return visible;
	}

	/**
	 * Sets the visibility of an overlay.
	 * @param overlayName The name of the overlay.
	 * @param visibility Whether the overlay should be visible.
	 */
	public void setOverlayVisibility(final String overlayName, final boolean visibility) {
		if (mTileOverlays.containsKey(overlayName)) {
			TileOverlay tileOverlay = mTileOverlays.get(overlayName);
			if (tileOverlay != null) {
				tileOverlay.setVisible(visibility);
			}
		}
	}
}
