package com.missionse.kestrelweather.reports.photos;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.missionse.kestrelweather.R;

/**
 * Provides a fragment used to preview images.
 */
public class PhotoPreviewFragment extends Fragment {
	private static final String IMAGE_URI = "image_uri";
	private String mImageUri;

	/**
	 * Constructor.
	 */
	public PhotoPreviewFragment() {
	}

	/**
	 * A factory method used to create a new instance of the fragment with the provided parameters.
	 * @param imageUri The path to the image.
	 * @return A new instance of a PhotoPreviewFragment.
	 */
	public static PhotoPreviewFragment newInstance(final String imageUri) {
		PhotoPreviewFragment fragment = null;
		if (imageUri != null && imageUri.length() > 0) {
			fragment = new PhotoPreviewFragment();

			Bundle arguments = new Bundle();
			arguments.putString(IMAGE_URI, imageUri);
			fragment.setArguments(arguments);
		}

		return fragment;
	}

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mImageUri = getArguments().getString(IMAGE_URI);
		}
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_report_photos_preview, container, false);
		if (view != null) {
			ImageView imageView = (ImageView) view.findViewById(R.id.fragment_report_photos_preview_image);
			if (imageView != null) {
				String serverAddress = getString(R.string.remote_server_development);
				Ion.with(imageView)
						.error(R.drawable.ic_action_warning)
						.load(serverAddress + mImageUri)
						.setCallback(new FutureCallback<ImageView>() {
							@Override
							public void onCompleted(final Exception e, final ImageView result) {
								ProgressBar progressBar = (ProgressBar) view.findViewById(
										R.id.fragment_report_photos_preview_progress_bar);
								if (progressBar != null) {
									progressBar.setVisibility(View.GONE);
								}
							}
						});
			}
		}

		return view;
	}
}
