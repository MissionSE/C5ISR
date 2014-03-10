package com.missionse.kestrelweather.reports.audio;

import android.media.MediaPlayer;

/**
 * Listen for events from media player wrapper.
 */
public interface MediaPlayerListener {

	/**
	 * @param mp The media player being used.
	 * @param percent The percent complete.
	 * @see android.media.MediaPlayer.OnBufferingUpdateListener#onBufferingUpdate(android.media.MediaPlayer, int).
	 */
	void onBufferingUpdate(MediaPlayer mp, int percent);

	/**
	 * @param mp The media player being used.
	 * @see android.media.MediaPlayer.OnPreparedListener#onPrepared(android.media.MediaPlayer).
	 */
	void onPrepared(MediaPlayer mp);

	/**
	 * @param mp The media player being used.
	 * @see android.media.MediaPlayer.OnCompletionListener#onCompletion(android.media.MediaPlayer).
	 */
	void onCompletion(MediaPlayer mp);

	/**
	 * @param mp The media player being used.
	 * @param what The general error code.
	 * @param extra The specified error code.
	 * @return true if its a valid error.
	 * @see android.media.MediaPlayer.OnErrorListener#onError(android.media.MediaPlayer, int, int).
	 */
	boolean onError(MediaPlayer mp, int what, int extra);

	/**
	 * @param mp The media player being used.
	 * @param what The general info code.
	 * @param extra The specified info code.
	 * @return boolean
	 * @see android.media.MediaPlayer.OnInfoListener#onInfo(android.media.MediaPlayer, int, int).
	 */
	boolean onInfo(MediaPlayer mp, int what, int extra);

	/**
	 * @param mp The media player being used.
	 * @see android.media.MediaPlayer.OnSeekCompleteListener#onSeekComplete(android.media.MediaPlayer)
	 */
	void onSeekComplete(MediaPlayer mp);
}
