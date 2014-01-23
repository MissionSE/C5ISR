package com.missionse.logisticsexample.model.orm;

import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.missionse.logisticsexample.model.InventoryItem;

/**
 * Wrapper for JSON.
 */
public class InventoryItemResponse extends Response {
	
	@SerializedName("inventory_items")
	private List<InventoryItem> mItems;

	/**
	 * @return the mItems
	 */
	public List<InventoryItem> getItems() {
		return mItems;
	}

	/**
	 * @param items the mItems to set
	 */
	public void setItems(List<InventoryItem> items) {
		this.mItems = items;
	}
	
	
}
