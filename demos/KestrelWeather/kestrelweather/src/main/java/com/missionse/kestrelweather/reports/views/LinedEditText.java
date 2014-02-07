package com.missionse.kestrelweather.reports.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Custom view to simulate lined paper.
 */
public class LinedEditText extends EditText {
	private Rect mRect;
	private Paint mPaint;

	/**
	 * Constructor.
	 * @param context - Context associated with this view
	 * @param attrs - AttributeSet that configures how this view will look.
	 */
	public LinedEditText(Context context, AttributeSet attrs) {
		super(context, attrs);

		mRect = new Rect();
		mPaint = new Paint();
		mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		mPaint.setColor(Color.BLACK);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		int height = canvas.getHeight();
		int curHeight = 0;
		Rect rect = mRect;
		Paint paint = mPaint;
		int baseline = getLineBounds(0, rect);
		for (curHeight = baseline + 1; curHeight < height;
			 curHeight += getLineHeight()) {
			canvas.drawLine(rect.left, curHeight, rect.right, curHeight, paint);
		}
		super.onDraw(canvas);
	}
}