package com.missionse.httpdatabaseexample.model;

/**
 * Provides the model for a classroom.
 */
public class Classroom {

	private String mName;

	/**
	 * Constructor.
	 * @param name The name of the classroom.
	 */
	public Classroom(final String name) {
		mName = name;
	}

	/**
	 * Gets the name of the classroom.
	 * @return The name of the classroom.
	 */
	public String getName() {
		return mName;
	}

	@Override
	public String toString() {
		return "[Classroom] " + mName;
	}
}
