package com.missionse.kestrelweather.preferences;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.ListPreference;
import android.util.AttributeSet;

import com.missionse.kestrelweather.R;

/**
 * Custom list preference to modify default behaviour.
 */
public class ServerChangePreference extends ListPreference {
	private Context mContext;
	/**
	 * Constructor.
	 * @param context The current context.
	 * @param attrs The xml attributes.
	 */
	public ServerChangePreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
	}

	/**
	 * Constructor.
	 * @param context The current context.
	 */
	public ServerChangePreference(Context context) {
		super(context);
		mContext = context;
	}

	@Override
	protected void showDialog(Bundle state) {
		showAlert(state);
	}

	private void showAlert(final Bundle state) {
		if (mContext != null) {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
			alertDialogBuilder.setTitle("Warning");
			alertDialogBuilder.setIcon(R.drawable.ic_action_warning);
			alertDialogBuilder
					.setMessage("Switching server will wipe current data!")
					.setCancelable(false)
					.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.dismiss();
							ServerChangePreference.super.showDialog(state);
						}
					})
					.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			alertDialogBuilder.create().show();
		}
	}
}
