package com.missionse.imageviewer;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MultiImageFragment extends Fragment {

	ViewPager pager;
	ImagePagerAdapter adapter;

	public void setAdapter(final ImagePagerAdapter imagePagerAdapter) {
		adapter = imagePagerAdapter;
	}

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_pager, null);

		pager = (ViewPager) view.findViewById(R.id.pager);
		pager.setAdapter(adapter);

		return view;
	}
}
