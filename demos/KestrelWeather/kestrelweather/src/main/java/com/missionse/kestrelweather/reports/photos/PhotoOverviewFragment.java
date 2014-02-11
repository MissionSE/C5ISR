package com.missionse.kestrelweather.reports.photos;

import android.app.Activity;
import android.app.Fragment;
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
	private static final int READ_REQUEST_CODE = 100;

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
		View contentView = inflater.inflate(R.layout.fragment_photo_overview, container, false);
		if (contentView != null) {
			ListView mPhotoList = (ListView) contentView.findViewById(R.id.photo_overview_list);

			mPhotoAdapter = new PhotoAdapter(mActivity, R.layout.fragment_photo_overview_item);
			mPhotoList.setAdapter(mPhotoAdapter);
			mPhotoList.setMultiChoiceModeListener(new PhotoMultiChoiceModeListener(mActivity, mPhotoList, mPhotoAdapter));
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
		if (item.getItemId() == R.id.action_add_photo) {
			Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
			intent.addCategory(Intent.CATEGORY_OPENABLE);
			intent.setType("image/*");
			intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
			startActivityForResult(intent, READ_REQUEST_CODE);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onActivityResult(final int requestCode, final int resultCode, final Intent resultData) {
		super.onActivityResult(requestCode, resultCode, resultData);

		if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
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
								mPhotoAdapter.add(item.getUri());
							}
						}
					}
				}
			}
		}
	}
}