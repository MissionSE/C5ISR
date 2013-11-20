package com.missionse.databaseexample;

import android.app.Application;

import com.missionse.database.GenericDatabase;

public class DatabaseApplication extends Application {

	private static final int VERSION = 2;

	@Override
	public void onCreate() {
		super.onCreate();
		GenericDatabase.initialize(this, "databaseexample.db", VERSION,
				new Class<?>[] { com.missionse.databaseexample.Model.Student.class,
						com.missionse.databaseexample.Model.Classroom.class });
	}
}
