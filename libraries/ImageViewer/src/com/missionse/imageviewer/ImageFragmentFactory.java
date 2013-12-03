package com.missionse.imageviewer;

import android.app.Fragment;
import android.os.Bundle;

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
		ImageFragment imageFragment = new ImageFragment();

		Bundle arguments = new Bundle();
		arguments.putInt(ImageFragment.ARG_IMAGE_ID, imageResourceId);
		imageFragment.setArguments(arguments);

		return imageFragment;
	}

	/**
	 * Creates an image fragment with multiple images.
	 * @param adapter The adapter that contains the images to be displayed.
	 * @return The fragment that displays the specified images.
	 */
	public static Fragment createImageFragment(final ImagePagerAdapter adapter) {
		MultiImageFragment imageFragment = new MultiImageFragment();
		imageFragment.setAdapter(adapter);

		return imageFragment;
	}
}
