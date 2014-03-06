package com.missionse.kestrelweather.database.model.tables;

import com.google.gson.JsonObject;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.missionse.kestrelweather.database.model.Entity;
import com.missionse.kestrelweather.database.model.SupplementType;

import org.joda.time.DateTime;

/**
 * A report supplement class.  Used for holding video, photos etc.
 */
@DatabaseTable(daoClass = com.missionse.kestrelweather.database.model.tables.manipulators.SupplementTable.class)
public class Supplement extends Entity {
	@DatabaseField(columnName = "uri")
	private String mUri;
	@DatabaseField(columnName = "remote_url")
	private String mRemoteUri;
	@DatabaseField(columnName = "filename")
	private String mFileName;
	@DatabaseField(columnName = "size")
	private long mSize;
	@DatabaseField(columnName = "date")
	private DateTime mDate;
	@DatabaseField(columnName = "type", dataType = DataType.ENUM_INTEGER)
	private SupplementType mType;
	@DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "report_id")
	private Report mReport;


	/**
	 * Default Constructor.
	 */
	public Supplement() {
		mUri = "";
		mRemoteUri = "";
		mFileName = "";
		mSize = 0L;
		mDate = DateTime.now();
		mType = SupplementType.UNKNOWN;
	}

	/**
	 * Getter.
	 * @return The string URI associated with this supplement.
	 */
	public String getUri() {
		return mUri;
	}

	/**
	 * Setter.
	 * @param uri The uri to associate this supplement with.
	 */
	public void setUri(String uri) {
		mUri = uri;
	}

	/**
	 * Getter.
	 * @return The string uri associated with this supplement.
	 */
	public String getRemoteUri() {
		return mRemoteUri;
	}

	/**
	 * Setter.
	 * @param uri The url to associate this supplement with.
	 */
	public void setRemoteUri(String uri) {
		mRemoteUri = uri;
	}

	/**
	 * Getter.
	 * @return The SupplementType.
	 */
	public SupplementType getType() {
		return mType;
	}

	/**
	 * Setter.
	 * @param type The SupplementType of this object.
	 */
	public void setType(SupplementType type) {
		mType = type;
	}

	/**
	 * Getter.
	 * @return Instance of Report associated with this report.
	 */
	public Report getReport() {
		return mReport;
	}

	/**
	 * Setter.
	 * @param report The Report to associate with.
	 */
	public void setReport(Report report) {
		mReport = report;
	}

	/**
	 * Retrieve the size of the supplement.
	 * @return The size of the supplement in long.
	 */
	public long getSize() {
		return mSize;
	}

	/**
	 * Set the size of the supplement.
	 * @param size The size of the supplement.
	 */
	public void setSize(long size) {
		mSize = size;
	}

	/**
	 * Retrieve the file name of the supplement.
	 * @return The supplement file name as a String.
	 */
	public String getFileName() {
		return mFileName;
	}

	/**
	 * Set the file name of the supplement.
	 * @param fileName The file name as a String.
	 */
	public void setFileName(String fileName) {
		mFileName = fileName;
	}

	/**
	 * Retrieve the date when supplement was created.
	 * This is the date that the file this supplement points to was created.
	 * @return Instance of DateTime.
	 */
	public DateTime getDate() {
		return mDate;
	}

	/**
	 * Set the date that the file this supplement points too was created.
	 * @param date Instance of DateTime to set.
	 */
	public void setDate(DateTime date) {
		mDate = date;
	}

	@Override
	public void populate(JsonObject json) {
		super.populate(json);
		String remoteUrl = ((json.get("url") == null ? "" : json.get("url").getAsString() ));
		String filename = ((json.get("filename") == null ? "" : json.get("filename").getAsString() ));
		long size = ((json.get("size") == null ? 0L : json.get("size").getAsLong()));
		long timeInMilli = parseDate(json.get("date"));

		setFileName(filename);
		setSize(size);
		setDate(new DateTime(timeInMilli));
		setRemoteUri(remoteUrl);
	}
}
