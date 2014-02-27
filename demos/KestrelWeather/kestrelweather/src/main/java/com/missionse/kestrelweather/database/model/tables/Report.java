package com.missionse.kestrelweather.database.model.tables;

import com.google.gson.JsonObject;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.missionse.kestrelweather.database.model.Entity;

import java.util.Map;

/**
 * Report object.
 */
@DatabaseTable(daoClass = com.missionse.kestrelweather.database.model.tables.manipulators.ReportTable.class)
public class Report extends Entity {
	@DatabaseField(columnName = "devicename")
	private String mUserName;

	@DatabaseField(columnName = "latitude")
	private double mLatitude;

	@DatabaseField(columnName = "longitude")
	private double mLongitude;

	@DatabaseField(foreign = true, canBeNull = true, foreignAutoCreate = true, foreignAutoRefresh = true)
	private KestrelWeather mKestrelWeather;

	@DatabaseField(foreign = true, canBeNull = true, foreignAutoCreate = true, foreignAutoRefresh = true)
	private OpenWeather mOpenWeather;

	@ForeignCollectionField(eager = true)
	private ForeignCollection<Supplement> mSupplements;

	@ForeignCollectionField(eager = true)
	private ForeignCollection<Note> mNotes;

	/**
	 * Default Constructor.
	 */
	public Report() {
		mUserName = "";
		mLatitude = 0;
		mLongitude = 0;
		mKestrelWeather = null;
		mNotes = null;
	}

	/**
	 * Getter.
	 * @return String indicating device name.
	 */
	public String getUserName() {
		return mUserName;
	}

	/**
	 * Setter.
	 * @param userName The name of the device.
	 */
	public void setUserName(String userName) {
		mUserName = userName;
	}

	/**
	 * Getter.
	 * @return Latitude of the device.
	 */
	public double getLatitude() {
		return mLatitude;
	}

	/**
	 * Setter.
	 * @param latitude position of the device.
	 */
	public void setLatitude(double latitude) {
		mLatitude = latitude;
	}

	/**
	 * Getter.
	 * @return Longitude of the device.
	 */
	public double getLongitude() {
		return mLongitude;
	}

	/**
	 * Setter.
	 * @param longitude position of the device.
	 */
	public void setLongitude(double longitude) {
		mLongitude = longitude;
	}

	/**
	 * Getter.
	 * @return Instance of KestrelWeather associated with this report.
	 */
	public KestrelWeather getKestrelWeather() {
		return mKestrelWeather;
	}

	/**
	 * Setter.
	 * @param kestrelWeather associated with this report.
	 */
	public void setKestrelWeather(KestrelWeather kestrelWeather) {
		mKestrelWeather = kestrelWeather;
	}

	/**
	 * Getter.
	 * @return Instance of OpenWeather associated with this report.
	 */
	public OpenWeather getOpenWeather() {
		return mOpenWeather;
	}

	/**
	 * Setter.
	 * @param openWeather associated with this report.
	 */
	public void setOpenWeather(OpenWeather openWeather) {
		mOpenWeather = openWeather;
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

	/**
	 * Getter.
	 * @return Instance of ForeignCollection.
	 */
	public ForeignCollection<Note> getNotes() {
		return mNotes;
	}

	/**
	 * Setter.
	 * @param notes set the note collection.
	 */
	public void setNotes(ForeignCollection<Note> notes) {
		mNotes = notes;
	}

	@Override
	public Map<String, String> toMap() {
		Map<String, String> map = super.toMap();
		map.put("userid", mUserName);
		map.put("latitude", Double.toString(mLatitude));
		map.put("longitude", Double.toString(mLongitude));

		return map;
	}

	@Override
	public void populate(JsonObject json) {
		super.populate(json);
		String uid = (json.get("userid") == null ? "" : json.get("userid").getAsString());
		double lat = (json.get("latitude") == null ? 0 : json.get("latitude").getAsDouble());
		double lng = (json.get("longitude") == null ? 0 : json.get("longitude").getAsDouble());

		setUserName(uid);
		setLatitude(lat);
		setLongitude(lng);
	}
}
