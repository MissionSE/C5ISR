package com.missionse.imageviewer;

import android.os.Bundle;

public class ImageFragmentFactory {

	private ImageFragmentFactory () {

	}

	public static ImageFragment createImageFragment(final int imageResourceId) {
		ImageFragment imageFragment = new ImageFragment();

		Bundle arguments = new Bundle();
		arguments.putInt(ImageFragment.ARG_IMAGE_ID, imageResourceId);
		imageFragment.setArguments(arguments);

		return imageFragment;
	}

	public static MultiImageFragment createMultiImageFragment(final ImagePagerAdapter adapter) {
		MultiImageFragment imageFragment = new MultiImageFragment();
		imageFragment.setAdapter(adapter);

		return imageFragment;
	}
}
