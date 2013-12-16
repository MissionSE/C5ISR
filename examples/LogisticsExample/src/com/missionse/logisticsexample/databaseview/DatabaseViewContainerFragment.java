package com.missionse.logisticsexample.databaseview;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.missionse.logisticsexample.R;

/**
 * Contains two side-by-side fragments, managing communication between the two.
 */
public class DatabaseViewContainerFragment extends Fragment {

	private Fragment mLeftFragment;
	private String mLeftTag;
	private Fragment mRightFragment;
	private String mRightTag;

	/**
	 * Sets the left fragment to be displayed.
	 * @param leftFragment the fragment to be displayed
	 * @param leftTag the tag to use when using a transaction
	 */
	public void setLeftFragment(final Fragment leftFragment, final String leftTag) {
		mLeftFragment = leftFragment;
		mLeftTag = leftTag;
	}

	/**
	 * Sets the right fragment to be displayed.
	 * @param rightFragment the fragment to be displayed
	 * @param rightTag the tag to use when using a transaction
	 */
	public void setRightFragment(final Fragment rightFragment, final String rightTag) {
		mRightFragment = rightFragment;
		mRightTag = rightTag;
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup parent, final Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.fragment_database_container, parent, false);

		getActivity().getFragmentManager().beginTransaction().replace(R.id.database_list, mLeftFragment, mLeftTag)
				.commit();
		getActivity().getFragmentManager().beginTransaction().replace(R.id.database_detail, mRightFragment, mRightTag)
				.commit();

		return contentView;
	}
}
