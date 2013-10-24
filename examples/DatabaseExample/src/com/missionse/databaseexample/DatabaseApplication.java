package com.missionse.databaseexample;

import org.orman.dbms.Database;
import org.orman.dbms.sqliteandroid.SQLiteAndroid;
import org.orman.mapper.MappingSession;
import org.orman.mapper.SchemaCreationPolicy;

import com.missionse.databaseexample.Model.ClassRoom;
import com.missionse.databaseexample.Model.Student;

import android.app.Application;

public class DatabaseApplication extends Application {
	
	public static String DATABASE_NAME = "generic.db";
	public static int DATABASE_VERSION = 1;

	@Override
	public void onCreate(){
		
		//The following lines are needed for the ORM lib
		Database lDb = new SQLiteAndroid(this,DATABASE_NAME,DATABASE_VERSION);
		MappingSession.getConfiguration().setCreationPolicy(SchemaCreationPolicy.CREATE_IF_NOT_EXISTS);
		//register all your entity objects :(    
		MappingSession.registerEntity(Student.class);
		MappingSession.registerEntity(ClassRoom.class);
		MappingSession.registerDatabase(lDb);
		MappingSession.start();
	
		
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		//TODO: May need some clean up but currently everything is fine.
	}
	
	

}
