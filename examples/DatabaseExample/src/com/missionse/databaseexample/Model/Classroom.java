package com.missionse.databaseexample.Model;

import java.util.Collection;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;

/**
 * A Classroom.
 */
public class Classroom {

	@DatabaseField(generatedId = true, columnName = "_id")
	private int mId;

	@DatabaseField(columnName = "name")
	private String mClassRoomName;

	@ForeignCollectionField
	private Collection<Student> mStudents;

	/**
	 * Creates a new Classroom.
	 */
	public Classroom() {
	}

	@Override
	public String toString() {
		return "[Classroom] " + mClassRoomName;
	}

	/**
	 * @return the mId
	 */
	public int getId() {
		return mId;
	}

	/**
	 * @return the mClassRoomName
	 */
	public String getClassRoomName() {
		return mClassRoomName;
	}

	/**
	 * @param classRoomName the mClassRoomName to set
	 */
	public void setClassRoomName(String classRoomName) {
		this.mClassRoomName = classRoomName;
	}

	/**
	 * @return the mStudents
	 */
	public Collection<Student> getStudents() {
		return mStudents;
	}

	/**
	 * @param students the mStudents to set
	 */
	public void setStudents(Collection<Student> students) {
		this.mStudents = students;
	}
	
	
}
