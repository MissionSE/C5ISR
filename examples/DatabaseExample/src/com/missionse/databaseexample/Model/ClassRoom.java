package com.missionse.databaseexample.Model;

import org.orman.mapper.EntityList;
import org.orman.mapper.Model;
import org.orman.mapper.annotation.Entity;
import org.orman.mapper.annotation.OneToMany;
import org.orman.mapper.annotation.PrimaryKey;

@Entity
public class ClassRoom extends Model<ClassRoom> {
	
	@PrimaryKey(autoIncrement=true)
	public int id;
	
	public String mClassRoomName;
	
	@OneToMany( toType = Student.class, onField = "mClassRoom")
	public EntityList<ClassRoom,Student> students = new EntityList(ClassRoom.class,Student.class,this);
	
	
	/*
	 * Default constructor needed for the ORM library
	 */
	public ClassRoom(){}
	
	
	
	/*
	 * Add all your getter/setters and other methods
	 * 
	 */

}
