package com.missionse.uiextensions.graph;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * A custom view that shows a Pie Chart.
 */
public class PieChart extends View {

	/**
	 * A listener to be called back when a slice is selected.
	 */
	public interface OnSliceClickedListener {
		void onClick(PieSlice slice);
	}

	private static final float DEFAULT_THICKNESS = 25.0f;
	private static final int INITIAL_ANGLE = 270;
	private static final int DEFAULT_PADDING = 2;
	private static final int SELECTED_PADDING = 4;
	private static final int DEGREES_IN_CIRCLE = 360;

	private static final int SOMEWHAT_TRANSPARENT = 100;
	private static final int OPAQUE = 255;

	private List<PieSlice> mSlices = new ArrayList<PieSlice>();
	private Paint mPaint = new Paint();

	private Path mReusablePath = new Path();
	private RectF mReusableRectF = new RectF();
	private Region mReusableRegion = new Region();

	private int mThickness;
	private OnSliceClickedListener mOnSliceClickedListener;

	private boolean mDrawCompleted = false;

	/**
	 * Constructor.
	 * @param context the application context
	 */
	public PieChart(final Context context) {
		super(context);
		mThickness = (int) (DEFAULT_THICKNESS * context.getResources().getDisplayMetrics().density);
	}

	/**
	 * Constructor.
	 * @param context the application context
	 * @param attrs the attributes
	 */
	public PieChart(Context context, AttributeSet attrs) {
		super(context, attrs);
		mThickness = (int) (DEFAULT_THICKNESS * context.getResources().getDisplayMetrics().density);
	}

	/**
	 * Called when the pie should be drawn.
	 * @param canvas the canvas with which to draw
	 */
	public void onDraw(final Canvas canvas) {
		canvas.drawColor(Color.TRANSPARENT);

		mPaint.reset();
		mPaint.setAntiAlias(true);

		float xMidPoint = getWidth() / 2;
		float yMidPoint = getHeight() / 2;
		float radius;
		if (xMidPoint < yMidPoint) {
			radius = xMidPoint;
		} else {
			radius = yMidPoint;
		}

		radius -= DEFAULT_PADDING;
		float innerRadius = radius - mThickness;

		int totalValue = 0;
		for (final PieSlice slice : mSlices) {
			totalValue += slice.getValue();
		}

		//int count = 0;
		float currentAngle = INITIAL_ANGLE;
		float currentSweep = 0;
		for (final PieSlice slice : mSlices) {
			mReusablePath.reset();
			mPaint.setColor(slice.getColor());

			currentSweep = slice.getValue() / totalValue * DEGREES_IN_CIRCLE;

			mReusableRectF.set(xMidPoint - radius, yMidPoint - radius, xMidPoint + radius, yMidPoint + radius);
			mReusablePath.arcTo(mReusableRectF, currentAngle + DEFAULT_PADDING, currentSweep - DEFAULT_PADDING);
			mReusableRectF.set(xMidPoint - innerRadius, yMidPoint - innerRadius, xMidPoint + innerRadius, yMidPoint + innerRadius);
			mReusablePath.arcTo(mReusableRectF, (currentAngle + DEFAULT_PADDING) + (currentSweep - DEFAULT_PADDING),
					-(currentSweep - DEFAULT_PADDING));
			mReusablePath.close();
			slice.setPath(mReusablePath);

			mReusableRegion.set((int) (xMidPoint - radius), (int) (yMidPoint - radius), (int) (xMidPoint + radius),
				(int) (yMidPoint + radius));
			slice.setRegion(mReusableRegion);

			canvas.drawPath(mReusablePath, mPaint);

			if (slice.isSelected() && mOnSliceClickedListener != null) {
				mReusablePath.reset();
				mPaint.setColor(slice.getColor());
				mPaint.setColor(Color.parseColor("#33B5E5"));
				mPaint.setAlpha(SOMEWHAT_TRANSPARENT);


				if (mSlices.size() > 1) {
					mReusableRectF.set(xMidPoint - radius - (SELECTED_PADDING),
						yMidPoint - radius - (SELECTED_PADDING),
						xMidPoint + radius + (SELECTED_PADDING),
						yMidPoint + radius + (SELECTED_PADDING));
					mReusablePath.arcTo(mReusableRectF, currentAngle, currentSweep + DEFAULT_PADDING);
					mReusableRectF.set(xMidPoint - innerRadius + (SELECTED_PADDING),
						yMidPoint - innerRadius + (SELECTED_PADDING),
						xMidPoint + innerRadius - (SELECTED_PADDING),
						yMidPoint + innerRadius - (SELECTED_PADDING));
					mReusablePath.arcTo(mReusableRectF, currentAngle + currentSweep + DEFAULT_PADDING,
						-(currentSweep + DEFAULT_PADDING));
					mReusablePath.close();
				} else {
					mReusablePath.addCircle(xMidPoint, yMidPoint, radius + DEFAULT_PADDING, Direction.CW);
				}

				canvas.drawPath(mReusablePath, mPaint);
				mPaint.setAlpha(OPAQUE);
			}

			currentAngle = currentAngle + currentSweep;
		}
		mDrawCompleted = true;
	}

	@Override
	public boolean onTouchEvent(final MotionEvent event) {
		if (mDrawCompleted) {
			Point point = new Point((int) event.getX(), (int) event.getY());
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				for (final PieSlice slice : mSlices) {
					Region region = new Region();
					region.setPath(slice.getPath(), slice.getRegion());
					if (region.contains(point.x, point.y)) {
						slice.select();
					} else {
						slice.unselect();
					}
				}
			} else if (event.getAction() == MotionEvent.ACTION_UP) {
				for (final PieSlice slice : mSlices) {
					Region region = new Region();
					region.setPath(slice.getPath(), slice.getRegion());
					if (region.contains(point.x, point.y) && mOnSliceClickedListener != null) {
						if (slice.isSelected()) {
							mOnSliceClickedListener.onClick(slice);
						}
					}
				}
			}

			if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_UP) {
				postInvalidate();
			}
		}
		return true;
	}

	/**
	 * Retrieves all of the slices used in this pie chart.
	 * @return the slices
	 */
	public List<PieSlice> getSlices() {
		return mSlices;
	}

	/**
	 * Sets the slices to be used in this pie chart. Will override any current slices set.
	 * @param slices the slices to add
	 */
	public void setSlices(final List<PieSlice> slices) {
		mSlices = slices;
		postInvalidate();
	}

	/**
	 * Retrieves the slice at a given index.
	 * @param index the index
	 * @return the slice
	 */
	public PieSlice getSlice(final int index) {
		return mSlices.get(index);
	}

	/**
	 * Adds a slice to the list of slices.
	 * @param slice the slice to add
	 */
	public void addSlice(final PieSlice slice) {
		mSlices.add(slice);
		postInvalidate();
	}

	/**
	 * Adds an OnSliceClickedListener to be called back when a slice is selected.
	 * @param listener the listener to callback
	 */
	public void setOnSliceClickedListener(final OnSliceClickedListener listener) {
		this.mOnSliceClickedListener = listener;
	}

	/**
	 * Gets the thickness used by this pie chart.
	 * @return the thickness
	 */
	public int getThickness() {
		return mThickness;
	}

	/**
	 * Sets the thickness to be used by this pie chart.
	 * @param thickness the thickness
	 */
	public void setThickness(final int thickness) {
		this.mThickness = thickness;
		postInvalidate();
	}

	/**
	 * Removes all of the slices in the pie chart.
	 */
	public void removeSlices() {
		mSlices.clear();
		postInvalidate();
	}
}

