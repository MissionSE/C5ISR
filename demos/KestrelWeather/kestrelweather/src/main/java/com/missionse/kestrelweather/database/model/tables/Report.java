package com.missionse.kestrelweather.database.model.tables;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.missionse.kestrelweather.database.model.Entity;

import org.joda.time.DateTime;

/**
 * Report object.
 */
@DatabaseTable(daoClass = com.missionse.kestrelweather.database.model.tables.manipulators.ReportTable.class)
public class Report extends Entity {
	@DatabaseField(columnName = "devicename")
	private String mDeviceName;
	@DatabaseField(columnName = "latitude")
	private long mLatitude;
	@DatabaseField(columnName = "longitude")
	private long mLongitude;
	@DatabaseField(foreign = true, canBeNull = true)
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

	/**
	 * Getter.
	 * @return String indicating device name.
	 */
	public String getDeviceName() {
		return mDeviceName;
	}

	/**
	 * Setter.
	 * @param deviceName The name of the device.
	 */
	public void setDeviceName(String deviceName) {
		mDeviceName = deviceName;
	}

	/**
	 * Getter.
	 * @return Latitude of the device.
	 */
	public long getLatitude() {
		return mLatitude;
	}

	/**
	 * Setter.
	 * @param latitude position of the device.
	 */
	public void setLatitude(long latitude) {
		mLatitude = latitude;
	}

	/**
	 * Getter.
	 * @return Longitude of the device.
	 */
	public long getLongitude() {
		return mLongitude;
	}

	/**
	 * Setter
	 * @param longitude position of the device.
	 */
	public void setLongitude(long longitude) {
		mLongitude = longitude;
	}

	/**
	 * Getter.
	 * @return Instance of WeatherData associated with this report.
	 */
	public WeatherData getWeatherData() {
		return mWeatherData;
	}

	/**
	 * Setter.
	 * @param weatherData associated with this report.
	 */
	public void setWeatherData(WeatherData weatherData) {
		mWeatherData = weatherData;
	}

	/**
	 * Getter.
	 * @return Instance of DateTime which states the time of last modification.
	 */
	public DateTime getUpdateAt() {
		return mUpdateAt;
	}

	/**
	 * Setter.
	 * @param updateAt DateTime this report was last modified.
	 */
	public void setUpdateAt(DateTime updateAt) {
		mUpdateAt = updateAt;
	}

	/**
	 * Getter.
	 * @return Instance of DataTime which states when this report was created.
	 */
	public DateTime getCreatedAt() {
		return mCreatedAt;
	}

	/**
	 * Setter.
	 * @param createdAt DateTime this report was created at.
	 */
	public void setCreatedAt(DateTime createdAt) {
		mCreatedAt = createdAt;
	}

	/**
	 * Add a Supplement to this report.
	 * @param supplement The supplement to be added.
	 */
	public void addSupplement(Supplement supplement) {
		mSupplements.add(supplement);
	}

	/**
	 * Remove supplement from this report.
	 * @param supplement The supplement to be removed.
	 */
	public void removeSupplement(Supplement supplement) {
		mSupplements.remove(supplement);
	}

	/**
	 * Getter.
	 * @return Instance of ForeignCollection.
	 */
	public ForeignCollection<Supplement> getSupplements() {
		return mSupplements;
	}

	/**
	 * Setter.
	 * @param supplements set the supplement collection.
	 */
	public void setSupplements(ForeignCollection<Supplement> supplements) {
		mSupplements = supplements;
	}
}
