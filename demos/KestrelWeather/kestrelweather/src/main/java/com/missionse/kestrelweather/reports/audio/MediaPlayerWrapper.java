package com.missionse.kestrelweather.reports.audio;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
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
	private Uri mCurrentMediaUri;
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
	 * Constructor.
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
	 * @param context The current context.
	 * @param uri The URI that represents the media to be played.
	 */
	public void setMediaSource(final Context context, final Uri uri) {
		mCurrentMediaUri = uri;
		if (uri != null) {
			mNewMediaSelected = true;

			try {
				mMediaPlayer.reset();
				mMediaPlayer.setDataSource(context, mCurrentMediaUri);
				mMediaPlayer.prepare();
			} catch (Exception exception) {
				Log.e(TAG, "Unable to prepare media.", exception);
			}
		}
	}

	/**
	 * Play the current media.
	 */
	public void playMedia() {
		if (mCurrentMediaUri != null) {
			if (!mNewMediaSelected) {
				mMediaPlayer.start();
				mMediaPlayer.seekTo(mSeekResumePosition);
				updateSeekBar();
			} else {
				mNewMediaSelected = false;
				mMediaPlayer.start();
				mSeekBar.setMax(mMediaPlayer.getDuration());
				updateDurationTime(mMediaPlayer.getDuration());
				updateSeekBar();
			}
		} else {
			pauseSeekBar();
			mMediaPlayer.pause();
			mSeekBar.setProgress(0);
			mSeekBar.setMax(0);
			updateCurrentTime(0);
			updateDurationTime(0);
		}
	}

	/**
	 * Pause the current media.
	 */
	public void pauseMedia() {
		mMediaPlayer.pause();
		mSeekResumePosition = mMediaPlayer.getCurrentPosition();
	}

	/**
	 * Fast-Forward the current media.
	 * @param amount The amount of time to fast-forward in milliseconds.
	 */
	public void fastForward(int amount) {
		if (mMediaPlayer != null && mCurrentMediaUri != null) {
			mMediaPlayer.seekTo(mMediaPlayer.getCurrentPosition() + amount);
		}
	}

	/**
	 * Rewind the current media.
	 * @param amount The amount of time to rewind in milliseconds.
	 */
	public void rewind(int amount) {
		if (mMediaPlayer != null && mCurrentMediaUri != null) {
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
		if (mMediaPlayer != null) {
			mMediaPlayer.release();
		}
		pauseSeekBar();
	}

	/**
	 * Listener for media player.
	 */
	public interface OnMediaPlayerEventListener {
		/**
		 * Callback function to notify when media playback has completed.
		 */
		void onMediaComplete();
	}
}
