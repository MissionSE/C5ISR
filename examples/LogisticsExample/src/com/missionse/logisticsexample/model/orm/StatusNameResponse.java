package com.missionse.logisticsexample.model.orm;

import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.missionse.logisticsexample.model.StatusName;

/**
 * Gson response wrapper for Status Names.
 */
public class StatusNameResponse extends Response {
	@SerializedName("status_names")
	private List<StatusName> mStatusNames;

	/**
	 * @return the mStatusNames
	 */
	public List<StatusName> getStatusNames() {
		return mStatusNames;
	}

	/**
	 * @param statusNames
	 *            the mStatusNames to set
	 */
	public void setStatusNames(List<StatusName> statusNames) {
		mStatusNames = statusNames;
	}
}
