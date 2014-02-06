package com.missionse.kestrelweather.map;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mike on 2/6/14.
 */
public class TiledMap implements MapLoadedListener {
	private GoogleMap mMap;
	private Map<String, TileProvider> mTileProviders;
	private Map<String, TileOverlay> mTileOverlays;

	public TiledMap() {
		mTileOverlays = new HashMap<String, TileOverlay>();
		mTileProviders = new HashMap<String, TileProvider>();
	}

	@Override
	public void mapLoaded(GoogleMap map) {
		mMap = map;

		mMap.setMyLocationEnabled(true);
		mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

		for (String overlayName : mTileProviders.keySet()) {
			TileProvider tileProvider = mTileProviders.get(overlayName);
			TileOverlay tileOverlay = mMap.addTileOverlay(
					new TileOverlayOptions()
							.tileProvider(tileProvider)
							.visible(false));

			mTileOverlays.put(overlayName, tileOverlay);
		}
	}

	public void addTileProvider(final String overlayName, final TileProvider tileProvider) {
		mTileProviders.put(overlayName, tileProvider);
	}

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

	public void setOverlayVisibility(final String overlayName, final boolean visibility) {
		if (mTileOverlays.containsKey(overlayName)) {
			TileOverlay tileOverlay = mTileOverlays.get(overlayName);
			if (tileOverlay != null) {
				tileOverlay.setVisible(visibility);
			}
		}
	}
}
