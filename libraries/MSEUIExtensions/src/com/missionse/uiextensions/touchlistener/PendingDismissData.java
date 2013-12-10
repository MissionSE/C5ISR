package com.missionse.uiextensions.touchlistener;

import android.view.View;

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

	/**
	 * Retrieves the position associated with this PendingDismissData.
	 * @return the position
	 */
	public int getPosition() {
		return mPosition;
	}

	/**
	 * Retrieves the View associated with this PendingDismissData.
	 * @return the View
	 */
	public View getView() {
		return mView;
	}
}
