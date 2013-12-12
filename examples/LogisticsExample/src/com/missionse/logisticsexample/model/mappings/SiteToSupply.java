package com.missionse.logisticsexample.model.mappings;

import com.j256.ormlite.field.DatabaseField;

/**
 * Mapping a {@link com.missionse.logisticsexample.model.SupplySite} to a inventory 
 * {@link com.missionse.logisticsexample.model.InventorySupply}.
 */
public class SiteToSupply {
	
	@DatabaseField(generatedId = true, columnName = "_id")
	private int mId;
	
	@DatabaseField(columnName = "site_id")
	private int mSiteId;
	
	@DatabaseField(columnName = "supply_id")
	private int mSupplyId;
	
	/**
	 * Empty constructor.
	 */
	public SiteToSupply() { }

	/**
	 * @return the mId
	 */
	public int getmId() {
		return mId;
	}

	/**
	 * @param id the mId to set
	 */
	public void setmId(int id) {
		this.mId = id;
	}

	/**
	 * @return the mSiteId
	 */
	public int getmSiteId() {
		return mSiteId;
	}

	/**
	 * @param siteId the mSiteId to set
	 */
	public void setmSiteId(int siteId) {
		this.mSiteId = siteId;
	}

	/**
	 * @return the mSupplyId
	 */
	public int getmSupplyId() {
		return mSupplyId;
	}

	/**
	 * @param supplyId the mSupplyId to set
	 */
	public void setmSupplyId(int supplyId) {
		this.mSupplyId = supplyId;
	}
}
