package com.missionse.logisticsexample.model.orm;

import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.missionse.logisticsexample.model.ItemName;

/**
 * Wrapper class for JSON. 
 */
public class ItemNameResponse extends Response {
	
	@SerializedName("item_names")
	private List<ItemName> mItemNames;

	/**
	 * @return the mItemNames
	 */
	public List<ItemName> getItemNames() {
		return mItemNames;
	}

	/**
	 * @param itemNames the mItemNames to set
	 */
	public void setItemNames(List<ItemName> itemNames) {
		this.mItemNames = itemNames;
	}
}
