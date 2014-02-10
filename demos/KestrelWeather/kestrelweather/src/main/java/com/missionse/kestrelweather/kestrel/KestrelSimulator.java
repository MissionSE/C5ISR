package com.missionse.kestrelweather.kestrel;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
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

public class KestrelSimulator {

	private Activity mActivity;
	private BluetoothConnector mBluetoothConnector;

	public KestrelSimulator(final Activity activity) {
		mActivity = activity;
	}

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

	public void onDestroy() {
		mBluetoothConnector.onDestroy();
	}

	public boolean checkBluetoothAvailability() {
		if (BluetoothAdapter.getDefaultAdapter() == null) {
			Toast.makeText(mActivity, R.string.error_bluetooth_not_available, Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

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

	public void stopSimulator() {
		mBluetoothConnector.stopService();
	}

	private void handleRequestMessage() {
		NewRequestAlertDialogFragment dialogFragment = new NewRequestAlertDialogFragment();
		dialogFragment.setTargetRunnable(mSendDataMessageRunnable);
		dialogFragment.show(mActivity.getFragmentManager(), "newrequestdialog");
	}

	private final Runnable mSendDataMessageRunnable = new Runnable() {
		@Override
		public void run() {
			KestrelMessage dataMessage = new KestrelMessage(KestrelMessage.DATA);
			Random random = new Random(System.currentTimeMillis());
			dataMessage.setTemperature(random.nextFloat() * 100);
			dataMessage.setHumidity(random.nextFloat() * 100 - 50);
			dataMessage.setPressure(random.nextFloat() * 100 - 25);
			dataMessage.setPressureTrend(random.nextFloat());
			dataMessage.setHeatIndex(random.nextInt(10));
			dataMessage.setWindSpeed(random.nextFloat() * 100);
			dataMessage.setWindDirection(random.nextFloat() * 360);
			dataMessage.setWindChill(random.nextFloat() * -40);
			dataMessage.setDewPoint(random.nextFloat() * 5);

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
