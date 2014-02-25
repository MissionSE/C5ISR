package com.missionse.kestrelweather.database.model;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;

import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.Map;

/**
 * Base class for database tables.
 */
public class Entity {
	@Expose(serialize = true, deserialize = true)
	@SerializedName("_id")
	@DatabaseField(columnName = "_id", generatedId = true)
	private int mId;

	@Expose(serialize = false, deserialize = false)
	@SerializedName("remote_id")
	@DatabaseField(columnName = "remote_id")
	private int mRemoteId;

	@Expose(serialize = false, deserialize = false)
	@SerializedName("dirty")
	@DatabaseField(columnName = "dirty")
	private boolean mDirty = true;

	@Expose(serialize = true, deserialize = true)
	@SerializedName("updatedAt")
	@DatabaseField(columnName = "update_at")
	private DateTime mUpdateAt = DateTime.now();

	@Expose(serialize = true, deserialize = true)
	@SerializedName("createdAt")
	@DatabaseField(columnName = "created_at")
	private DateTime mCreatedAt = DateTime.now();

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
	 * Return the database ID associated with this object.
	 * @return The database ID.
	 */
	public int getRemoteId() {
		return mRemoteId;
	}

	/**
	 * Set the database ID associated with this object.
	 * @param id The database ID.
	 */
	public void setRemoteId(final int id) {
		mRemoteId = id;
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

	/**
	 * Creates a map representation of the fields in the entity.
	 * @return A map containing the fields in the entity.
	 */
	public Map<String, String> toMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("updatedat", Long.toString(mUpdateAt.getMillis()));
		map.put("createdat", Long.toString(mCreatedAt.getMillis()));

		return map;
	}

	/**
	 * Populates this object based on the JsonObject param.
	 * @param json - JsonObject param.
	 */
	public void populate(JsonObject json) {
		int id = (json.get("_id") == null ? -1 : json.get("_id").getAsInt());
		long update = (json.get("updatedat") == null ? 0 : json.get("updatedat").getAsLong());
		long create = (json.get("createdat") == null) ? 0 : json.get("createdat").getAsLong();

		setId(id);
		setUpdateAt(new DateTime(update));
		setCreatedAt(new DateTime(create));
		setDirty(false);
		setRemoteId(id);
	}
}
