package com.missionse.logisticsexample.model;

import java.util.Date;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.missionse.logisticsexample.model.orm.MyDatePersister;

/**
 * Represents an order from a {@link Site}.
 */
public class MyOrder extends DBEntity {	

	@Expose(serialize = true, deserialize = true)
	@SerializedName("order_at")
	@DatabaseField(columnName = "order_at",  persisterClass = MyDatePersister.class)
	private Date mTimeStamp;
	
	@Expose(serialize = true, deserialize = true)
	@SerializedName("severity")
	@DatabaseField(dataType = DataType.ENUM_INTEGER, columnName = "severity")
	private Severity mSeverity;
	
	@Expose(serialize = true, deserialize = true)
	@SerializedName("status")
	@DatabaseField(dataType = DataType.ENUM_INTEGER, columnName = "status")
	private Status mStatus;

	/**
	 * Empty constructor.  Needed for the ORM library.
	 */
	public MyOrder() { 
		mTimeStamp = new Date();
		mSeverity = Severity.NORMAL;
		mStatus = Status.INCOMPLETE;
		
	}

	/**
	 * @return the mTimeStamp
	 */
	public Date getTimeStamp() {
		return mTimeStamp;
	}

	/**
	 * @param timeStamp the mTimeStamp to set
	 */
	public void setTimeStamp(Date timeStamp) {
		this.mTimeStamp = timeStamp;
	}

	/**
	 * @return the mSeverity
	 */
	public Severity getSeverity() {
		return mSeverity;
	}

	/**
	 * @param severity the mSeverity to set
	 */
	public void setSeverity(Severity severity) {
		this.mSeverity = severity;
	}

	/**
	 * @return the mStatus
	 */
	public Status getStatus() {
		return mStatus;
	}

	/**
	 * @param status the mStatus to set
	 */
	public void setStatus(Status status) {
		this.mStatus = status;
	}
}
