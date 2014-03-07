package com.missionse.kestrelweather.communication;

import com.missionse.kestrelweather.database.model.tables.KestrelWeather;

/**
 * Defines the basic message format for a message between two devices, to support both a request for data, and the
 * incoming payload.
 */
public class KestrelMessage {
	public static final int REQUEST = 0;
	public static final int DATA = 1;

	public static final String TOKEN = "&";

	public static final int MESSAGE_TYPE = 0;
	public static final int TEMPERATURE = 1;
	public static final int HUMIDITY = 2;
	public static final int PRESSURE = 3;
	public static final int PRESSURE_TREND = 4;
	public static final int HEAT_INDEX = 5;
	public static final int WIND_SPEED = 6;
	public static final int WIND_DIRECTION = 7;
	public static final int WIND_CHILL = 8;
	public static final int DEW_POINT = 9;

	private int mMessageType;
	private KestrelWeather mKestrelWeather;

	/**
	 * Provides an exception that indicates an invalid kestrel message.
	 */
	public static class InvalidKestrelMessageException extends Exception {
		/**
		 * Constructor.
		 */
		public InvalidKestrelMessageException() {
			super("Invalid message.");
		}
	}

	/**
	 * Creates a new, empty KestrelMessage of a certain type. Used when preparing a message to be sent.
	 * @param type the type of message
	 */
	public KestrelMessage(final int type) {
		mMessageType = type;
	}

	/**
	 * Translates a raw message String into a KestrelMessage.
	 * @param rawMessage the raw message String
	 * @return a new KestrelMessage
	 * @throws InvalidKestrelMessageException when the message is invalid.
	 */
	public static KestrelMessage translateRawMessage(final String rawMessage) throws InvalidKestrelMessageException {
		String[] tokenizedMessage = rawMessage.split(TOKEN);
		KestrelMessage translation = new KestrelMessage(Integer.valueOf(tokenizedMessage[MESSAGE_TYPE]));
		if (translation.isData()) {
			if (tokenizedMessage.length != 10) {
				throw new InvalidKestrelMessageException();
			}

			KestrelWeather kestrelWeather = new KestrelWeather();
			kestrelWeather.setTemperature(Float.valueOf(tokenizedMessage[TEMPERATURE]));
			kestrelWeather.setHumidity(Integer.valueOf(tokenizedMessage[HUMIDITY]));
			kestrelWeather.setPressure(Float.valueOf(tokenizedMessage[PRESSURE]));
			kestrelWeather.setPressureTrend(Integer.valueOf(tokenizedMessage[PRESSURE_TREND]));
			kestrelWeather.setHeatIndex(Float.valueOf(tokenizedMessage[HEAT_INDEX]));
			kestrelWeather.setWindSpeed(Float.valueOf(tokenizedMessage[WIND_SPEED]));
			kestrelWeather.setWindDirection(Float.valueOf(tokenizedMessage[WIND_DIRECTION]));
			kestrelWeather.setWindChill(Float.valueOf(tokenizedMessage[WIND_CHILL]));
			kestrelWeather.setDewPoint(Float.valueOf(tokenizedMessage[DEW_POINT]));
			translation.setKestrelWeather(kestrelWeather);
		}

		return translation;
	}

	/**
	 * Converts this KestrelMessage into a raw message String.
	 * @return the raw message
	 */
	public String toString() {
		String message = "";
		message += mMessageType + TOKEN;
		if (mKestrelWeather != null) {
			message += mKestrelWeather.getTemperature() + TOKEN;
			message += mKestrelWeather.getHumidity() + TOKEN;
			message += mKestrelWeather.getPressure() + TOKEN;
			message += mKestrelWeather.getPressureTrend() + TOKEN;
			message += mKestrelWeather.getHeatIndex() + TOKEN;
			message += mKestrelWeather.getWindSpeed() + TOKEN;
			message += mKestrelWeather.getWindDirection() + TOKEN;
			message += mKestrelWeather.getWindChill() + TOKEN;
			message += mKestrelWeather.getDewPoint() + TOKEN;
		}
		return message;
	}

	/**
	 * Determines whether or not this KestrelMessage is a REQUEST type.
	 * @return whether or not this is a request
	 */
	public boolean isRequest() {
		return mMessageType == REQUEST;
	}

	/**
	 * Determines whether or not this KestrelMessage is a DATA type.
	 * @return whether or not this contains a data payload
	 */
	public boolean isData() {
		return mMessageType == DATA;
	}

	/**
	 * Sets the KestrelWeather data.
	 * @param kestrelWeather The kestrel weather data.
	 */
	public void setKestrelWeather(final KestrelWeather kestrelWeather) {
		mKestrelWeather = kestrelWeather;
	}

	/**
	 * Gets the kestrel weather data.
	 * @return The kestrel weather data.
	 */
	public KestrelWeather getKestrelWeather() {
		return mKestrelWeather;
	}
}
