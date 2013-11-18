package com.missionse.bluetooth;

import java.util.UUID;

public class ServiceIdentifier {

	// Name for the SDP record when creating server socket
	protected static String NAME_SECURE = "Secure";
	protected static String NAME_INSECURE = "Insecure";

	// Unique UUID for this application
	protected static UUID UUID_SECURE = UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");
	protected static UUID UUID_INSECURE = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");

	public static void setSecureServiceName(final String name) {
		NAME_SECURE = name;
	}

	public static void setInsecureServiceName(final String name) {
		NAME_INSECURE = name;
	}

	public static void setSecureUUIDFromString(final String uuid) {
		UUID_SECURE = UUID.fromString(uuid);
	}

	public static void setInsecureUUIDFromString(final String uuid) {
		UUID_INSECURE = UUID.fromString(uuid);
	}

	public static void setSecureUUID(final UUID uuid) {
		UUID_SECURE = uuid;
	}

	public static void setInsecureUUID(final UUID uuid) {
		UUID_INSECURE = uuid;
	}
}
