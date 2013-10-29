package com.missionse.database;

import org.orman.dbms.Database;
import org.orman.dbms.sqliteandroid.SQLiteAndroid;
import org.orman.mapper.MappingSession;
import org.orman.mapper.SchemaCreationPolicy;

import android.content.Context;

public final class GenericDatabase {
	
	/**
	 * Initialize the database framework with the given Entity classes.
	 * This should be could in an <code>andoird.app.Application</code> classes <code>onCreate</code> class. 
	 * @param tContext 
	 * @param tDatabaseName 
	 * @param tDatabaseVersion 
	 * @param tEntities
	 */
	public static void initialize(Context tContext, String tDatabaseName, int tDatabaseVersion, Class<?>[] tEntities){
		
		if(tDatabaseName == null)
			throw new NullPointerException("DATABASE NAME IS NULL");
		if(tDatabaseVersion < 0)
			throw new NullPointerException("NEGATIVE DATABASE VERSION NOT ACCEPTABLE");
		
		Database lDatabase = new SQLiteAndroid(tContext,tDatabaseName,tDatabaseVersion);
		MappingSession.getConfiguration().setCreationPolicy(SchemaCreationPolicy.CREATE_IF_NOT_EXISTS);
		for(Class<?> entity : tEntities){
			MappingSession.registerEntity(entity);
		}
        MappingSession.registerDatabase(lDatabase);
        MappingSession.start();

	}
	
	/**
	 * Cleans up any left over pieces from the database
	 */
	public static void terminate(){
		//currently nothing to clean up 
	}

}
