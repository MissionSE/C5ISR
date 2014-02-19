package com.missionse.kestrelweather.reports.utils;

/**
 * Util class to help converting run times into real time.
 */
public final class MediaTimeConverter {
	private static final int MILLI_TO_SEC = 1000;
	private static final int MILLI_TO_MIN = 60000;

	private MediaTimeConverter() {
	}

	/**
	 * Extract the amount of seconds from the given parameter.
	 * @param time The run time in milliseconds.
	 * @return The amount of seconds. (long)
	 */
	public static long extractSeconds(long time) {
		return (time % MILLI_TO_MIN) / MILLI_TO_SEC;
	}

	/**
	 * Extract the amount of seconds from the given parameter.
	 * @param time The run time in milliseconds.
	 * @return The amount of seconds. (string)
	 */
	public static String extractSecondsAsString(long time) {
		return String.valueOf(extractSeconds(time));
	}

	/**
	 * Extract the amount of minutes from the given parameter.
	 * @param time The run time in milliseconds.
	 * @return The amount of minutes. (long)
	 */
	public static long extractMinutes(long time) {
		return time / MILLI_TO_MIN;
	}

	/**
	 * Extract the amount of minutes from the given parameter.
	 * @param time The run time in milliseconds.
	 * @return The amount of minutes. (string)
	 */
	public static String extractMinutesAsString(long time) {
		return String.valueOf(extractMinutes(time));
	}

	/**
	 * Converts the time in milliseconds to a more human readable format.
	 * Format: 00:00
	 * @param time The run time in milliseconds.
	 * @return Human readable time. (String)
	 */
	public static String extractRunTime(long time) {
		String seconds = extractSecondsAsString(time);
		String minutes = extractMinutesAsString(time);
		if (seconds.length() == 1) {
			return "0" + minutes + ":0" + seconds;
		} else {
			return "0" + minutes + ":" + seconds;
		}
	}
}
