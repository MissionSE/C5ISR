package com.missionse.nfc;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Parcelable;

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

	public boolean isReady() {
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

	public static NdefMessage[] parseIntent(final Intent intent) {
		NdefMessage[] messages = null;

		String action = intent.getAction();
		if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action) || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
				|| NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
			Parcelable[] rawMessages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

			if (rawMessages != null) {
				messages = new NdefMessage[rawMessages.length];
				for (int i = 0; i < rawMessages.length; i++) {
					messages[i] = (NdefMessage) rawMessages[i];
				}
			} else {
				// Unknown tag type
				byte[] empty = new byte[0];
				byte[] id = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
				Parcelable tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
				byte[] payload = NfcUtilities.dumpTagData(tag).getBytes();
				NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, id, payload);
				NdefMessage msg = new NdefMessage(new NdefRecord[] { record });
				messages = new NdefMessage[] { msg };
			}
		}
		return messages;
	}

}
