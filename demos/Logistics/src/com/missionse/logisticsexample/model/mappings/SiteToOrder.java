package com.missionse.logisticsexample.model.mappings;

import java.util.Map;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.missionse.logisticsexample.model.DBEntity;

/**
 * Mapping a {@link com.missionse.logisticsexample.model.Site} to an {@link com.missionse.logisticsexample.model.Order}.
 */
public class SiteToOrder extends DBEntity {

	@Expose(serialize = true, deserialize = true)
	@SerializedName("order_id")
	@DatabaseField(columnName = "order_id")
	private int mOrderId;

	@Expose(serialize = true, deserialize = true)
	@SerializedName("site_id")
	@DatabaseField(columnName = "site_id")
	private int mSiteId;

	/**
	 * Empty constructor.
	 */
	public SiteToOrder() {
	}

	/**
	 * Gets the order id for the mapping.
	 * @return The order id.
	 */
	public int getOrderId() {
		return mOrderId;
	}

	/**
	 * Sets the order id for the mapping.
	 * @param orderId The order id.
	 */
	public void setOrderId(final int orderId) {
		mOrderId = orderId;
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

	@Override
	public String toString() {
		StringBuilder string = new StringBuilder();
		string.append("SiteToOrder>: ");
		string.append(" id = " + getId());
		string.append(" site_id = " + mSiteId);
		string.append(" order_id = " + mOrderId);
		return string.toString();
	}

	@Override
	public Map<String, String> toMap() {
		Map<String, String> map = super.toMap();
		map.put("site_id", Integer.toString(mSiteId));
		map.put("order_id", Integer.toString(mOrderId));

		return map;
	}
}
