package com.missionse.kestrelweather.reports.photos;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.missionse.kestrelweather.R;

/**
 * A fragment used to manage the photos attached to a report.
 */
public class PhotoOverviewFragment extends Fragment {
	private static final int ADD_PHOTO_REQUEST = 10;

	private PhotoAdapter mPhotoAdapter;
	private Activity mActivity;

	/**
	 * Constructor.
	 */
	public PhotoOverviewFragment() {
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
		View contentView = inflater.inflate(R.layout.fragment_report_photos, container, false);
		if (contentView != null) {
			ListView mPhotoList = (ListView) contentView.findViewById(R.id.photo_overview_list);

			mPhotoAdapter = new PhotoAdapter(mActivity, R.layout.fragment_report_list_entry);
			mPhotoList.setAdapter(mPhotoAdapter);
			mPhotoList.setMultiChoiceModeListener(new PhotoMultiChoiceModeListener(mActivity, mPhotoList, mPhotoAdapter));
		}

		return contentView;
	}

	@Override
	public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.report_photos, menu);
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		if (item.getItemId() == R.id.action_add_photo) {
			FragmentManager fragmentManager = getFragmentManager();
			if (fragmentManager != null) {
				PhotoDialogFragment photoDialogFragment = new PhotoDialogFragment();
				photoDialogFragment.setTargetFragment(this, ADD_PHOTO_REQUEST);
				photoDialogFragment.show(fragmentManager, "");
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onActivityResult(final int requestCode, final int resultCode, final Intent resultData) {
		super.onActivityResult(requestCode, resultCode, resultData);

		if (requestCode == ADD_PHOTO_REQUEST && resultCode == Activity.RESULT_OK) {
			if (resultData != null) {
				Uri uri = resultData.getData();
				if (uri != null) {
					mPhotoAdapter.add(uri);
				} else {
					ClipData clipData = resultData.getClipData();
					if (clipData != null) {
						for (int index = 0; index < clipData.getItemCount(); ++index) {
							ClipData.Item item = clipData.getItemAt(index);
							if (item != null) {
								uri = item.getUri();
								mPhotoAdapter.add(uri);
							}
						}
					}
				}
			}
		}
	}
}