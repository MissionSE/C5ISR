package com.missionse.httpdatabaseexample.model;

/**
 * Provides the model of a student.
 */
public class Student {
	private String mFirstName;
	private String mLastName;

	/**
	 * Constructor.
	 * @param firstName The first name of the student.
	 * @param lastName The last name of the student.
	 */
	public Student(final String firstName, final String lastName) {
		mFirstName = firstName;
		mLastName = lastName;
	}

	/**
	 * Gets the first name of the student.
	 * @return The first name of the student.
	 */
	public String getFirstName() {
		return mFirstName;
	}

	/**
	 * Gets the last name of the student.
	 * @return The last name of the student.
	 */
	public String getLastName() {
		return mLastName;
	}

	@Override
	public String toString() {
		return "[Student] " + mFirstName + " " + mLastName;
	}
}
