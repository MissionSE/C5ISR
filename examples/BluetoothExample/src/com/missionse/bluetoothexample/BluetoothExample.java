package com.missionse.bluetoothexample;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.missionse.bluetooth.BluetoothConnector;
import com.missionse.bluetooth.BluetoothIntentListener;
import com.missionse.bluetooth.network.BluetoothNetworkService;
import com.missionse.bluetooth.network.ServiceIdentifier;

/**
 * Acts as the entry point to the BluetoothExample application.
 */
public class BluetoothExample extends Activity {

	private static final int REQUEST_ENABLE_BT = 1;
	private static final int DISCOVERY_TIME = 20; //in seconds

	private String mConnectedDevice = "";

	private BluetoothConnector mBluetoothConnector;
	private ConversationFragment mConversationFragment;

	// The Handler that gets information back from the BluetoothNetworkService.
	@SuppressLint("HandlerLeak")
	private final Handler mBluetoothServiceMessageHandler = new Handler() {
		@Override
		public void handleMessage(final Message msg) {
			switch (msg.what) {
				case BluetoothNetworkService.MESSAGE_STATE_CHANGE:
					switch (msg.arg1) {
						case BluetoothNetworkService.STATE_CONNECTED:
							getActionBar().setSubtitle("connected to " + mConnectedDevice);
							break;
						case BluetoothNetworkService.STATE_CONNECTING:
							getActionBar().setSubtitle("connecting...");
							break;
						case BluetoothNetworkService.STATE_LISTEN:
						case BluetoothNetworkService.STATE_NONE:
							getActionBar().setSubtitle("not connected");
							break;
						default:
							break;
					}
					break;
				case BluetoothNetworkService.MESSAGE_DEVICE_NAME:
					mConnectedDevice = (String) msg.obj;
					Toast.makeText(BluetoothExample.this, "Connected to " + mConnectedDevice, Toast.LENGTH_SHORT)
							.show();
					break;
				case BluetoothNetworkService.MESSAGE_TOAST:
					String message = (String) msg.obj;
					Toast.makeText(BluetoothExample.this, message, Toast.LENGTH_SHORT).show();
					break;
				default:
					break;
			}
		}
	};

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_bluetooth_example);

		if (BluetoothAdapter.getDefaultAdapter() == null) {
			Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
			finish();
			return;
		}

		mBluetoothConnector = new BluetoothConnector(this);

		mConversationFragment = new ConversationFragment();

		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.content, mConversationFragment);
		transaction.commit();

		mBluetoothConnector.onCreate(new BluetoothIntentListener() {
			@Override
			public void onDeviceFound(final BluetoothDevice device) {
				// If it's already paired, skip it, because it's been listed already.
				if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
					Fragment dialogFragment = getFragmentManager().findFragmentByTag("dialog");
					if (dialogFragment != null) {
						DeviceListFragment deviceList = (DeviceListFragment) dialogFragment;
						deviceList.addDevice(device.getName() + "\n" + device.getAddress());
					}
				}
			}

			@Override
			public void onDiscoveryFinished() {
				Fragment dialogFragment = getFragmentManager().findFragmentByTag("dialog");
				if (dialogFragment != null) {
					DeviceListFragment deviceList = (DeviceListFragment) dialogFragment;
					deviceList.onDiscoveryFinished();
				}
			}
		});
	}

	@Override
	public void onStart() {
		super.onStart();

		// Request that Bluetooth be enabled if not enabled already.
		if (!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
			Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
		} else {
			createNetworkService();
		}
	}

	@Override
	public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
		if (requestCode == REQUEST_ENABLE_BT) {
			if (resultCode == Activity.RESULT_OK) {
				createNetworkService();
			} else {
				Toast.makeText(this, "Bluetooth not enabled. Exiting.", Toast.LENGTH_SHORT).show();
				finish();
			}
		}
	}

	private void createNetworkService() {
		ServiceIdentifier.identifyService(ServiceIdentifier.ConnectionType.SECURE, "BluetoothExampleSecure",
				"fa87c0d0-afac-11de-8a39-0800200c9a66");

		mBluetoothConnector.registerHandler(mConversationFragment.getHandler());
		mBluetoothConnector.registerHandler(mBluetoothServiceMessageHandler);
		mBluetoothConnector.createService();
	}

	/**
	 * Connects this device to the specified address.
	 * @param address the address to connect to
	 * @param type the type of connection
	 */
	public void connectDevice(final String address, final ServiceIdentifier.ConnectionType type) {
		mBluetoothConnector.connect(address, type);
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		getMenuInflater().inflate(R.menu.main_menu, menu);
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
			default:
				break;
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

		// Launch the DeviceListActivity to see devices and do a scan.
		DeviceListFragment deviceListFragment = DeviceListFragment.newInstance(secure);
		deviceListFragment.show(getFragmentManager(), "dialog");
	}

	private void enableDiscovery() {
		mBluetoothConnector.startDiscovery(DISCOVERY_TIME);
	}

	@Override
	public synchronized void onResume() {
		super.onResume();
		mBluetoothConnector.startService();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mBluetoothConnector.stopService();
	}

	/**
	 * Sends the specified string across the wire using the BluetoothConnector.
	 * @param message the message to be sent
	 * @return whether or not sending was successful
	 */
	public boolean sendMessage(final String message) {
		// Check that there's actually something to send.
		byte[] data = message.getBytes();

		if (!mBluetoothConnector.getService().write(data)) {
			Toast.makeText(this, "Error: Not connected.", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
}
