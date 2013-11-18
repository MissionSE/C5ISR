package com.missionse.bluetooth;

import java.util.UUID;

public class ServiceIdentifier {

	public static class ServiceNotIdentifiedException extends RuntimeException {
		private static final long serialVersionUID = 6166719170878873681L;

		public ServiceNotIdentifiedException(final String message) {
			super(message);
		}
	}

	// Name for the SDP record when creating server socket
	protected static String NAME_SECURE = null;
	protected static String NAME_INSECURE = null;

	// Unique UUID for this application
	protected static UUID UUID_SECURE = null;
	protected static UUID UUID_INSECURE = null;

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

	public static void validateSecureService() throws ServiceNotIdentifiedException {
		if (NAME_SECURE == null || UUID_SECURE == null) {
			throw new ServiceNotIdentifiedException(
					"Did you forget to set the Service Name and/or UUID before starting?");
		}
	}

	public static void validateInsecureService() throws ServiceNotIdentifiedException {
		if (NAME_INSECURE == null || UUID_INSECURE == null) {
			throw new ServiceNotIdentifiedException(
					"Did you forget to set the Service Name and/or UUID before starting?");
		}
	}
}
