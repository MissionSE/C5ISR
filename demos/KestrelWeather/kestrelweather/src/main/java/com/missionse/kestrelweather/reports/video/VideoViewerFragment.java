package com.missionse.kestrelweather.reports.video;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.missionse.kestrelweather.R;
import com.missionse.kestrelweather.database.model.tables.Supplement;
import com.missionse.kestrelweather.database.util.MediaResolver;
import com.missionse.kestrelweather.reports.audio.MediaPlayerWrapper;

import java.io.File;

/**
 * Fragment for viewing videos.
 */
public final class VideoViewerFragment extends Fragment implements MediaPlayerWrapper.OnMediaPlayerEventListener {
	private static final String ARG_VIDEO_LOCAL_URI = "local_video_uri";
	private static final String ARG_VIDEO_REMOTE_URI = "local_remote_uri";
	private static final int SKIP_INTERVAL_IN_MILLISECONDS = 5000;
	private MediaPlayerWrapper mMediaWrapper;
	private ImageButton mPlayButton;
	private ImageButton mPauseButton;

	private String mLocalUri;
	private String mRemoteUri;


	public VideoViewerFragment() {
		mMediaWrapper = new MediaPlayerWrapper();
		mMediaWrapper.setCompleteListener(this);
	}

	/**
	 * Create an instance of VideoViewerFragment based of the given parameter.
	 * @param supplement The supplement to be viewed.
	 * @return Instance of VideoViewerFragment.
	 */
	public static VideoViewerFragment newInstance(final Supplement supplement) {
		VideoViewerFragment videoViewerFragment = new VideoViewerFragment();

		if (supplement != null) {
			Bundle arguments = new Bundle();
			arguments.putString(ARG_VIDEO_LOCAL_URI, supplement.getUri());
			arguments.putString(ARG_VIDEO_REMOTE_URI, supplement.getRemoteUri());
			videoViewerFragment.setArguments(arguments);
		}

		return videoViewerFragment;
	}

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final Bundle arguments = getArguments();
		if (arguments != null) {
			mLocalUri = arguments.getString(ARG_VIDEO_LOCAL_URI);
			mRemoteUri = arguments.getString(ARG_VIDEO_REMOTE_URI);
		}
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_report_videos_watch, null);
		if (view != null) {
			VideoView videoView = (VideoView) view.findViewById(R.id.fragment_report_video_display);
			setupSurfaceView((videoView));
			mMediaWrapper.setSeekBar((SeekBar) view.findViewById(R.id.audio_control_seek_bar));
			mMediaWrapper.setDurationTextView((TextView) view.findViewById(R.id.audio_control_end_time_time));
			mMediaWrapper.setCurrentTextView((TextView) view.findViewById(R.id.audio_control_current_time));
			mPlayButton = (ImageButton) view.findViewById(R.id.audio_control_play_button);
			mPauseButton = (ImageButton) view.findViewById(R.id.audio_control_pause_button);
			ImageButton fastForwardButton = (ImageButton) view.findViewById(R.id.audio_control_forward_button);
			ImageButton rewindButton = (ImageButton) view.findViewById(R.id.audio_control_reverse_button);

			setMediaSource();

			mPlayButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					onMediaPlayButtonPressed();
				}
			});
			mPauseButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					onMediaPauseButtonPressed();
				}
			});
			fastForwardButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					onMediaFastForwardButtonPressed();
				}
			});
			rewindButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					onMediaReverseButtonPressed();
				}
			});
		}

		return view;
	}

	private void setupSurfaceView(final View view) {
		VideoView videoView = (VideoView) view;
		final SurfaceHolder lHolder = videoView.getHolder();
		if (lHolder != null) {
			lHolder.addCallback(new SurfaceHolder.Callback() {
				@Override
				public void surfaceCreated(SurfaceHolder holder) { mMediaWrapper.setDisplay(lHolder); }

				@Override
				public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) { }

				@Override
				public void surfaceDestroyed(SurfaceHolder holder) { }
			});
		}
	}

	private void setMediaSource() {
		String localUri = mLocalUri;
		String remoteUri = mRemoteUri;
		if (validString(localUri) && uriExist(localUri)) {
			String uriPath = MediaResolver.getPath(getActivity(), Uri.parse(localUri));
			File uriAsFile = new File(uriPath);
			mMediaWrapper.setMediaSource(getActivity(), Uri.fromFile(uriAsFile));
		} else {
			if (validString(remoteUri)) {
				mMediaWrapper.setMediaSource(getString(R.string.remote_database) + remoteUri);
			}
		}
	}

	private boolean uriExist(final String uriString) {
		Uri uri = Uri.parse(uriString);
		String uriPath = MediaResolver.getPath(getActivity(), uri);
		File uriAsFile = new File(uriPath);
		return uriAsFile.exists();
	}

	private static boolean validString(final String string) {
		return string != null && string.length() > 0;
	}

	private void showPlayButton() {
		mPauseButton.setVisibility(View.INVISIBLE);
		mPlayButton.setVisibility(View.VISIBLE);
	}

	private void onMediaReverseButtonPressed() {
		mMediaWrapper.rewind(SKIP_INTERVAL_IN_MILLISECONDS);
	}

	private void onMediaFastForwardButtonPressed() {
		mMediaWrapper.fastForward(SKIP_INTERVAL_IN_MILLISECONDS);
	}

	private void onMediaPauseButtonPressed() {
		if (mPauseButton.getVisibility() == View.VISIBLE) {
			showPlayButton();
			mMediaWrapper.pauseMedia();
		}
	}

	private void onMediaPlayButtonPressed() {
		showPauseButton();
		mMediaWrapper.playMedia();
	}

	private void showPauseButton() {
		mPlayButton.setVisibility(View.INVISIBLE);
		mPauseButton.setVisibility(View.VISIBLE);
	}

	@Override
	public void onMediaComplete() {
		showPlayButton();
	}
}
