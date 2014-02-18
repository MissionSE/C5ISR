package com.missionse.kestrelweather.database.model.tables;

import com.j256.ormlite.field.DatabaseField;
import com.missionse.kestrelweather.database.model.Entity;

/**
 * Container for weather data information.
 */
public class WeatherData extends Entity {
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
	public WeatherData() {
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

	public float getDewPoint() {
		return mDewPoint;
	}

	public void setDewPoint(float dewPoint) {
		mDewPoint = dewPoint;
	}

	public float getHeatIndex() {
		return mHeatIndex;
	}

	public void setHeatIndex(float heatIndex) {
		mHeatIndex = heatIndex;
	}

	public int getHumidity() {
		return mHumidity;
	}

	public void setHumidity(int humidity) {
		mHumidity = mHumidity;
	}

	public float getPressure() {
		return mPressure;
	}

	public void setPressure(float pressure) {
		mPressure = pressure;
	}

	public int getPressureTrend() {
		return mPressureTrend;
	}

	public void setPressureTrend(int pressureTrend) {
		mPressureTrend = pressureTrend;
	}

	public float getTemperature() {
		return mTemperature;
	}

	public void setTemperature(float temperature) {
		mTemperature = temperature;
	}

	public float getWindChill() {
		return mWindChill;
	}

	public void setWindChill(float windChill) {
		mWindChill = windChill;
	}

	public int getWindDirection() {
		return mWindDirection;
	}

	public void setWindDirection(int windDirection) {
		mWindDirection = windDirection;
	}

	public float getWindSpeed() {
		return mWindSpeed;
	}

	public void setWindSpeed(float windSpeed) {
		mWindSpeed = windSpeed;
	}
}
