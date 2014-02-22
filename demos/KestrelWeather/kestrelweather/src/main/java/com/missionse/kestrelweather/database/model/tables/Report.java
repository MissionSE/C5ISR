package com.missionse.kestrelweather.database.model.tables;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.missionse.kestrelweather.database.model.Entity;

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
	@ForeignCollectionField
	private ForeignCollection<Supplement> mSupplements;
	@ForeignCollectionField
	private ForeignCollection<Note> mNotes;

	/**
	 * Default Constructor.
	 */
	public Report() {
		mDeviceName = "";
		mLatitude = 0L;
		mLongitude = 0L;
		mWeatherData = null;
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
	 * Add a Note to this report.
	 * @param note The Note to be added.
	 */
	public void addNote(Note note) {
		mNotes.add(note);
	}

	/**
	 * Remove Note from this report.
	 * @param note The Note to be removed.
	 */
	public void removeNote(Note note) {
		mNotes.remove(note);
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
