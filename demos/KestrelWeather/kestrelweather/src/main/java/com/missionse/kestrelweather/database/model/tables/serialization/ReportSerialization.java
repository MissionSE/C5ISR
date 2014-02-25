package com.missionse.kestrelweather.database.model.tables.serialization;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.missionse.kestrelweather.database.local.LocalDatabaseHelper;
import com.missionse.kestrelweather.database.model.tables.Note;
import com.missionse.kestrelweather.database.model.tables.Report;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Serialization object that helps convert this object from/too JsonObject.
 */
public class ReportSerialization implements JsonSerializer<Report>, JsonDeserializer<Report> {

	private LocalDatabaseHelper mLocalDatabaseHelper = null;

	/**
	 * Constructor.
	 * @param localHelper Instance of LocalDatabaseHelper.
	 */
	public ReportSerialization(LocalDatabaseHelper localHelper) {
		mLocalDatabaseHelper = localHelper;
	}

	@Override
	public JsonElement serialize(Report src, Type typeOfSrc, JsonSerializationContext context) {
		final JsonObject jsonObject = new JsonObject();
		addEntryToJson(jsonObject, src.toMap());


		final JsonObject jsonKestrel = new JsonObject();
		if (src.getKestrelWeather() != null) {
			addEntryToJson(jsonKestrel, src.getKestrelWeather().toMap());
		}
		jsonObject.add("kestrel", jsonKestrel);

		final JsonObject jsonOpen = new JsonObject();
		if (src.getOpenWeather() != null) {
			addEntryToJson(jsonOpen, src.getOpenWeather().toMap());
		}
		jsonObject.add("weather", jsonOpen);

		final JsonArray jsonNotes = new JsonArray();
		if (src.getNotes() != null) {
			for (Note note : src.getNotes()) {
				final JsonObject jsonNote = new JsonObject();
				if (note != null) {
					addEntryToJson(jsonNote, note.toMap());
				}
				jsonNotes.add(jsonNote);
			}
		}
		jsonObject.add("notes", jsonNotes);

		return jsonObject;
	}

	@Override
	public Report deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
		Report report = mLocalDatabaseHelper.getReportTable().newReport();
		if (json != null) {
			report.populate(json.getAsJsonObject());
		}
		return report;
	}

	private void addEntryToJson(JsonObject element, Map<String, String> entity) {
		for (Map.Entry<String, String> entry : entity.entrySet()) {
			element.addProperty(entry.getKey(), entry.getValue());
		}
	}
}

