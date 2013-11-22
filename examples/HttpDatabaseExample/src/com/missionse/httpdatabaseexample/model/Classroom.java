package com.missionse.httpdatabaseexample.model;

public class Classroom {

	private String classRoomName;

	public Classroom(final String name) {
		classRoomName = name;
	}

	public String getName() {
		return classRoomName;
	}

	@Override
	public String toString() {
		return "[Classroom] " + classRoomName;
	}
}
