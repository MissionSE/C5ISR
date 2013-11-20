package com.missionse.databaseexample.Model;

import org.orman.mapper.Model;
import org.orman.mapper.annotation.Entity;
import org.orman.mapper.annotation.ManyToOne;
import org.orman.mapper.annotation.PrimaryKey;

@Entity
public class Student extends Model<Student> {

	@PrimaryKey(autoIncrement = true)
	public int id;

	public String mFirstName;

	public String mLastName;

	@ManyToOne
	public Classroom mClassroom;

	public Student() {
	}

	@Override
	public String toString() {
		return "[Student] " + mFirstName + " " + mLastName;
	}
}
