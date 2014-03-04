package com.missionse.kestrelweather.reports.audio;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.missionse.kestrelweather.KestrelWeatherActivity;
import com.missionse.kestrelweather.R;
import com.missionse.kestrelweather.database.model.SupplementType;
import com.missionse.kestrelweather.database.model.tables.Supplement;
import com.missionse.kestrelweather.reports.utils.MediaMultiChoiceModeListener;
import com.missionse.kestrelweather.reports.utils.UriRemovedListener;
import com.missionse.kestrelweather.util.ReportBuilder;

/**
 * A simple {@link android.app.Fragment} subclass.
 * Use the {@link AudioOverviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AudioOverviewFragment extends Fragment implements MediaPlayerWrapper.OnMediaPlayerEventListener {
	private static final String TAG = AudioOverviewFragment.class.getSimpleName();
	private static final int ATTACH_AUDIO_REQUEST = 20;
	private static final String REPORT_ID = "report_id";
	private static final int INVALID_REPORT_ID = -1;
	private static final int SKIP_INTERVAL_IN_MILLISECONDS = 5000;

	private AudioAdapter mAudioAdapter;
	private KestrelWeatherActivity mActivity;
	private boolean mEditable = true;
	private int mReportId = INVALID_REPORT_ID;
	private Uri mPlayingUri;

	private View mMediaControls;
	private ImageButton mPlayButton;
	private ImageButton mPauseButton;
	private MediaPlayerWrapper mMediaWrapper;

	/**
	 * Constructor.
	 */
	public AudioOverviewFragment() {
		mMediaWrapper = new MediaPlayerWrapper();
		mMediaWrapper.setCompleteListener(this);
	}

	/**
	 * A factory method used to create a new instance of the fragment with the provided parameters.
	 * @param reportId The database report id that is associated with the report (if one exists).
	 * @return A new instance of an AudioOverviewFragment.
	 */
	public static AudioOverviewFragment newInstance(final int reportId) {
		AudioOverviewFragment fragment = new AudioOverviewFragment();

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

		mAudioAdapter = new AudioAdapter(mActivity, R.layout.fragment_report_item_list_entry);
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.fragment_report_audio, container, false);
		if (contentView != null) {
			ListView mAudioList = (ListView) contentView.findViewById(R.id.fragment_report_audio_list);
			mAudioList.setAdapter(mAudioAdapter);
			mAudioList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(final AdapterView<?> adapterView, final View view, final int position, final long id) {
					mPlayingUri = mAudioAdapter.getItem(position);
					mMediaControls.setVisibility(View.VISIBLE);
					mMediaWrapper.setMediaSource(mActivity, mPlayingUri);
					onMediaPlayButtonPressed();
				}
			});

			if (mEditable) {
				mAudioList.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
				MediaMultiChoiceModeListener mediaMultiChoiceModeListener = new MediaMultiChoiceModeListener(
						mActivity, mAudioList, mAudioAdapter);
				mediaMultiChoiceModeListener.setUriRemovedListener(new UriRemovedListener() {
					@Override
					public void uriRemoved(final Uri uri) {
						if (mPlayingUri.equals(uri)) {
							mPlayingUri = null;
							mMediaControls.setVisibility(View.GONE);
							onMediaPauseButtonPressed();
						}
						ReportBuilder.removeSupplement(mActivity, uri.toString(), mReportId);
					}
				});
				mAudioList.setMultiChoiceModeListener(mediaMultiChoiceModeListener);
			}

			TextView emptyView = (TextView) contentView.findViewById(R.id.fragment_report_audio_empty);
			if (emptyView != null) {
				mAudioList.setEmptyView(emptyView);
			}

			initializeMediaComponents(contentView);

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
	@TargetApi(Build.VERSION_CODES.KITKAT)
	public boolean onOptionsItemSelected(final MenuItem item) {
		if (item.getItemId() == R.id.action_add_media) {
			FragmentManager fragmentManager = getFragmentManager();
			if (fragmentManager != null) {
				Intent intent;
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
					intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
					intent.addCategory(Intent.CATEGORY_OPENABLE);
				} else {
					intent = new Intent(Intent.ACTION_GET_CONTENT);
				}

				intent.setType("audio/*");
				intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
				startActivityForResult(intent, ATTACH_AUDIO_REQUEST);
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onActivityResult(final int requestCode, final int resultCode, final Intent resultData) {
		super.onActivityResult(requestCode, resultCode, resultData);

		if (requestCode == ATTACH_AUDIO_REQUEST && resultCode == Activity.RESULT_OK) {
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

	private void addPreventDuplicateEntry(Uri uri) {
		if (!mAudioAdapter.contains(uri)) {
			mAudioAdapter.add(uri);
			createNewSupplement(uri.toString());
		} else {
			Toast.makeText(mActivity, mActivity.getString(R.string.already_exists), Toast.LENGTH_SHORT)
					.show();
		}
	}

	private void createNewSupplement(String uri) {
		ReportBuilder.buildSupplement(mActivity, uri, mReportId, SupplementType.AUDIO);
	}

	private void populateAdapter() {
		for (Supplement sup : mActivity.getDatabaseAccessor().getAudioSupplements(mReportId)) {
			mAudioAdapter.add(Uri.parse(sup.getUri()));
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		mMediaWrapper.destroy();
	}

	private void initializeMediaComponents(final View root) {
		mMediaWrapper.setSeekBar((SeekBar) root.findViewById(R.id.audio_control_seek_bar));
		mMediaWrapper.setDurationTextView((TextView) root.findViewById(R.id.audio_control_end_time_time));
		mMediaWrapper.setCurrentTextView((TextView) root.findViewById(R.id.audio_control_current_time));
		mPlayButton = (ImageButton) root.findViewById(R.id.audio_control_play_button);
		mPauseButton = (ImageButton) root.findViewById(R.id.audio_control_pause_button);
		mMediaControls = root.findViewById(R.id.fragment_report_audio_controls);

		ImageButton fastForwardButton = (ImageButton) root.findViewById(R.id.audio_control_forward_button);
		ImageButton rewindButton = (ImageButton) root.findViewById(R.id.audio_control_reverse_button);

		mPlayButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onMediaPlayButtonPressed();
			}
		});
		mPauseButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onMediaPauseButtonPressed();
			}
		});
		fastForwardButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onMediaFastForwardButtonPressed();
			}
		});
		rewindButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onMediaReverseButtonPressed();
			}
		});
	}

	private void showPlayButton() {
		mPauseButton.setVisibility(View.INVISIBLE);
		mPlayButton.setVisibility(View.VISIBLE);
	}

	private void onMediaReverseButtonPressed() {
		mMediaWrapper.rewind(SKIP_INTERVAL_IN_MILLISECONDS);
	}

	private void onMediaFastForwardButtonPressed() {
		mMediaWrapper.fastForward(SKIP_INTERVAL_IN_MILLISECONDS);
	}

	private void onMediaPauseButtonPressed() {
		if (mPauseButton.getVisibility() == View.VISIBLE) {
			showPlayButton();
			mMediaWrapper.pauseMedia();
		}
	}

	private void onMediaPlayButtonPressed() {
		showPauseButton();
		mMediaWrapper.playMedia();
	}

	private void showPauseButton() {
		mPlayButton.setVisibility(View.INVISIBLE);
		mPauseButton.setVisibility(View.VISIBLE);
	}

	@Override
	public void onMediaComplete() {
		showPlayButton();
	}
}
