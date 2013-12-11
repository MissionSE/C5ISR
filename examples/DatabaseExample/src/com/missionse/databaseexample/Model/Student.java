package com.missionse.databaseexample.Model;

import com.j256.ormlite.field.DatabaseField;

/**
 * A Student.
 */
public class Student {

	@DatabaseField(generatedId = true, columnName = "_id")
	private int mId;

	@DatabaseField(columnName = "firstname")
	private String mFirstName;

	@DatabaseField(columnName = "lastname")
	private String mLastName;

	@DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "classroom_id")
	private Classroom mClassroom;

	/**
	 * Creates a new Student.
	 */
	public Student() {
	}

	@Override
	public String toString() {
		return "[Student] " + mFirstName + " " + mLastName;
	}

	/**
	 * @return the mId
	 */
	public int getId() {
		return mId;
	}

	/**
	 * @return the mFirstName
	 */
	public String getFirstName() {
		return mFirstName;
	}

	/**
	 * @param firstName the mFirstName to set
	 */
	public void setFirstName(String firstName) {
		this.mFirstName = firstName;
	}

	/**
	 * @return the mLastName
	 */
	public String getLastName() {
		return mLastName;
	}

	/**
	 * @param lastName the mLastName to set
	 */
	public void setLastName(String lastName) {
		this.mLastName = lastName;
	}

	/**
	 * @return the mClassroom
	 */
	public Classroom getClassroom() {
		return mClassroom;
	}

	/**
	 * @param classRoom the mClassroom to set
	 */
	public void setClassroom(Classroom classRoom) {
		this.mClassroom = classRoom;
	}
}
