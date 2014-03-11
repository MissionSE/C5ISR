package com.missionse.kestrelweather.util;

import android.content.ContentResolver;
import android.net.Uri;

import com.missionse.kestrelweather.database.DatabaseAccessor;
import com.missionse.kestrelweather.database.model.SupplementType;
import com.missionse.kestrelweather.database.model.tables.Supplement;
import com.missionse.kestrelweather.reports.utils.UriMetadataProvider;

import org.joda.time.DateTime;

/**
 * Provides the functionality to build supplements.
 */
public final class SupplementBuilder {
	private SupplementBuilder() {
	}

	/**
	 * Create a Supplement entry into the database.
	 * @param databaseAccessor An accessor to the database.
	 * @param contentResolver The content resolver used to get the metadata.
	 * @param uri The string uri that points to the supplement.
	 * @param reportId The database report id associated with the supplement.
	 * @param type The type of supplement.
	 * @return returns the database ID of the newly created supplement.
	 */
	public static Supplement buildSupplement(final DatabaseAccessor databaseAccessor, final ContentResolver contentResolver,
			final Uri uri, final int reportId, final SupplementType type) {
		Supplement supplement = new Supplement();
		supplement.setType(type);
		supplement.setUri(uri.toString());
		supplement.setReport(databaseAccessor.getReportById(reportId));

		UriMetadataProvider uriMetadataProvider = new UriMetadataProvider(contentResolver, uri);
		supplement.setFileName(uriMetadataProvider.getName());
		supplement.setSize(uriMetadataProvider.getSize());
		supplement.setDate(new DateTime(uriMetadataProvider.getDateModified()));

		uriMetadataProvider.close();

		databaseAccessor.getSupplementTable().create(supplement);

		return supplement;
	}
}
