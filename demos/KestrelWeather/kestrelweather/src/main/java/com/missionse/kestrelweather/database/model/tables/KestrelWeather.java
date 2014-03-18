package com.missionse.kestrelweather.database.model.tables;

import com.google.gson.JsonElement;
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
	private float mWindDirection;

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
	 * @return The dew point in degrees Celsius.
	 */
	public float getDewPoint() {
		return mDewPoint;
	}

	/**
	 * Setter.
	 * @param dewPoint The dewPoint in degrees Celsius.
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
	 * @return The humidity in percent. e.g 100%
	 */
	public int getHumidity() {
		return mHumidity;
	}

	/**
	 * Setter.
	 * @param humidity The humidity in percent. e.g 100%
	 */
	public void setHumidity(int humidity) {
		mHumidity = humidity;
	}

	/**
	 * Getter.
	 * @return The pressure in hPa units.
	 */
	public float getPressure() {
		return mPressure;
	}

	/**
	 * Setter.
	 * @param pressure The pressure in hPa units.
	 */
	public void setPressure(float pressure) {
		mPressure = pressure;
	}

	/**
	 * Getter.
	 * @return The pressure trend direction in numerical format.
	 */
	public int getPressureTrend() {
		return mPressureTrend;
	}

	/**
	 * Setter.
	 * @param pressureTrend The pressure trend direction in numerical format
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
	 * @return The wind chill in degrees celsius.
	 */
	public float getWindChill() {
		return mWindChill;
	}

	/**
	 * Setter.
	 * @param windChill The wind chill in degrees celsius.
	 */
	public void setWindChill(float windChill) {
		mWindChill = windChill;
	}

	/**
	 * Getter.
	 * @return The wind direction in degrees (meteorological).
	 */
	public float getWindDirection() {
		return mWindDirection;
	}

	/**
	 * Setter.
	 * @param windDirection The wind direction in degrees (meteorological).
	 */
	public void setWindDirection(float windDirection) {
		mWindDirection = windDirection;
	}

	/**
	 * Getter.
	 * @return The wind speed in mps.
	 */
	public float getWindSpeed() {
		return mWindSpeed;
	}

	/**
	 * Setter.
	 * @param windSpeed The wind speed in mps.
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
		map.put("winddirection", Float.toString(mWindDirection));
		map.put("windchill", Float.toString(mWindChill));
		map.put("dewpoint", Float.toString(mDewPoint));

		return map;
	}

	@Override
	public void populate(JsonObject json) {
		super.populate(json);

		setTemperature(json);
		setHumidity(json);
		setPressure(json);
		setPressureTrend(json);
		setHeatIndex(json);
		setWindSpeed(json);
		setWindDirection(json);
		setWindChill(json);
		setDewPoint(json);
	}

	private void setDewPoint(final JsonObject json) {
		JsonElement dewPoint = json.get("dewpoint");
		if (dewPoint != null) {
			setDewPoint(dewPoint.getAsFloat());
		} else {
			setDewPoint(0.0f);
		}
	}

	private void setWindChill(final JsonObject json) {
		JsonElement windChill = json.get("windchill");
		if (windChill != null) {
			setWindChill(windChill.getAsFloat());
		} else {
			setWindChill(0.0f);
		}
	}

	private void setWindDirection(final JsonObject json) {
		JsonElement windDirection = json.get("winddirection");
		if (windDirection != null) {
			setWindDirection(windDirection.getAsInt());
		} else {
			setWindDirection(0);
		}
	}

	private void setWindSpeed(final JsonObject json) {
		JsonElement windSpeed = json.get("windspeed");
		if (windSpeed != null) {
			setWindSpeed(windSpeed.getAsFloat());
		} else {
			setWindSpeed(0.0f);
		}
	}

	private void setHeatIndex(final JsonObject json) {
		JsonElement heatIndex = json.get("heatindex");
		if (heatIndex != null) {
			setHeatIndex(heatIndex.getAsFloat());
		} else {
			setHeatIndex(0.0f);
		}
	}

	private void setPressureTrend(final JsonObject json) {
		JsonElement pressureTrend = json.get("pressuretrend");
		if (pressureTrend != null) {
			setPressureTrend(pressureTrend.getAsInt());
		} else {
			setPressureTrend(0);
		}
	}

	private void setPressure(final JsonObject json) {
		JsonElement pressure = json.get("pressure");
		if (pressure != null) {
			setPressure(pressure.getAsFloat());
		} else {
			setPressure(0.0f);
		}
	}

	private void setHumidity(final JsonObject json) {
		JsonElement humidity = json.get("humidity");
		if (humidity != null) {
			setHumidity(humidity.getAsInt());
		} else {
			setHumidity(0);
		}
	}

	private void setTemperature(final JsonObject json) {
		JsonElement temperature = json.get("temperature");
		if (temperature != null) {
			setTemperature(temperature.getAsFloat());
		} else {
			setTemperature(0.0f);
		}
	}
}
