package com.missionse.kestrelweather.kestrel;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.missionse.kestrelweather.R;

/**
 * An alert dialog displayed when a new request received. Runs a target runnable upon confirmation, or does
 * nothing when not confirmed.
 */
public class NewRequestAlertDialogFragment extends DialogFragment {

	private Runnable mPositiveRunnable;
	private Runnable mNeutralRunnable;

	/**
	 * Sets the runnable to be invoked on a confirmation from this dialog.
	 * @param positiveRunnable the runnable to be invoked on a positive action
	 * @param neutralRunnable the runnable to be invoked on a neutral action
	 */
	public void setTargetRunnable(final Runnable positiveRunnable, final Runnable neutralRunnable) {
		mPositiveRunnable = positiveRunnable;
		mNeutralRunnable = neutralRunnable;
	}

	@Override
	public Dialog onCreateDialog(final Bundle savedInstanceState) {
		if (getActivity() != null) {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setIcon(getResources().getDrawable(R.drawable.ic_action_warning));
			builder.setTitle(getResources().getString(R.string.new_request_alert_title));
			builder.setMessage(R.string.new_request_alert_content)
				.setPositiveButton(R.string.new_request_use_saved, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						mPositiveRunnable.run();
					}
				})
				.setNeutralButton(R.string.new_request_use_random, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(final DialogInterface dialogInterface, final int i) {
						mNeutralRunnable.run();
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
