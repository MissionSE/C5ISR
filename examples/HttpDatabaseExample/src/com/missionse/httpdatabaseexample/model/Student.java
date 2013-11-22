package com.missionse.httpdatabaseexample.model;

public class Student {

	private String firstName;
	private String lastName;

	public Student(final String first, final String last) {
		firstName = first;
		lastName = last;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	@Override
	public String toString() {
		return "[Student] " + firstName + " " + lastName;
	}
}
