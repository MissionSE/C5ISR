package com.missionse.videoviewer;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

/**
 * Provides a fragment that displays a video.
 */
public class VideoFragment extends Fragment {
	private VideoView mVideo;
	private MediaController mMediaController;
	private int mVideoId;

	public static final String ARG_VIDEO_ID = "video_id";

	/**
	 * Constructor.
	 */
	public VideoFragment() {
		mVideoId = 0;
	}

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final Bundle arguments = getArguments();
		mVideoId = arguments.getInt(ARG_VIDEO_ID);
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_video, null);
		mVideo = (VideoView) view.findViewById(R.id.video_view);

		String uriPath = "android.resource://" + getActivity().getPackageName() + "/" + mVideoId;
		mVideo.setVideoURI(Uri.parse(uriPath));

		mMediaController = new MediaController(inflater.getContext());
		mMediaController.setMediaPlayer(mVideo);
		mVideo.setMediaController(mMediaController);
		mVideo.requestFocus();
		mVideo.start();

		return view;
	}

	/**
	 * Starts the video.
	 */
	public void start() {
		if (mVideo != null) {
			mVideo.start();
		}
	}

	/**
	 * Stops the video.
	 */
	public void stop() {
		if (mVideo != null) {
			mVideo.stopPlayback();
		}
	}

	/**
	 * Pauses the video.
	 */
	public void pause() {
		if (mVideo != null) {
			mVideo.pause();
		}
	}

	/**
	 * Resumes the video.
	 */
	public void resume() {
		if (mVideo != null) {
			mVideo.resume();
		}
	}
}
