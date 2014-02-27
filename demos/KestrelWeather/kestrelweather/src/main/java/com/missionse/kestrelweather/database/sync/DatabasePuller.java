package com.missionse.kestrelweather.database.sync;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.missionse.kestrelweather.R;
import com.missionse.kestrelweather.database.DatabaseAccessor;
import com.missionse.kestrelweather.database.model.SupplementType;
import com.missionse.kestrelweather.database.model.tables.KestrelWeather;
import com.missionse.kestrelweather.database.model.tables.Note;
import com.missionse.kestrelweather.database.model.tables.OpenWeather;
import com.missionse.kestrelweather.database.model.tables.Report;
import com.missionse.kestrelweather.database.model.tables.Supplement;
import com.missionse.kestrelweather.database.util.IonUtil;

import java.sql.SQLException;
import java.util.List;

/**
 * Handles the pulling of information from the remote database.
 */
public class DatabasePuller implements Runnable {
	private static final String TAG = DatabasePuller.class.getSimpleName();
	private DatabaseAccessor mAccessor;
	private Context mContext;
	private String mRemoteUrl;


	/**
	 * Constructor.
	 * @param context The application context.
	 * @param accessor Instance of DatabaseAccessor.
	 */
	public DatabasePuller(Context context, DatabaseAccessor accessor) {
		mContext = context;
		mAccessor = accessor;
		mRemoteUrl = mContext.getString(R.string.remote_database) ;
	}

	@Override
	public void run() {
		String latestId = mAccessor.getLatestEvent();
		IonUtil.pullLatestEvent(mContext, latestId, new FutureCallback<JsonObject>() {
			@Override
			public void onCompleted(Exception e, JsonObject result) {
				if (e == null) {

					JsonArray fetchList = result.getAsJsonArray("toFetch");
					JsonArray removeList = result.getAsJsonArray("toRemove");

					int fetchListSize = fetchList.size();
					Log.d(TAG, "Fetch Size>: " + fetchListSize);
					for (JsonElement element : fetchList) {
						pullReport(element.getAsString());
					}

					int removeListSize = removeList.size();
					Log.d(TAG, "Remove Size>: " + removeListSize);
					for (JsonElement element : removeList) {
						removeReport(element.getAsString());
					}

					String newLatestId = result.get("latestEvent").getAsString();
					Log.d(TAG, "Setting new latest event too:" + newLatestId);
					mAccessor.setLatestEvent(newLatestId);

				} else {
					Log.e(TAG, "Unable to pull from remote database.", e);
				}
			}
		});
	}

	/*
	 * This logic will need to change once we implement the ability to
	 * to modify a report remotely.
	 */
	private void pullReport(final String reportId) {
		if (!remoteReportExists(Integer.valueOf(reportId))) {
			IonUtil.pullReport(mContext, reportId, new FutureCallback<JsonObject>() {
				@Override
				public void onCompleted(Exception e, JsonObject result) {
					if (e == null) {
						Log.d(TAG, "Parsing Report: " + result.toString());
						createReportFromJson(result);
					} else {
						Log.e(TAG, "Unable to pull a single report with id:" + reportId, e);
					}
				}
			});
		}
	}

	private boolean remoteReportExists(int id) {
		try {
			List<Report> reports =
			   mAccessor.getReportTable().queryForEq("remote_id", id);
			return reports.size() > 0;
		} catch (SQLException e) {
			Log.d(TAG, "unable to determine if remote id exists.", e);
			return false;
		}
	}

	private void createReportFromJson(final JsonObject json) {
		Report report = mAccessor.getReportTable().newReport();
		report.populate(json);
		report.setRemoteId(report.getId());
		report.setId(0);
		report.setDirty(false);

		Log.d(TAG, "parse kestrelweather...");
		final KestrelWeather kestrelWeather = new KestrelWeather();
		if (json.getAsJsonObject("kestrel") != null) {
			kestrelWeather.populate(json.getAsJsonObject("kestrel"));
			mAccessor.getKestrelWeatherTable().create(kestrelWeather);
			report.setKestrelWeather(kestrelWeather);
		} else {
			Log.d(TAG, "kestrelweather is null... skipping");
		}

		Log.d(TAG, "parse openweather...");
		final OpenWeather openWeather = new OpenWeather();
		if (json.getAsJsonObject("weather") != null) {
			openWeather.populate(json.getAsJsonObject("weather"));
			mAccessor.getOpenWeatherTable().create(openWeather);
			report.setOpenWeather(openWeather);
		} else {
			Log.d(TAG, "openweather is null... skipping");
		}

		mAccessor.getReportTable().create(report);

		Log.d(TAG, "parse notes...");
		JsonArray jsonArray = json.getAsJsonArray("notes");
		if (jsonArray != null) {
			for (JsonElement jElem : jsonArray) {
				Log.d(TAG, "parsing note from: " + jElem.toString());
				Note note = new Note();
				note.populate(jElem.getAsJsonObject());
				note.setReport(report);
				mAccessor.getNoteTable().create(note);
			}
		}

		Log.d(TAG, "parse audio...");
		JsonArray audioArray = json.getAsJsonArray("audio");
		packReportSupplement(report, SupplementType.AUDIO, audioArray);

		Log.d(TAG, "parse photos...");
		JsonArray imageArray = json.getAsJsonArray("images");
		packReportSupplement(report, SupplementType.PHOTO, imageArray);
	}

	private void packReportSupplement(Report report, SupplementType type, JsonArray jsonArray) {
		if (jsonArray != null) {
			for (JsonElement jElem : jsonArray) {
				Supplement supp = new Supplement();
				supp.setType(type);
				supp.setUri(mRemoteUrl + jElem.getAsString());
				supp.setDirty(false);
				supp.setReport(report);
				mAccessor.getSupplementTable().create(supp);
			}
		}
	}

	private void removeReport(final String reportId) {
		int id = Integer.valueOf(reportId);
		Log.d(TAG, "Removing Report with id=" + id);
		//TODO: Not yet implemented remotely or locally
	}
}
