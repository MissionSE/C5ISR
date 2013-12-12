package com.missionse.uiextensions.imageview;

import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.ImageView;

public class GifImageView extends ImageView {

	private InputStream mStream;

	private boolean mIsPlayingGif = false;

	private GifDecoder mGifDecoder;

	private Bitmap mTmpBitmap;

	final Handler mHandler = new Handler();

	final Runnable mUpdateResults = new Runnable() {
		@Override
		public void run() {
			if (mTmpBitmap != null && !mTmpBitmap.isRecycled()) {
				GifImageView.this.setImageBitmap(mTmpBitmap);
			}
		}
	};

	public GifImageView(final Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public GifImageView(final Context context, final AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public GifImageView(final Context context, final AttributeSet attrs, final int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public void setInputStream(final InputStream stream) {
		mStream = stream;
	}

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

	public void stopRendering() {
		mIsPlayingGif = true;
	}

}
