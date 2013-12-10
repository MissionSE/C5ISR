package com.missionse.gsonexample.model.shopboard;

import com.google.gson.annotations.SerializedName;

/**
 * Model that represents individual shop board item. 
 * @author rvieras
 *
 */
public class ShopBoardItem {
	
	@SerializedName("item_name")
	private String mItemName;

	@SerializedName("price")
	private String mPrice;
	
	@SerializedName("description")
	private String mDescription;
	
	@SerializedName("user_name")
	private String mUserName;
	
	/**
	 * Empty Constructor. 
	 */
	public ShopBoardItem() {
		mItemName = "N/A";
		mPrice = "0.00";
		mDescription = "N/A";
		mUserName = "JohnDoe";
	}
	
	/**
	 * Get the item name. 
	 * @return - string
	 */
	public String getItemName() {
		return mItemName;
	}
	
	/**
	 * Set the item name.
	 * @param pItemName - String
	 */
	public void setItemName(String pItemName) {
		this.mItemName = pItemName;
	}
	
	/**
	 * Get the item price. 
	 * @return - string
	 */
	public String getPrice() {
		return mPrice;
	}

	/**
	 * Set the item price.
	 * @param pPrice - String
	 */
	public void setPrice(String pPrice) {
		this.mPrice = pPrice;
	}

	/**
	 * Get the item description. 
	 * @return - string
	 */
	public String getDescription() {
		return mDescription;
	}

	/**
	 * Set the item description.
	 * @param pDescription - String
	 */
	public void setDescription(String pDescription) {
		this.mDescription = pDescription;
	}

	/**
	 * Get the item username. 
	 * @return - string
	 */
	public String getUserName() {
		return mUserName;
	}

	/**
	 * Set the item username.
	 * @param pUserName - String
	 */
	public void setUserName(String pUserName) {
		this.mUserName = pUserName;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(mItemName + " (" + mPrice + ") [" + mDescription + "] by>:" + mUserName);
		return sb.toString();
	}


}
