package com.missionse.kestrelweather.database.model.tables;

import com.google.gson.JsonObject;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.missionse.kestrelweather.database.model.Entity;
import com.missionse.kestrelweather.database.model.tables.manipulators.KestrelWeatherTable;

import java.util.Map;

/**
 * Container for weather data information.
 */
@DatabaseTable(daoClass = KestrelWeatherTable.class)
public class KestrelWeather extends Entity {
	@DatabaseField(columnName = "temperature")
	private float mTemperature;

	@DatabaseField(columnName = "humidity")
	private int mHumidity;

	@DatabaseField(columnName = "pressure")
	private float mPressure;

	@DatabaseField(columnName = "pressuretrend")
	private int mPressureTrend;

	@DatabaseField(columnName = "heatindex")
	private float mHeatIndex;

	@DatabaseField(columnName = "windspeed")
	private float mWindSpeed;

	@DatabaseField(columnName = "winddirection")
	private int mWindDirection;

	@DatabaseField(columnName = "windchill")
	private float mWindChill;

	@DatabaseField(columnName = "dewpoint")
	private float mDewPoint;

	/**
	 * Default Constructor.
	 */
	public KestrelWeather() {
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
	 * Getter.
	 * @return The dew point.
	 */
	public float getDewPoint() {
		return mDewPoint;
	}

	/**
	 * Setter.
	 * @param dewPoint The dewPoint.
	 */
	public void setDewPoint(float dewPoint) {
		mDewPoint = dewPoint;
	}

	/**
	 * Getter.
	 * @return The heat index.
	 */
	public float getHeatIndex() {
		return mHeatIndex;
	}

	/**
	 * Setter.
	 * @param heatIndex The heat index.
	 */
	public void setHeatIndex(float heatIndex) {
		mHeatIndex = heatIndex;
	}

	/**
	 * Getter.
	 * @return The humidity.
	 */
	public int getHumidity() {
		return mHumidity;
	}

	/**
	 * Setter.
	 * @param humidity The humidity.
	 */
	public void setHumidity(int humidity) {
		mHumidity = humidity;
	}

	/**
	 * Getter.
	 * @return The pressure.
	 */
	public float getPressure() {
		return mPressure;
	}

	/**
	 * Setter.
	 * @param pressure The pressure.
	 */
	public void setPressure(float pressure) {
		mPressure = pressure;
	}

	/**
	 * Getter.
	 * @return The pressure trend.
	 */
	public int getPressureTrend() {
		return mPressureTrend;
	}

	/**
	 * Setter.
	 * @param pressureTrend The pressure trend.
	 */
	public void setPressureTrend(int pressureTrend) {
		mPressureTrend = pressureTrend;
	}

	/**
	 * Getter.
	 * @return The temperature. In degrees celsius.
	 */
	public float getTemperature() {
		return mTemperature;
	}

	/**
	 * Setter.
	 * @param temperature The temperature in degrees celsius.
	 */
	public void setTemperature(float temperature) {
		mTemperature = temperature;
	}

	/**
	 * Getter.
	 * @return The wind chill.
	 */
	public float getWindChill() {
		return mWindChill;
	}

	/**
	 * Setter.
	 * @param windChill The wind chill.
	 */
	public void setWindChill(float windChill) {
		mWindChill = windChill;
	}

	/**
	 * Getter.
	 * @return The wind direction.
	 */
	public int getWindDirection() {
		return mWindDirection;
	}

	/**
	 * Setter.
	 * @param windDirection The wind direction.
	 */
	public void setWindDirection(int windDirection) {
		mWindDirection = windDirection;
	}

	/**
	 * Getter.
	 * @return The wind speed.
	 */
	public float getWindSpeed() {
		return mWindSpeed;
	}

	/**
	 * Setter.
	 * @param windSpeed The wind speed.
	 */
	public void setWindSpeed(float windSpeed) {
		mWindSpeed = windSpeed;
	}

	@Override
	public Map<String, String> toMap() {
		Map<String, String> map = super.toMap();
		map.put("temperature", Float.toString(mTemperature));
		map.put("humidity", Integer.toString(mHumidity));
		map.put("pressure", Float.toString(mPressure));
		map.put("pressuretrend", Integer.toString(mPressureTrend));
		map.put("heatindex", Float.toString(mHeatIndex));
		map.put("windspeed", Float.toString(mWindSpeed));
		map.put("winddirection", Integer.toString(mWindDirection));
		map.put("windchill", Float.toString(mWindChill));
		map.put("dewpoint", Float.toString(mDewPoint));

		return map;
	}

	@Override
	public void populate(JsonObject json) {
		super.populate(json);
		setTemperature((json.get("temperature") == null ? 0f : json.get("temperature").getAsFloat()));
		setHumidity((json.get("humidity") == null ? 0 : json.get("humidity").getAsInt()));
		setPressure((json.get("pressure") == null ? 0f : json.get("pressure").getAsFloat()));
		setPressureTrend((json.get("pressuretrend") == null ? 0 : json.get("pressuretrend").getAsInt()));
		setHeatIndex((json.get("heatindex") == null ? 0f : json.get("heatindex").getAsFloat()));
		setWindSpeed((json.get("windspeed") == null ? 0f : json.get("windspeed").getAsFloat()));
		setWindDirection((json.get("winddirection") == null ? 0 : json.get("winddirection").getAsInt()));
		setWindChill((json.get("windchill") == null ? 0f : json.get("windchill").getAsFloat()));
		setDewPoint((json.get("dewpoint") == null ? 0f : json.get("dewpoint").getAsFloat()));
	}
}