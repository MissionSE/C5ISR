package com.missionse.kestrelweather.database.model.tables;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.missionse.kestrelweather.database.model.Entity;

import java.util.Map;

/**
 * Report Note table.
 */
@DatabaseTable(daoClass = com.missionse.kestrelweather.database.model.tables.manipulators.NoteTable.class)
public class Note extends Entity {
	@DatabaseField(columnName = "title")
	private String mTitle;

	@DatabaseField(columnName = "content")
	private String mContent;

	@DatabaseField(columnName = "size")
	private long mSize;

	@DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "report_id")
	private Report mReport;

	/**
	 * Constructor. Needed for API.
	 */
	public Note() {
		mTitle = "";
		mContent = "";
		mSize = 0L;
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

	/**
	 * Getter.
	 * @return Instance of Report associated with this Note.
	 */
	public Report getReport() {
		return mReport;
	}

	/**
	 * Setter.
	 * @param report - The report to associate with.
	 */
	public void setReport(Report report) {
		mReport = report;
	}

	/**
	 * Retrieve the size of the note.
	 * @return The size of the note in long.
	 */
	public long getSize() {
		return mSize;
	}

	/**
	 * Set the size of the note.
	 * @param size The size of the note.
	 */
	public void setSize(long size) {
		mSize = size;
	}

	@Override
	public Map<String, String> toMap() {
		Map<String, String> map = super.toMap();
		map.put("title", mTitle);
		map.put("content", mContent);
		map.put("size", Long.toString(mSize));

		return map;
	}

	@Override
	public void populate(JsonObject json) {
		super.populate(json);
		setTitle(json);
		setContent(json);
		setSize(json);
	}

	private void setTitle(final JsonObject json) {
		JsonElement title = json.get("title");
		if (title != null) {
			setTitle(title.getAsString());
		} else {
			setTitle("");
		}
	}

	private void setContent(final JsonObject json) {
		JsonElement content = json.get("content");
		if (content != null) {
			setContent(content.getAsString());
		} else {
			setContent("");
		}
	}

	private void setSize(final JsonObject json) {
		JsonElement size = json.get("size");
		if (size != null) {
			setSize(size.getAsLong());
		} else {
			setSize(0L);
		}
	}
}
