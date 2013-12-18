package com.missionse.logisticsexample.databaseview;

import com.missionse.logisticsexample.model.Site;

/**
 * Manages several views which either prompt the user to select from a list for details, or display details about a
 * given Site.
 */
public class SiteDetailFragment extends DatabaseEntryDetailFragment {

	private Site mShownSite;

	/**
	 * Displays the provided site, creating text content to represent the site.
	 * @param site the site to display
	 */
	public void displaySite(final Site site) {
		mShownSite = site;
		setTitleText(mShownSite.getName());

		String content = "";
		content += "ID: " + mShownSite.getId() + "\n";
		content += "Parent ID: " + mShownSite.getParentId() + "\n";
		content += "Latitude: " + mShownSite.getLatitude() + "\n";
		content += "Longitude: " + mShownSite.getLongitude() + "\n";

		setContent(content);
		showContentViews();
	}

}
