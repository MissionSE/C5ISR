package com.missionse.kestrelweather.reports;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.missionse.kestrelweather.KestrelWeatherActivity;
import com.missionse.kestrelweather.R;

import java.io.File;

/**
 * Abstract class that helps create simple dialog menus.
 */
public abstract class AttachMediaDialogFragment extends DialogFragment {
	private static final String TAG = AttachMediaDialogFragment.class.getSimpleName();
	private static final int DRAWABLE_PADDING = 5;
	public static final int TAKE_REQUEST = 100;
	public static final int ATTACH_REQUEST = 200;

	private KestrelWeatherActivity mActivity;

	/**
	 * Constructor.
	 */
	public AttachMediaDialogFragment() {
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
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Dialog dialog = super.onCreateDialog(savedInstanceState);

		dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		return dialog;
	}

	@Override
	@TargetApi(Build.VERSION_CODES.KITKAT)
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_report_photos_dialog, container);
		if (view != null) {
			Button takeButton = (Button) view.findViewById(R.id.photo_overview_take_photo_btn);
			if (takeButton != null) {
				takeButton.setText(getString(getTakeButtonTextId()));
				takeButton.setCompoundDrawablesWithIntrinsicBounds(getTakeButtonDrawableId(), 0, 0, 0);
				takeButton.setCompoundDrawablePadding(DRAWABLE_PADDING);
				takeButton.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(final View view) {
						onTakeButtonPressed();
					}
				});
			}

			Button attachButton = (Button) view.findViewById(R.id.photo_overview_attach_photo_btn);
			if (attachButton != null) {
				attachButton.setText(getString(getAttachButtonTextId()));
				attachButton.setCompoundDrawablesWithIntrinsicBounds(getAttachButtonDrawableId(), 0, 0, 0);
				attachButton.setCompoundDrawablePadding(DRAWABLE_PADDING);
				attachButton.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(final View view) {
						FragmentManager fragmentManager = getFragmentManager();
						if (fragmentManager != null) {
							Intent intent;
							if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
								intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
								intent.addCategory(Intent.CATEGORY_OPENABLE);
							} else {
								intent = new Intent(Intent.ACTION_GET_CONTENT);
							}

							intent.setType(getMimeType());
							intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
							startActivityForResult(intent, ATTACH_REQUEST);
						}
					}
				});
			}
		}
		return view;
	}

	@Override
	public void onActivityResult(final int requestCode, final int resultCode, final Intent resultData) {
		super.onActivityResult(requestCode, resultCode, resultData);

		final Fragment targetFragment = getTargetFragment();
		final int targetRequestCode = getTargetRequestCode();
		if (targetFragment != null) {
			if (requestCode == TAKE_REQUEST && resultCode == Activity.RESULT_OK) {
				onTakeActionRequestReceived(targetFragment, targetRequestCode, resultCode, resultData);
			} else {
				targetFragment.onActivityResult(targetRequestCode, resultCode, resultData);
				dismiss();
			}
		}
	}

	protected KestrelWeatherActivity getKestrelActivity() {
		return mActivity;
	}

	protected void onTakeActionRequestReceived(final Fragment targetFragment,
			final int targetRequestCode, final int resultCode, final Intent resultData) {
		MediaScannerConnection.scanFile(getKestrelActivity(),
				new String[]{getMediaFile().toString()}, null,
				new MediaScannerConnection.OnScanCompletedListener() {
					public void onScanCompleted(final String path, final Uri uri) {
						if (getKestrelActivity() != null) {
							getKestrelActivity().runOnUiThread(new Runnable() {
								@Override
								public void run() {
									Intent dialogResultData = new Intent();
									dialogResultData.setData(uri);
									targetFragment.onActivityResult(targetRequestCode, resultCode,
											dialogResultData);
									dismiss();
								}
							});
						}
					}
				}
		);
	}

	protected abstract int getTakeButtonTextId();

	protected abstract int getAttachButtonTextId();

	protected abstract int getTakeButtonDrawableId();

	protected abstract int getAttachButtonDrawableId();

	protected abstract File getMediaFile();

	protected abstract String getMimeType();

	protected abstract void onTakeButtonPressed();
}
