package com.missionse.kestrelweather.reports.video;

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
 * Provides a dialog fragment to select how to add a video.
 */
public class AttachVideoDialogFragment extends AttachMediaDialogFragment {
	private static final File PUBLIC_VIDEO_PATH = Environment.getExternalStoragePublicDirectory(
			Environment.DIRECTORY_PICTURES);
	private File mFile;

	@Override
	protected int getTakeButtonTextId() {
		return R.string.take_video;
	}

	@Override
	protected int getAttachButtonTextId() {
		return R.string.attach_video;
	}

	@Override
	protected int getTakeButtonDrawableId() {
		return R.drawable.ic_action_video;
	}

	@Override
	protected int getAttachButtonDrawableId() {
		return R.drawable.ic_action_video;
	}

	@Override
	protected File getMediaFile() {
		return mFile;
	}

	@Override
	protected String getMimeType() {
		return "video/*";
	}

	@Override
	protected void onTakeButtonPressed() {
		Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
		PackageManager packageManager = getKestrelActivity().getPackageManager();
		if (packageManager != null) {
			if (takeVideoIntent.resolveActivity(packageManager) != null) {

				SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmss");
				Date now = new Date();
				mFile = new File(PUBLIC_VIDEO_PATH, "VID_" + formatter.format(now) + ".mp4");

				takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mFile));
				startActivityForResult(takeVideoIntent, TAKE_REQUEST);
			}
		}
	}
}
