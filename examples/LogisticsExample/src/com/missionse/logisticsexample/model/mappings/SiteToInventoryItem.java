package com.missionse.logisticsexample.model.mappings;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;

/**
 * Mapping a {@link com.missionse.logisticsexample.model.Site} to a inventory
 * {@link com.missionse.logisticsexample.model.InventoryItem}.
 */
public class SiteToInventoryItem {

	@Expose(serialize = true, deserialize = true)
	@SerializedName("site_id")
	@DatabaseField(columnName = "site_id")
	private int mSiteId;

	@Expose(serialize = true, deserialize = true)
	@SerializedName("item_id")
	@DatabaseField(columnName = "item_id")
	private int mItemId;

	/**
	 * Empty constructor.
	 */
	public SiteToInventoryItem() {
	}

	/**
	 * Gets the site id for this mapping.
	 * @return the site id
	 */
	public int getSiteId() {
		return mSiteId;
	}

	/**
	 * Sets the site id for this mapping.
	 * @param siteId the site id to set
	 */
	public void setSiteId(final int siteId) {
		mSiteId = siteId;
	}

	/**
	 * Gets the item id for this mapping.
	 * @return the item id
	 */
	public int getItemId() {
		return mItemId;
	}

	/**
	 * Sets the item id for this mapping.
	 * @param itemId the item id to set
	 */
	public void setItemId(final int itemId) {
		mItemId = itemId;
	}
}
