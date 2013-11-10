package com.missionse.bluetoothexample;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.missionse.bluetoothexample.chatservice.ChatService;

public class BluetoothExample extends Activity {

	// Intent request codes
	private static final int REQUEST_ENABLE_BT = 1;

	private BluetoothAdapter bluetoothAdapter;
	private ChatService chatService;
	private ConversationFragment conversationFragment;

	private BroadcastReceiver broadcastReceiver;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bluetooth_example);

		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		if (bluetoothAdapter == null) {
			Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
			finish();
			return;
		}

		conversationFragment = new ConversationFragment();

		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.content, conversationFragment);
		transaction.commit();

		// Register for broadcasts when a device is discovered and discovery has finished.
		broadcastReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(final Context context, final Intent intent) {
				String action = intent.getAction();
				if (BluetoothDevice.ACTION_FOUND.equals(action)) {
					// Get the BluetoothDevice object from the Intent
					BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
					// If it's already paired, skip it, because it's been listed already

					if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
						Fragment dialogFragment = getFragmentManager().findFragmentByTag("dialog");
						if (dialogFragment != null) {
							//Dialog is showing
							DeviceListFragment deviceList = (DeviceListFragment) dialogFragment;
							deviceList.addDevice(device.getName() + "\n" + device.getAddress());
						}
					}
				} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
					//setProgressBarIndeterminateVisibility(false);
					//setTitle(R.string.select_device);

					Fragment dialogFragment = getFragmentManager().findFragmentByTag("dialog");
					if (dialogFragment != null) {
						//Dialog is showing
						DeviceListFragment deviceList = (DeviceListFragment) dialogFragment;
						deviceList.onDiscoveryFinished();
					}
				}
			}
		};

		IntentFilter filter = new IntentFilter();
		filter.addAction(BluetoothDevice.ACTION_FOUND);
		filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		registerReceiver(broadcastReceiver, filter);

	}

	@Override
	public void onStart() {
		super.onStart();

		// Request that Bluetooth be enabled if not enabled already.
		// setupChat() will be called during onActivityResult()
		if (!bluetoothAdapter.isEnabled()) {
			Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
		} else {
			if (chatService == null) {
				startChatService();
			}
		}
	}

	@Override
	public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
		if (requestCode == REQUEST_ENABLE_BT) {
			if (resultCode == Activity.RESULT_OK) {
				startChatService();
			} else {
				Toast.makeText(this, "Bluetooth not enabled. Exiting.", Toast.LENGTH_SHORT).show();
				finish();
			}
		}
	}

	private void startChatService() {
		chatService = new ChatService(this, conversationFragment.getHandler());
	}

	public ChatService getChatService() {
		return chatService;
	}

	public void connectDevice(final String address, final boolean secure) {
		// Get the BluetoothDevice object
		BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);
		// Attempt to connect to the device
		chatService.connect(device, secure);
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		switch (item.getItemId()) {
			case R.id.secure_connect_scan:
				showDeviceList(true);
				return true;
			case R.id.insecure_connect_scan:
				showDeviceList(false);
				return true;
			case R.id.discoverable:
				enableDiscovery();
				return true;
		}
		return false;
	}

	private void showDeviceList(final boolean secure) {
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		Fragment previousFragment = getFragmentManager().findFragmentByTag("dialog");
		if (previousFragment != null) {
			transaction.remove(previousFragment);
		}
		transaction.addToBackStack(null).commit();

		// Launch the DeviceListActivity to see devices and do scan
		DeviceListFragment deviceListFragment = DeviceListFragment.newInstance(secure);
		deviceListFragment.show(getFragmentManager(), "dialog");
	}

	private void enableDiscovery() {
		if (bluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
			Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
			discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
			startActivity(discoverableIntent);
		}
	}

	@Override
	public synchronized void onResume() {
		super.onResume();
		if (chatService != null) {
			// Only if the state is STATE_NONE, do we know that we haven't started already
			if (chatService.getState() == ChatService.STATE_NONE) {
				chatService.start();
			}
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		this.unregisterReceiver(broadcastReceiver);

		if (chatService != null) {
			chatService.stop();
		}
	}
}