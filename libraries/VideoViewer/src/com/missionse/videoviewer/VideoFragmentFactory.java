package com.missionse.videoviewer;

import android.os.Bundle;

public class VideoFragmentFactory {

	private VideoFragmentFactory () {

	}

	public static VideoFragment createVideoFragment(final int videoResourceId) {
		VideoFragment videoFragment = new VideoFragment();

		Bundle arguments = new Bundle();
		arguments.putInt(VideoFragment.ARG_VIDEO_ID, videoResourceId);
		videoFragment.setArguments(arguments);

		return videoFragment;
	}
}
