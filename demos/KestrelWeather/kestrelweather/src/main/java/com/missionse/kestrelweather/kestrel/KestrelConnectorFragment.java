package com.missionse.kestrelweather.kestrel;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.missionse.bluetooth.BluetoothConnector;
import com.missionse.bluetooth.BluetoothIntentListener;
import com.missionse.bluetooth.network.BluetoothNetworkService;
import com.missionse.bluetooth.network.ServiceIdentifier;
import com.missionse.kestrelweather.KestrelWeatherActivity;
import com.missionse.kestrelweather.R;
import com.missionse.kestrelweather.communication.BluetoothDeviceListFragment;
import com.missionse.kestrelweather.communication.KestrelMessage;
import com.missionse.kestrelweather.database.DatabaseAccessor;
import com.missionse.kestrelweather.database.model.tables.KestrelWeather;
import com.missionse.kestrelweather.database.model.tables.OpenWeather;
import com.missionse.kestrelweather.database.model.tables.Report;
import com.missionse.kestrelweather.reports.ReportDetailFragment;
import com.missionse.kestrelweather.util.OpenWeatherRequester;
import com.missionse.kestrelweather.util.ReportBuilder;

import java.util.Map;

/**
 * Provides a fragment to connect to a kestrel device.
 */
public class KestrelConnectorFragment extends Fragment {
	private static final String TAG = KestrelConnectorFragment.class.getSimpleName();
	private static final int REQUEST_ENABLE_BT = 1;
	private static final int REQUEST_DEVICE_SELECTION = 2;

	private KestrelWeatherActivity mActivity;
	private Button mConnectToDeviceButton;
	private Button mRequestReadingsButton;
	private Button mContinueButton;
	private ArrayAdapter<String> mReadingsAdapter;

	private BluetoothConnector mBluetoothConnector;

	private KestrelWeather mKestrelWeather;
	private Location mLocation;
	private OpenWeather mOpenWeather;

	private String mConnectedDevice = "";

	@Override
	public void onAttach(final Activity activity) {
		super.onAttach(activity);
		try {
			mActivity = (KestrelWeatherActivity) activity;
		} catch (ClassCastException exception) {
			Log.d(TAG, "Unable to cast activity.");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mActivity = null;
	}

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (mActivity != null) {
			if (BluetoothAdapter.getDefaultAdapter() == null) {
				Toast.makeText(mActivity, R.string.error_bluetooth_not_available, Toast.LENGTH_SHORT).show();
				mActivity.getFragmentManager().popBackStack();
				return;
			}

			mBluetoothConnector = new BluetoothConnector(mActivity);
			mBluetoothConnector.onCreate(new BluetoothIntentListener() {
				@Override
				public void onDeviceFound(final BluetoothDevice device) {
					if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
						Fragment dialogFragment = getChildFragmentManager().findFragmentByTag("dialog");
						if (dialogFragment != null) {
							BluetoothDeviceListFragment deviceListDialog = (BluetoothDeviceListFragment) dialogFragment;
							deviceListDialog.addDevice(device);
						}
					}
				}

				@Override
				public void onDiscoveryFinished() {
					Fragment dialogFragment = getChildFragmentManager().findFragmentByTag("dialog");
					if (dialogFragment != null) {
						BluetoothDeviceListFragment deviceListDialog = (BluetoothDeviceListFragment) dialogFragment;
						deviceListDialog.onDiscoveryFinished();
					}
				}
			});
		}
	}

	@Override
	public void onStart() {
		super.onStart();

		if (!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
			Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
		} else {
			createNetworkService();
		}
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

	@Override
	public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
		if (requestCode == REQUEST_ENABLE_BT) {
			if (resultCode == Activity.RESULT_OK) {
				createNetworkService();
			} else {
				if (mActivity != null) {
					Toast.makeText(mActivity, R.string.error_bluetooth_not_enabled, Toast.LENGTH_SHORT).show();
					mActivity.getFragmentManager().popBackStack();
				}
			}
		} else if (requestCode == REQUEST_DEVICE_SELECTION) {
			if (resultCode == Activity.RESULT_OK) {
				String mConnectedMacAddress = data.getStringExtra(BluetoothDeviceListFragment.MAC_ADDRESS_KEY);
				mBluetoothConnector.connect(mConnectedMacAddress, ServiceIdentifier.ConnectionType.SECURE);
			}
		}
	}

	private void createNetworkService() {
		ServiceIdentifier.identifyService(ServiceIdentifier.ConnectionType.SECURE, "KestrelWeatherSecure",
				"79d137f1-be68-4a2a-b8ba-d8c71c4b4d75");

		mBluetoothConnector.registerHandler(mBluetoothServiceMessageHandler);
		mBluetoothConnector.createService();
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.fragment_kestrel_connector, container, false);

		if (contentView != null) {
			mConnectToDeviceButton = (Button) contentView.findViewById(R.id.connect_to_device_btn);
			mRequestReadingsButton = (Button) contentView.findViewById(R.id.request_readings_btn);
			ListView receivedReadings = (ListView) contentView.findViewById(R.id.device_readings);
			mReadingsAdapter = new ArrayAdapter<String>(mActivity, R.layout.fragment_kestrel_connector_reading_entry);
			receivedReadings.setEmptyView(contentView.findViewById(R.id.no_readings_available));
			receivedReadings.setAdapter(mReadingsAdapter);
			mContinueButton = (Button) contentView.findViewById(R.id.continue_btn);

			setInitialButtonStates();
			setButtonBehavior();
		}

		return contentView;
	}

	private void setInitialButtonStates() {
		mConnectToDeviceButton.setEnabled(true);
		mRequestReadingsButton.setEnabled(false);
		mContinueButton.setEnabled(false);
	}

	private void setButtonBehavior() {
		mConnectToDeviceButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View view) {
				if (mRequestReadingsButton.isEnabled()) {
					mBluetoothConnector.disconnect();
				} else {
					if (mActivity != null) {
						SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mActivity);
						if (preferences.getBoolean(getString(R.string.key_simulation_mode), false)) {
							showSimulationModeAlert();
						} else {
							showBluetoothDialog();
						}
					} else {
						showBluetoothDialog();
					}
				}
			}
		});
		mRequestReadingsButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View view) {
				KestrelMessage requestMessage = new KestrelMessage(KestrelMessage.REQUEST);
				if (!mBluetoothConnector.getService().write(requestMessage.toString().getBytes())) {
					if (mActivity != null) {
						Toast.makeText(mActivity, R.string.error_unable_to_request, Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
		mContinueButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View view) {
				if (mActivity != null) {
					if (mKestrelWeather != null && mOpenWeather != null && mLocation != null) {
						Report report = ReportBuilder.buildReport(getDatabaseAccessor(), mKestrelWeather, mOpenWeather,
								mLocation.getLatitude(), mLocation.getLongitude());

						mActivity.getFragmentManager().beginTransaction()
								.setCustomAnimations(
										R.animator.fade_in, R.animator.fade_out,
										R.animator.fade_in, R.animator.fade_out)
								.replace(R.id.content, ReportDetailFragment.newInstance(report.getId()), "report_addon")
								.addToBackStack("report_addon")
								.commit();
					}
				}
			}
		});
	}

	private void showSimulationModeAlert() {
		SimulationModeAlertDialogFragment simModeAlert = new SimulationModeAlertDialogFragment();
		simModeAlert.setTargetRunnable(mUseSavedDataRunnable, mUseCurrentConditionsRunnable);
		simModeAlert.show(mActivity.getFragmentManager(), "simmodealert");
	}

	private void showBluetoothDialog() {
		FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
		Fragment previousFragment = getChildFragmentManager().findFragmentByTag("dialog");
		if (previousFragment != null) {
			transaction.remove(previousFragment);
		}
		transaction.addToBackStack(null).commit();

		BluetoothDeviceListFragment deviceListFragment = BluetoothDeviceListFragment.newInstance();
		deviceListFragment.setTargetFragment(KestrelConnectorFragment.this, REQUEST_DEVICE_SELECTION);
		deviceListFragment.show(getChildFragmentManager(), "dialog");
	}

	private final Runnable mUseSavedDataRunnable = new Runnable() {
		@Override
		public void run() {
			if (mActivity != null) {
				mKestrelWeather = KestrelWeatherFactory.getSavedKestrelWeatherData(mActivity);
				updateReadingsAdapter();
				getOpenWeatherData();
			}
		}
	};

	private final Runnable mUseCurrentConditionsRunnable = new Runnable() {
		@Override
		public void run() {
			queryOpenWeather(
					new FutureCallback<JsonObject>() {
						@Override
						public void onCompleted(final Exception e, final JsonObject result) {
							boolean requestSuccessful = false;
							if (e == null && result != null) {
								mKestrelWeather = KestrelWeatherFactory.getCurrentWeatherData(result);
								mOpenWeather = OpenWeatherRequester.parseOpenWeatherResponse(result);
								if (mKestrelWeather != null && mOpenWeather != null) {
									updateReadingsAdapter();
									mContinueButton.setEnabled(true);
									requestSuccessful = true;
								}
							}

							if (!requestSuccessful) {
								Toast.makeText(mActivity, "Unable to fetch current conditions.", Toast.LENGTH_SHORT).show();
								showSimulationModeAlert();
							}
						}
					}
			);
		}
	};

	private void updateReadingsAdapter() {
		mReadingsAdapter.clear();
		if (mKestrelWeather != null) {
			Map<String, String> kestrelWeatherData = mKestrelWeather.toMap();
			for (String key : kestrelWeatherData.keySet()) {
				String formattedKestrelData = String.format("%s: %s",
						key, kestrelWeatherData.get(key));
				mReadingsAdapter.add(formattedKestrelData);
			}
		}

		if (mOpenWeather != null) {
			Map<String, String> openWeatherData = mOpenWeather.toMap();
			for (String key : openWeatherData.keySet()) {
				String formattedOpenWeatherData = String.format("%s: %s",
						key, openWeatherData.get(key));
				mReadingsAdapter.add(formattedOpenWeatherData);
			}
		}

		if (mLocation != null) {
			mReadingsAdapter.add("latitude: " + mLocation.getLatitude());
			mReadingsAdapter.add("longitude: " + mLocation.getLongitude());
		}
	}

	private DatabaseAccessor getDatabaseAccessor() {
		DatabaseAccessor databaseAccessor = null;

		if (mActivity != null) {
			databaseAccessor = mActivity.getDatabaseAccessor();
		}

		return databaseAccessor;
	}

	private void getLocation() {
		if (mActivity != null) {
			mLocation = mActivity.getLastLocation();
			if (mLocation != null) {
				updateReadingsAdapter();
			} else {
				Toast.makeText(mActivity, "Unable to get current location.", Toast.LENGTH_SHORT).show();
			}
		}
	}

	private void queryOpenWeather(final FutureCallback<JsonObject> callback) {
		if (mActivity != null) {
			getLocation();
			if (mLocation != null) {
				OpenWeatherRequester.queryOpenWeather(
						mActivity,
						Double.toString(mLocation.getLatitude()),
						Double.toString(mLocation.getLongitude()),
						callback);
			}
		}
	}

	private void getOpenWeatherData() {
		queryOpenWeather(
				new FutureCallback<JsonObject>() {
					@Override
					public void onCompleted(final Exception e, final JsonObject result) {
						boolean requestSuccessful = false;
						if (e == null && result != null) {
							mOpenWeather = OpenWeatherRequester.parseOpenWeatherResponse(result);
							if (mOpenWeather != null) {
								requestSuccessful = true;
								updateReadingsAdapter();

								if (mKestrelWeather != null) {
									mContinueButton.setEnabled(true);
								}
							}
						}

						if (!requestSuccessful) {
							showAlertDialog();
						}
					}
				}
		);
	}

	private void showAlertDialog() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mActivity);
		alertDialogBuilder.setTitle("Warning");
		alertDialogBuilder.setIcon(R.drawable.ic_action_warning);
		alertDialogBuilder
				.setMessage("Unable to weather data.  Try again?")
				.setCancelable(false)
				.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();
						getOpenWeatherData();
					}
				})
				.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						mActivity.onBackPressed();
					}
				});
		alertDialogBuilder.create().show();
	}

	private final Handler mBluetoothServiceMessageHandler = new Handler() {
		@Override
		public void handleMessage(final Message msg) {
			switch (msg.what) {
				case BluetoothNetworkService.MESSAGE_STATE_CHANGE:
					switch (msg.arg1) {
						case BluetoothNetworkService.STATE_CONNECTED:
							mRequestReadingsButton.setText(getResources().getString(R.string.request_readings)
									+ " " + mConnectedDevice);
							mRequestReadingsButton.setEnabled(true);

							mConnectToDeviceButton.setText(getResources().getString(R.string.disconnect_from_device)
									+ " " + mConnectedDevice);
							break;
						case BluetoothNetworkService.STATE_CONNECTING:
							mRequestReadingsButton.setText(R.string.connecting);
							mRequestReadingsButton.setEnabled(false);

							mConnectToDeviceButton.setText(R.string.connect_to_device);
							break;
						case BluetoothNetworkService.STATE_LISTEN:
						case BluetoothNetworkService.STATE_NONE:
							mRequestReadingsButton.setText(R.string.no_device);
							mRequestReadingsButton.setEnabled(false);

							mConnectToDeviceButton.setText(R.string.connect_to_device);
							break;
						default:
							break;
					}
					break;
				case BluetoothNetworkService.MESSAGE_DEVICE_NAME:
					mConnectedDevice = (String) msg.obj;
					break;
				case BluetoothNetworkService.MESSAGE_INCOMING_DATA:
					byte[] readBuf = (byte[]) msg.obj;
					String message = new String(readBuf, 0, msg.arg1);
					KestrelMessage kestrelMessage;
					try {
						kestrelMessage = KestrelMessage.translateRawMessage(message);
						if (kestrelMessage != null && kestrelMessage.isData()) {
							mKestrelWeather = kestrelMessage.getKestrelWeather();
							updateReadingsAdapter();

							getOpenWeatherData();
						}
					} catch (KestrelMessage.InvalidKestrelMessageException ex) {
						ex.printStackTrace();
					}
					break;
				default:
					break;
			}
		}
	};
}
