package com.missionse.kestrelweather.reports.photos;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.missionse.kestrelweather.R;
import com.missionse.kestrelweather.reports.AttachMediaDialogFragment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Provides a dialog fragment to select how to add a photo.
 */
public class AttachPhotoDialogFragment extends AttachMediaDialogFragment {
	private static final File PUBLIC_IMAGE_PATH = Environment.getExternalStoragePublicDirectory(
			Environment.DIRECTORY_PICTURES);

	private File mFile;

	@Override
	protected int getTakeButtonTextId() {
		return R.string.take_photo;
	}

	@Override
	protected int getAttachButtonTextId() {
		return R.string.attach_photo;
	}

	@Override
	protected int getTakeButtonDrawableId() {
		return R.drawable.ic_action_camera;
	}

	@Override
	protected int getAttachButtonDrawableId() {
		return R.drawable.ic_action_picture;
	}

	@Override
	protected File getMediaFile() {
		return mFile;
	}

	@Override
	protected String getMimeType() {
		return "image/*";
	}

	@Override
	protected void onTakeButtonPressed() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		PackageManager packageManager = getKestrelActivity().getPackageManager();
		if (packageManager != null) {
			if (intent.resolveActivity(packageManager) != null) {
				SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmss");
				Date now = new Date();
				mFile = new File(PUBLIC_IMAGE_PATH, "IMG_" + formatter.format(now) + ".jpg");

				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mFile));
				startActivityForResult(intent, TAKE_REQUEST);
			}
		}
	}
}
