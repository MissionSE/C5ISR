package com.missionse.nfc;

import android.app.Activity;
import android.app.PendingIntent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;

public class NfcConnector {

	private Activity activity;

	private NfcAdapter nfcAdapter;
	private PendingIntent pendingIntent;
	private NdefMessage ndefMessage;

	public boolean onCreate(final Activity activity) {
		this.activity = activity;
		nfcAdapter = NfcAdapter.getDefaultAdapter(activity);
		if (nfcAdapter == null) {
			return false;
		}
		return true;
	}

	public void setPendingIntent(final PendingIntent intent) {
		pendingIntent = intent;
	}

	public void setMessage(final NdefMessage message) {
		ndefMessage = message;
	}

	public boolean isEnabled() {
		return nfcAdapter.isEnabled();
	}

	public void onResume() {
		if (nfcAdapter != null) {
			if (pendingIntent != null) {
				nfcAdapter.enableForegroundDispatch(activity, pendingIntent, null, null);
			}
			if (ndefMessage != null) {
				nfcAdapter.setNdefPushMessage(ndefMessage, activity);
			}
		}
	}

	public void onPause() {
		if (nfcAdapter != null) {
			nfcAdapter.disableForegroundDispatch(activity);
		}
	}
}
