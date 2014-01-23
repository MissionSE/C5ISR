package com.missionse.logisticsexample.model;

import java.util.Map;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;

/**
 * Represents inventory items for different locations.
 */
public class InventoryItem extends DBEntity {

	@Expose(serialize = true, deserialize = true)
	@SerializedName("quantity")
	@DatabaseField(columnName = "quantity")
	private double mQuantity;

	@Expose(serialize = true, deserialize = true)
	@SerializedName("maximum")
	@DatabaseField(columnName = "maximum")
	private double mMaximum;

	@Expose(serialize = true, deserialize = true)
	@SerializedName("name_id")
	@DatabaseField(columnName = "name_id")
	private int mNameId;

	/**
	 * Default constructor.
	 */
	public InventoryItem() {
	}

	/**
	 * Retrieves the quantity associated with this inventory item.
	 * @return The quantity of the item.
	 */
	public double getQuantity() {
		return mQuantity;
	}

	/**
	 * Sets the quantity of this inventory item.
	 * @param quantity The quantity of the item.
	 */
	public void setQuantity(final double quantity) {
		mQuantity = quantity;
	}

	/**
	 * Retrieves the maximum amount of the item.
	 * @return The maximum amount of the item.
	 */
	public double getMaximum() {
		return mMaximum;
	}

	/**
	 * Sets the maximum amount of the item.
	 * @param maximum The maximum amount of the item
	 */
	public void setMaximum(final double maximum) {
		mMaximum = maximum;
	}

	/**
	 * Retrieves the id of the name.
	 * @return The id associated with this item's name
	 */
	public int getNameId() {
		return mNameId;
	}

	/**
	 * Sets the id of the name of the item.
	 * @param nameId The id of the name of the item.
	 */
	public void setNameId(final int nameId) {
		mNameId = nameId;
	}

	@Override
	public String toString() {
		StringBuilder string = new StringBuilder();
		string.append("InventoryItem>: ");
		string.append(" id = " + getId());
		string.append(" quantity = " + this.mQuantity);
		string.append(" maximum = " + this.mMaximum);
		string.append(" nameid = " + this.mNameId);
		return string.toString();
	}

	@Override
	public Map<String, String> toMap() {
		Map<String, String> map = super.toMap();
		map.put("quantity", Double.toString(mQuantity));
		map.put("maximum", Double.toString(mMaximum));
		map.put("name_id", Integer.toString(mNameId));

		return map;
	}
}
