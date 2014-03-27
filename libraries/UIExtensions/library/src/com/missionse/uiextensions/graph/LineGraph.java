package com.missionse.uiextensions.graph;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.NinePatchDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.missionse.uiextensions.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A representation of a Line Graph.
 */
public class LineGraph extends View {

	/**
	 * Defines a listener to be called back when a point is selected.
	 */
	public interface OnPointClickedListener {
		void onClick(Line line, LinePoint linePoint);
	}

	private static final int TRANSPARENT = 100;
	private static final int OPAQUE = 255;

	private static final int DEFAULT_FILL_STROKE_WIDTH = 3;
	private static final int DEFAULT_FILL_START = 10;
	private static final int DEFAULT_FILL_SPACING = 20;
	private static final int DEFAULT_FILL_STAGGER = 4;

	private static final int POINT_COLLISION_RADIUS = 60;
	private static final int POPUP_FONT_SIZE = 24;
	private static final int POPUP_SIDE_PADDING = 10;
	private static final int POPUP_TOP_PADDING = 18;
	private static final int POPUP_BOTTOM_GAP = 50;

	private static final int AXES_PADDING = 80;
	private static final int AXES_FONT_SIZE = 14;
	private static final int AXES_TEXT_PADDING = 6;

	private List<Line> mLines = new ArrayList<Line>();

	private Paint mReusablePaint = new Paint();
	private Path mReusablePath = new Path();

	private OnPointClickedListener mOnPointClickedListener;

	private Bitmap mRenderedLineGraph = null;
	private boolean mShouldUpdate = false;

	private boolean mUserSetXBound = false, mUserSetYBound = false;
	private float mLowerBoundX = 0, mLowerBoundY = 0, mUpperBoundX = 0, mUpperBoundY = 0;
	private String mLowerBoundXPrintable, mLowerBoundYPrintable, mUpperBoundXPrintable, mUpperBoundYPrintable;

	/**
	 * Constructor.
	 * @param context the application context
	 */
	public LineGraph(final Context context) {
		super(context);
	}

	/**
	 * Constructor.
	 * @param context the application context
	 * @param attrs the attributes
	 */
	public LineGraph(final Context context, final AttributeSet attrs) {
		super(context, attrs);
	}

	private int getPixelForDip(int dipValue) {
		if (getResources() != null) {
			return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, getResources().getDisplayMetrics());
		}
		return 1;
	}

	/**
	 * Retrieves the lines used by this line graph.
	 * @return the lines
	 */
	public List<Line> getLines() {
		return mLines;
	}

	/**
	 * Sets the lines to be used by this line graph. This will overwrite any existing lines.
	 * @param lines the lines to be used
	 */
	public void setLines(final List<Line> lines) {
		mLines = lines;
		mShouldUpdate = true;
		postInvalidate();
	}

	/**
	 * Adds a line to be used by this line graph.
	 * @param line a line to add
	 */
	public void addLine(final Line line) {
		mLines.add(line);
		mShouldUpdate = true;
		postInvalidate();
	}

	/**
	 * Removes all lines used by this line graph.
	 */
	public void removeAllLines() {
		mLines.clear();
		mShouldUpdate = true;
		postInvalidate();
	}

	/**
	 * Removes the specified line from this line graph.
	 * @param line the line to remove
	 */
	public void removeLine(final Line line) {
		mLines.remove(line);
		mShouldUpdate = true;
		postInvalidate();
	}

	/**
	 * Sets the X axis values, and the range at which to scale all x values.
	 * @param lower the lower bound x value
	 * @param upper the upper bound x value
	 */
	public void setXRange(final float lower, final float upper) {
		if (lower == upper) {
			throw new IllegalArgumentException("Lower and upper bounds cannot be the same!");
		}
		mLowerBoundX = lower;
		mUpperBoundX = upper;
		mUserSetXBound = true;
	}

	/**
	 * Sets the values to be printed for the x axis.
	 * @param lowerPrintable the value to be printed for the lower bound of the x axis
	 * @param upperPrintable the value to be printed for the upper bound of the x axis
	 */
	public void setXAxisBounds(final String lowerPrintable, final String upperPrintable) {
		mLowerBoundXPrintable = lowerPrintable;
		mUpperBoundXPrintable = upperPrintable;
	}

	/**
	 * Sets the values to be printed for the y axis.
	 * @param lowerPrintable the value to be printed for the lower bound of the y axis
	 * @param upperPrintable the value to be printed for the upper bound of the y axis
	 */
	public void setYAxisBounds(final String lowerPrintable, final String upperPrintable) {
		mLowerBoundYPrintable = lowerPrintable;
		mUpperBoundYPrintable = upperPrintable;
	}

	/**
	 * Sets the Y axis values, and the range at which to scale all y values.
	 * @param lower the lower bound y value
	 * @param upper the upper bound y value
	 */
	public void setYRange(final float lower, final float upper) {
		if (lower == upper) {
			throw new IllegalArgumentException("Lower and upper bounds cannot be the same!");
		}
		mLowerBoundY = lower;
		mUpperBoundY = upper;
		mUserSetYBound = true;
	}

	/**
	 * Notifies this line graph of a draw command, rendering all lines for the line graph. Specifically, the graph is
	 * rendered into a bitmap, which is then subsequently rendered, and the bitmap won't be re-rendered unless changes
	 * are made to the underlying data structure.
	 * @param canvas the canvas with which to render all lines
	 */
	public void onDraw(final Canvas canvas) {
		if (mRenderedLineGraph == null || mShouldUpdate) {
			mRenderedLineGraph = renderLineGraph(Bitmap.createBitmap(getWidth(), getHeight(), Config.ARGB_8888));
			mShouldUpdate = false;
		}

		canvas.drawBitmap(mRenderedLineGraph, 0, 0, null);
	}

	/**
	 * Renders a line graph into the specified bitmap by:
	 *  1. Correcting upper and lower bounds.
	 *  2. Setting staggered line fills on each line to be filled.
	 *  3. Rendering the line fills.
	 *  4. Rendering the axes.
	 *  5. Rendering the line strokes (the lines themselves).
	 *  6. Rendering the points on the lines.
	 *  7. Rendering any popups for selected points.
	 * @param bitmap the bitmap to render into
	 * @return a bitmap into which the line graph has been rendered
	 */
	private Bitmap renderLineGraph(final Bitmap bitmap) {
		Canvas bitmapRendererCanvas = new Canvas(bitmap);
		validateAndCorrectUpperAndLowerBounds();
		reconcileLineFills(mLines);
		renderLineFills(bitmapRendererCanvas);
		renderAxes(bitmapRendererCanvas);
		renderLineStrokes(bitmapRendererCanvas);
		renderLinePoints(bitmapRendererCanvas);
		renderTitlePopups(bitmapRendererCanvas);
		return bitmap;
	}

	/**
	 * Validates and adjusts the upper and lower bounds for the x and y values if the user has not set them.
	 */
	private void validateAndCorrectUpperAndLowerBounds() {
		if (!mUserSetXBound) {
			for (final Line line : mLines) {
				if (line == mLines.get(0)) {
					mLowerBoundX = line.getLowerBoundX();
					mUpperBoundX = line.getUpperBoundX();
				} else {
					if (line.getLowerBoundX() < mLowerBoundX) {
						mLowerBoundX = line.getLowerBoundX();
					}
					if (line.getUpperBoundX() > mUpperBoundX) {
						mUpperBoundX = line.getUpperBoundX();
					}
				}
			}
		}
		if (!mUserSetYBound) {
			for (final Line line : mLines) {
				if (line == mLines.get(0)) {
					mLowerBoundY = line.getLowerBoundY();
					mUpperBoundY = line.getUpperBoundY();
				} else {
					if (line.getLowerBoundY() < mLowerBoundY) {
						mLowerBoundY = line.getLowerBoundY();
					}
					if (line.getUpperBoundY() > mUpperBoundY) {
						mUpperBoundY = line.getUpperBoundY();
					}
				}
			}
		}
	}

	/**
	 * Cycles through line fills, so that sequentially rendered lines have staggered fills.
	 * @param lines the lines for which to render fills
	 */
	private void reconcileLineFills(final List<Line> lines) {
		LineGraphFillType currentFillType = LineGraphFillType.BACKSLASH;
		int currentFillOffset = 0;
		for (final Line line : lines) {
			if (line.isFilled()) {
				line.setFillType(currentFillType);
				line.setFillOffset(currentFillOffset);
				currentFillType = LineGraphFillType.getNext(currentFillType);
				if (currentFillType == LineGraphFillType.BACKSLASH) {
					currentFillOffset += DEFAULT_FILL_STAGGER;
				}
			}
		}
	}

	/**
	 * Renders all line fills, by:
	 *  1. Rendering the line fills over the entire drawable graph space.
	 *  2. Rendering CLEAR to occlude the fill where it should not show.
	 *  3. Rendering CLEAR for the axes padding on the edges of the graph.
	 *  4. After each line fill is rendered separately in its own bitmap, each bitmap is overlaid on the same space.
	 * @param canvas the canvas to use to render
	 */
	private void renderLineFills(final Canvas canvas) {
		List<Bitmap> lineFillBitmaps = new ArrayList<Bitmap>();
		for (final Line line : mLines) {
			if (line.isFilled()) {
				Bitmap lineFill = Bitmap.createBitmap(getWidth(), getHeight(), Config.ARGB_8888);
				renderInitialLineFills(line, lineFill);
				renderLineFillOcclusions(line, lineFill);
				renderAxesPaddingFillOcclusions(lineFill);
				lineFillBitmaps.add(lineFill);
			}
		}

		for (Bitmap bitmap : lineFillBitmaps) {
			mReusablePaint.reset();
			mReusablePaint.setAntiAlias(true);
			canvas.drawBitmap(bitmap, 0, 0, mReusablePaint);
		}
	}

	/**
	 * Renders initial line fills.
	 * @param line the line for which to render a fill
	 * @param bitmap the bitmap in which to render
	 */
	private void renderInitialLineFills(final Line line, final Bitmap bitmap) {
		Canvas canvas = new Canvas(bitmap);

		mReusablePaint.reset();
		mReusablePaint.setAntiAlias(true);
		mReusablePaint.setColor(line.getFillColor());
		mReusablePaint.setAlpha(TRANSPARENT);
		mReusablePaint.setStrokeWidth(DEFAULT_FILL_STROKE_WIDTH);

		if (line.getFillType() == LineGraphFillType.BACKSLASH) {
			for (int i = DEFAULT_FILL_START + line.getFillOffset(); i - getWidth() < getHeight(); i += DEFAULT_FILL_SPACING) {
				canvas.drawLine(i, getHeight(), 0, getHeight() - i, mReusablePaint);
			}
		} else if (line.getFillType() == LineGraphFillType.FORWARDSLASH) {
			for (int i = getWidth() - DEFAULT_FILL_START + line.getFillOffset();
				 i > -1 * getHeight() - getWidth(); i -= DEFAULT_FILL_SPACING) {
				canvas.drawLine(i, getHeight(), getWidth(), getHeight() - (getWidth() - i), mReusablePaint);
			}
		} else if (line.getFillType() == LineGraphFillType.HORIZONTAL) {
			for (int i = DEFAULT_FILL_START + line.getFillOffset(); i < getHeight(); i += DEFAULT_FILL_SPACING) {
				canvas.drawLine(0, i, 0, getWidth(), mReusablePaint);
			}
		} else if (line.getFillType() == LineGraphFillType.VERTICAL) {
			for (int i = DEFAULT_FILL_START + line.getFillOffset(); i < getWidth(); i += DEFAULT_FILL_SPACING) {
				canvas.drawLine(i, 0, getHeight(), 0, mReusablePaint);
			}
		}
	}

	private void renderLineFillOcclusions(final Line line, final Bitmap bitmap) {
		Canvas canvas = new Canvas(bitmap);

		float usableHeight = getHeight() - (2 * AXES_PADDING);
		float usableWidth = getWidth() - (2 * AXES_PADDING);

		mReusablePaint.reset();
		mReusablePaint.setXfermode(new PorterDuffXfermode(android.graphics.PorterDuff.Mode.CLEAR));
		mReusablePath.reset();

		float lastXPosition, newXPosition, lastYPosition, newYPosition;
		float xRelativePositionRatio, yRelativePositionRatio;
		if (line.getPoints().size() > 0) {

			xRelativePositionRatio = (line.getPoints().get(0).getX() - mLowerBoundX) / (mUpperBoundX - mLowerBoundX);
			yRelativePositionRatio = (line.getPoints().get(0).getY() - mLowerBoundY) / (mUpperBoundY - mLowerBoundY);

			lastXPosition = AXES_PADDING + (xRelativePositionRatio * usableWidth);
			lastYPosition = getHeight() - AXES_PADDING - (usableHeight * yRelativePositionRatio);

			//Clear before the first point, from top to bottom.
			mReusablePath.moveTo(0, getHeight());
			mReusablePath.lineTo(lastXPosition, getHeight());
			mReusablePath.lineTo(lastXPosition, 0);
			mReusablePath.lineTo(0, 0);
			mReusablePath.close();
			canvas.drawPath(mReusablePath, mReusablePaint);

			mReusablePath.reset();
			mReusablePath.moveTo(lastXPosition, lastYPosition);

			//Clear above each line segment.
			for (final LinePoint point : line.getPoints()) {
				if (point == line.getPoints().get(0)) {
					continue;
				}
				xRelativePositionRatio = (point.getX() - mLowerBoundX) / (mUpperBoundX - mLowerBoundX);
				yRelativePositionRatio = (point.getY() - mLowerBoundY) / (mUpperBoundY - mLowerBoundY);

				newXPosition = AXES_PADDING + (xRelativePositionRatio * usableWidth);
				newYPosition = getHeight() - AXES_PADDING - (usableHeight * yRelativePositionRatio);
				mReusablePath.lineTo(newXPosition, newYPosition);
				Path aboveLineEnclosure = new Path();
				aboveLineEnclosure.moveTo(lastXPosition, lastYPosition);
				aboveLineEnclosure.lineTo(newXPosition, newYPosition);
				aboveLineEnclosure.lineTo(newXPosition, 0);
				aboveLineEnclosure.lineTo(lastXPosition, 0);
				aboveLineEnclosure.close();
				canvas.drawPath(aboveLineEnclosure, mReusablePaint);
				lastXPosition = newXPosition;
				lastYPosition = newYPosition;
			}

			//Clear after the last point, from top to bottom.
			mReusablePath.reset();
			mReusablePath.moveTo(lastXPosition, getHeight());
			mReusablePath.lineTo(getWidth(), getHeight());
			mReusablePath.lineTo(getWidth(), 0);
			mReusablePath.lineTo(lastXPosition, 0);
			mReusablePath.close();
			canvas.drawPath(mReusablePath, mReusablePaint);
		}
	}

	private void renderAxesPaddingFillOcclusions(final Bitmap bitmap) {
		Canvas canvas = new Canvas(bitmap);

		mReusablePaint.reset();
		mReusablePaint.setXfermode(new PorterDuffXfermode(android.graphics.PorterDuff.Mode.CLEAR));

		mReusablePath.reset();
		mReusablePath.moveTo(0, getHeight());
		mReusablePath.lineTo(AXES_PADDING, getHeight());
		mReusablePath.lineTo(AXES_PADDING, 0);
		mReusablePath.lineTo(0, 0);
		mReusablePath.close();
		canvas.drawPath(mReusablePath, mReusablePaint);

		mReusablePath.reset();
		mReusablePath.moveTo(0, getHeight());
		mReusablePath.lineTo(getWidth(), getHeight());
		mReusablePath.lineTo(getWidth(), getHeight() - AXES_PADDING);
		mReusablePath.lineTo(0, getHeight() - AXES_PADDING);
		mReusablePath.close();
		canvas.drawPath(mReusablePath, mReusablePaint);
	}

	private void renderAxes(final Canvas canvas) {
		mReusablePaint.reset();
		mReusablePaint.setColor(Color.BLACK);
		mReusablePaint.setAlpha(TRANSPARENT);
		mReusablePaint.setAntiAlias(true);
		canvas.drawLine(AXES_PADDING, getHeight() - AXES_PADDING, getWidth() - AXES_PADDING, getHeight() - AXES_PADDING, mReusablePaint);
		canvas.drawLine(AXES_PADDING, AXES_PADDING, AXES_PADDING, getHeight() - AXES_PADDING, mReusablePaint);

		String xLowerBound = mLowerBoundXPrintable;
		if (xLowerBound == null) {
			xLowerBound = String.valueOf(mLowerBoundX);
		}

		String xUpperBound = mUpperBoundXPrintable;
		if (xUpperBound == null) {
			xUpperBound = String.valueOf(mUpperBoundX);
		}

		String yLowerBound = mLowerBoundYPrintable;
		if (yLowerBound == null) {
			yLowerBound = String.valueOf(mLowerBoundY);
		}

		String yUpperBound = mUpperBoundYPrintable;
		if (yUpperBound == null) {
			yUpperBound = String.valueOf(mUpperBoundY);
		}

		mReusablePaint.reset();
		mReusablePaint.setAntiAlias(true);
		if (getResources() != null) {
			mReusablePaint.setTextSize(AXES_FONT_SIZE * getResources().getDisplayMetrics().scaledDensity);
		} else {
			mReusablePaint.setTextSize(AXES_FONT_SIZE);
		}

		mReusablePaint.setColor(Color.BLACK);
		mReusablePaint.setAlpha(TRANSPARENT);
		mReusablePaint.setAntiAlias(true);
		Rect fontRectangle = new Rect();

		//Render lower bound X.
		mReusablePaint.getTextBounds(xLowerBound, 0, 1, fontRectangle);
		canvas.drawText(xLowerBound,
			(int) (AXES_PADDING - (mReusablePaint.measureText(xLowerBound) / 2)),
			(getHeight() - AXES_TEXT_PADDING - fontRectangle.height() / 2),
			mReusablePaint);

		//Render upper bound X.
		mReusablePaint.getTextBounds(xUpperBound, 0, 1, fontRectangle);
		canvas.drawText(xUpperBound,
			(int) (getWidth() - AXES_PADDING - (mReusablePaint.measureText(xUpperBound) / 2)),
			(getHeight() - AXES_TEXT_PADDING - fontRectangle.height() / 2),
			mReusablePaint);

		//Render lower bound Y.
		mReusablePaint.getTextBounds(yLowerBound, 0, 1, fontRectangle);
		canvas.drawText(yLowerBound,
			(AXES_TEXT_PADDING),
			(getHeight() - AXES_PADDING + fontRectangle.height() / 2),
			mReusablePaint);

		//Render upper bound Y.
		mReusablePaint.getTextBounds(yUpperBound, 0, 1, fontRectangle);
		canvas.drawText(yUpperBound,
			(AXES_TEXT_PADDING),
			(AXES_PADDING + fontRectangle.height() / 2),
			mReusablePaint);
	}

	private void renderLineStrokes(final Canvas canvas) {

		float usableHeight = getHeight() - (2 * AXES_PADDING);
		float usableWidth = getWidth() - (2 * AXES_PADDING);

		for (final Line line : mLines) {
			mReusablePaint.reset();
			mReusablePaint.setAlpha(OPAQUE);
			mReusablePaint.setAntiAlias(true);
			mReusablePaint.setColor(line.getColor());
			mReusablePaint.setStrokeWidth(getStrokeWidth(line));

			float xRelativePositionRatio, yRelativePositionRatio;

			if (line.getPoints().size() > 0) {
				xRelativePositionRatio = (line.getPoints().get(0).getX() - mLowerBoundX) / (mUpperBoundX - mLowerBoundX);
				yRelativePositionRatio = (line.getPoints().get(0).getY() - mLowerBoundY) / (mUpperBoundY - mLowerBoundY);

				float lastXPixels = AXES_PADDING + (xRelativePositionRatio * usableWidth);
				float lastYPixels = getHeight() - AXES_PADDING - (usableHeight * yRelativePositionRatio);
				float newXPixels, newYPixels;

				for (final LinePoint point : line.getPoints()) {
					if (point == line.getPoints().get(0)) {
						continue;
					} else {
						xRelativePositionRatio = (point.getX() - mLowerBoundX) / (mUpperBoundX - mLowerBoundX);
						yRelativePositionRatio = (point.getY() - mLowerBoundY) / (mUpperBoundY - mLowerBoundY);
						newXPixels = AXES_PADDING + (xRelativePositionRatio * usableWidth);
						newYPixels = getHeight() - AXES_PADDING - (usableHeight * yRelativePositionRatio);
						canvas.drawLine(lastXPixels, lastYPixels, newXPixels, newYPixels, mReusablePaint);
						lastXPixels = newXPixels;
						lastYPixels = newYPixels;
					}
				}
			}
		}
	}

	private void renderLinePoints(final Canvas canvas) {
		float usableHeight = getHeight() - (2 * AXES_PADDING);
		float usableWidth = getWidth() - (2 * AXES_PADDING);

		for (final Line line : mLines) {
			mReusablePaint.reset();
			mReusablePaint.setAntiAlias(true);
			mReusablePaint.setStrokeWidth(getStrokeWidth(line));
			mReusablePaint.setStrokeCap(Paint.Cap.ROUND);

			for (LinePoint point : line.getPoints()) {
				float yRelativePositionRatio = (point.getY() - mLowerBoundY) / (mUpperBoundY - mLowerBoundY);
				float xRelativePositionRatio = (point.getX() - mLowerBoundX) / (mUpperBoundX - mLowerBoundX);

				float xPointPosition = AXES_PADDING + (xRelativePositionRatio * usableWidth);
				float yPointPosition = getHeight() - AXES_PADDING - (usableHeight * yRelativePositionRatio);

				int outerRadius = getPixelForDip(line.getStrokeWidth());
				int innerRadius = outerRadius / 2;
				if (point.isSelected()) {
					outerRadius *= 2;
					innerRadius *= 2;
				}

				mReusablePaint.setColor(line.getColor());
				canvas.drawCircle(xPointPosition, yPointPosition, outerRadius, mReusablePaint);
				mReusablePaint.setColor(Color.WHITE);
				canvas.drawCircle(xPointPosition, yPointPosition, innerRadius, mReusablePaint);

				Path pointPath = new Path();
				pointPath.addCircle(xPointPosition, yPointPosition, POINT_COLLISION_RADIUS, Path.Direction.CW);
				point.setPath(pointPath);
				point.setRegion(new Region((int) (xPointPosition - POINT_COLLISION_RADIUS), (int) (yPointPosition - POINT_COLLISION_RADIUS),
					(int) (xPointPosition + POINT_COLLISION_RADIUS), (int) (yPointPosition + POINT_COLLISION_RADIUS)));
			}
		}
	}

	private void renderTitlePopups(final Canvas canvas) {
		float usableHeight = getHeight() - (2 * AXES_PADDING);
		float usableWidth = getWidth() - (2 * AXES_PADDING);

		for (final Line line : mLines) {
			for (LinePoint point : line.getPoints()) {
				float yRelativePositionRatio = (point.getY() - mLowerBoundY) / (mUpperBoundY - mLowerBoundY);
				float xRelativePositionRatio = (point.getX() - mLowerBoundX) / (mUpperBoundX - mLowerBoundX);

				float xPointPosition = AXES_PADDING + (xRelativePositionRatio * usableWidth);
				float yPointPosition = getHeight() - AXES_PADDING - (usableHeight * yRelativePositionRatio);

				if (point.isSelected() && getResources() != null) {
					NinePatchDrawable popup = (NinePatchDrawable) getResources().getDrawable(R.drawable.popup_black);
					if (popup != null) {
						String printedValue = point.toString();

						mReusablePaint.reset();
						mReusablePaint.setAntiAlias(true);
						mReusablePaint.setTextSize(POPUP_FONT_SIZE * getResources().getDisplayMetrics().scaledDensity);
						mReusablePaint.setColor(Color.WHITE);
						Rect fontRectangle = new Rect();

						int height = 0;
						for (char character : printedValue.toCharArray()) {
							mReusablePaint.getTextBounds(String.valueOf(character), 0, 1, fontRectangle);
							if (fontRectangle.height() > height) {
								height = fontRectangle.height();
							}
						}

						int boundLeft = (int) (xPointPosition - (mReusablePaint.measureText(printedValue) / 2)
							- POPUP_SIDE_PADDING * getResources().getDisplayMetrics().density);
						int boundTop = (int) ((yPointPosition - POPUP_BOTTOM_GAP) - height
							- POPUP_TOP_PADDING * getResources().getDisplayMetrics().density);
						int boundRight = (int) (xPointPosition + (mReusablePaint.measureText(printedValue) / 2)
							+ POPUP_SIDE_PADDING * getResources().getDisplayMetrics().density);

						popup.setBounds(boundLeft, boundTop, boundRight, (int) (yPointPosition - POPUP_BOTTOM_GAP));
						popup.draw(canvas);

						canvas.drawText(printedValue,
							(int) (xPointPosition - (mReusablePaint.measureText(printedValue) / 2)),
							(yPointPosition - POPUP_BOTTOM_GAP) - ((yPointPosition - POPUP_BOTTOM_GAP) - boundTop) / 2f
								+ (float) fontRectangle.height() / 2f * 0.7f,
							mReusablePaint);
					}
				}
			}
		}
	}

	private int getStrokeWidth(final Line line) {
		return getPixelForDip(line.getStrokeWidth());
	}

	@Override
	public boolean onTouchEvent(final MotionEvent event) {
		Point eventPoint = new Point((int) event.getX(), (int) event.getY());
		mShouldUpdate = false;

		Region pointRegion = new Region();
		for (final Line line : mLines) {
			for (final LinePoint point : line.getPoints()) {
				pointRegion.setPath(point.getPath(), point.getRegion());
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					if (pointRegion.contains(eventPoint.x, eventPoint.y)) {
						if (!point.isSelected()) {
							point.setSelected(true);
							mShouldUpdate = true;
						}
					} else {
						if (point.isSelected()) {
							point.setSelected(false);
							mShouldUpdate = true;
						}
					}
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					if (pointRegion.contains(eventPoint.x, eventPoint.y) && mOnPointClickedListener != null) {
						mOnPointClickedListener.onClick(line, point);
					}
				}
			}
		}

		if (mShouldUpdate) {
			postInvalidate();
		}
		return true;
	}

	/**
	 * Adds a listener to be called back when a point is selected.
	 * @param listener the listener to call back
	 */
	public void setOnPointClickedListener(final OnPointClickedListener listener) {
		mOnPointClickedListener = listener;
	}

	/**
	 * Forces the line graph to be re-renderered.
	 */
	public void refresh() {
		mShouldUpdate = true;
		postInvalidate();
	}
}
