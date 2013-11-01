package com.missionse.commandablemodel;

import android.app.Activity;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;

import com.missionse.wifidirect.WifiUtilities;

public class WifiDirectModelNotifier implements ModelNotifier, WifiDirectConnectionListener {

	private final Activity activity;
	private final ModelStatus modelStatus;
	private WifiP2pInfo connectionInfo;
	private WifiP2pDevice targetDevice;

	public WifiDirectModelNotifier (final Activity activity, final ModelStatus status) {
		this.activity = activity;
		modelStatus = status;
		connectionInfo = null;
		targetDevice = null;
	}

	@Override
	public void onConnectionSuccessful(final WifiP2pInfo p2pInfo, final WifiP2pDevice p2pDevice) {
		connectionInfo = p2pInfo;
		targetDevice = p2pDevice;
	}

	@Override
	public void sendModelStatus() {
		String address = "";

		if (connectionInfo != null && targetDevice != null) {
			if (connectionInfo.isGroupOwner) {
				address = WifiUtilities.getIPAddressFromMacAddress(targetDevice.deviceAddress);
			} else {
				address = connectionInfo.groupOwnerAddress.getHostAddress();
			}

			Intent modelStatusIntent = new Intent(activity, ModelStatusSenderIntent.class);
			modelStatusIntent.setAction(ModelStatusSenderIntent.ACTION_SEND_MODEL_STATUS);
			modelStatusIntent.putExtra(ModelStatusSenderIntent.EXTRAS_STATUS, modelStatus.toString());
			modelStatusIntent.putExtra(ModelStatusSenderIntent.EXTRAS_HOST, address);
			modelStatusIntent.putExtra(ModelStatusSenderIntent.EXTRAS_PORT, ModelControllerServer.PORT);
		}
	}
}
