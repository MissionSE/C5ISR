package com.missionse.logisticsexample.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;

/**
 * Represents the offline database. 
 * @author rvieras
 *
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
	
	private static final String DATABASE_NAME = "mylocaldatabase.db";
	private static final int DATABASE_VERSION = 1;

	//private Dao<Classroom, Integer> mClassRoomDao = null;
	//private Dao<Student, Integer> mStudentDao = null;

	/**
	 * Constructor.
	 * @param context - {@link android.app.Context}
	 */
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
//		try {
//			Log.i(DatabaseHelper.class.getName(), "onCreate");
//			TableUtils.createTable(connectionSource, Student.class);
//			TableUtils.createTable(connectionSource, Classroom.class);
//		} catch (SQLException e) {
//			Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
//			throw new RuntimeException(e);
//		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
//		try {
//			Log.i(DatabaseHelper.class.getName(), "onUpgrade");
//			TableUtils.dropTable(connectionSource, Student.class, true);
//			TableUtils.dropTable(connectionSource, Classroom.class, true);
//
//			onCreate(db, connectionSource);
//		} catch (SQLException e) {
//			Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
//			throw new RuntimeException(e);
//		}
	}

//	/**
//	 * Returns the Database Access Object (DAO) for our Student class. It will create it or just give the cached
//	 * value.
//	 * @return - DAO object
//	 */
//	public Dao<Student, Integer> getStudentDao() {
//		try {
//			if (mStudentDao == null) {
//				mStudentDao = getDao(Student.class);
//			}
//			return mStudentDao;
//		} catch (Exception sqlException) {
//			Log.e("DatabaseHelper", "SQL Exception", sqlException);
//			return null;
//		}
//	}
//	
//	/**
//	 * Returns the Database Access Object (DAO) for our Classroom class. It will create it or just give the cached
//	 * value.
//	 * @return - DAO object
//	 */
//	public Dao<Classroom, Integer> getClassRoomDao() {
//		try {
//			if (mClassRoomDao == null) {
//				mClassRoomDao = getDao(Classroom.class);
//			}
//			return mClassRoomDao;
//		} catch (Exception sqlException) {
//			Log.e("DatabaseHelper", "SQL Exception", sqlException);
//			return null;
//		}
//	}
//
//	/**
//	 * Close the database connections and clear any cached DAOs.
//	 */
//	@Override
//	public void close() {
//		super.close();
//		mClassRoomDao = null;
//		mStudentDao = null;
//	}

}
