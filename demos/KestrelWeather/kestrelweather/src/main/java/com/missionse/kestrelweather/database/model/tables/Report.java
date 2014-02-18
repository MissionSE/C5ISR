package com.missionse.kestrelweather.database.model.tables;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.missionse.kestrelweather.database.model.Entity;

import org.joda.time.DateTime;

/**
 * Report object.
 */
public class Report extends Entity {
	@DatabaseField(columnName = "devicename")
	private String mDeviceName;
	@DatabaseField(columnName = "latitude")
	private long mLatitude;
	@DatabaseField(columnName = "longitude")
	private long mLongitude;
	@DatabaseField(columnName = "weatherdata_id")
	private WeatherData mWeatherData;
	@DatabaseField(columnName = "update_at")
	private DateTime mUpdateAt;
	@DatabaseField(columnName = "created_at")
	private DateTime mCreatedAt;
	@ForeignCollectionField
	private ForeignCollection<Supplement> mSupplements;

	/**
	 * Default Constructor.
	 */
	public Report() {
		mDeviceName = "";
		mLatitude = 0L;
		mLongitude = 0L;
		mWeatherData = null;
		mUpdateAt = null;
		mCreatedAt = null;
	}

	public String getDeviceName() {
		return mDeviceName;
	}

	public void setDeviceName(String deviceName) {
		mDeviceName = deviceName;
	}

	public long getLatitude() {
		return mLatitude;
	}

	public void setLatitude(long latitude) {
		mLatitude = latitude;
	}

	public long getLongitude() {
		return mLongitude;
	}

	public void setLongitude(long longitude) {
		mLongitude = longitude;
	}

	public WeatherData getWeatherData() {
		return mWeatherData;
	}

	public void setWeatherData(WeatherData weatherData) {
		mWeatherData = weatherData;
	}

	public DateTime getUpdateAt() {
		return mUpdateAt;
	}

	public void setUpdateAt(DateTime updateAt) {
		mUpdateAt = updateAt;
	}

	public DateTime getCreatedAt() {
		return mCreatedAt;
	}

	public void setCreatedAt(DateTime createdAt) {
		mCreatedAt = createdAt;
	}

	public void addSupplement(Supplement supplement) {
		mSupplements.add(supplement);
	}

	public void removeSupplement(Supplement supplment) {
		mSupplements.remove(supplment);
	}
}
