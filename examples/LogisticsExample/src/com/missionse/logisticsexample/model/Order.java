package com.missionse.logisticsexample.model;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DataType;
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
	@SerializedName("severity")
	@DatabaseField(dataType = DataType.ENUM_INTEGER, columnName = "severity")
	private Severity mSeverity;

	@Expose(serialize = true, deserialize = true)
	@SerializedName("status")
	@DatabaseField(dataType = DataType.ENUM_INTEGER, columnName = "status")
	private Status mStatus;

	/**
	 * Empty constructor. Needed for the ORM library.
	 */
	public Order() {
		mTimeStamp = new DateTime();
		mSeverity = Severity.NORMAL;
		mStatus = Status.INCOMPLETE;
	}

	/**
	 * Retrieves the timestamp on this order.
	 * @return the timestamp
	 */
	public DateTime getTimeStamp() {
		return mTimeStamp;
	}

	/**
	 * Sets the timestamp of this order.
	 * @param timeStamp the timestamp to set
	 */
	public void setTimeStamp(final DateTime timeStamp) {
		mTimeStamp = timeStamp;
	}

	/**
	 * Gets the severity of this order.
	 * @return the severity
	 */
	public Severity getSeverity() {
		return mSeverity;
	}

	/**
	 * Sets the severity of this order.
	 * @param severity the severity to set
	 */
	public void setSeverity(final Severity severity) {
		mSeverity = severity;
	}

	/**
	 * Retrieves the status of this order.
	 * @return the status
	 */
	public Status getStatus() {
		return mStatus;
	}

	/**
	 * Sets the status of this order.
	 * @param status the status to set
	 */
	public void setStatus(final Status status) {
		mStatus = status;
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
