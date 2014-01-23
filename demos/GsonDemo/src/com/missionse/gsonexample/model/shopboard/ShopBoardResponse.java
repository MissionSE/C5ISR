package com.missionse.gsonexample.model.shopboard;
import com.google.gson.annotations.SerializedName;

/**
 * This acts a interface for interfacing with the shop board server api. 
 * @author rvieras
 *
 */
public class ShopBoardResponse {
	
	@SerializedName("item")
	private ShopBoardItem mItem;
	
	/**
	 * Get the current set board item.
	 * @return - {@link ShopBoardItem}
	 */
	public ShopBoardItem getItem() {
		return mItem;
	}
	
	/**
	 * Set the shop board item.
	 * @param pItem - {@link ShopBoardItem}
	 */
	public void setItem(ShopBoardItem pItem) {
		mItem = pItem;
	}

}
