package com.missionse.nfcexample;

import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.missionse.nfc.NfcConnector;
import com.missionse.nfc.NfcUtilities;
import com.missionse.nfc.TextRecord;

public class NfcExample extends Activity {

	private MessageLogFragment messageLogFragment;

	private NfcConnector nfcConnector;

	private PendingIntent nfcBroadcastIntent;
	private NdefMessage nfcMessage;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nfcexample);

		messageLogFragment = new MessageLogFragment();

		//Create the NFC Connector.
		nfcConnector = new NfcConnector();
		if (!nfcConnector.onCreate(this)) {
			Toast.makeText(this, "Error: Cannot resolve NFC Adapter", Toast.LENGTH_SHORT).show();
			finish();
			return;
		};

		nfcBroadcastIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		nfcMessage = new NdefMessage(new NdefRecord[] { TextRecord.newTextRecord("Message from the other side!",
				Locale.ENGLISH, true) });

		nfcConnector.setPendingIntent(nfcBroadcastIntent);
		nfcConnector.setMessage(nfcMessage);

		getFragmentManager().beginTransaction().replace(R.id.container, messageLogFragment).commit();
	}

	private void displayMessages(final NdefMessage[] messages) {
		List<TextRecord> textRecords = NfcUtilities.parse(messages[0]);

		for (TextRecord record : textRecords) {
			Log.e(NfcExample.class.getSimpleName(), "message: " + record.getText());
			messageLogFragment.addMessage(record.getText());
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (!nfcConnector.isReady()) {
			Toast.makeText(this, "NFC is not enabled.", Toast.LENGTH_SHORT).show();
		} else {
			nfcConnector.onResume();
		}
	}

	@Override
	public void onPause() {
		super.onPause();

		nfcConnector.onPause();
	}

	@Override
	public void onNewIntent(final Intent intent) {
		setIntent(intent);

		String action = intent.getAction();
		if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action) || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
				|| NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
			Log.e(NfcExample.class.getSimpleName(), "Received NFC intent.");
			messageLogFragment.addMessage("Received NFC intent.");
		}

		NdefMessage[] receivedNfcMessages = NfcConnector.parseIntent(getIntent());
		if (receivedNfcMessages != null) {
			displayMessages(receivedNfcMessages);
		}
	}
}
