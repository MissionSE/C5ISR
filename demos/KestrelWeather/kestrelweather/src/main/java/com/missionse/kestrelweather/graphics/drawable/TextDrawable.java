package com.missionse.kestrelweather.graphics.drawable;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;

/**
 * Provides a drawable that displays a specified character sequence.
 */
public class TextDrawable extends Drawable {
	private static final int DEFAULT_COLOR = Color.WHITE;
	private static final int DEFAULT_TEXTSIZE = 18;
	private static final float TEXT_PADDING = 0.5f;

	private Paint mPaint;
	private CharSequence mText;
	private int mIntrinsicWidth;
	private int mIntrinsicHeight;

	public TextDrawable(Resources res, CharSequence text) {
		mText = text;
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setColor(DEFAULT_COLOR);
		mPaint.setTextAlign(Paint.Align.CENTER);
		float textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
				DEFAULT_TEXTSIZE, res.getDisplayMetrics());
		mPaint.setTextSize(textSize);
		mIntrinsicWidth = (int) (mPaint.measureText(mText, 0, mText.length()) + TEXT_PADDING);
		mIntrinsicHeight = mPaint.getFontMetricsInt(null);
	}

	@Override
	public void draw(Canvas canvas) {
		Rect bounds = getBounds();

		canvas.drawText(mText, 0, mText.length(),
				bounds.centerX(), bounds.centerY(), mPaint);
	}

	@Override
	public int getOpacity() {
		return mPaint.getAlpha();
	}

	@Override
	public int getIntrinsicWidth() {
		return mIntrinsicWidth;
	}

	@Override
	public int getIntrinsicHeight() {
		return mIntrinsicHeight;
	}

	@Override
	public void setAlpha(int alpha) {
		mPaint.setAlpha(alpha);
	}

	@Override
	public void setColorFilter(ColorFilter filter) {
		mPaint.setColorFilter(filter);
	}

}