package com.missionse.kestrelweather.reports.utils;


import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Date;


/**
 * Util class to help handle file management.
 */
public final class FileManager {
	private static final String LOG_TAG = "FileManager";
	private static final String EXTERNAL_STORAGE_BASE = Environment.getExternalStorageDirectory().getAbsolutePath();
	private static final String PROJECT_ROOT = "/.kestrelweather/";

	private FileManager() {
	}

	/**
	 * Creates a new directory for a report to link too.
	 *
	 * @return The Uri associated with the new directory.
	 */
	public static Uri createReportDirectory() {
		Date today = new Date();
		String reportDirString = getProjectRoot() + "/" + today.getTime();
		File reportDir = new File(reportDirString);
		mkdir(reportDir);
		return Uri.fromFile(reportDir);
	}

	/**
	 * Obtain a File[] that has all files in the reportDir that passes the filter.
	 *
	 * @param reportDir The base report directory to look in.
	 * @param filter The filter that determine what files we are looking for. this parm cannot be null
	 * @return A File[] with all accepted files from reportDir.
	 */
	public static File[] getFiles(Uri reportDir, FilenameFilter filter) {
		if (filter == null) {
			throw new IllegalArgumentException("FilenameFilter cannot be null");
		}

		File rootDir = new File(reportDir.getPath());
		return rootDir.listFiles(filter);
	}

	/**
	 * Obtain a File[] that has all files in the reportDir that passes the filter.
	 *
	 * @param filter The filter that determine what files we are looking for. this parm cannot be null
	 * @return A File[] with all accepted files from reportDir.
	 */
	public static File[] getTestFiles(FilenameFilter filter) {
		if (filter == null) {
			throw new IllegalArgumentException("FilenameFilter cannot be null");
		}

		File rootDir = new File(getTestDir().getPath());
		return rootDir.listFiles(filter);
	}

	private static String getProjectRoot() {
		String projectRoot = EXTERNAL_STORAGE_BASE + PROJECT_ROOT;
		createProjectRoot(projectRoot);
		return projectRoot;
	}

	private static void createProjectRoot(String projectRoot) {
		mkdir(new File(projectRoot));
	}

	private static void mkdir(File file) {
		if (!file.mkdirs()) {
			Log.d(LOG_TAG, "Unable to create directory (" + file.getAbsoluteFile() + ")");
		}
	}

	private static File getTestDir() {
		String testDirString = EXTERNAL_STORAGE_BASE + PROJECT_ROOT + "/test" ;
		File testDir = new File(testDirString);
		mkdir(testDir);
		return testDir;
	}

}