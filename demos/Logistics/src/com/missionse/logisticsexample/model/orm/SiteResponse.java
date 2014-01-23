package com.missionse.logisticsexample.model.orm;

import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.missionse.logisticsexample.model.Site;

/**
 * Response for query for available sites.
 */
public class SiteResponse extends Response {

	@SerializedName("sites")
	private List<Site> mSites;

	/**
	 * @return the mSites
	 */
	public List<Site> getSites() {
		return mSites;
	}

	/**
	 * @param sites the mSites to set
	 */
	public void setSites(final List<Site> sites) {
		this.mSites = sites;
	}
}
