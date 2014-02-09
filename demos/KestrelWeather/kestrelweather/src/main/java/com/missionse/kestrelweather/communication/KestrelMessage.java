package com.missionse.kestrelweather.communication;

/**
 * Defines the basic message format for a message between two devices, to support both a request for data, and the
 * incoming payload.
 */
public class KestrelMessage {

	public static final int REQUEST = 0;
	public static final int DATA = 1;

	public static final String token = "&";

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
	private float mTemperature;
	private float mHumidity;
	private float mPressure;
	private float mPressureTrend;
	private int mHeatIndex;
	private float mWindSpeed;
	private float mWindDirection;
	private float mWindChill;
	private float mDewPoint;

	public static class InvalidKestrelMessageException extends Exception {
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
		mTemperature = 0.0f;
		mHumidity = 0.0f;
		mPressure = 0.0f;
		mPressureTrend = 0.0f;
		mHeatIndex = 0;
		mWindSpeed = 0.0f;
		mWindDirection = 0.0f;
		mWindChill = 0.0f;
		mDewPoint = 0.0f;
	}

	/**
	 * Translates a raw message String into a KestrelMessage.
	 * @param rawMessage the raw message String
	 * @return a new KestrelMessage
	 */
	public static KestrelMessage translateRawMessage(final String rawMessage) throws InvalidKestrelMessageException {
		String[] tokenizedMessage = rawMessage.split(token);
		KestrelMessage translation = new KestrelMessage(Integer.valueOf(tokenizedMessage[MESSAGE_TYPE]));
		if (translation.isData()) {
			if (tokenizedMessage.length != 10) {
				throw new InvalidKestrelMessageException();
			}

			translation.setTemperature(Float.valueOf(tokenizedMessage[TEMPERATURE]));
			translation.setHumidity(Float.valueOf(tokenizedMessage[HUMIDITY]));
			translation.setPressure(Float.valueOf(tokenizedMessage[PRESSURE]));
			translation.setPressureTrend(Float.valueOf(tokenizedMessage[PRESSURE_TREND]));
			translation.setHeatIndex(Integer.valueOf(tokenizedMessage[HEAT_INDEX]));
			translation.setWindSpeed(Float.valueOf(tokenizedMessage[WIND_SPEED]));
			translation.setWindDirection(Float.valueOf(tokenizedMessage[WIND_DIRECTION]));
			translation.setWindChill(Float.valueOf(tokenizedMessage[WIND_CHILL]));
			translation.setDewPoint(Float.valueOf(tokenizedMessage[DEW_POINT]));
		}

		return translation;
	}

	/**
	 * Converts this KestrelMessage into a raw message String.
	 * @return the raw message
	 */
	public String toString() {
		String message = "";
		message += mMessageType + token;
		message += mTemperature + token;
		message += mHumidity + token;
		message += mPressure + token;
		message += mPressureTrend + token;
		message += mHeatIndex + token;
		message += mWindSpeed + token;
		message += mWindDirection + token;
		message += mWindChill + token;
		message += mDewPoint + token;
		return message;
	}

	/**
	 * Converts this KestrelMessage to a pretty, printable String.
	 * @return a pretty, printable String
	 */
	public String makePretty() {
		String printable = "";
		printable += "Temperature (°C): " + mTemperature + "\n";
		printable += "Humidity: " + mHumidity + "\n";
		printable += "Barometric Pressure: " + mPressure + "\n";
		printable += "Pressure Trend: " + mPressureTrend + "\n";
		printable += "Heat Index: " + mHeatIndex + "\n";
		printable += "Wind Speed: " + mWindSpeed + "\n";
		printable += "Wind Direction: " + mWindDirection + "\n";
		printable += "Wind Chill: " + mWindChill + "\n";
		printable += "Dew Point: " + mDewPoint + "\n";
		return printable;
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
	 * Retrieves the temperature in this message.
	 * @return the temperature
	 */
	public float getTemperature() {
		return mTemperature;
	}

	/**
	 * Sets the temperature of this message.
	 * @param temperature the temperature to use
	 */
	public void setTemperature(final float temperature) {
		mTemperature = temperature;
	}

	/**
	 * Retrieves the humidity of this message.
	 * @return the humidity
	 */
	public float getHumidity() {
		return mHumidity;
	}

	/**
	 * Sets the humidity of this message.
	 * @param humidity the humidity to use
	 */
	public void setHumidity(final float humidity) {
		mHumidity = humidity;
	}

	/**
	 * Retrieves the pressure of this message.
	 * @return the pressure
	 */
	public float getPressure() {
		return mPressure;
	}

	/**
	 * Sets the pressure of this message.
	 * @param pressure the humidity to use
	 */
	public void setPressure(final float pressure) {
		mPressure = pressure;
	}

	/**
	 * Retrieves the pressure trend of this message.
	 * @return the pressure trend
	 */
	public float getPressureTrend() {
		return mPressureTrend;
	}

	/**
	 * Sets the pressure trend of this message.
	 * @param pressureTrend the humidity to use
	 */
	public void setPressureTrend(final float pressureTrend) {
		mPressureTrend = pressureTrend;
	}

	/**
	 * Retrieves the heat index of this message.
	 * @return the heat index
	 */
	public int getHeatIndex() {
		return mHeatIndex;
	}

	/**
	 * Sets the heat index of this message.
	 * @param heatIndex the humidity to use
	 */
	public void setHeatIndex(final int heatIndex) {
		mHeatIndex = heatIndex;
	}

	/**
	 * Retrieves the wind speed of this message.
	 * @return the wind speed
	 */
	public float getWindSpeed() {
		return mWindSpeed;
	}

	/**
	 * Sets the wind speed of this message.
	 * @param windSpeed the humidity to use
	 */
	public void setWindSpeed(final float windSpeed) {
		mWindSpeed = windSpeed;
	}

	/**
	 * Retrieves the wind direction of this message.
	 * @return the wind direction
	 */
	public float getWindDirection() {
		return mWindDirection;
	}

	/**
	 * Sets the wind direction of this message.
	 * @param windDirection the humidity to use
	 */
	public void setWindDirection(final float windDirection) {
		mWindDirection = windDirection;
	}

	/**
	 * Retrieves the wind chill of this message.
	 * @return the wind chill
	 */
	public float getWindChill() {
		return mWindChill;
	}

	/**
	 * Sets the wind chill of this message.
	 * @param windChill the humidity to use
	 */
	public void setWindChill(final float windChill) {
		mWindChill = windChill;
	}

	/**
	 * Retrieves the dew point of this message.
	 * @return the dew point
	 */
	public float getDewPoint() {
		return mDewPoint;
	}

	/**
	 * Sets the dew point of this message.
	 * @param dewPoint the humidity to use
	 */
	public void setDewPoint(final float dewPoint) {
		mDewPoint = dewPoint;
	}
}
