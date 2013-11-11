package com.missionse.basefragmentexample.videoviewer;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import com.missionse.basefragmentexample.R;

public class VideoFragment extends Fragment {
	private VideoView video;
	private MediaController mediaController;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_video, null);
		video = (VideoView) view.findViewById(R.id.video_view);
		if (video.getParent() != null) {
			((ViewGroup) video.getParent()).removeView(video);
		}

		String uriPath = "android.resource://" + getActivity().getPackageName() + "/" + R.raw.security_video;
		video.setVideoURI(Uri.parse(uriPath));

		mediaController = new MediaController(inflater.getContext());
		mediaController.setMediaPlayer(video);
		video.setMediaController(mediaController);
		video.requestFocus();
		video.start();

		return video;
	}
}
