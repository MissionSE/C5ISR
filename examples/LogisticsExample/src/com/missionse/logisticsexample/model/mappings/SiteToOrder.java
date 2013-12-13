package com.missionse.logisticsexample.model.mappings;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.missionse.logisticsexample.model.DBEntity;

/**
 * Mapping a {@link com.missionse.logisticsexample.model.Site} to an {@link com.missionse.logisticsexample.model.MyOrder}.
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
	public SiteToOrder() { }

	/**
	 * @return the mOrderId
	 */
	public int getOrderId() {
		return mOrderId;
	}

	/**
	 * @param orderId the mOrderId to set
	 */
	public void setOrderId(int orderId) {
		this.mOrderId = orderId;
	}
	
	/**
	 * @return the mSiteId
	 */
	public int getSiteId() {
		return mSiteId;
	}

	/**
	 * @param siteId the mSiteId to set
	 */
	public void setSiteId(int siteId) {
		this.mSiteId = siteId;
	}
}
