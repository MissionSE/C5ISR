package com.missionse.logisticsexample.model.mappings;

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
	 * Gets the order id for this mapping.
	 * @return the order id
	 */
	public int getOrderId() {
		return mOrderId;
	}

	/**
	 * Sets the order id for this mapping.
	 * @param orderId the order id to set
	 */
	public void setOrderId(final int orderId) {
		mOrderId = orderId;
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
}
