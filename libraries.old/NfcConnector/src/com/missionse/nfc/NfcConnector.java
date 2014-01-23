package com.missionse.nfc;

import android.app.Activity;
import android.app.PendingIntent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;

/**
 * Wraps the NfcAdapter to automatically setup and maintain NFC related processes.
 */
public class NfcConnector {

	private Activity mActivity;

	private NfcAdapter mNfcAdapter;
	private PendingIntent mPendingIntent;
	private NdefMessage mNdefMessage;

	/**
	 * Sets up the NfcAdapter. To be called by the parent activity's onCreate().
	 * @param activity the parent activity
	 * @return whether or not the NfcAdapter exists
	 */
	public boolean onCreate(final Activity activity) {
		this.mActivity = activity;
		mNfcAdapter = NfcAdapter.getDefaultAdapter(activity);
		if (mNfcAdapter == null) {
			return false;
		}
		return true;
	}

	/**
	 * Sets the pending intent to be broadcast when requested.
	 * @param intent the intent to broadcast
	 */
	public void setPendingIntent(final PendingIntent intent) {
		mPendingIntent = intent;
	}

	/**
	 * Sets the message to be sent when requested.
	 * @param message the message to send
	 */
	public void setMessage(final NdefMessage message) {
		mNdefMessage = message;
	}

	/**
	 * Retrieves the NfcAdapter state.
	 * @return whether or not the NfcAdapter is enabled
	 */
	public boolean isEnabled() {
		return mNfcAdapter.isEnabled();
	}

	/**
	 * Enables foreground dispatch and push message if available. To be called by the parent activity's onResume().
	 */
	public void onResume() {
		if (mNfcAdapter != null) {
			if (mPendingIntent != null) {
				mNfcAdapter.enableForegroundDispatch(mActivity, mPendingIntent, null, null);
			}
			if (mNdefMessage != null) {
				mNfcAdapter.setNdefPushMessage(mNdefMessage, mActivity);
			}
		}
	}

	/**
	 * Disables all operations of the NfcAdapter. To be called by the parent activity's onPause().
	 */
	public void onPause() {
		if (mNfcAdapter != null) {
			mNfcAdapter.disableForegroundDispatch(mActivity);
		}
	}
}
