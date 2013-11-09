package com.missionse.nfcexample;

import java.util.List;
import java.util.Locale;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class NfcExample extends Activity implements ActionBar.OnNavigationListener {

	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

	private MessageSenderFragment messageSenderFragment;
	private MessageLogFragment messageLogFragment;

	private NfcConnector nfcConnector;

	private PendingIntent nfcBroadcastIntent;
	private NdefMessage nfcMessage;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nfcexample);

		messageSenderFragment = new MessageSenderFragment();
		messageLogFragment = new MessageLogFragment();

		// Set up the action bar to show a dropdown list.
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		// Set up the dropdown list navigation in the action bar.
		actionBar.setListNavigationCallbacks(new ArrayAdapter<String>(actionBar.getThemedContext(),
				android.R.layout.simple_list_item_1, android.R.id.text1, new String[] {
						getString(R.string.title_section1), getString(R.string.title_section2), }), this);

		//Determine if the intent that started this activity was an NFC intent.
		NdefMessage[] receivedNfcMessages = NfcConnector.parseIntent(getIntent());
		if (receivedNfcMessages != null) {
			displayMessages(receivedNfcMessages);
		}

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
		NdefMessage[] receivedNfcMessages = NfcConnector.parseIntent(getIntent());
		if (receivedNfcMessages != null) {
			displayMessages(receivedNfcMessages);
		}
	}

	@Override
	public void onRestoreInstanceState(final Bundle savedInstanceState) {
		if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
			getActionBar().setSelectedNavigationItem(savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
		}
	}

	@Override
	public void onSaveInstanceState(final Bundle outState) {
		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar().getSelectedNavigationIndex());
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		getMenuInflater().inflate(R.menu.nfcexample, menu);
		return true;
	}

	@Override
	public boolean onNavigationItemSelected(final int position, final long id) {
		Fragment fragment;
		if (position == 0) {
			fragment = messageSenderFragment;
		} else {
			fragment = messageLogFragment;
		}
		getFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
		return true;
	}

}
