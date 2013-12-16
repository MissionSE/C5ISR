package com.missionse.logisticsexample.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;

/**
 * Represents supply/assets for different locations.
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
	 * Default constructor needed by ORM library.
	 */
	public InventoryItem() {
	}

	/**
	 * Retrieves the quantity associated with this inventory item.
	 * @return the quantity
	 */
	public double getQuantity() {
		return mQuantity;
	}

	/**
	 * Sets the quantity of this inventory item.
	 * @param amount the quantity
	 */
	public void setQuantity(final double amount) {
		mQuantity = amount;
	}

	/**
	 * Retrieves the maximum amount.
	 * @return the maximum
	 */
	public double getMaxAmount() {
		return mMaximum;
	}

	/**
	 * Sets the maximum amount.
	 * @param maxAmount the maximum to set
	 */
	public void setMaxAmout(final double maxAmount) {
		mMaximum = maxAmount;
	}

	/**
	 * Retrieves the id.
	 * @return the DB ID associated with this item's name
	 */
	public int getNameId() {
		return mNameId;
	}

	/**
	 * Sets the id.
	 * @param nameId the DB id. NOTE: this should only be set if you know what you are doing!
	 */
	public void setNameId(final int nameId) {
		mNameId = nameId;
	}
}
