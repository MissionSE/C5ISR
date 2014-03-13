package com.missionse.kestrelweather.reports.audio;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ClipData;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
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
import com.missionse.kestrelweather.database.model.tables.Report;
import com.missionse.kestrelweather.database.model.tables.Supplement;
import com.missionse.kestrelweather.database.util.MediaResolver;
import com.missionse.kestrelweather.reports.utils.ItemRemovedListener;
import com.missionse.kestrelweather.reports.utils.SupplementMultiChoiceModeListener;
import com.missionse.kestrelweather.util.ReportRemover;
import com.missionse.kestrelweather.util.SupplementBuilder;

import java.io.File;

/**
 * A simple {@link android.app.Fragment} subclass.
 * Use the {@link AudioOverviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AudioOverviewFragment extends Fragment implements MediaPlayerListener {
	private static final String TAG = AudioOverviewFragment.class.getSimpleName();
	private static final int ATTACH_AUDIO_REQUEST = 20;
	private static final String REPORT_ID = "report_id";
	private static final int INVALID_REPORT_ID = -1;
	private static final int SKIP_INTERVAL_IN_MILLISECONDS = 5000;

	private AudioAdapter mAudioAdapter;
	private KestrelWeatherActivity mActivity;
	private boolean mEditable = true;
	private int mReportId = INVALID_REPORT_ID;
	private Supplement mCurrentlySelectedSupplement;

	private View mMediaControls;
	private ImageButton mPlayButton;
	private ImageButton mPauseButton;
	private MediaPlayerWrapper mMediaWrapper;

	/**
	 * Constructor.
	 */
	public AudioOverviewFragment() {
		mMediaWrapper = new MediaPlayerWrapper();
		mMediaWrapper.setMediaPlayerListener(this);
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
			if (mActivity != null) {
				Report report = mActivity.getDatabaseAccessor().getReportById(mReportId);
				if (report != null) {
					mEditable = report.isDraft();
				}
			}
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
					Supplement selectedSupplement = mAudioAdapter.getItem(position);
					Pair<String, Boolean> playPair = checkForValidPath(selectedSupplement);
					if (playPair.first != null) {
						mCurrentlySelectedSupplement = selectedSupplement;
						mMediaControls.setVisibility(View.VISIBLE);
						if (playPair.second) {
							String uriPath = MediaResolver.getPath(mActivity, Uri.parse(playPair.first));
							File uriAsFile = new File(uriPath);
							mMediaWrapper.setMediaSource(mActivity, Uri.fromFile(uriAsFile));
						} else {
							mMediaWrapper.setMediaSource(playPair.first);
						}
						onMediaPlayButtonPressed();
					} else {
						Log.d(TAG, "Unable to play supplement with id=" + mAudioAdapter.getItem(position).getId());
					}
				}
			});

			mAudioList.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
			SupplementMultiChoiceModeListener auxiliaryDataMultiChoiceModeListener =
					new SupplementMultiChoiceModeListener(mActivity, mAudioList, mAudioAdapter, mEditable);
			auxiliaryDataMultiChoiceModeListener.setItemRemovedListener(new ItemRemovedListener<Supplement>() {
				@Override
				public void itemRemoved(final Supplement supplement) {
					if (mCurrentlySelectedSupplement != null) {
						if (mCurrentlySelectedSupplement.getId() == supplement.getId()) {
							mCurrentlySelectedSupplement = null;
							mMediaControls.setVisibility(View.GONE);
							mMediaWrapper.setMediaSource(mActivity, null);
							onMediaPauseButtonPressed();
							mMediaWrapper.playMedia();
						}
					}
					if (mActivity != null) {
						ReportRemover.removeSupplement(mActivity.getDatabaseAccessor(), supplement);
					}
				}
			});
			mAudioList.setMultiChoiceModeListener(auxiliaryDataMultiChoiceModeListener);

			TextView emptyView = (TextView) contentView.findViewById(R.id.fragment_report_audio_empty);
			if (emptyView != null) {
				mAudioList.setEmptyView(emptyView);
			}

			initializeMediaComponents(contentView);

			populateAdapter();
		}

		return contentView;
	}

	/*
	 * Returns a Pair where the first value is the URL to use and the
	 * second value is a boolean that if it is true then the URL is an android
	 * uri.
	 * The first value is null if neither the localUri or remoteUri are valid.
	 */
	private Pair<String, Boolean> checkForValidPath(Supplement supplement) {
		String localUri = supplement.getUri();
		String remoteUri = supplement.getRemoteUri();
		if (validString(localUri) && uriExist(localUri)) {
			return new Pair<String, Boolean>(localUri, true);
		} else {
			if (validString(remoteUri)) {
				return new Pair<String, Boolean>(getString(R.string.remote_server_development) + remoteUri, false);
			}
		}
		return new Pair<String, Boolean>(null, false);
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
				AttachAudioDialogFragment audioDialogFragment = new AttachAudioDialogFragment();
				audioDialogFragment.setTargetFragment(this, ATTACH_AUDIO_REQUEST);
				audioDialogFragment.show(fragmentManager, "");
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

	private void addPreventDuplicateEntry(final Uri uri) {
		if (!mAudioAdapter.contains(uri)) {
			Supplement supplement = createNewSupplement(uri);
			if (supplement != null) {
				mAudioAdapter.add(supplement);
			}
		} else {
			Toast.makeText(mActivity, mActivity.getString(R.string.already_exists), Toast.LENGTH_SHORT)
					.show();
		}
	}

	private Supplement createNewSupplement(final Uri uri) {
		Supplement supplement = null;
		if (mActivity != null) {
			supplement = SupplementBuilder.buildSupplement(mActivity.getDatabaseAccessor(), mActivity.getContentResolver(),
					uri, mReportId, SupplementType.AUDIO);
		}

		return supplement;
	}

	private void populateAdapter() {
		mAudioAdapter.clear();

		if (mActivity != null) {
			for (Supplement supplement : mActivity.getDatabaseAccessor().getAudioSupplements(mReportId)) {
				mAudioAdapter.add(supplement);
			}
		}
	}

	private boolean uriExist(final String uriString) {
		Uri uri = Uri.parse(uriString);
		String uriPath = MediaResolver.getPath(mActivity, uri);
		File uriAsFile = new File(uriPath);
		return uriAsFile.exists();
	}

	private boolean validString(final String string) {
		return string != null && string.length() > 0;
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
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		showPlayButton();
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		return false;
	}

	@Override
	public boolean onInfo(MediaPlayer mp, int what, int extra) {
		return false;
	}

	@Override
	public void onSeekComplete(MediaPlayer mp) {
	}
}
