package com.missionse.imageviewer;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Provides a fragment with a single image displayed.
 */
public class UriImageFragment extends Fragment {
	private Uri mImageUri;

	public static final String ARG_IMAGE_URI = "image_uri";

	public static UriImageFragment newInstance(final Uri uri) {
		UriImageFragment uriImageFragment = new UriImageFragment();

		if (uri != null) {
			Bundle arguments = new Bundle();
			arguments.putString(ARG_IMAGE_URI, uri.toString());
			uriImageFragment.setArguments(arguments);
		}

		return uriImageFragment;
	}

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final Bundle arguments = getArguments();
		if (arguments != null) {
			String imagePath = arguments.getString(ARG_IMAGE_URI);
			mImageUri = Uri.parse(imagePath);
		}
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_image, null);
		if (view != null) {
			ImageView imageView = (ImageView) view.findViewById(R.id.image_view);
			if (imageView != null) {
				imageView.setImageURI(mImageUri);
			}
		}

		return view;
	}
}
