package com.missionse.kestrelweather.database.model.tables;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonObject;
import com.google.maps.android.clustering.ClusterItem;
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
public class Report extends Entity implements ClusterItem {
	@DatabaseField(columnName = "devicename")
	private String mUserName;

	@DatabaseField(columnName = "title")
	private String mTitle;

	@DatabaseField(columnName = "latitude")
	private double mLatitude;

	@DatabaseField(columnName = "longitude")
	private double mLongitude;

	@DatabaseField(columnName = "draft")
	private boolean mDraft;

	@DatabaseField(columnName = "read")
	private boolean mRead;

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
		mTitle = "";
		mLatitude = 0;
		mLongitude = 0;
		mKestrelWeather = null;
		mNotes = null;
		mDraft = false;
		mRead = false;
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Report report = (Report) o;

		if (mDraft != report.mDraft) return false;
		if (Double.compare(report.mLatitude, mLatitude) != 0) return false;
		if (Double.compare(report.mLongitude, mLongitude) != 0) return false;
		if (mRead != report.mRead) return false;
		if (mKestrelWeather != null ? !mKestrelWeather.equals(report.mKestrelWeather) : report.mKestrelWeather != null)
			return false;
		if (mNotes != null ? !mNotes.equals(report.mNotes) : report.mNotes != null) return false;
		if (mOpenWeather != null ? !mOpenWeather.equals(report.mOpenWeather) : report.mOpenWeather != null)
			return false;
		if (mSupplements != null ? !mSupplements.equals(report.mSupplements) : report.mSupplements != null)
			return false;
		if (mTitle != null ? !mTitle.equals(report.mTitle) : report.mTitle != null) return false;
		if (mUserName != null ? !mUserName.equals(report.mUserName) : report.mUserName != null)
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result;
		long temp;
		result = mUserName != null ? mUserName.hashCode() : 0;
		result = 31 * result + (mTitle != null ? mTitle.hashCode() : 0);
		temp = Double.doubleToLongBits(mLatitude);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(mLongitude);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		result = 31 * result + (mDraft ? 1 : 0);
		result = 31 * result + (mRead ? 1 : 0);
		result = 31 * result + (mKestrelWeather != null ? mKestrelWeather.hashCode() : 0);
		result = 31 * result + (mOpenWeather != null ? mOpenWeather.hashCode() : 0);
		result = 31 * result + (mSupplements != null ? mSupplements.hashCode() : 0);
		result = 31 * result + (mNotes != null ? mNotes.hashCode() : 0);
		return result;
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
		map.put("title", mTitle);

		return map;
	}

	@Override
	public void populate(JsonObject json) {
		super.populate(json);
		String uid = (json.get("userid") == null ? "" : json.get("userid").getAsString());
		double lat = (json.get("latitude") == null ? 0 : json.get("latitude").getAsDouble());
		double lng = (json.get("longitude") == null ? 0 : json.get("longitude").getAsDouble());
		String title = (json.get("title") == null ? "" : json.get("title").getAsString());

		setUserName(uid);
		setLatitude(lat);
		setLongitude(lng);
		setTitle(title);
	}

	/**
	 * Getter.
	 * @return The title of the report.
	 */
	public String getTitle() {
		return mTitle;
	}

	/**
	 * Setter.
	 * @param title The title to give this report.
	 */
	public void setTitle(String title) {
		mTitle = title;
	}

	@Override
	public LatLng getPosition() {
		return new LatLng(mLatitude, mLongitude);
	}

	/**
	 * Determine if this report is a draft.
	 * @return true if the report is a draft.
	 */
	public boolean isDraft() {
		return mDraft;
	}

	/**
	 * Set if this report should be a draft of not.
	 * @param draft true if the report is a draft.
	 */
	public void setDraft(boolean draft) {
		mDraft = draft;
	}

	/**
	 * Determine if this report has been read.
	 * @return true if the report has been read.
	 */
	public boolean hasBeenRead() {
		return mRead;
	}

	/**
	 * Set if this report should read.
	 * @param read true if the report is has been read.
	 */
	public void setRead(boolean read) {
		mRead = read;
	}
}