package com.missionse.kestrelweather.map;

import com.google.android.gms.maps.model.TileProvider;
import com.google.android.gms.maps.model.UrlTileProvider;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Provides utility functions to create TileProviders.
 */
public final class TileProviderFactory {
	private static final int TILE_SIZE = 256;

	private TileProviderFactory() {
	}

	/**
	 * Creates a TileProvider utilizing a URL.
	 * @param tileAddress The address of the tile provider.
	 * @return The TileProvider created.
	 */
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
