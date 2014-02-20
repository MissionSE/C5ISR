package com.missionse.imageviewer;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Provides a fragment with a single image displayed.
 */
public class ResourceImageFragment extends Fragment {
	public static final String ARG_IMAGE_ID = "image_id";

	private int mImageId = 0;

	public static ResourceImageFragment newInstance(final int imageResourceId) {
		ResourceImageFragment resourceImageFragment = new ResourceImageFragment();

		Bundle arguments = new Bundle();
		arguments.putInt(ARG_IMAGE_ID, imageResourceId);
		resourceImageFragment.setArguments(arguments);

		return resourceImageFragment;
	}

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle arguments = getArguments();
		if (arguments != null) {
			mImageId = arguments.getInt(ARG_IMAGE_ID);
		}
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_image, null);
		if (view != null) {
			ImageView imageView = (ImageView) view.findViewById(R.id.image_view);
			imageView.setImageResource(mImageId);
		}

		return view;
	}
}
