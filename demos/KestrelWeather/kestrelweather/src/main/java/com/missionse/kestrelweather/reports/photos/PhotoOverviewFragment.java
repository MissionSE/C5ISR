package com.missionse.kestrelweather.reports.photos;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.missionse.kestrelweather.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment used to manage the photos attached to a report.
 */
public class PhotoOverviewFragment extends Fragment implements ActionMode.Callback, AdapterView.OnItemClickListener {
	private PhotoAdapter mPhotoAdapter;
	private Activity mActivity;
	private ActionMode mActionMode;
	private List<ImageView> mSelectedPhotos;

	/**
	 * Constructor.
	 */
	public PhotoOverviewFragment() {
		mSelectedPhotos = new ArrayList<ImageView>();
	}

	@Override
	public void onAttach(final Activity activity) {
		super.onAttach(activity);
		mActivity = activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mActivity = null;
	}

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.fragment_photo_overview, container, false);
		if (contentView != null) {
			GridView photoGrid = (GridView) contentView.findViewById(R.id.photo_grid_view);

			mPhotoAdapter = new PhotoAdapter(mActivity);
			photoGrid.setAdapter(mPhotoAdapter);
			photoGrid.setOnItemClickListener(this);
		}

		return contentView;
	}

	@Override
	public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.photo_overview_menu, menu);
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onPrepareActionMode(final ActionMode actionMode, final Menu menu) {
		return false;
	}

	@Override
	public boolean onCreateActionMode(final ActionMode actionMode, final Menu menu) {
		mActionMode = actionMode;

		MenuInflater menuInflater = mActionMode.getMenuInflater();
		if (menuInflater != null) {
			menuInflater.inflate(R.menu.photo_overview_action_bar, menu);
		}
		return true;
	}

	@Override
	public boolean onActionItemClicked(final ActionMode actionMode, final MenuItem menuItem) {
		return false;
	}

	@Override
	public void onDestroyActionMode(final ActionMode actionMode) {
		mActionMode = null;
		for (ImageView selectedPhoto : mSelectedPhotos) {
			selectedPhoto.clearColorFilter();
		}
		mSelectedPhotos.clear();
	}

	@Override
	public void onItemClick(final AdapterView<?> adapterView, final View view, final int position, final long id) {
		ImageView imageView = (ImageView) view;
		if (mSelectedPhotos.contains(imageView)) {
			mSelectedPhotos.remove(imageView);
			imageView.clearColorFilter();

			if (mSelectedPhotos.size() == 0 && mActionMode != null) {
				mActionMode.finish();
			}
		} else {
			mSelectedPhotos.add(imageView);
			if (mActivity != null) {
				imageView.setColorFilter(
						mActivity.getResources().getColor(R.color.holo_orange_light),
						PorterDuff.Mode.MULTIPLY);
				if (mActionMode == null) {
					mActivity.startActionMode(this);
				}
			}
		}
	}
}