package com.missionse.logisticsexample.model.orm;

import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.missionse.logisticsexample.model.SeverityName;

/**
 * Gson response wrapper for Severity Names.
 */
public class SeverityNameResponse extends Response {
	@SerializedName("severity_names")
	private List<SeverityName> mSeverityNames;

	/**
	 * @return the mSeverityNames
	 */
	public List<SeverityName> getSeverityNames() {
		return mSeverityNames;
	}

	/**
	 * @param severityNames
	 *            the mSeverityNames to set
	 */
	public void setSeverityNames(List<SeverityName> severityNames) {
		mSeverityNames = severityNames;
	}
}
