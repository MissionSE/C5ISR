package com.missionse.uiextensions.touchlistener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

/**
 * Touch listener that listens for swipe motion events and adjust list items as necessary.
 */
public class SwipeDismissListViewTouchListener implements View.OnTouchListener {
	private static final int VELOCITY_UNITS = 1000;
	private static final int MIN_FLING_VELOCITY_FACTOR = 16;
	// Cached ViewConfiguration and system-wide constant values
	private int mSlop;
	private int mMinFlingVelocity;
	private int mMaxFlingVelocity;
	private long mAnimationTime;

	// Fixed properties
	private ListView mListView;
	private DismissCallbacks mCallbacks;
	private int mViewWidth = 1; // 1 and not 0 to prevent dividing by zero

	// Transient properties
	private List<PendingDismissData> mPendingDismisses = new ArrayList<PendingDismissData>();
	private int mDismissAnimationRefCount = 0;
	private float mDownX;
	private boolean mSwiping;
	private VelocityTracker mVelocityTracker;
	private int mDownPosition;
	private View mDownView;
	private boolean mPaused;

	/**
	 * The callback interface used by {@link SwipeDismissListViewTouchListener} to inform its client about a successful
	 * dismissal of one or more list item positions.
	 */
	public interface DismissCallbacks {
		/**
		 * Called to determine whether the given mPosition can be dismissed.
		 * @param position the item to determine state for
		 * @return whether or not this item can be dismissed
		 */
		boolean canDismiss(int position);

		/**
		 * Called when the user has indicated they she would like to dismiss one or more list item positions.
		 * 
		 * @param listView The originating {@link ListView}.
		 * @param reverseSortedPositions An array of positions to dismiss, sorted in descending order for convenience.
		 */
		void onDismiss(ListView listView, int[] reverseSortedPositions);
	}

	/**
	 * Constructs a new swipe-to-dismiss touch listener for the given list mView.
	 * 
	 * @param listView The list mView whose items should be dismissable.
	 * @param callbacks The callback to trigger when the user has indicated that she would like to dismiss one or more
	 *            list items.
	 */
	public SwipeDismissListViewTouchListener(final ListView listView, final DismissCallbacks callbacks) {
		ViewConfiguration vc = ViewConfiguration.get(listView.getContext());
		mSlop = vc.getScaledTouchSlop();
		mMinFlingVelocity = vc.getScaledMinimumFlingVelocity() * MIN_FLING_VELOCITY_FACTOR;
		mMaxFlingVelocity = vc.getScaledMaximumFlingVelocity();
		mAnimationTime = listView.getContext().getResources().getInteger(android.R.integer.config_shortAnimTime);
		mListView = listView;
		mCallbacks = callbacks;
	}

	/**
	 * Enables or disables (pauses or resumes) watching for swipe-to-dismiss gestures.
	 * 
	 * @param enabled Whether or not to watch for gestures.
	 */
	public void setEnabled(final boolean enabled) {
		mPaused = !enabled;
	}

	/**
	 * Returns an {@link AbsListView.OnScrollListener} to be added to the {@link ListView} using
	 * {@link ListView#setOnScrollListener(AbsListView.OnScrollListener)}. If a scroll listener is already assigned, the
	 * caller should still pass scroll changes through to this listener. This will ensure that this
	 * {@link SwipeDismissListViewTouchListener} is paused during list mView scrolling.
	 * 
	 * @see SwipeDismissListViewTouchListener
	 * @return the created scroll listener
	 */
	public AbsListView.OnScrollListener makeScrollListener() {
		return new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(final AbsListView absListView, final int scrollState) {
				setEnabled(scrollState != AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL);
			}

			@Override
			public void onScroll(final AbsListView absListView, final int i, final int i1, final int i2) {
			}
		};
	}

	@Override
	public boolean onTouch(final View view, final MotionEvent motionEvent) {
		if (mViewWidth < 2) {
			mViewWidth = mListView.getWidth();
		}

		switch (motionEvent.getActionMasked()) {
			case MotionEvent.ACTION_DOWN:
				return handleDownMotionEvent(motionEvent);
			case MotionEvent.ACTION_CANCEL:
				if (mVelocityTracker == null) {
					break;
				}

				if (mDownView != null && mSwiping) {
					// cancel
					mDownView.animate().translationX(0).alpha(1).setDuration(mAnimationTime).setListener(null);
				}
				mVelocityTracker.recycle();
				mVelocityTracker = null;
				mDownX = 0;
				mDownView = null;
				mDownPosition = ListView.INVALID_POSITION;
				mSwiping = false;
				break;
			case MotionEvent.ACTION_UP:
				if (mVelocityTracker == null) {
					break;
				}

				handleUpMotionEvent(motionEvent);
				break;
			case MotionEvent.ACTION_MOVE:
				if (mVelocityTracker == null || mPaused) {
					break;
				}

				mVelocityTracker.addMovement(motionEvent);
				float deltaX1 = motionEvent.getRawX() - mDownX;
				if (Math.abs(deltaX1) > mSlop) {
					mSwiping = true;
					mListView.requestDisallowInterceptTouchEvent(true);

					// Cancel ListView's touch (un-highlighting the item)
					MotionEvent cancelEvent = MotionEvent.obtain(motionEvent);
					cancelEvent.setAction(MotionEvent.ACTION_CANCEL
							| (motionEvent.getActionIndex() << MotionEvent.ACTION_POINTER_INDEX_SHIFT));
					mListView.onTouchEvent(cancelEvent);
					cancelEvent.recycle();
				}

				if (mSwiping) {
					mDownView.setTranslationX(deltaX1);
					mDownView.setAlpha(Math.max(0f, Math.min(1f, 1f - 2f * Math.abs(deltaX1) / mViewWidth)));
					return true;
				}
				break;
			default:
				break;
		}
		return false;
	}

	private boolean handleDownMotionEvent(final MotionEvent motionEvent) {
		if (mPaused) {
			return false;
		}

		// TODO: ensure this is a finger, and set a flag
		// Find the child mView that was touched (perform a hit test)
		Rect rect = new Rect();
		int childCount = mListView.getChildCount();
		int[] listViewCoords = new int[2];
		mListView.getLocationOnScreen(listViewCoords);
		int x = (int) motionEvent.getRawX() - listViewCoords[0];
		int y = (int) motionEvent.getRawY() - listViewCoords[1];
		for (int i = 0; i < childCount; i++) {
			View child = mListView.getChildAt(i);
			child.getHitRect(rect);
			if (rect.contains(x, y)) {
				mDownView = child;
				break;
			}
		}

		if (mDownView != null) {
			mDownX = motionEvent.getRawX();
			mDownPosition = mListView.getPositionForView(mDownView);
			if (mCallbacks.canDismiss(mDownPosition)) {
				mVelocityTracker = VelocityTracker.obtain();
				mVelocityTracker.addMovement(motionEvent);
			} else {
				mDownView = null;
			}
		}
		return false;
	}

	private void handleUpMotionEvent(final MotionEvent motionEvent) {
		float deltaX = motionEvent.getRawX() - mDownX;
		mVelocityTracker.addMovement(motionEvent);
		mVelocityTracker.computeCurrentVelocity(VELOCITY_UNITS);
		float velocityX = mVelocityTracker.getXVelocity();
		float absVelocityX = Math.abs(velocityX);
		float absVelocityY = Math.abs(mVelocityTracker.getYVelocity());
		boolean dismiss = false;
		boolean dismissRight = false;
		if (Math.abs(deltaX) > mViewWidth / 2) {
			dismiss = true;
			dismissRight = deltaX > 0;
		} else if (mMinFlingVelocity <= absVelocityX && absVelocityX <= mMaxFlingVelocity
				&& absVelocityY < absVelocityX) {
			// dismiss only if flinging in the same direction as dragging
			dismiss = (velocityX < 0) == (deltaX < 0);
			dismissRight = mVelocityTracker.getXVelocity() > 0;
		}
		if (dismiss) {
			final View downView = mDownView; // mDownView gets null'd before animation ends
			final int downPosition = mDownPosition;
			++mDismissAnimationRefCount;
			if (dismissRight) {
				mDownView.animate().translationX(mViewWidth).alpha(0).setDuration(mAnimationTime)
						.setListener(new AnimatorListenerAdapter() {
							@Override
							public void onAnimationEnd(final Animator animation) {
								performDismiss(downView, downPosition);
							}
						});
			} else {
				mDownView.animate().translationX(-mViewWidth).alpha(0).setDuration(mAnimationTime)
						.setListener(new AnimatorListenerAdapter() {
							@Override
							public void onAnimationEnd(final Animator animation) {
								performDismiss(downView, downPosition);
							}
						});
			}

		} else {
			// cancel
			mDownView.animate().translationX(0).alpha(1).setDuration(mAnimationTime).setListener(null);
		}
		mVelocityTracker.recycle();
		mVelocityTracker = null;
		mDownX = 0;
		mDownView = null;
		mDownPosition = ListView.INVALID_POSITION;
		mSwiping = false;
	}

	/**
	 * A comparable to hold a Pending dismiss action.
	 */
	class PendingDismissData implements Comparable<PendingDismissData> {
		private int mPosition;
		private View mView;

		/**
		 * Creates a new PendingDismissData.
		 * @param mPosition the mPosition of the View
		 * @param mView the View being dismissed
		 */
		public PendingDismissData(final int position, final View view) {
			mPosition = position;
			mView = view;
		}

		@Override
		public int compareTo(final PendingDismissData other) {
			// Sort by descending mPosition
			return other.mPosition - mPosition;
		}
	}

	private void performDismiss(final View dismissView, final int dismissPosition) {
		// Animate the dismissed list item to zero-height and fire the dismiss callback when
		// all dismissed list item animations have completed. This triggers layout on each animation
		// frame; in the future we may want to do something smarter and more performant.

		final ViewGroup.LayoutParams lp = dismissView.getLayoutParams();
		final int originalHeight = dismissView.getHeight();

		ValueAnimator animator = ValueAnimator.ofInt(originalHeight, 1).setDuration(mAnimationTime);

		animator.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(final Animator animation) {
				--mDismissAnimationRefCount;
				if (mDismissAnimationRefCount == 0) {
					// No active animations, process all pending dismisses.
					// Sort by descending mPosition
					Collections.sort(mPendingDismisses);

					int[] dismissPositions = new int[mPendingDismisses.size()];
					for (int i = mPendingDismisses.size() - 1; i >= 0; i--) {
						dismissPositions[i] = mPendingDismisses.get(i).mPosition;
					}
					mCallbacks.onDismiss(mListView, dismissPositions);

					ViewGroup.LayoutParams lp;
					for (PendingDismissData pendingDismiss : mPendingDismisses) {
						// Reset mView presentation
						pendingDismiss.mView.setAlpha(1f);
						pendingDismiss.mView.setTranslationX(0);
						lp = pendingDismiss.mView.getLayoutParams();
						lp.height = originalHeight;
						pendingDismiss.mView.setLayoutParams(lp);
					}

					mPendingDismisses.clear();
				}
			}
		});

		animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(final ValueAnimator valueAnimator) {
				lp.height = (Integer) valueAnimator.getAnimatedValue();
				dismissView.setLayoutParams(lp);
			}
		});

		mPendingDismisses.add(new PendingDismissData(dismissPosition, dismissView));
		animator.start();
	}
}
