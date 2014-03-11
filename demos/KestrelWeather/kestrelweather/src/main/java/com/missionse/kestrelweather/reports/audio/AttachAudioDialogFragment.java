package com.missionse.kestrelweather.reports.audio;

import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;

import com.missionse.kestrelweather.R;
import com.missionse.kestrelweather.reports.AttachMediaDialogFragment;

import java.io.File;

/**
 * Provides a dialog fragment to select how to add a audio.
 */
public class AttachAudioDialogFragment extends AttachMediaDialogFragment {
	@Override
	protected int getTakeButtonTextId() {
		return R.string.take_audio;
	}

	@Override
	protected int getAttachButtonTextId() {
		return R.string.attach_audio;
	}

	@Override
	protected int getTakeButtonDrawableId() {
		return R.drawable.ic_action_mic;
	}

	@Override
	protected int getAttachButtonDrawableId() {
		return R.drawable.ic_action_audio;
	}

	@Override
	protected File getMediaFile() {
		return null;
	}

	@Override
	protected String getMimeType() {
		return "audio/*";
	}

	@Override
	protected void onTakeButtonPressed() {
		Intent takeAudioIntent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
		PackageManager packageManager = getKestrelActivity().getPackageManager();
		if (packageManager != null) {
			if (takeAudioIntent.resolveActivity(packageManager) != null) {
				startActivityForResult(takeAudioIntent, TAKE_REQUEST);
			}
		}
	}

	@Override
	protected void onTakeActionRequestReceived(Fragment targetFragment, int targetRequestCode, int resultCode, Intent resultData) {
		if (resultData != null) {
			if (resultData.getData() != null) {
				Uri uri = resultData.getData();
				Intent dialogResultData = new Intent();
				dialogResultData.setData(uri);
				targetFragment.onActivityResult(targetRequestCode, resultCode,
						dialogResultData);
				dismiss();
			}
		}
	}
}