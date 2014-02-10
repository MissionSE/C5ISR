package com.missionse.kestrelweather.communication;

import java.util.ArrayList;
import java.util.List;

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
	private int mHumidity;
	private float mPressure;
	private int mPressureTrend;
	private float mHeatIndex;
	private float mWindSpeed;
	private int mWindDirection;
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
		mHumidity = 0;
		mPressure = 0.0f;
		mPressureTrend = 0;
		mHeatIndex = 0.0f;
		mWindSpeed = 0.0f;
		mWindDirection = 0;
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
			translation.setHumidity(Integer.valueOf(tokenizedMessage[HUMIDITY]));
			translation.setPressure(Float.valueOf(tokenizedMessage[PRESSURE]));
			translation.setPressureTrend(Integer.valueOf(tokenizedMessage[PRESSURE_TREND]));
			translation.setHeatIndex(Float.valueOf(tokenizedMessage[HEAT_INDEX]));
			translation.setWindSpeed(Float.valueOf(tokenizedMessage[WIND_SPEED]));
			translation.setWindDirection(Integer.valueOf(tokenizedMessage[WIND_DIRECTION]));
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
	public List<String> makePretty() {
		List<String> printables = new ArrayList<String>();
		printables.add("Temperature: " + mTemperature + " °C");
		printables.add("Humidity: " + mHumidity + " %");
		printables.add("Barometric Pressure: " + mPressure + " Hg");
		String printablePressureTrend;
		if (mPressureTrend > 0) {
			printablePressureTrend = "Up";
		} else {
			printablePressureTrend = "Down";
		}
		printables.add("Pressure Trend: " + printablePressureTrend);
		printables.add("Heat Index: " + mHeatIndex + " °C");
		printables.add("Wind Speed: " + mWindSpeed + " mph");
		printables.add("Wind Direction: " + mWindDirection + " °");
		printables.add("Wind Chill: " + mWindChill + " °C");
		printables.add("Dew Point: " + mDewPoint + " °C");
		return printables;
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
	 * Retrieves the temperature, in degrees Celsius.
	 * @return the temperature
	 */
	public float getTemperature() {
		return mTemperature;
	}

	/**
	 * Sets the temperature, in degrees Celsius.
	 * @param temperature the temperature to use
	 */
	public void setTemperature(final float temperature) {
		mTemperature = temperature;
	}

	/**
	 * Retrieves the humidity, a percentage.
	 * @return the humidity
	 */
	public int getHumidity() {
		return mHumidity;
	}

	/**
	 * Sets the humidity, a percentage.
	 * @param humidity the humidity to use
	 */
	public void setHumidity(final int humidity) {
		mHumidity = humidity;
	}

	/**
	 * Retrieves the pressure, in inches..
	 * @return the pressure
	 */
	public float getPressure() {
		return mPressure;
	}

	/**
	 * Sets the pressure, in inches.
	 * @param pressure the pressure to use
	 */
	public void setPressure(final float pressure) {
		mPressure = pressure;
	}

	/**
	 * Retrieves the pressure trend, which can be 0 or 1 (denoting down or up, respectively).
	 * @return the pressure trend
	 */
	public int getPressureTrend() {
		return mPressureTrend;
	}

	/**
	 * Sets the pressure trend, which can be 0 or 1 (denoting down or up, respectively).
	 * @param pressureTrend the pressure trend to use
	 */
	public void setPressureTrend(final int pressureTrend) {
		mPressureTrend = pressureTrend;
	}

	/**
	 * Retrieves the heat index, in degrees Celsius.
	 * @return the heat index
	 */
	public float getHeatIndex() {
		return mHeatIndex;
	}

	/**
	 * Sets the heat index, in degrees Celsius.
	 * @param heatIndex the heat index to use
	 */
	public void setHeatIndex(final float heatIndex) {
		mHeatIndex = heatIndex;
	}

	/**
	 * Retrieves the wind speed, in miles per hour.
	 * @return the wind speed
	 */
	public float getWindSpeed() {
		return mWindSpeed;
	}

	/**
	 * Sets the wind speed, in miles per hour.
	 * @param windSpeed the wind speed to use
	 */
	public void setWindSpeed(final float windSpeed) {
		mWindSpeed = windSpeed;
	}

	/**
	 * Retrieves the wind direction, in degrees (of a circle).
	 * @return the wind direction
	 */
	public int getWindDirection() {
		return mWindDirection;
	}

	/**
	 * Sets the wind direction, in degrees (of a circle).
	 * @param windDirection the wind direction to use
	 */
	public void setWindDirection(final int windDirection) {
		mWindDirection = windDirection;
	}

	/**
	 * Retrieves the wind chill, in degrees Celsius.
	 * @return the wind chill
	 */
	public float getWindChill() {
		return mWindChill;
	}

	/**
	 * Sets the wind chill, in degrees Celsius.
	 * @param windChill the wind chill to use
	 */
	public void setWindChill(final float windChill) {
		mWindChill = windChill;
	}

	/**
	 * Retrieves the dew point, in degrees Celsius.
	 * @return the dew point
	 */
	public float getDewPoint() {
		return mDewPoint;
	}

	/**
	 * Sets the dew point, in degrees Celsius.
	 * @param dewPoint the dew point to use
	 */
	public void setDewPoint(final float dewPoint) {
		mDewPoint = dewPoint;
	}
}
