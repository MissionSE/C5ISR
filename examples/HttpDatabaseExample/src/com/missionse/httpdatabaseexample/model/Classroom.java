package com.missionse.httpdatabaseexample.model;

public class Classroom {

	public int id;
	public String classRoomName;

	public Classroom() {
	}

	@Override
	public String toString() {
		return "[Classroom] " + classRoomName;
	}
}
