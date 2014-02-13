package com.missionse.kestrelweather.reports.photos;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.missionse.kestrelweather.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Provides a dialog fragment to select how to add a photo.
 */
public class PhotoDialogFragment extends DialogFragment {
	private static final int TAKE_PHOTO_REQUEST = 100;
	private static final int ATTACH_PHOTO_REQUEST = 200;
	private static final File PUBLIC_IMAGE_PATH = Environment.getExternalStoragePublicDirectory(
			Environment.DIRECTORY_PICTURES);

	private File mFile;
	private Activity mActivity;

	/**
	 * Constructor.
	 */
	public PhotoDialogFragment() {
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
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Dialog dialog = super.onCreateDialog(savedInstanceState);

		dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		return dialog;
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_report_photos_dialog, container);
		if (view != null) {
			Button takePhotoButton = (Button) view.findViewById(R.id.photo_overview_take_photo_btn);
			if (takePhotoButton != null) {
				takePhotoButton.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(final View view) {
						Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

						SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmss");
						Date now = new Date();
						mFile = new File(PUBLIC_IMAGE_PATH, "IMG_" + formatter.format(now) + ".jpg");

						intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mFile));
						startActivityForResult(intent, TAKE_PHOTO_REQUEST);
					}
				});
			}

			Button attachPhotoButton = (Button) view.findViewById(R.id.photo_overview_attach_photo_btn);
			if (attachPhotoButton != null) {
				attachPhotoButton.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(final View view) {
						Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
						intent.addCategory(Intent.CATEGORY_OPENABLE);
						intent.setType("image/*");
						intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
						startActivityForResult(intent, ATTACH_PHOTO_REQUEST);
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
			if (requestCode == TAKE_PHOTO_REQUEST && resultCode == Activity.RESULT_OK) {
				MediaScannerConnection.scanFile(mActivity,
						new String[]{mFile.toString()}, null,
						new MediaScannerConnection.OnScanCompletedListener() {
							public void onScanCompleted(final String path, final Uri uri) {
								if (mActivity != null) {
									mActivity.runOnUiThread(new Runnable() {
										@Override
										public void run() {
											Intent cameraResultData = new Intent();
											cameraResultData.setData(uri);
											targetFragment.onActivityResult(targetRequestCode, resultCode, cameraResultData);
											dismiss();
										}
									});
								}
							}
						});
			} else {
				targetFragment.onActivityResult(targetRequestCode, resultCode, resultData);
				dismiss();
			}
		}
	}
}
