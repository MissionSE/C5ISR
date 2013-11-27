package com.missionse.databaseexample;

import java.util.ArrayList;
import java.util.List;

import org.orman.dbms.Database;
import org.orman.dbms.sqliteandroid.SQLiteAndroid;
import org.orman.mapper.MappingSession;
import org.orman.mapper.SchemaCreationPolicy;

import android.app.Application;

import com.missionse.databaseexample.Model.Classroom;
import com.missionse.databaseexample.Model.Student;

/**
 * Acts as the entry point to the DatabaseApplication. Initializes/connects to the database.
 */
public class DatabaseApplication extends Application {

	private static final int VERSION = 2;
	private static final String DATABASE_NAME = "databasexample.db";

	@Override
	public void onCreate() {
		super.onCreate();
		List<Class<?>> entities = new ArrayList<Class<?>>();
		entities.add(Student.class);
		entities.add(Classroom.class);

		Database database = new SQLiteAndroid(this, DATABASE_NAME, VERSION);
		MappingSession.getConfiguration().setCreationPolicy(SchemaCreationPolicy.CREATE_IF_NOT_EXISTS);
		for (Class<?> entity : entities) {
			MappingSession.registerEntity(entity);
		}
		MappingSession.registerDatabase(database);
		MappingSession.start();
	}
}
