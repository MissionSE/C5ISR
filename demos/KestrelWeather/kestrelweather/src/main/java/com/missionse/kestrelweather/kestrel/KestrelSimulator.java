package com.missionse.kestrelweather.kestrel;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.location.Location;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.missionse.bluetooth.BluetoothConnector;
import com.missionse.bluetooth.BluetoothIntentListener;
import com.missionse.bluetooth.network.BluetoothNetworkService;
import com.missionse.bluetooth.network.ServiceIdentifier;
import com.missionse.kestrelweather.KestrelWeatherActivity;
import com.missionse.kestrelweather.R;
import com.missionse.kestrelweather.communication.KestrelMessage;
import com.missionse.kestrelweather.database.model.tables.KestrelWeather;
import com.missionse.kestrelweather.util.OpenWeatherRequester;

/**
 * This class generates KestrelMessages, simulating an external Kestrel device. It manages the Bluetooth Network Service,
 * accepting requests for Kestrel data and replying with a Kestrel data message.
 */
public class KestrelSimulator {

	private KestrelWeatherActivity mActivity;
	private BluetoothConnector mBluetoothConnector;

	/**
	 * Constructor.
	 * @param activity the parent activity
	 */
	public KestrelSimulator(final KestrelWeatherActivity activity) {
		mActivity = activity;
	}

	/**
	 * Creates the BluetoothConnector. To be called from the parent activity's onCreate().
	 */
	public void onCreate() {
		mBluetoothConnector = new BluetoothConnector(mActivity);
		mBluetoothConnector.onCreate(new BluetoothIntentListener() {
			@Override
			public void onDeviceFound(final BluetoothDevice device) {
			}

			@Override
			public void onDiscoveryFinished() {
			}
		});
	}

	/**
	 * Notifies the BluetoothConnector of activity start. To be called from the parent activity's onStart().
	 */
	public void onStart() {
		mBluetoothConnector.onStart();
	}

	/**
	 * Notifies the BluetoothConnector of activity stop. To be called from the parent activity's onStop().
	 */
	public void onStop() {
		mBluetoothConnector.onStop();
	}

	/**
	 * Checks the availability of the bluetooth feature. Will display a Toast if it isn't.
	 * @return whether or not Bluetooth is available
	 */
	public boolean checkBluetoothAvailability() {
		if (BluetoothAdapter.getDefaultAdapter() == null) {
			Toast.makeText(mActivity, R.string.error_bluetooth_not_available, Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	/**
	 * Starts the Kestrel Simulator service, which listens for incoming bluetooth data.
	 */
	public void startSimulator() {
		if (!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
			Toast.makeText(mActivity, R.string.error_bluetooth_not_enabled, Toast.LENGTH_SHORT).show();
		} else {
			createNetworkService();
		}

		mBluetoothConnector.startService();
	}

	private void createNetworkService() {
		ServiceIdentifier.identifyService(ServiceIdentifier.ConnectionType.SECURE, "KestrelWeatherSecure",
				"79d137f1-be68-4a2a-b8ba-d8c71c4b4d75");

		mBluetoothConnector.registerHandler(mBluetoothServiceMessageHandler);
		mBluetoothConnector.createService();
	}

	/**
	 * Stops the Kestrel Simulator service.
	 */
	public void stopSimulator() {
		mBluetoothConnector.stopService();
	}

	private void handleRequestMessage() {
		NewRequestAlertDialogFragment dialogFragment = new NewRequestAlertDialogFragment();
		dialogFragment.setTargetRunnable(mSendSavedDataMessageRunnable, mSendCurrentConditionDataMessageRunnable);
		dialogFragment.show(mActivity.getFragmentManager(), "newrequestdialog");
	}

	private final Runnable mSendSavedDataMessageRunnable = new Runnable() {
		@Override
		public void run() {
			KestrelWeather kestrelWeather = KestrelWeatherFactory.getSavedKestrelWeatherData(mActivity);

			KestrelMessage dataMessage = new KestrelMessage(KestrelMessage.DATA);
			dataMessage.setKestrelWeather(kestrelWeather);

			mBluetoothConnector.getService().write(dataMessage.toString().getBytes());
		}
	};

	private final Runnable mSendCurrentConditionDataMessageRunnable = new Runnable() {
		@Override
		public void run() {
			if (mActivity != null) {
				Location location = mActivity.getLastLocation();
				if (location != null) {
					OpenWeatherRequester.queryOpenWeather(
							mActivity,
							Double.toString(location.getLatitude()),
							Double.toString((location.getLongitude())),
							new FutureCallback<JsonObject>() {
								@Override
								public void onCompleted(final Exception e, final JsonObject result) {
									if (e == null && result != null) {
										KestrelWeather kestrelWeather =
												KestrelWeatherFactory.getCurrentWeatherData(result);
										KestrelMessage dataMessage = new KestrelMessage(KestrelMessage.DATA);
										dataMessage.setKestrelWeather(kestrelWeather);

										mBluetoothConnector.getService().write(dataMessage.toString().getBytes());
									} else {
										Toast.makeText(mActivity, "Unable to fetch current conditions.", Toast.LENGTH_SHORT).show();
										handleRequestMessage();
									}
								}
							}
					);
				}
			}
		}
	};

	private final Handler mBluetoothServiceMessageHandler = new Handler() {
		@Override
		public void handleMessage(final Message msg) {
			switch (msg.what) {
				case BluetoothNetworkService.MESSAGE_INCOMING_DATA:
					byte[] readBuf = (byte[]) msg.obj;
					String message = new String(readBuf, 0, msg.arg1);
					KestrelMessage kestrelMessage = null;
					try {
						kestrelMessage = KestrelMessage.translateRawMessage(message);
					} catch (KestrelMessage.InvalidKestrelMessageException ex) {
						ex.printStackTrace();
					}
					if (kestrelMessage != null && kestrelMessage.isRequest()) {
						handleRequestMessage();
					}
					break;
				default:
					break;
			}
		}
	};
}
