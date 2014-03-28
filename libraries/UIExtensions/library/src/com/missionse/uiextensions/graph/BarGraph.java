package com.missionse.uiextensions.graph;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.drawable.NinePatchDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.missionse.uiextensions.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Draws a bar graph.
 */
public class BarGraph extends View {

	/**
	 * Describes a listener to be called when a bar is clicked.
	 */
	public interface OnBarClickedListener {
		void onClick(Bar bar);
	}

	private static final int VALUE_FONT_SIZE = 30;
	private static final int AXIS_LABEL_FONT_SIZE = 15;
	private static final float LABEL_PADDING_MULTIPLIER = 1.6f;
	private static final int POPUP_FONT_SIZE = 24;

	private Context mContext;
	private List<Bar> mBars = new ArrayList<Bar>();
	private OnBarClickedListener mListener;
	private Bitmap mRenderedBarGraph = null;
	private Paint mReusablePaint = new Paint();
	private Rect mReusableRectangle = new Rect();
	private boolean mShowBarText = true;
	private boolean mShowAxis = true;
	private boolean mShouldUpdate = false;

	/**
	 * Constructor.
	 * @param context the application context
	 */
	public BarGraph(final Context context) {
		super(context);
		mContext = context;
	}

	/**
	 * Constructor.
	 * @param context the application context
	 * @param attrs the attribute set
	 */
	public BarGraph(final Context context, final AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
	}

	/**
	 * Sets whether or not the bar text should be shown.
	 * @param show whether or not to show the bar text
	 */
	public void setShowBarText(final boolean show) {
		mShowBarText = show;
	}

	/**
	 * Sets whether or not to show the axes.
	 * @param show whether or not to show the axes.
	 */
	public void setShowAxis(boolean show) {
		mShowAxis = show;
	}

	/**
	 * Sets the bars to be used in this bar graph. Will overwrite any existing bars.
	 * @param bars the bars to use
	 */
	public void setBars(final List<Bar> bars) {
		mBars = bars;
		mShouldUpdate = true;
		postInvalidate();
	}

	/**
	 * Retrieves the bars currently being used by this bar graph.
	 * @return the bars in use
	 */
	public List<Bar> getBars() {
		return mBars;
	}

	/**
	 * Renders the bar graph.
	 * @param canvas the canvas with which to draw
	 */
	public void onDraw(Canvas canvas) {
		if (mRenderedBarGraph == null || mShouldUpdate) {
			mRenderedBarGraph = renderBarGraph(Bitmap.createBitmap(getWidth(), getHeight(), Config.ARGB_8888));
			mShouldUpdate = false;
		}

		canvas.drawBitmap(mRenderedBarGraph, 0, 0, null);
	}

	private Bitmap renderBarGraph(final Bitmap bitmap) {
		Canvas canvas = new Canvas(bitmap);
		canvas.drawColor(Color.TRANSPARENT);
		NinePatchDrawable popup = (NinePatchDrawable) getResources().getDrawable(R.drawable.popup_black);

		float maxValue = 0;
		float padding = 7 * mContext.getResources().getDisplayMetrics().density;
		int selectPadding = (int) (4 * mContext.getResources().getDisplayMetrics().density);
		float bottomPadding = 30 * mContext.getResources().getDisplayMetrics().density;

		float usableHeight;
		if (mShowBarText) {
			this.mReusablePaint.setTextSize(VALUE_FONT_SIZE * mContext.getResources().getDisplayMetrics().scaledDensity);
			Rect r3 = new Rect();
			this.mReusablePaint.getTextBounds("$", 0, 1, r3);
			usableHeight = getHeight() - bottomPadding - Math.abs(r3.top - r3.bottom) - 24
				* mContext.getResources().getDisplayMetrics().density;
		} else {
			usableHeight = getHeight() - bottomPadding;
		}

		// Draw x-axis line
		if (mShowAxis){
			mReusablePaint.setColor(Color.BLACK);
			mReusablePaint.setStrokeWidth(2 * mContext.getResources().getDisplayMetrics().density);
			mReusablePaint.setAlpha(50);
			mReusablePaint.setAntiAlias(true);
			canvas.drawLine(0, getHeight() - bottomPadding + 10 * mContext.getResources().getDisplayMetrics().density,
				getWidth(), getHeight() - bottomPadding + 10 * mContext.getResources().getDisplayMetrics().density, mReusablePaint);
		}
		float barWidth = (getWidth() - (padding * 2) * mBars.size()) / mBars.size();

		// Maximum y value = sum of all values.
		for (final Bar bar : mBars) {
			if (bar.getValue() > maxValue) {
				maxValue = bar.getValue();
			}
		}

		int barCount = 0;
		for (final Bar bar : mBars) {
			// Set bar bounds
			int left = (int) ((padding * 2) * barCount + padding + barWidth * barCount);
			int top = (int) (getHeight() - bottomPadding - (usableHeight * (bar.getValue() / maxValue)));
			int right = (int) ((padding * 2) * barCount + padding + barWidth * (barCount + 1));
			int bottom = (int) (getHeight() - bottomPadding);
			mReusableRectangle.set(left, top, right, bottom);

			// Draw bar
			mReusablePaint.setColor(bar.getColor());
			mReusablePaint.setAlpha(255);
			canvas.drawRect(mReusableRectangle, mReusablePaint);

			// Create selection region
			Path path = new Path();
			path.addRect(new RectF(mReusableRectangle.left - selectPadding, mReusableRectangle.top - selectPadding,
				mReusableRectangle.right + selectPadding, mReusableRectangle.bottom + selectPadding), Path.Direction.CW);
			bar.setPath(path);
			bar.setRegion(new Region(mReusableRectangle.left - selectPadding, mReusableRectangle.top - selectPadding,
				mReusableRectangle.right + selectPadding, mReusableRectangle.bottom + selectPadding));

			// Draw x-axis label text
			if (mShowAxis){
				mReusablePaint.setTextSize(AXIS_LABEL_FONT_SIZE * mContext.getResources().getDisplayMetrics().scaledDensity);
				float textWidth = mReusablePaint.measureText(bar.getName());
				while (right - left + (padding * LABEL_PADDING_MULTIPLIER) < textWidth)
				{
					mReusablePaint.setTextSize(mReusablePaint.getTextSize() -  1);
					textWidth = mReusablePaint.measureText(bar.getName());
				}
				int x = (int) (((mReusableRectangle.left + mReusableRectangle.right) / 2) - (textWidth / 2));
				int y = (int) (getHeight() - 3 * mContext.getResources().getDisplayMetrics().scaledDensity);
				canvas.drawText(bar.getName(), x, y, mReusablePaint);
			}

			// Draw value text
			if (mShowBarText) {
				mReusablePaint.setTextSize(VALUE_FONT_SIZE * mContext.getResources().getDisplayMetrics().scaledDensity);
				mReusablePaint.setColor(Color.WHITE);
				Rect r2 = new Rect();
				mReusablePaint.getTextBounds(bar.getValueString(), 0, 1, r2);

				int boundLeft = (int) (((mReusableRectangle.left + mReusableRectangle.right) / 2)
					- (mReusablePaint.measureText(bar.getValueString()) / 2) - 10
					* mContext.getResources().getDisplayMetrics().density);
				int boundTop = (int) (mReusableRectangle.top + (r2.top - r2.bottom) - 18
					* mContext.getResources().getDisplayMetrics().density);
				int boundRight = (int) (((mReusableRectangle.left + mReusableRectangle.right) / 2)
					+ (mReusablePaint.measureText(bar.getValueString()) / 2) + 10
					* mContext.getResources().getDisplayMetrics().density);

				if (boundLeft < mReusableRectangle.left) {
					boundLeft = mReusableRectangle.left - ((int) padding / 2);
				}

				if (boundRight > mReusableRectangle.right) {
					boundRight = mReusableRectangle.right + ((int) padding /2);
				}

				popup.setBounds(boundLeft, boundTop, boundRight, mReusableRectangle.top);
				popup.draw(canvas);

				mReusablePaint.setTextSize(POPUP_FONT_SIZE);
				canvas.drawText(bar.getValueString(), (int) (((mReusableRectangle.left + mReusableRectangle.right) / 2)
					- (mReusablePaint.measureText(bar.getValueString())) / 2), mReusableRectangle.top
					- (mReusableRectangle.top - boundTop) / 2f + (float) Math.abs(r2.top - r2.bottom) / 2f * 0.7f,
					mReusablePaint);
			}
			if (bar.isSelected()) {
				mReusablePaint.setColor(Color.parseColor("#33B5E5"));
				mReusablePaint.setAlpha(100);
				canvas.drawPath(bar.getPath(), mReusablePaint);
				mReusablePaint.setAlpha(255);
			}
			barCount++;
		}
		return bitmap;
	}

	@Override
	public boolean onTouchEvent(final MotionEvent event) {
		Point point = new Point((int) event.getX(), (int) event.getY());

		mShouldUpdate = false;
		for (final Bar bar : mBars) {
			Region region = new Region();
			region.setPath(bar.getPath(), bar.getRegion());
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				if (region.contains(point.x, point.y)) {
					if (!bar.isSelected()) {
						bar.setSelected(true);
						mShouldUpdate = true;
					}
				} else {
					if (bar.isSelected()) {
						bar.setSelected(false);
						mShouldUpdate = true;
					}
				}
			} else if (event.getAction() == MotionEvent.ACTION_UP) {
				if (region.contains(point.x, point.y) && mListener != null) {
					mListener.onClick(bar);
				}
			} else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
				if (bar.isSelected()) {
					bar.setSelected(false);
					mShouldUpdate = true;
				}
			}
		}
		if (mShouldUpdate) {
			postInvalidate();
		}

		return true;
	}

	@Override
	protected void onDetachedFromWindow() {
		if (mRenderedBarGraph != null) {
			mRenderedBarGraph.recycle();
		}
		super.onDetachedFromWindow();
	}

	/**
	 * Sets the listener to be called back when a bar is selected.
	 * @param listener the listener
	 */
	public void setOnBarClickedListener(final OnBarClickedListener listener) {
		mListener = listener;
	}
}

