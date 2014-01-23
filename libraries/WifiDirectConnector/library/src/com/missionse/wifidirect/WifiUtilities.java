package com.missionse.wifidirect;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Provides generic utilities for use in initiating and maintaining a WifiDirect P2P network connection.
 */
public final class WifiUtilities {

	private WifiUtilities() {
	}

	private static final int HWADDRESS = 3;
	private static final int DEVICE = 5;

	private static final int INVALID_SECTION = 4;

	private static final String P2P_INDICATOR = "p2p-p2p0";

	/**
	 * Retrieves an IP Address from /proc/net/arp given a MAC address.
	 * @param macAddress the MAC address to be matched
	 * @return an IP address
	 */
	public static String getIPAddressFromMacAddress(final String macAddress) {
		// The MAC address provided is different than that which will be found
		// in the /proc/net/arp file. So ignoring the 8th bit, or more simply,
		// just ignoring the 5th offset of the MAC address.

		String[] splitMacAddress = macAddress.split(":");

		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader("/proc/net/arp"));
			String line;
			while ((line = br.readLine()) != null) {
				String[] entry = line.split(" +");
				if (entry != null) {
					// Basic sanity check
					String device = entry[DEVICE];
					if (device.matches(".*" + P2P_INDICATOR + ".*")) {
						String entryMacAddress = entry[HWADDRESS];
						String[] splitEntryMacAddress = entryMacAddress.split(":");

						boolean matching = true;
						if (splitMacAddress.length == splitEntryMacAddress.length) {
							for (int index = 0; index < splitMacAddress.length; ++index) {
								// As per note above, ignoring some bits.
								if (index != INVALID_SECTION) {
									if (!splitMacAddress[index].equals(splitEntryMacAddress[index])) {
										matching = false;
									}
								}
							}
						}
						if (matching) {
							return entry[0];
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "";
	}
}
