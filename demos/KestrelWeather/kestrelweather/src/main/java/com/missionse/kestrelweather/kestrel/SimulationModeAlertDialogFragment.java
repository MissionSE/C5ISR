package com.missionse.kestrelweather.kestrel;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.missionse.kestrelweather.R;

/**
 * An alert dialog displayed when simulation mode is detected when attempting to use the bluetooth service.
 * Runs a target runnable upon confirmation, or does nothing when not confirmed.
 */
public class SimulationModeAlertDialogFragment extends DialogFragment {

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
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
			builder.setIcon(R.drawable.ic_action_warning);
			builder.setTitle(R.string.sim_mode_alert_title);
			builder.setCancelable(true);
			builder.setMessage(R.string.sim_mode_alert_content)
					.setPositiveButton(R.string.sim_mode_use_saved, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							mPositiveRunnable.run();
						}
					})
					.setNeutralButton(R.string.sim_mode_connect_normally, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(final DialogInterface dialogInterface, final int i) {
							mNeutralRunnable.run();
						}
					});
			return builder.create();
		}
		return null;
	}
}
