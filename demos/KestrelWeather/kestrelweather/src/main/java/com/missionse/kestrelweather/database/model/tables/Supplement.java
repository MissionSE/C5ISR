package com.missionse.kestrelweather.database.model.tables;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.missionse.kestrelweather.database.model.Entity;
import com.missionse.kestrelweather.database.model.SupplementType;

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
}
