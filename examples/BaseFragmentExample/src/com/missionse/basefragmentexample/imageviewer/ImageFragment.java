package com.missionse.basefragmentexample.imageviewer;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.missionse.basefragmentexample.R;

public class ImageFragment extends Fragment {
	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_image, null);
	}
}
