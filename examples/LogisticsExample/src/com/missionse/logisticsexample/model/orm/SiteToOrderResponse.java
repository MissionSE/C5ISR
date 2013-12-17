package com.missionse.logisticsexample.model.orm;

import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.missionse.logisticsexample.model.mappings.SiteToOrder;

/**
 * Wrapper class for JSON. 
 */
public class SiteToOrderResponse extends Response {
	
	@SerializedName("sites_to_orders")
	private List<SiteToOrder> mSiteToOrders;

	/**
	 * @return the mSiteToOrders
	 */
	public List<SiteToOrder> getSiteToOrders() {
		return mSiteToOrders;
	}

	/**
	 * @param siteToOrders the mSiteToOrders to set
	 */
	public void setSiteToOrders(List<SiteToOrder> siteToOrders) {
		this.mSiteToOrders = siteToOrders;
	}

	
}
