package com.missionse.videoviewer;

import android.os.Bundle;

/**
 * Provides functions used to create video fragments.
 */
public final class VideoFragmentFactory {

	private VideoFragmentFactory() {
	}

	/**
	 * Creates a video fragment with a specified video resource.
	 * @param videoResourceId The resource id of the video.
	 * @return A fragment that displays the requested video.
	 */
	public static VideoFragment createVideoFragment(final int videoResourceId) {
		VideoFragment videoFragment = new VideoFragment();

		Bundle arguments = new Bundle();
		arguments.putInt(VideoFragment.ARG_VIDEO_ID, videoResourceId);
		videoFragment.setArguments(arguments);

		return videoFragment;
	}
}
