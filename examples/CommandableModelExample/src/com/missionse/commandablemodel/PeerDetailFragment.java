package com.missionse.commandablemodel;

import android.app.Fragment;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.missionse.wifidirect.WifiDirectConnector;

public class PeerDetailFragment extends Fragment {

	// private static final String TAG = PeerDetailFragment.class.getSimpleName();

	private View contentView;

	private WifiP2pDevice selectedDevice;
	private WifiP2pInfo connectionSuccessfulInfo;

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		contentView = inflater.inflate(R.layout.commandable_model_peer_detail, null);
		contentView.findViewById(R.id.peer_connection_progress).setVisibility(View.GONE);

		contentView.findViewById(R.id.btn_connect).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View v) {
				((CommandableModelActivity) getActivity()).connect();
			}
		});
		contentView.findViewById(R.id.btn_disconnect).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View v) {
				((CommandableModelActivity) getActivity()).disconnect();
			}
		});

		return contentView;
	}

	public void setTargetDevice(final WifiP2pDevice device) {
		// Called by the main activity, when a device is selected in the PeersList.
		selectedDevice = device;
	}

	public void setConnectionSuccessfulInformation(final WifiP2pInfo info) {
		// Called by the main activity, when a connection is made.
		connectionSuccessfulInfo = info;
	}

	public void refresh() {
		// Refreshes all information to be shown. This is called by the main activity when necessary. The main
		// activity ensures that this view is showing (and therefore will return Views to be manipulated
		// successfully) before calling.
		setConnectButtonEnabled(false);
		setDisconnectButtonEnabled(false);

		if (selectedDevice != null) {
			TextView view = (TextView) contentView.findViewById(R.id.device_detail_status);
			view.setText("Status: " + WifiDirectConnector.deviceStatuses.get(selectedDevice.status));

			view = (TextView) contentView.findViewById(R.id.device_detail_primarytype);
			view.setText("Primary type: " + selectedDevice.primaryDeviceType);

			view = (TextView) contentView.findViewById(R.id.device_detail_address);
			view.setText("Address: " + selectedDevice.deviceAddress);

			if (connectionSuccessfulInfo != null) {
				contentView.findViewById(R.id.device_detail_success_info).setVisibility(View.VISIBLE);

				view = (TextView) contentView.findViewById(R.id.host_address);
				view.setText("Host address: " + connectionSuccessfulInfo.groupOwnerAddress.getHostAddress());

				view = (TextView) contentView.findViewById(R.id.group_owner);
				view.setText("Group owner: " + ((connectionSuccessfulInfo.isGroupOwner == true) ? "yes" : "no"));

				setDisconnectButtonEnabled(true);
			} else {
				contentView.findViewById(R.id.device_detail_success_info).setVisibility(View.GONE);
				setConnectButtonEnabled(true);
			}
		} else {
			TextView view = (TextView) contentView.findViewById(R.id.device_detail_status);
			view.setText("");

			view = (TextView) contentView.findViewById(R.id.device_detail_primarytype);
			view.setText("");

			view = (TextView) contentView.findViewById(R.id.device_detail_address);
			view.setText("");

			view = (TextView) contentView.findViewById(R.id.host_address);
			view.setText("");

			view = (TextView) contentView.findViewById(R.id.group_owner);
			view.setText("");
		}
	}

	private void setConnectButtonEnabled(final boolean enabled) {
		contentView.findViewById(R.id.btn_connect).setEnabled(enabled);
	}

	private void setDisconnectButtonEnabled(final boolean enabled) {
		contentView.findViewById(R.id.btn_disconnect).setEnabled(enabled);
	}
}
