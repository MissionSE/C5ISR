package com.missionse.wifidirect.listener;

public interface IncomingDataListener {

	/**
	 * Handle to receive incoming data via the server connection.
	 * @param data data received
	 */
	public void processReceivedData(byte[] data);
}
