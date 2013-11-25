package com.missionse.wifidirect.network;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;
import android.util.Log;

import com.missionse.wifidirect.WifiUtilities;

public class Client {

	private Context context;

	private WifiP2pInfo connectionInfo;
	private WifiP2pDevice targetDevice;

	public Client(final Context context) {
		this.context = context;
	}

	public void setConnectionSuccessful(final WifiP2pInfo p2pInfo, final WifiP2pDevice p2pDevice) {
		connectionInfo = p2pInfo;
		targetDevice = p2pDevice;
	}

	public void onDisconnect() {
		connectionInfo = null;
		targetDevice = null;
	}

	public boolean sendData(final byte[] data) {
		if (connectionInfo != null && targetDevice != null) {
			String address = "";
			if (connectionInfo.isGroupOwner) {
				address = WifiUtilities.getIPAddressFromMacAddress(targetDevice.deviceAddress);
			} else {
				address = connectionInfo.groupOwnerAddress.getHostAddress();
			}

			Log.e(Client.class.getSimpleName(), "Sending to " + address);

			Intent modelStatusIntent = new Intent(context, ClientIntentService.class);
			modelStatusIntent.setAction(ClientIntentService.ACTION_SEND_DATA);

			modelStatusIntent.putExtra(ClientIntentService.EXTRAS_DATA, data);
			modelStatusIntent.putExtra(ClientIntentService.EXTRAS_HOST, address);
			modelStatusIntent.putExtra(ClientIntentService.EXTRAS_PORT, Server.PORT);

			context.startService(modelStatusIntent);
			return true;
		} else {
			return false;
		}
	}
}
