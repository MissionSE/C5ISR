package com.missionse.commandablemodel;

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
import com.missionse.commandablemodel.network.ModelControllerClient;
import com.missionse.commandablemodel.network.ModelState;
import com.missionse.commandablemodel.network.NotifyingModelGestureListener;
import com.missionse.modelviewer.ModelViewerFragment;
import com.missionse.modelviewer.ModelViewerFragmentFactory;

public class CommandableModelActivity extends Activity {

	private static final int REQUEST_ENABLE_BT = 1;

	private ModelViewerFragment modelFragment;

	private NotifyingModelGestureListener modelGestureListener;

	private ModelControllerClient modelClient;

	private BluetoothConnector bluetoothConnector;
	private String connectedDevice;

	// The Handler that gets information back from the BluetoothNetworkService.
	@SuppressLint("HandlerLeak")
	private final Handler bluetoothServiceMessageHandler = new Handler() {
		@Override
		public void handleMessage(final Message msg) {
			switch (msg.what) {
				case BluetoothNetworkService.MESSAGE_STATE_CHANGE:
					switch (msg.arg1) {
						case BluetoothNetworkService.STATE_CONNECTED:
							getActionBar().setSubtitle("connected to " + connectedDevice);
							break;
						case BluetoothNetworkService.STATE_CONNECTING:
							getActionBar().setSubtitle("connecting...");
							break;
						case BluetoothNetworkService.STATE_LISTEN:
						case BluetoothNetworkService.STATE_NONE:
							getActionBar().setSubtitle("not connected");
							break;
					}
					break;
				case BluetoothNetworkService.MESSAGE_DEVICE_NAME:
					connectedDevice = (String) msg.obj;
					Toast.makeText(CommandableModelActivity.this, "Connected to " + connectedDevice, Toast.LENGTH_SHORT)
							.show();
					break;
				case BluetoothNetworkService.MESSAGE_TOAST:
					String message = (String) msg.obj;
					Toast.makeText(CommandableModelActivity.this, message, Toast.LENGTH_SHORT).show();
					break;
				case BluetoothNetworkService.MESSAGE_INCOMING_DATA:
					byte[] readBuf = (byte[]) msg.obj;
					ModelState modelState = new ModelState();
					if (modelState.setModelValues(new String(readBuf, 0, msg.arg1))) {
						setModelState(modelState);
					}
					break;
			}
		}
	};

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.app_name);
		setContentView(R.layout.activity_main);

		// Create the model client
		modelClient = new ModelControllerClient(this);

		// Create the gesture listener and add the model client as a recipient of model changes
		modelGestureListener = new NotifyingModelGestureListener();
		modelGestureListener.addRecipient(modelClient);

		// Create the ModelViewer fragment and give the ModelController to the ModelClient (to retrieve state changes)
		// when necessary
		modelFragment = ModelViewerFragmentFactory.createObjModelFragment(R.raw.multiobjects_obj, modelGestureListener);
		modelClient.setModelViewerFragment(modelFragment);

		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.content, modelFragment);
		transaction.commit();

		if (BluetoothAdapter.getDefaultAdapter() == null) {
			Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
			finish();
			return;
		}

		bluetoothConnector = new BluetoothConnector(this);

		bluetoothConnector.onCreate(new BluetoothIntentListener() {
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
		ServiceIdentifier.setSecureServiceName("CommandableModelSecure");

		ServiceIdentifier.setSecureUUIDFromString("65b4f759-a7a2-46a7-a501-f22a666d1375");

		bluetoothConnector.registerHandler(bluetoothServiceMessageHandler);
		bluetoothConnector.createService();
	}

	public void connectDevice(final String address, final boolean secure) {
		bluetoothConnector.connect(address, secure);
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		switch (item.getItemId()) {
			case R.id.connect_scan:
				showDeviceList(true);
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

		// Launch the DeviceListActivity to see devices and do a scan.
		DeviceListFragment deviceListFragment = DeviceListFragment.newInstance(secure);
		deviceListFragment.show(getFragmentManager(), "dialog");
	}

	private void enableDiscovery() {
		bluetoothConnector.startDiscovery(20);
	}

	@Override
	public synchronized void onResume() {
		super.onResume();
		bluetoothConnector.startService();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		bluetoothConnector.stopService();
	}

	public boolean sendModelState(final String message) {
		// Check that there's actually something to send.
		byte[] data = message.getBytes();

		if (!bluetoothConnector.getService().write(data)) {
			return false;
		};
		return true;
	}

	private void setModelState(final ModelState state) {
		modelFragment.getController().setOrientation(
				state.get(ModelState.ORIENTATION_W),
				state.get(ModelState.ORIENTATION_X),
				state.get(ModelState.ORIENTATION_Y),
				state.get(ModelState.ORIENTATION_Z));
		modelFragment.getController().setScale(
				state.get(ModelState.SCALE_X),
				state.get(ModelState.SCALE_Y),
				state.get(ModelState.SCALE_Z));
		modelFragment.getController().setPosition(
				state.get(ModelState.POSITION_X),
				state.get(ModelState.POSITION_Y),
				state.get(ModelState.POSITION_Z));
	}
}
