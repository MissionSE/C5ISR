package com.missionse.logisticsexample.model;

import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Represents an order from a {@link Site}.
 */
@DatabaseTable(tableName = "myorders")
public class Order extends DBEntity implements Comparable<Object> {

	@Expose(serialize = true, deserialize = true)
	@SerializedName("order_at")
	@DatabaseField(columnName = "order_at")
	private DateTime mTimeStamp;

	@Expose(serialize = true, deserialize = true)
	@SerializedName("severity_id")
	@DatabaseField(columnName = "severity_id")
	private int mSeverityId;

	@Expose(serialize = true, deserialize = true)
	@SerializedName("status_id")
	@DatabaseField(columnName = "status_id")
	private int mStatusId;

	/**
	 * Default constructor.
	 */
	public Order() {
		mTimeStamp = new DateTime();
		mSeverityId = 0;
		mStatusId = 0;
	}

	/**
	 * Retrieves the timestamp on this order.
	 * @return the timestamp
	 */
	public DateTime getTimeStamp() {
		return mTimeStamp;
	}

	/**
	 * Sets the timestamp of the order.
	 * @param timeStamp The timestamp of the order.
	 */
	public void setTimeStamp(final DateTime timeStamp) {
		mTimeStamp = timeStamp;
	}

	/**
	 * Gets the severity of the order.
	 * @return The severity of the order.
	 */
	public int getSeverityId() {
		return mSeverityId;
	}

	/**
	 * Sets the severity of the order.
	 * @param severity The severity of the order.
	 */
	public void setSeverityId(final int severity) {
		mSeverityId = severity;
	}

	/**
	 * Retrieves the status of this order.
	 * @return the status
	 */
	public int getStatusId() {
		return mStatusId;
	}

	/**
	 * Sets the status of this order.
	 * @param status the status to set
	 */
	public void setStatusId(final int status) {
		mStatusId = status;
	}

	@Override
	public String toString() {
		DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyyMMdd");

		StringBuilder string = new StringBuilder();
		string.append(getId());
		string.append("-" + formatter.print(mTimeStamp));
		return string.toString();
	}

	@Override
	public Map<String, String> toMap() {
		Map<String, String> map = super.toMap();
		DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd hh:mm:ss");
		map.put("ordered_at", formatter.print(mTimeStamp));
		map.put("severity_id", Integer.toString(mSeverityId));
		map.put("status_id", Integer.toString(mStatusId));

		return map;
	}

	@Override
	public int compareTo(final Object another) {
		if (another != null) {
			if (getClass() == another.getClass()) {
				return toString().compareTo(((Order) another).toString());
			} else {
				return 1;
			}
		} else {
			return 1;
		}
	}
}
