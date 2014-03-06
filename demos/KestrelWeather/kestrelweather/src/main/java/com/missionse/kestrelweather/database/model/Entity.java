package com.missionse.kestrelweather.database.model;

import android.util.Log;

import com.google.gson.JsonElement;
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
		int id = parseId(json.get("_id"));
		long update = parseDate(json.get("updatedat"));
		long create = parseDate(json.get("createdat"));

		setId(id);
		setUpdateAt(new DateTime(update));
		setCreatedAt(new DateTime(create));
		setDirty(false);
		setRemoteId(id);
	}

	/*
	 * This was needed to because some ID are formatted as hash from the remote database.
	 * Hash format is currently not supported locally so it is defaulted to '0' so the local
	 * database can correctly insert it into the local database.
	 */
	private int parseId(JsonElement element) {
		int retValue = 0;
		if (element != null) {
			try {
				retValue = element.getAsInt();
			} catch (NumberFormatException e) {
				Log.d("Entity", "Unable to read db id... defaulting to 0");
			}
		}
		return retValue;
	}

	/*
	 * This was needed to support both long dates formatted in milliseconds from epoch
	 * as well as string gmt format.
	 */
	protected long parseDate(JsonElement element) {
		long retValue = DateTime.now().getMillis();
		if (element != null) {
			try {
				retValue = element.getAsLong();
			} catch (NumberFormatException e) {
				Log.d("Entity", "Unable to parse as long:" + element.toString());
				Log.d("Entity", "parse as string..");
				retValue = DateTime.parse(element.getAsString()).getMillis();
			}
		}
		return retValue;
	}
}
