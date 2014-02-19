package com.missionse.kestrelweather.database.model.tables;

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
	@DatabaseField(columnName = "type", dataType = DataType.ENUM_INTEGER)
	private SupplementType mType;
	@DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "report_id")
	private Report mReport;


	/**
	 * Default Constructor.
	 */
	public Supplement() {
		mUri = "";
		mType = SupplementType.UNKNOWN;
	}

	public String getUri() {
		return mUri;
	}

	public void setUri(String uri) {
		mUri = uri;
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
