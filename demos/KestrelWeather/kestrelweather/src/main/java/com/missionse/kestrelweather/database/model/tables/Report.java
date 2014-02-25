package com.missionse.kestrelweather.database.model.tables;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.missionse.kestrelweather.database.model.Entity;

import java.util.LinkedList;
import java.util.List;
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
	 * Setter
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

	/**
	 * Getter.
	 * @return A List<Note>.
	 */
	public List<Note> getNotes() {
		List<Note> notes = new LinkedList<Note>();
		if (mNotes != null) {
			for (Note note : mNotes) {
				notes.add(note);
			}
		}
		return notes;
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

		final KestrelWeather kestrelWeather = new KestrelWeather();
		kestrelWeather.populate(json.getAsJsonObject("kestrel"));
		setKestrelWeather(kestrelWeather);

		final OpenWeather openWeather = new OpenWeather();
		openWeather.populate(json.getAsJsonObject("weather"));
		setOpenWeather(openWeather);

		JsonArray jsonArray = json.getAsJsonArray("notes");
		if (jsonArray != null) {
			for (JsonElement jElem : jsonArray) {
				Note note = new Note();
				note.populate(jElem.getAsJsonObject());
			}
		}
	}
}
