package com.missionse.logisticsexample.model.mappings;

import com.j256.ormlite.field.DatabaseField;
import com.missionse.logisticsexample.model.Order;
import com.missionse.logisticsexample.model.SupplySite;

/**
 * Mapping a {@link SupplySite} to an {@link Order}.
 */
public class SiteToOrder {
	
	@DatabaseField(generatedId = true, columnName = "_id")
	private int mId;
	
	@DatabaseField(columnName = "order_id")
	private int mOrderId;
	
	@DatabaseField(columnName = "site_id")
	private int mSiteId;
	
	/**
	 * Empty constructor. 
	 */
	public SiteToOrder() { }

	/**
	 * @return the mId
	 */
	public int getId() {
		return mId;
	}

	/**
	 * @param id the mId to set
	 */
	public void setId(int id) {
		this.mId = id;
	}

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
