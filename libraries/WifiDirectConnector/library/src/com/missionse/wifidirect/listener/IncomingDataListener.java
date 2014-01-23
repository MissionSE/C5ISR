package com.missionse.wifidirect.listener;

/**
 * Provides callbacks for when data is received.
 */
public interface IncomingDataListener {

	/**
	 * Handle to receive incoming data via the server connection.
	 * @param data data received
	 */
	void processReceivedData(byte[] data);
}
