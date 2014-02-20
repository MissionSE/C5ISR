package com.missionse.imageviewer;

import android.app.Fragment;
import android.net.Uri;
import android.support.v13.app.FragmentStatePagerAdapter;

/**
 * Provides functions used to create image fragments.
 */
public final class ImageFragmentFactory {

	private ImageFragmentFactory() {
	}

	/**
	 * Creates an image fragment with one image.
	 * @param imageResourceId The resource id associated with an image.
	 * @return The fragment that displays the specified image.
	 */
	public static Fragment createImageFragment(final int imageResourceId) {
		return ResourceImageFragment.newInstance(imageResourceId);
	}

	/**
	 * Creates an image fragment with one image from the image uri.
	 * @param imageUri The uri of the image.
	 * @return The fragment that displays the specified image.
	 */
	public static Fragment createImageFragment(final Uri imageUri) {
		return UriImageFragment.newInstance(imageUri);
	}

	/**
	 * Creates an image fragment with multiple images.
	 * @param adapter The adapter that contains the images to be displayed.
	 * @return The fragment that displays the specified images.
	 */
	public static Fragment createImageFragment(final FragmentStatePagerAdapter adapter) {
		return MultiImageFragment.newInstance(adapter);
	}
}
