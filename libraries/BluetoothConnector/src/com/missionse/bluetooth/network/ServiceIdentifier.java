package com.missionse.bluetooth.network;

import java.util.UUID;

/**
 * Contains Service identification information.
 */
public final class ServiceIdentifier {

	private ServiceIdentifier() {
	}

	/**
	 * Denotes type of bluetooth connection.
	 */
	public enum ConnectionType {
		SECURE, INSECURE
	}

	// Name for the SDP record when creating server socket
	private static String secureName = null;
	private static String insecureName = null;

	// Unique UUID for this application
	private static UUID secureUuid = null;
	private static UUID insecureUuid = null;

	/**
	 * Identifies the service.
	 * @param type type of connection to identify
	 * @param name the name to assign to the service
	 * @param uuid the unique id of the service
	 */
	public static void identifyService(final ConnectionType type, final String name, final UUID uuid) {
		if (type == ConnectionType.SECURE) {
			secureName = name;
			secureUuid = uuid;
		} else {
			insecureName = name;
			insecureUuid = uuid;
		}
	}

	/**
	 * Identifies the service given a String.
	 * @param type type of connection to identify
	 * @param name the name to assign to the service
	 * @param uuid the string from which the unique id of the service will be parsed
	 */
	public static void identifyService(final ConnectionType type, final String name, final String uuid) {
		if (type == ConnectionType.SECURE) {
			secureName = name;
			secureUuid = UUID.fromString(uuid);
		} else {
			insecureName = name;
			insecureUuid = UUID.fromString(uuid);
		}
	}

	/**
	 * Retrieves the service name of the connection type.
	 * @param type the type of connection
	 * @return the service name
	 */
	public static String getServiceName(final ConnectionType type) {
		if (type == ConnectionType.SECURE) {
			return secureName;
		} else {
			return insecureName;
		}
	}

	/**
	 * Retrieves the UUID of the connection type.
	 * @param type the type of connection
	 * @return the UUID of the service
	 */
	public static UUID getServiceUUID(final ConnectionType type) {
		if (type == ConnectionType.SECURE) {
			return secureUuid;
		} else {
			return insecureUuid;
		}
	}

	/**
	 * Thrown when an entity tries to use the Service without first identifying it.
	 */
	public static class ServiceNotIdentifiedException extends RuntimeException {
		private static final long serialVersionUID = 6166719170878873681L;

		/**
		 * Creates a new ServiceNotIdentifiedException with a given message.
		 * @param message the message to display when the exception is thrown
		 */
		public ServiceNotIdentifiedException(final String message) {
			super(message);
		}
	}

	/**
	 * Determines whether or not the secure service has been identified.
	 */
	public static void validateSecureService() {
		if (secureName == null || secureUuid == null) {
			throw new ServiceNotIdentifiedException(
					"Did you forget to set the Service Name and/or UUID before starting?");
		}
	}

	/**
	 * Determines whether or not the insecure service has been identified.
	 */
	public static void validateInsecureService() {
		if (insecureName == null || insecureUuid == null) {
			throw new ServiceNotIdentifiedException(
					"Did you forget to set the Service Name and/or UUID before starting?");
		}
	}
}
