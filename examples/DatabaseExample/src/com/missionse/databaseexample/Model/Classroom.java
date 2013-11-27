package com.missionse.databaseexample.Model;

import org.orman.mapper.EntityList;
import org.orman.mapper.Model;
import org.orman.mapper.annotation.Entity;
import org.orman.mapper.annotation.OneToMany;
import org.orman.mapper.annotation.PrimaryKey;

// BEGIN MODEL CODE

/**
 * A Classroom.
 */
@Entity
public class Classroom extends Model<Classroom> {

	@PrimaryKey(autoIncrement = true)
	public int id;

	public String mClassRoomName;

	@OneToMany(toType = Student.class, onField = "mClassroom")
	public EntityList<Classroom, Student> students = new EntityList<Classroom, Student>(Classroom.class, Student.class,
			this);

	/**
	 * Creates a new Classroom.
	 */
	public Classroom() {
	}

	@Override
	public String toString() {
		return "[Classroom] " + mClassRoomName;
	}
}

// END MODEL CODE
