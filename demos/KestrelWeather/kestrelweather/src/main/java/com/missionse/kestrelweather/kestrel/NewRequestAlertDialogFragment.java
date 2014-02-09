package com.missionse.kestrelweather.kestrel;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.missionse.kestrelweather.R;

public class NewRequestAlertDialogFragment extends DialogFragment {

	private Runnable mRunnable;

	public void setTargetRunnable(final Runnable runnable) {
		mRunnable = runnable;
	}

	@Override
	public Dialog onCreateDialog(final Bundle savedInstanceState) {
		if (getActivity() != null) {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setIcon(getResources().getDrawable(R.drawable.ic_action_warning));
			builder.setTitle(getResources().getString(R.string.new_request_alert_title));
			builder.setMessage(R.string.new_request_alert_content)
				.setPositiveButton(R.string.send, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						mRunnable.run();
					}
				})
				.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						//Nothing to do.
					}
				});
			return builder.create();
		}
		return null;
	}
}
