package com.missionse.kestrelweather.reports.video;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
import android.widget.Toast;

import com.missionse.kestrelweather.KestrelWeatherActivity;
import com.missionse.kestrelweather.R;
import com.missionse.kestrelweather.database.model.SupplementType;
import com.missionse.kestrelweather.database.model.tables.Supplement;
import com.missionse.kestrelweather.reports.utils.MediaMultiChoiceModeListener;
import com.missionse.kestrelweather.reports.utils.SupplementRemovedListener;
import com.missionse.kestrelweather.util.ReportRemover;
import com.missionse.kestrelweather.util.SupplementBuilder;

/**
 * A simple {@link android.app.Fragment} subclass.
 * Use the {@link VideoOverviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public final class VideoOverviewFragment extends Fragment {
	private static final String TAG = VideoOverviewFragment.class.getSimpleName();
	private static final int ATTACH_VIDEO_REQUEST = 30;
	private static final String REPORT_ID = "report_id";
	private static final int INVALID_REPORT_ID = -1;

	private VideoAdapter mVideoAdapter;
	private KestrelWeatherActivity mActivity;
	private boolean mEditable = true;
	private int mReportId = INVALID_REPORT_ID;

	/**
	 * Constructor.
	 */
	public VideoOverviewFragment() { }

	/**
	 * A factory method used to create a new instance of the fragment with the provided parameters.
	 * @param reportId The database report id that is associated with the report (if one exists).
	 * @return A new instance of an VideoOverviewFragment.
	 */
	public static VideoOverviewFragment newInstance(final int reportId) {
		VideoOverviewFragment fragment = new VideoOverviewFragment();

		Bundle args = new Bundle();
		args.putInt(REPORT_ID, reportId);
		fragment.setArguments(args);

		return fragment;
	}

	@Override
	public void onAttach(final Activity activity) {
		super.onAttach(activity);
		try {
			mActivity = (KestrelWeatherActivity) activity;
		} catch (ClassCastException e) {
			Log.e(TAG, "Unable to cast activity.", e);
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mActivity = null;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mReportId = getArguments().getInt(REPORT_ID);
		}

		if (mEditable) {
			setHasOptionsMenu(true);
		}

		mVideoAdapter = new VideoAdapter(mActivity, R.layout.fragment_report_item_list_entry);
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.fragment_report_video, container, false);
		if (contentView != null) {
			ListView mAudioList = (ListView) contentView.findViewById(R.id.fragment_report_video_list);
			mAudioList.setAdapter(mVideoAdapter);
			mAudioList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(final AdapterView<?> adapterView, final View view, final int position, final long id) {
					Supplement selectedSupplement = mVideoAdapter.getItem(position);
					FragmentManager fragmentManager = getFragmentManager();
					if (fragmentManager != null) {
						Fragment videoFragment = VideoViewerFragment.newInstance(selectedSupplement);
						fragmentManager.beginTransaction()
							.replace(R.id.content, videoFragment, "video_preview")
							.addToBackStack("video_preview")
							.commit();
					}
				}
			});

			if (mEditable) {
				mAudioList.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
				MediaMultiChoiceModeListener mediaMultiChoiceModeListener =
						new MediaMultiChoiceModeListener(mActivity, mAudioList, mVideoAdapter);
				mediaMultiChoiceModeListener.setSupplementRemovedListener(new SupplementRemovedListener() {
					@Override
					public void supplementRemoved(final Supplement supplement) {
						if (mActivity != null) {
							ReportRemover.removeSupplement(mActivity.getDatabaseAccessor(), supplement);
						}
					}
				});
				mAudioList.setMultiChoiceModeListener(mediaMultiChoiceModeListener);
			}

			TextView emptyView = (TextView) contentView.findViewById(R.id.fragment_report_video_empty);
			if (emptyView != null) {
				mAudioList.setEmptyView(emptyView);
			}

			populateAdapter();
		}

		return contentView;
	}

	@Override
	public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.report_media, menu);
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		if (item.getItemId() == R.id.action_add_media) {
			FragmentManager fragmentManager = getFragmentManager();
			if (fragmentManager != null) {
				AttachVideoDialogFragment videoDialogFragment = new AttachVideoDialogFragment();
				videoDialogFragment.setTargetFragment(this, ATTACH_VIDEO_REQUEST);
				videoDialogFragment.show(fragmentManager, "");
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onActivityResult(final int requestCode, final int resultCode, final Intent resultData) {
		super.onActivityResult(requestCode, resultCode, resultData);

		if (requestCode == ATTACH_VIDEO_REQUEST && resultCode == Activity.RESULT_OK) {
			if (resultData != null) {
				if (resultData.getData() != null) {
					addPreventDuplicateEntry(resultData.getData());
				} else {
					ClipData clipData = resultData.getClipData();
					if (clipData != null) {
						for (int index = 0; index < clipData.getItemCount(); ++index) {
							ClipData.Item item = clipData.getItemAt(index);
							if (item != null) {
								addPreventDuplicateEntry(item.getUri());
							}
						}
					}
				}
			}
		}
	}

	private void addPreventDuplicateEntry(final Uri uri) {
		if (!mVideoAdapter.contains(uri)) {
			Supplement supplement = createNewSupplement(uri);
			if (supplement != null) {
				mVideoAdapter.add(supplement);
			}
		} else {
			Toast.makeText(mActivity, mActivity.getString(R.string.already_exists),
					Toast.LENGTH_SHORT)
					.show();
		}
	}

	private Supplement createNewSupplement(final Uri uri) {
		Supplement supplement = null;
		if (mActivity != null) {
			supplement = SupplementBuilder.buildSupplement(mActivity.getDatabaseAccessor(),
					mActivity.getContentResolver(),
					uri, mReportId, SupplementType.VIDEO);
		}

		return supplement;
	}

	private void populateAdapter() {
		mVideoAdapter.clear();

		if (mActivity != null) {
			for (Supplement supplement : mActivity.getDatabaseAccessor().getVideoSupplements(mReportId)) {
				mVideoAdapter.add(supplement);
			}
		}
	}
}
