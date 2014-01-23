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
public class ImageFragment extends Fragment {
	private ImageView mImage;
	private int mImageId;

	public static final String ARG_IMAGE_ID = "image_id";

	/**
	 * Constructor.
	 */
	public ImageFragment() {
		mImageId = 0;
	}

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final Bundle arguments = getArguments();
		mImageId = arguments.getInt(ARG_IMAGE_ID);
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_image, null);

		mImage = (ImageView) view.findViewById(R.id.image_view);
		mImage.setImageResource(mImageId);

		return view;
	}
}
