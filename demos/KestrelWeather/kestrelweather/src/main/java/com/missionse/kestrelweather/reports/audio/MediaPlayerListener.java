package com.missionse.kestrelweather.reports.audio;

import android.media.MediaPlayer;

/**
 * Listen for events from media player wrapper.
 */
public interface MediaPlayerListener {

	/**
	 * @see android.media.MediaPlayer.OnBufferingUpdateListener#onBufferingUpdate(android.media.MediaPlayer, int).
	 * @param mp The media player being used.
	 * @param percent The percent complete.
	 */
	void onBufferingUpdate(MediaPlayer mp, int percent);

	/**
	 * @see android.media.MediaPlayer.OnPreparedListener#onPrepared(android.media.MediaPlayer).
	 * @param mp The media player being used.
	 */
	void onPrepared(MediaPlayer mp);

	/**
	 * @see android.media.MediaPlayer.OnCompletionListener#onCompletion(android.media.MediaPlayer).
	 * @param mp The media player being used.
	 */
	void onCompletion(MediaPlayer mp);

	/**
	 * @see android.media.MediaPlayer.OnErrorListener#onError(android.media.MediaPlayer, int, int).
	 * @param mp The media player being used.
	 * @param what The general error code.
	 * @param extra The specified error code.
	 * @return true if its a valid error.
	 */
	boolean onError(MediaPlayer mp, int what, int extra);

	/**
	 * @see android.media.MediaPlayer.OnInfoListener#onInfo(android.media.MediaPlayer, int, int).
	 * @param mp The media player being used.
	 * @param what The general info code.
	 * @param extra The specified info code.
	 * @return boolean
	 */
	boolean onInfo(MediaPlayer mp, int what, int extra);

	/**
	 * @see android.media.MediaPlayer.OnSeekCompleteListener#onSeekComplete(android.media.MediaPlayer)
	 * @param mp The media player being used.
	 */
	void onSeekComplete(MediaPlayer mp);
}
