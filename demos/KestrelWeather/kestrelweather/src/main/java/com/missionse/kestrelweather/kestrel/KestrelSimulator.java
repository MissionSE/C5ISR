package com.missionse.kestrelweather.kestrel;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.missionse.bluetooth.BluetoothConnector;
import com.missionse.bluetooth.BluetoothIntentListener;
import com.missionse.bluetooth.network.BluetoothNetworkService;
import com.missionse.bluetooth.network.ServiceIdentifier;
import com.missionse.kestrelweather.R;
import com.missionse.kestrelweather.communication.KestrelMessage;

import java.util.Random;

/**
 * This class generates KestrelMessages, simulating an external Kestrel device. It manages the Bluetooth Network Service,
 * accepting requests for Kestrel data and replying with a Kestrel data message.
 */
public class KestrelSimulator {

	private Activity mActivity;
	private BluetoothConnector mBluetoothConnector;

	/**
	 * Constructor.
	 * @param activity the parent activity
	 */
	public KestrelSimulator(final Activity activity) {
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
	 * Notifies the BluetoothConnector of activity stop. To be called from the parent activity's onStop().
	 */
	public void onStop() {
		mBluetoothConnector.onStop();
	}

	/**
	 * Checks the availability of the bluetooth feature. Will display a Toast if it isn't.
	 * @return whehter or not Bluetooth is available
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
		dialogFragment.setTargetRunnable(mSendSavedDataMessageRunnable, mSendRandomDataMessageRunnable);
		dialogFragment.show(mActivity.getFragmentManager(), "newrequestdialog");
	}

	private final Runnable mSendSavedDataMessageRunnable = new Runnable() {
		@Override
		public void run() {
			SharedPreferences mSharedPreferences = mActivity.getSharedPreferences(
				KestrelSimulationSharedPreferences.SIMULATION_PREFERENCES, 0);

			KestrelMessage dataMessage = new KestrelMessage(KestrelMessage.DATA);

			dataMessage.setTemperature(mSharedPreferences.getFloat(KestrelSimulationSharedPreferences.KESTREL_TEMPERATURE,
				KestrelSimulationSharedPreferences.NONSENSE_FLOAT));
			dataMessage.setHumidity(mSharedPreferences.getInt(KestrelSimulationSharedPreferences.KESTREL_HUMIDITY,
				KestrelSimulationSharedPreferences.NONSENSE_INT));
			dataMessage.setPressure(mSharedPreferences.getFloat(KestrelSimulationSharedPreferences.KESTREL_PRESSURE,
				KestrelSimulationSharedPreferences.NONSENSE_FLOAT));
			dataMessage.setPressureTrend(mSharedPreferences.getInt(KestrelSimulationSharedPreferences.KESTREL_PRESSURE_TREND,
				KestrelSimulationSharedPreferences.NONSENSE_INT));
			dataMessage.setHeatIndex(mSharedPreferences.getFloat(KestrelSimulationSharedPreferences.KESTREL_HEAT_IDX,
				KestrelSimulationSharedPreferences.NONSENSE_FLOAT));
			dataMessage.setWindSpeed(mSharedPreferences.getFloat(KestrelSimulationSharedPreferences.KESTREL_WIND_SPD,
				KestrelSimulationSharedPreferences.NONSENSE_FLOAT));
			dataMessage.setWindDirection(mSharedPreferences.getInt(KestrelSimulationSharedPreferences.KESTREL_WIND_DIR,
				KestrelSimulationSharedPreferences.NONSENSE_INT));
			dataMessage.setWindChill(mSharedPreferences.getFloat(KestrelSimulationSharedPreferences.KESTREL_WIND_CHILL,
				KestrelSimulationSharedPreferences.NONSENSE_FLOAT));
			dataMessage.setDewPoint(mSharedPreferences.getFloat(KestrelSimulationSharedPreferences.KESTREL_DEW_PT,
				KestrelSimulationSharedPreferences.NONSENSE_FLOAT));

			mBluetoothConnector.getService().write(dataMessage.toString().getBytes());
		}
	};

	private final Runnable mSendRandomDataMessageRunnable = new Runnable() {
		@Override
		public void run() {
			KestrelMessage dataMessage = new KestrelMessage(KestrelMessage.DATA);
			Random random = new Random(System.currentTimeMillis());
			dataMessage.setTemperature(random.nextFloat() * 200 - 100);
			dataMessage.setHumidity(random.nextInt(100));
			dataMessage.setPressure(random.nextFloat() * 2 + 29);
			dataMessage.setPressureTrend(random.nextInt(1));
			dataMessage.setHeatIndex(random.nextFloat() * dataMessage.getHumidity()/10 + dataMessage.getTemperature());
			dataMessage.setWindSpeed(random.nextFloat() * 30);
			dataMessage.setWindDirection(random.nextInt(360));
			dataMessage.setWindChill(random.nextFloat() * -1 * dataMessage.getWindSpeed()/6);
			dataMessage.setDewPoint(random.nextFloat() * 60);

			mBluetoothConnector.getService().write(dataMessage.toString().getBytes());
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
