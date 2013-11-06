package com.missionse.videoviewer;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoFragment extends Fragment {
	private VideoView video;
	private MediaController mediaController;
	private int videoId;

	public static final String ARG_VIDEO_ID = "video_id";

	public VideoFragment() {
		videoId = 0;
	}

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final Bundle arguments = getArguments();
		videoId = arguments.getInt(ARG_VIDEO_ID);
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_video, null);
		video = (VideoView) view.findViewById(R.id.video_view);
		if (video.getParent() != null) {
			((ViewGroup) video.getParent()).removeView(video);
		}

		String uriPath = "android.resource://" + getActivity().getPackageName() + "/" + videoId;
		video.setVideoURI(Uri.parse(uriPath));

		mediaController = new MediaController(inflater.getContext());
		mediaController.setMediaPlayer(video);
		video.setMediaController(mediaController);
		video.requestFocus();
		video.start();

		return video;
	}

	public void start() {
		if (video != null) {
			video.start();
		}
	}

	public void stop() {
		if (video != null) {
			video.stopPlayback();
		}
	}

	public void pause() {
		if (video != null) {
			video.pause();
		}
	}

	public void resume() {
		if (video != null) {
			video.resume();
		}
	}
}
