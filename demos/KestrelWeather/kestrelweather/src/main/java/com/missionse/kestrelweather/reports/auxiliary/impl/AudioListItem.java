package com.missionse.kestrelweather.reports.auxiliary.impl;

import android.app.FragmentManager;
import android.content.Context;

import com.missionse.kestrelweather.R;
import com.missionse.kestrelweather.reports.audio.AudioViewFragment;
import com.missionse.kestrelweather.reports.auxiliary.AuxiliaryDataListItem;

/**
 * An auxiliary data list item for Audio.
 */
public class AudioListItem implements AuxiliaryDataListItem {
	private Context mContext;
	private FragmentManager mFragmentManager;

	/**
	 * Constructor.
	 * @param context The current Context.
	 * @param fragmentManager The fragment manager.
	 */
	public AudioListItem(final Context context, final FragmentManager fragmentManager) {
		mContext = context;
		mFragmentManager = fragmentManager;
	}

	@Override
	public int getIcon() {
		return R.drawable.ic_action_volume_on;
	}

	@Override
	public int getCount() {
		return 0;
	}

	@Override
	public String getType() {
		return mContext.getString(R.string.audio_recording);
	}

	@Override
	public void onClick() {
		mFragmentManager.beginTransaction()
				.replace(R.id.content, AudioViewFragment.newInstance(true), "audio_overview")
				.addToBackStack("audio_overview")
				.commit();
	}
}
