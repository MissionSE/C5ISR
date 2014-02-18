package com.missionse.kestrelweather.database.model;

import com.j256.ormlite.field.DatabaseField;

/**
 * Base class for database tables.
 */
public class Entity {
	@DatabaseField(columnName = "_id", generatedId = true)
	private int mId;
	@DatabaseField(columnName = "dirty")
	private boolean mDirty;

	/**
	 * Return the database ID associated with this object.
	 * @return The database ID. 
	 */
	public int getId() {
		return mId;
	}

	/**
	 * Set the database ID associated with this object.
	 * @param id The database ID.
	 */
	public void setId(final int id) {
		mId = id;
	}

	/**
	 * Determine whether or not this object is dirty.
	 * @return Boolean true if it's dirty.
	 */
	public boolean isDirty() {
		return mDirty;
	}

	/**
	 * Set whether or not this object is dirty.
	 * @param dirty Boolean true if it's dirty.
	 */
	public void setDirty(final boolean dirty) {
		mDirty = dirty;
	}
}
