package com.missionse.kestrelweather.map;

import com.google.android.gms.maps.model.TileProvider;
import com.google.android.gms.maps.model.UrlTileProvider;

import java.net.MalformedURLException;
import java.net.URL;

public final class TileProviderFactory {
	private final static int TILE_SIZE = 256;

	private TileProviderFactory() {
	}

	public static TileProvider createUrlTileProvider(final String tileAddress) {
		return new UrlTileProvider(TILE_SIZE, TILE_SIZE) {
			@Override
			public synchronized URL getTileUrl(int x, int y, int zoom) {
				String address = String.format(tileAddress, zoom, x, y);

				URL url = null;
				try {
					url = new URL(address);
				} catch (MalformedURLException exception) {
					throw new AssertionError(exception);
				}

				return url;
			}
		};
	}
}
