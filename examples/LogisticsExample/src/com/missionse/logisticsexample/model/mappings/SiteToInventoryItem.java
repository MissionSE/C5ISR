package com.missionse.logisticsexample.model.mappings;

import java.util.Map;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.missionse.logisticsexample.model.DBEntity;

/**
 * Mapping a {@link com.missionse.logisticsexample.model.Site} to a inventory
 * {@link com.missionse.logisticsexample.model.InventoryItem}.
 */
public class SiteToInventoryItem extends DBEntity {

	@Expose(serialize = true, deserialize = true)
	@SerializedName("site_id")
	@DatabaseField(columnName = "site_id")
	private int mSiteId;

	@Expose(serialize = true, deserialize = true)
	@SerializedName("item_id")
	@DatabaseField(columnName = "item_id")
	private int mItemId;

	/**
	 * Default constructor.
	 */
	public SiteToInventoryItem() {
	}

	/**
	 * Gets the site id for the mapping.
	 * @return The site id.
	 */
	public int getSiteId() {
		return mSiteId;
	}

	/**
	 * Sets the site id for the mapping.
	 * @param siteId The site id.
	 */
	public void setSiteId(final int siteId) {
		mSiteId = siteId;
	}

	/**
	 * Gets the item id for the mapping.
	 * @return The item id.
	 */
	public int getItemId() {
		return mItemId;
	}

	/**
	 * Sets the item id for the mapping.
	 * @param itemId The item id to set.
	 */
	public void setItemId(final int itemId) {
		mItemId = itemId;
	}

	@Override
	public String toString() {
		StringBuilder string = new StringBuilder();
		string.append("SiteToInventoryItem>: ");
		string.append(" id = " + getId());
		string.append(" site_id = " + mSiteId);
		string.append(" item_id = " + mItemId);
		return string.toString();
	}

	@Override
	public Map<String, String> toMap() {
		Map<String, String> map = super.toMap();
		map.put("site_id", Integer.toString(mSiteId));
		map.put("item_id", Integer.toString(mItemId));

		return map;
	}
}
