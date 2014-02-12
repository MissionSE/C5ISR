package com.missionse.kestrelweather.reports.audio;

import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;

import com.missionse.kestrelweather.reports.utils.MediaTimeConverter;

/**
 * Wrapper around Android's MediaPlayer to help sync TextViews and SeekBars.
 */
public class MediaPlayerWrapper {
	private static final String TAG = MediaPlayerWrapper.class.getSimpleName();
	private static final int SEEK_UPDATE_DELAY = 100;

	private MediaPlayer mMediaPlayer;
	private TextView mDurationTextView;
	private TextView mCurrentTextView;
	private SeekBar mSeekBar;
	private OnMediaPlayerEventListener mCompleteListener;
	private String mCurrentMediaPath;
	private boolean mNewMediaSelected = false;
	private int mSeekResumePosition = 0;
	private Handler mHandler = new Handler();
	private Runnable mUpdateSeekRunnable = new Runnable() {
		@Override
		public void run() {
			if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
				mSeekBar.setProgress(mMediaPlayer.getCurrentPosition());
				updateCurrentTime(mMediaPlayer.getCurrentPosition());
			}
			mHandler.postDelayed(this, 100);
		}
	};

	/**
	 * Default Constructor.
	 */
	public MediaPlayerWrapper() {
		mMediaPlayer = new MediaPlayer();
	}

	/**
	 * Set listener for media player events.
	 * @param listener - Instance of OnMediaPlayerEventListener.
	 */
	public void setCompleteListener(OnMediaPlayerEventListener listener) {
		mCompleteListener = listener;
		mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				onMediaCompletion();
			}
		});
	}

	/**
	 * Set the TextView that will receive updates on the duration of this selected media source.
	 * @param durationTextView The TextView to receive the updates.
	 */
	public void setDurationTextView(TextView durationTextView) {
		mDurationTextView = durationTextView;
	}

	/**
	 * Set the TextView that will receive updates on the current time the media source is playing
	 * on.
	 * @param currentTextView The TextView to receive the updates.
	 */
	public void setCurrentTextView(TextView currentTextView) {
		mCurrentTextView = currentTextView;
	}

	/**
	 * Set the SeekBar that will receive updates as well as control position/play of the
	 * media.
	 * @param seekBar The SeekBar that will receive the updates.
	 */
	public void setSeekBar(SeekBar seekBar) {
		mSeekBar = seekBar;
		mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				mHandler.removeCallbacks(mUpdateSeekRunnable);
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				mHandler.removeCallbacks(mUpdateSeekRunnable);
				mMediaPlayer.seekTo(seekBar.getProgress());
				updateSeekBar();
			}
		});
	}

	/**
	 * Set the media source that will be playing.
	 * @param path The absolute path to the media to be played.
	 */
	public void setMediaSource(String path) {
		if (path == null || path.length() == 0) {
			throw new IllegalArgumentException("Path cannot be null or empty string.");
		}
		mCurrentMediaPath = path;
		mNewMediaSelected = true;
		prepareMedia();
	}

	private void prepareMedia() {
		try {
			mMediaPlayer.reset();
			mMediaPlayer.setDataSource(mCurrentMediaPath);
			mMediaPlayer.prepare();
		} catch (Exception exp) {
			Log.e(TAG, "Unable to prepare media.", exp);
		}
	}

	/**
	 * Play the current media.
	 */
	public void playMedia() {
		Log.d(TAG, "playMedia - playing media is about to begin");
		if (mCurrentMediaPath != null && mCurrentMediaPath.length() > 0) {
			if (!mNewMediaSelected) {
				Log.d(TAG, "playMedia - Resume media from last position.");
				mMediaPlayer.start();
				mMediaPlayer.seekTo(mSeekResumePosition);
				updateSeekBar();
			} else {
				Log.d(TAG, "playMedia - Start new media. ");
				mNewMediaSelected = false;
				mMediaPlayer.start();
				mSeekBar.setMax(mMediaPlayer.getDuration());
				updateDurationTime(mMediaPlayer.getDuration());
				updateSeekBar();
			}
		}
	}

	/**
	 * Pause the current media.
	 */
	public void pauseMedia() {
		Log.d(TAG, "pauseMedia - Pausing media and setting current position to: " + mMediaPlayer.getCurrentPosition());
		mMediaPlayer.pause();
		mSeekResumePosition = mMediaPlayer.getCurrentPosition();
	}

	/**
	 * Fast-Forward the current media.
	 * @param amount The amount of time to fast-forward in milliseconds.
	 */
	public void fastForward(int amount) {
		Log.d(TAG, "fastForward - fast forwarding");
		if (mMediaPlayer != null) {
			mMediaPlayer.seekTo(mMediaPlayer.getCurrentPosition() + amount);
		}
	}

	/**
	 * Rewind the current media.
	 * @param amount The amount of time to rewind in milliseconds.
	 */
	public void rewind(int amount) {
		Log.d(TAG, "rewind - rewinding");
		if (mMediaPlayer != null) {
			mMediaPlayer.seekTo(mMediaPlayer.getCurrentPosition() - amount);
		}
	}

	private void updateCurrentTime(long time) {
		if (mCurrentTextView != null) {
			mCurrentTextView.setText(MediaTimeConverter.extractRunTime(time));
		}
	}

	private void updateDurationTime(long time) {
		if (mDurationTextView != null) {
			mDurationTextView.setText(MediaTimeConverter.extractRunTime(time));
		}
	}

	private void updateSeekBar() {
		mHandler.postDelayed(mUpdateSeekRunnable, SEEK_UPDATE_DELAY);
	}

	private void pauseSeekBar() {
		mHandler.removeCallbacks(mUpdateSeekRunnable);
	}

	private void onMediaCompletion() {
		if (mCompleteListener != null) {
			mCompleteListener.onMediaComplete();
		}
	}

	/**
	 * Cleans up all processes.
	 */
	public void destroy() {
		Log.d(TAG, "destroy - Destroying media player components");
		if (mMediaPlayer != null) {
			mMediaPlayer.reset();
			mMediaPlayer.release();
		}
		pauseSeekBar();
	}

	/**
	 * Listener for media player.
	 */
	public interface OnMediaPlayerEventListener {
		void onMediaComplete();
	}

}
