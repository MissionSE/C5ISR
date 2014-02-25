package com.missionse.kestrelweather.reports.photos;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.missionse.imageviewer.ImageFragmentFactory;
import com.missionse.kestrelweather.R;
import com.missionse.kestrelweather.reports.utils.MediaMultiChoiceModeListener;

/**
 * A fragment used to manage the photos attached to a report.
 */
public class PhotoOverviewFragment extends Fragment {
	private static final int ADD_PHOTO_REQUEST = 10;
	private static final String REPORT_ID = "report_id";
	private static final int INVALID_REPORT_ID = -1;

	private PhotoAdapter mPhotoAdapter;
	private Activity mActivity;
	private boolean mEditable = true;
	private int mReportId = INVALID_REPORT_ID;

	/**
	 * Constructor.
	 */
	public PhotoOverviewFragment() {
	}

	/**
	 * A factory method used to create a new instance of the fragment with the provided parameters.
	 * @param reportId The database report id that is associated with the report.
	 * @return A new instance of a PhotoOverviewFragment.
	 */
	public static PhotoOverviewFragment newInstance(final int reportId) {
		PhotoOverviewFragment fragment = new PhotoOverviewFragment();

		Bundle arguments = new Bundle();
		arguments.putInt(REPORT_ID, reportId);
		fragment.setArguments(arguments);

		return fragment;
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
		if (getArguments() != null) {
			mReportId = getArguments().getInt(REPORT_ID);
		}

		if (mEditable) {
			setHasOptionsMenu(true);
		}

		mPhotoAdapter = new PhotoAdapter(mActivity, R.layout.fragment_report_item_list_entry);
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.fragment_report_photos, container, false);
		if (contentView != null) {
			ListView photoList = (ListView) contentView.findViewById(R.id.fragment_report_photos_list);
			photoList.setAdapter(mPhotoAdapter);
			photoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(final AdapterView<?> adapterView, final View view, final int position, final long id) {
					FragmentManager fragmentManager = getFragmentManager();
					if (fragmentManager != null) {
						Fragment imageFragment = ImageFragmentFactory.createImageFragment(mPhotoAdapter.getItem(position));
						fragmentManager.beginTransaction()
								.replace(R.id.content, imageFragment, "image_preview")
								.addToBackStack("image_preview")
								.commit();
					}
				}
			});

			if (mEditable) {
				photoList.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
				photoList.setMultiChoiceModeListener(new MediaMultiChoiceModeListener(mActivity, photoList, mPhotoAdapter));
			}

			TextView emptyView = (TextView) contentView.findViewById(R.id.fragment_report_photos_empty);
			if (emptyView != null) {
				photoList.setEmptyView(emptyView);
			}
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
				if (resultData.getData() != null) {
					mPhotoAdapter.add(resultData.getData());
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