package com.missionse.logisticsexample.model;

import java.util.Date;
import java.util.Collection;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;

/**
 * Represents an order from a {@link SupplySite}.
 * @author rvieras
 *
 */
public class Order {	
	@DatabaseField(generatedId = true, columnName = "_id")
	private int mId;

	@ForeignCollectionField()
	private Collection<Supply> mSupplies;
	
	@DatabaseField(dataType = DataType.DATE, columnName = "timestamp")
	private Date mTimeStamp;
	
	@DatabaseField(dataType = DataType.ENUM_INTEGER, columnName = "severity")
	private Severity mSeverity;
	
	@DatabaseField(dataType = DataType.ENUM_INTEGER, columnName = "status")
	private Status mStatus;
	
	@DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "site_id")
	private SupplySite mSite;
	
	/**
	 * Empty constructor.  Needed for the ORM library.
	 */
	public Order() { }

	/**
	 * @return the mSupplies
	 */
	public Collection<Supply> getSupplies() {
		return mSupplies;
	}

	/**
	 * @param supplies the mSupplies to set
	 */
	public void setSupplies(Collection<Supply> supplies) {
		this.mSupplies = supplies;
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

	/**
	 * @return the mId
	 */
	public int getId() {
		return mId;
	}
	
}
