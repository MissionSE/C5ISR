package com.missionse.wifidirect.network;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;
import android.util.Log;

import com.missionse.wifidirect.WifiUtilities;

/**
 * A client class for initiating data sending via an intent service.
 */
public class Client {

	private Context mContext;

	private WifiP2pInfo mConnectionInfo;
	private WifiP2pDevice mTargetDevice;

	/**
	 * Creates a Client, given the context of its creator.
	 * @param context Android Context of the parent activity
	 */
	public Client(final Context context) {
		this.mContext = context;
	}

	/**
	 * Sets data necessary for data sending when a connection is successfully made.
	 * @param p2pInfo connection information
	 * @param p2pDevice the target device to which we are connected
	 */
	public void setConnectionSuccessful(final WifiP2pInfo p2pInfo, final WifiP2pDevice p2pDevice) {
		mConnectionInfo = p2pInfo;
		mTargetDevice = p2pDevice;
	}

	/**
	 * Clears out saved connection states when the connection is lost.
	 */
	public void onDisconnect() {
		mConnectionInfo = null;
		mTargetDevice = null;
	}

	/**
	 * Sends data by invoking an intent service.
	 * @param data data to send
	 * @return whether or not there was a valid connection over which to send
	 */
	public boolean sendData(final byte[] data) {
		if (mConnectionInfo != null && mTargetDevice != null) {
			String address = "";
			if (mConnectionInfo.isGroupOwner) {
				address = WifiUtilities.getIPAddressFromMacAddress(mTargetDevice.deviceAddress);
			} else {
				address = mConnectionInfo.groupOwnerAddress.getHostAddress();
			}

			Log.e(Client.class.getSimpleName(), "Sending to " + address);

			Intent modelStatusIntent = new Intent(mContext, ClientIntentService.class);
			modelStatusIntent.setAction(ClientIntentService.ACTION_SEND_DATA);

			modelStatusIntent.putExtra(ClientIntentService.EXTRAS_DATA, data);
			modelStatusIntent.putExtra(ClientIntentService.EXTRAS_HOST, address);
			modelStatusIntent.putExtra(ClientIntentService.EXTRAS_PORT, Server.PORT);

			mContext.startService(modelStatusIntent);
			return true;
		} else {
			return false;
		}
	}
}
