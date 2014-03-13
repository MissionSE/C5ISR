package com.missionse.kestrelweather.database.model.tables;

import com.google.gson.JsonElement;
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
	@DatabaseField(columnName = "thumbnail")
	private String mThumbnailUri;
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
		mThumbnailUri = "";
		mFileName = "";
		mSize = 0L;
		mDate = DateTime.now();
		mType = SupplementType.UNKNOWN;
	}

	/**
	 * Gets the Uri representing the local location of the supplement.
	 * @return The string Uri associated with this supplement.
	 */
	public String getUri() {
		return mUri;
	}

	/**
	 * Sets the Uri representing the local location of the supplement.
	 * @param uri The Uri to associate this supplement with.
	 */
	public void setUri(final String uri) {
		mUri = uri;
	}

	/**
	 * Gets the Uri representing the remote location of the supplement.
	 * @return The string remote Uri associated with this supplement.
	 */
	public String getRemoteUri() {
		return mRemoteUri;
	}

	/**
	 * Sets the Uri representing the remote location of the supplement.
	 * @param uri The remote Uri to associate this supplement with.
	 */
	public void setRemoteUri(final String uri) {
		mRemoteUri = uri;
	}

	/**
	 * Gets the location of the thumbnail.
	 * @return The uri that represents the location of the thumbnail.
	 */
	public String getThumbnailUri() {
		return mThumbnailUri;
	}

	/**
	 * Sets the location of the thumbnail.
	 * @param thumbnailUri The location of the thumbnail.
	 */
	public void setThumbnailUri(final String thumbnailUri) {
		mThumbnailUri = thumbnailUri;
	}

	/**
	 * Gets the type of the supplement.
	 * @return The type of the supplement.
	 */
	public SupplementType getType() {
		return mType;
	}

	/**
	 * Sets the type of the supplement.
	 * @param type The type of the supplement.
	 */
	public void setType(final SupplementType type) {
		mType = type;
	}

	/**
	 * Gets the report associated with the supplement.
	 * @return The report associated with this supplement.
	 */
	public Report getReport() {
		return mReport;
	}

	/**
	 * Sets the report associated with the supplement.
	 * @param report The report associated with the supplement.
	 */
	public void setReport(Report report) {
		mReport = report;
	}

	/**
	 * Retrieve the size of the supplement.
	 * @return The size of the supplement in bytes.
	 */
	public long getSize() {
		return mSize;
	}

	/**
	 * Set the size of the supplement.
	 * @param size The size of the supplement in bytes.
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

		JsonElement remoteUri = json.get("url");
		if (remoteUri != null) {
			setRemoteUri(remoteUri.getAsString());
		}

		JsonElement filename = json.get("filename");
		if (filename != null) {
			setFileName(filename.getAsString());
		}

		JsonElement size = json.get("size");
		if (size != null) {
			setSize(size.getAsLong());
		}

		JsonElement thumbnailUri = json.get("thumbnail");
		if (thumbnailUri != null) {
			setThumbnailUri(thumbnailUri.getAsString());
		}

		setDate(new DateTime(parseDate(json.get("date"))));
	}
}
