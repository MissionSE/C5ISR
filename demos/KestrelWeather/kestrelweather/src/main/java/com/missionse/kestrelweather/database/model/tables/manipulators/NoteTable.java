package com.missionse.kestrelweather.database.model.tables.manipulators;

import com.j256.ormlite.support.ConnectionSource;
import com.missionse.kestrelweather.database.model.tables.Note;

import java.sql.SQLException;

/**
 * The DAO class associated with Notes.
 */
public class NoteTable extends BaseTable<Note> {
	private static final String TAG = NoteTable.class.getSimpleName();

	/**
	 * Constructor.
	 *
	 * @param connectionSource The database source connection.
	 * @throws java.sql.SQLException Thrown if any issues with connection.
	 */
	public NoteTable(ConnectionSource connectionSource) throws SQLException {
		super(connectionSource, Note.class);
	}
}
