package com.missionse.kestrelweather.reports.utils;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Util class to help with zipping/unzipping zip files.
 */
public class ZipManager {
	private static final String TAG = ZipManager.class.getSimpleName();
	private static final int BUFFER = 2048;

	private String mZipFile;
	private List<String> mFilesToZip;

	/**
	 * Constructor.
	 * @param zipFile The absolute path to zip file.
	 */
	public ZipManager(String zipFile) {
		mZipFile = zipFile;
		mFilesToZip = new LinkedList<String>();
	}

	/**
	 * Constructor.
	 * @param zipFile The absolute path to zip file.
	 */
	public ZipManager(File zipFile) {
		this(zipFile.getAbsolutePath());
	}

	/**
	 * Add a file that needs to be zipped.
	 * @param fileToZip File to be zipped.
	 */
	public void addFile(File fileToZip) {
		mFilesToZip.add(fileToZip.getAbsolutePath());
	}

	/**
	 * Add a file that needs to be zipped.
	 * @param fileToZip The absoulte path to the file that needs to be zipped.
	 */
	public void addFile(String fileToZip) {
		mFilesToZip.add(fileToZip);
	}

	/**
	 * Add a file that needs to be zipped.
	 * @param fileToZip Uri of the file to be zipped.
	 */
	public void addFile(Uri fileToZip) {
		addFile(new File(fileToZip.getPath()));
	}

	/**
	 * Add files that need to be zipped.
	 * @param filesToZip List<File>s to be zipped.
	 */
	public void addFiles(List<File> filesToZip) {
		for (File file : filesToZip) {
			addFile(file);
		}
	}

	/**
	 * Add files that need to be zipped.
	 * @param filesToZip List of files that need to be zipped.
	 */
	public void addStringFiles(List<String> filesToZip) {
		for (String file : filesToZip) {
			addFile(file);
		}
	}

	/**
	 * Add files that need to be zipped.
	 * @param filesToZip List of files that need to be zipped.
	 */
	public void addUriFiles(List<Uri> filesToZip) {
		for (Uri file : filesToZip) {
			addFile(file);
		}
	}

	/**
	 * Zip the contents.
	 */
	public void zip() {
		try {
			BufferedInputStream origin = null;
			FileOutputStream dest = new FileOutputStream(mZipFile);
			ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
			byte data[] = new byte[BUFFER];

			for (String file : mFilesToZip) {
				Log.v(TAG, mZipFile + " - Adding: " + file);
				FileInputStream fi = new FileInputStream(file);
				origin = new BufferedInputStream(fi, BUFFER);
				ZipEntry entry = new ZipEntry(file.substring(file.lastIndexOf("/") + 1));
				out.putNextEntry(entry);
				int count;
				while ((count = origin.read(data, 0, BUFFER)) != -1) {
					out.write(data, 0, count);
				}
				origin.close();
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Unzip the contents of the specified zip file
	 * @param targetDir The directory the contents will be unzipped too.
	 */
	public void unzip(File targetDir) {

		if (targetDir != null && targetDir.exists() && targetDir.isDirectory()) {
			try {
				FileInputStream source = new FileInputStream(mZipFile);
				ZipInputStream zipStream = new ZipInputStream(source);
				ZipEntry entry = null;
				while ((entry = zipStream.getNextEntry()) != null) {
					if (entry.isDirectory()) {
						dirChecker(entry.getName());
					} else {
						FileOutputStream dest = new FileOutputStream(targetDir.getAbsolutePath() + entry.getName());
						for (int c = zipStream.read(); c != -1; c = zipStream.read()) {
							dest.write(c);
						}
						zipStream.closeEntry();
						dest.close();
					}
				}
				zipStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void dirChecker(String dir) {
		File file = new File(dir);
		if (!file.isDirectory()) {
			file.mkdirs();
		}
	}
}
