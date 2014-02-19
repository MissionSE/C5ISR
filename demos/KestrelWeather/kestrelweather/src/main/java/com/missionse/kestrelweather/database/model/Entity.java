package com.missionse.kestrelweather.database.model;

import com.j256.ormlite.field.DatabaseField;

import org.joda.time.DateTime;

/**
 * Base class for database tables.
 */
public class Entity {
	@DatabaseField(columnName = "_id", generatedId = true)
	private int mId;
	@DatabaseField(columnName = "dirty")
	private boolean mDirty = false;
	@DatabaseField(columnName = "update_at", version = true)
	private DateTime mUpdateAt = DateTime.now();
	@DatabaseField(columnName = "created_at")
	private DateTime mCreatedAt =DateTime.now();

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

	/**
	 * Getter.
	 * @return Instance of DateTime which states the time of last modification.
	 */
	public DateTime getUpdateAt() {
		return mUpdateAt;
	}

	/**
	 * Setter.
	 * @param updateAt DateTime this report was last modified.
	 */
	public void setUpdateAt(DateTime updateAt) {
		mUpdateAt = updateAt;
	}

	/**
	 * Getter.
	 * @return Instance of DataTime which states when this report was created.
	 */
	public DateTime getCreatedAt() {
		return mCreatedAt;
	}

	/**
	 * Setter.
	 * @param createdAt DateTime this report was created at.
	 */
	public void setCreatedAt(DateTime createdAt) {
		mCreatedAt = createdAt;
	}

}
