package com.missionse.uiextensions.imageview;

import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * An extension of an ImageView that displays an animated .gif file.
 */
public class GifImageView extends ImageView {

	private InputStream mStream;
	private boolean mIsPlayingGif = false;
	private GifDecoder mGifDecoder;
	private Bitmap mTmpBitmap;

	private final Handler mHandler = new Handler();

	private final Runnable mUpdateResults = new Runnable() {
		@Override
		public void run() {
			if (mTmpBitmap != null && !mTmpBitmap.isRecycled()) {
				GifImageView.this.setImageBitmap(mTmpBitmap);
			}
		}
	};

	/**
	 * Constructs a new GifImageView.
	 * @param context parent activity's context
	 */
	public GifImageView(final Context context) {
		super(context);
	}

	/**
	 * Constructs a new GifImageView.
	 * @param context parent activity's context
	 * @param attrs attributes to apply on construction
	 */
	public GifImageView(final Context context, final AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * Constructs a new GifImageView.
	 * @param context parent activity's context
	 * @param attrs attributes to apply on construction
	 * @param defStyle the defined style?
	 */
	public GifImageView(final Context context, final AttributeSet attrs, final int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * Sets the InputStream (the raw .gif file) to use as the source to be played.
	 * @param stream the input stream
	 */
	public void setInputStream(final InputStream stream) {
		mStream = stream;
	}

	/**
	 * Plays the .gif animation indefinitely.
	 */
	public void playGif() {
		mGifDecoder = new GifDecoder();
		mGifDecoder.read(mStream);

		mIsPlayingGif = true;

		new Thread(new Runnable() {
			@Override
			public void run() {
				final int n = mGifDecoder.getFrameCount();
				final int ntimes = mGifDecoder.getLoopCount();
				int repetitionCounter = 0;
				do {
					for (int i = 0; i < n; i++) {
						mTmpBitmap = mGifDecoder.getFrame(i);
						int t = mGifDecoder.getDelay(i);
						mHandler.post(mUpdateResults);
						try {
							Thread.sleep(t);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					if (ntimes != 0) {
						repetitionCounter++;
					}
				} while (mIsPlayingGif && (repetitionCounter <= ntimes));
			}
		}).start();
	}

	/**
	 * Stops rendering.
	 */
	public void stopRendering() {
		mIsPlayingGif = true;
	}
}
