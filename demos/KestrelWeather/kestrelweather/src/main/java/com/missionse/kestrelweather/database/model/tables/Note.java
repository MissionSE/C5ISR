package com.missionse.kestrelweather.database.model.tables;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.missionse.kestrelweather.database.model.Entity;

/**
 * Report Note table.
 */
@DatabaseTable(daoClass = com.missionse.kestrelweather.database.model.tables.manipulators.NoteTable.class)
public class Note extends Entity {
	@DatabaseField(columnName = "title")
	private String mTitle;
	@DatabaseField(columnName = "content")
	private String mContent;

	public Note() {
		mTitle = "";
		mContent = "";
	}

	/**
	 * Getter.
	 * @return String that represents the title of this note.
	 */
	public String getTitle() {
		return mTitle;
	}

	/**
	 * Setter.
	 * @param title The title that represents this note.
	 */
	public void setTitle(String title) {
		mTitle = title;
	}

	/**
	 * Getter.
	 * @return The contents in the note.
	 */
	public String getContent() {
		return mContent;
	}

	/**
	 * Setter.
	 * @param content The content associated with the note.
	 */
	public void setContent(String content) {
		mContent = content;
	}
}
