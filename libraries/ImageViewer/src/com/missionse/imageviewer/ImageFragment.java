package com.missionse.imageviewer;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ImageFragment extends Fragment {
	private ImageView image;
	private int imageId;

	public static final String ARG_IMAGE_ID = "image_id";

	public ImageFragment() {
		imageId = 0;
	}

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final Bundle arguments = getArguments();
		imageId = arguments.getInt(ARG_IMAGE_ID);
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_image, null);

		image = (ImageView) view.findViewById(R.id.image_view);
		image.setImageResource(imageId);

		return view;
	}
}
