package com.missionse.databaseexample;

import android.app.Application;

import com.missionse.database.GenericDatabase;

/**
 * Acts as the entry point to the DatabaseApplication. Initializes/connects to the database.
 */
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
