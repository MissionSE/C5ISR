package com.missionse.databaseexample;

import android.app.Application;

import com.missionse.database.GenericDatabase;

public class DatabaseApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		GenericDatabase.initialize(
				getApplicationContext(), /* Context */ 
				"default.db",            /* Database name */
				1,                       /* Database version */
				new Class<?>[]{          /* Entity Classes  */ 
							com.missionse.databaseexample.Model.Student.class, 
			                com.missionse.databaseexample.Model.ClassRoom.class
			                });
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}
	
		
	

}
