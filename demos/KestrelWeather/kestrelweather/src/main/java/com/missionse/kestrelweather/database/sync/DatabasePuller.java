package com.missionse.kestrelweather.database.sync;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.missionse.kestrelweather.database.DatabaseAccessor;
import com.missionse.kestrelweather.database.model.SupplementType;
import com.missionse.kestrelweather.database.model.tables.KestrelWeather;
import com.missionse.kestrelweather.database.model.tables.Note;
import com.missionse.kestrelweather.database.model.tables.OpenWeather;
import com.missionse.kestrelweather.database.model.tables.Report;
import com.missionse.kestrelweather.database.model.tables.Supplement;
import com.missionse.kestrelweather.database.util.IonUtil;

import java.util.List;

/**
 * Handles the pulling of information from the remote database.
 */
public class DatabasePuller implements Runnable {
	private static final String TAG = DatabasePuller.class.getSimpleName();
	private DatabaseAccessor mAccessor;
	private Context mContext;
	private SyncStatusListener mListener;
	private int mFetchSize = 0;
	private int mCurrentFetch = 0;

	/**
	 * Constructor.
	 * @param context The application context.
	 * @param accessor Instance of DatabaseAccessor.
	 */
	public DatabasePuller(Context context, DatabaseAccessor accessor) {
		mContext = context;
		mAccessor = accessor;
	}

	@Override
	public void run() {
		String latestId = mAccessor.getLatestEvent();
		JsonObject result = IonUtil.pullLatestEvent(mContext, latestId);
		if (result != null) {
			String newLatestId = result.get("latestEvent").getAsString();

			if (!validNewEventField(latestId, newLatestId)) {
				mAccessor.clearDataTables();
				mAccessor.setLatestEvent("0");
				result = IonUtil.pullLatestEvent(mContext, "0");
				if (result == null) {
					Log.d(TAG, "Unable to pull latest after table reset.");
					return;
				}
			}

			JsonArray fetchList = result.getAsJsonArray("toFetch");
			JsonArray removeList = result.getAsJsonArray("toRemove");

			mFetchSize = fetchList.size();
			for (JsonElement element : fetchList) {
				pullReport(element.getAsString());
			}

			int removeListSize = removeList.size();
			for (JsonElement element : removeList) {
				removeReport(element.getAsString());
			}

			if (!newLatestId.equals(mAccessor.getLatestEvent())) {
				mAccessor.setLatestEvent(newLatestId);
			}

			if (mFetchSize == 0 && removeListSize == 0) {
				if (mListener != null) {
					mListener.onSyncComplete();
				}
			}
		}
	}

	private boolean validNewEventField(String latestId, String newLatestId) {
		boolean retValue = false;
		try {
			int currentId = Integer.valueOf(latestId);
			int newId = Integer.valueOf(newLatestId);

			if (newId > currentId || newId == currentId) {
				retValue = true;
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return retValue;
	}

	private synchronized void notifyPullComplete(int reportId) {
		mCurrentFetch += 1;
		if (mListener != null) {
			mListener.onSyncedReport(reportId);
			if (mCurrentFetch == mFetchSize) {
				mListener.onSyncComplete();
			}
		}
	}

	/**
	 * Set the sync status listener for this object.
	 * @param listener Instance of SyncStatusListener.
	 */
	public void setSyncStatusListener(SyncStatusListener listener) {
		mListener = listener;
	}

	/*
	 * This logic will need to change once we implement the ability to
	 * to modify a report remotely.
	 */
	private void pullReport(final String reportId) {
		if (!remoteReportExists(Integer.valueOf(reportId))) {
			JsonObject result = IonUtil.pullReport(mContext, reportId);
			if (result != null) {
				JsonElement statusElem = result.get("status");
				if (statusElem != null) {
					String status = statusElem.getAsString();
					if (status.equals("ok")) {
						Log.d(TAG, "Parsing Report: " + result.toString());
						createReportFromJson(result);
					} else {
						Log.d(TAG, "Bad status returned: " + status);
					}
				} else {
					Log.d(TAG, "Parsing Report: " + result.toString());
					createReportFromJson(result);
				}
			} else {
				Log.e(TAG, "Unable to pull a single report with id: " + reportId);
			}
		} else {
			Log.d(TAG, "Remote report already exists: " + reportId);
			notifyPullComplete(Integer.valueOf(reportId));
		}
	}

	private boolean remoteReportExists(int id) {
		List<Report> reports =
				mAccessor.getReportTable().queryForEq("remote_id", id);
		return reports.size() > 0;
	}

	private void createReportFromJson(final JsonObject json) {
		Report report = mAccessor.getReportTable().newReport();
		report.populate(json);
		report.setRemoteId(report.getId());
		report.setId(0);
		report.setDirty(false);
		report.setDraft(false);
		report.setRead(false);

		final KestrelWeather kestrelWeather = new KestrelWeather();
		if (json.getAsJsonObject("kestrel") != null) {
			kestrelWeather.populate(json.getAsJsonObject("kestrel"));
			mAccessor.getKestrelWeatherTable().create(kestrelWeather);
			report.setKestrelWeather(kestrelWeather);
		}

		final OpenWeather openWeather = new OpenWeather();
		if (json.getAsJsonObject("weather") != null) {
			openWeather.populate(json.getAsJsonObject("weather"));
			mAccessor.getOpenWeatherTable().create(openWeather);
			report.setOpenWeather(openWeather);
		}

		mAccessor.getReportTable().create(report);

		JsonArray jsonArray = json.getAsJsonArray("notes");
		if (jsonArray != null) {
			for (JsonElement jsonElement : jsonArray) {
				Note note = new Note();
				note.populate(jsonElement.getAsJsonObject());
				note.setReport(report);
				mAccessor.getNoteTable().create(note);
			}
		}

		JsonArray audioArray = json.getAsJsonArray("audio");
		packReportSupplement(report, SupplementType.AUDIO, audioArray);

		JsonArray imageArray = json.getAsJsonArray("images");
		packReportSupplement(report, SupplementType.PHOTO, imageArray);

		JsonArray videoArray = json.getAsJsonArray("video");
		packReportSupplement(report, SupplementType.VIDEO, videoArray);

		notifyPullComplete(report.getId());
	}

	private void packReportSupplement(Report report, SupplementType type, JsonArray jsonArray) {
		if (jsonArray != null) {
			for (JsonElement jElem : jsonArray) {
				JsonObject jsonObject = jElem.getAsJsonObject();
				if (jsonObject != null) {
					Supplement supplement = new Supplement();
					supplement.setType(type);
					supplement.setDirty(false);
					supplement.setReport(report);
					supplement.populate(jsonObject);
					mAccessor.getSupplementTable().create(supplement);
				}
			}
		}
	}

	private void removeReport(final String reportId) {
		int id = Integer.valueOf(reportId);
		Log.d(TAG, "Removing Report with id: " + id);
		//TODO: Not yet implemented remotely or locally
	}
}
