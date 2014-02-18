package com.missionse.kestrelweather.database.model.tables;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.missionse.kestrelweather.database.model.SupplementType;

import org.joda.time.DateTime;

/**
 * A report supplement class.  Used for holding video, photos etc.
 */
public class Supplement {
	@DatabaseField(columnName = "uri")
	private String mUri;
	@DatabaseField(columnName = "modified_at")
	private DateTime mLastModified;
	@DatabaseField(columnName = "type", dataType = DataType.ENUM_INTEGER)
	private SupplementType mType;
	@DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "report_id")
	private Report mReport;


	/**
	 * Default Constructor.
	 */
	public Supplement() {
		mUri = "";
		mLastModified = null;
		mType = SupplementType.UNKNOWN;
	}

	public String getUri() {
		return mUri;
	}

	public void setUri(String uri) {
		mUri = uri;
	}

	public DateTime getLastModified() {
		return mLastModified;
	}

	public void setLastModified(DateTime lastModified) {
		mLastModified = lastModified;
	}

	public SupplementType getType() {
		return mType;
	}

	public void setType(SupplementType type) {
		mType = type;
	}

	public Report getReport() {
		return mReport;
	}

	public void setReport(Report report) {
		mReport = report;
	}
}
