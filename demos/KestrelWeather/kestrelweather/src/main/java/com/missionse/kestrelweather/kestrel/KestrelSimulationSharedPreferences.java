package com.missionse.kestrelweather.kestrel;

/**
 * Defines String constants as keys for saving preferences for the Kestrel Simulator.
 */
public final class KestrelSimulationSharedPreferences {

	private KestrelSimulationSharedPreferences() {

	}

	public static final String SIMULATION_PREFERENCES = "KestrelSimulationPreferences";

	public static final String KESTREL_TEMPERATURE = "KestrelTemperature";
	public static final String KESTREL_HUMIDITY = "KestrelHumidity";
	public static final String KESTREL_PRESSURE = "KestrelPressure";
	public static final String KESTREL_PRESSURE_TREND = "KestrelPressureTrend";
	public static final String KESTREL_HEAT_IDX = "KestrelHeatIndex";
	public static final String KESTREL_WIND_SPD = "KestrelWindSpeed";
	public static final String KESTREL_WIND_DIR = "KestrelWindDirection";
	public static final String KESTREL_WIND_CHILL = "KestrelWindChill";
	public static final String KESTREL_DEW_PT = "KestrelDewPoint";

	public static final float NONSENSE_FLOAT = 0.0f;
	public static final int NONSENSE_INT = 0;
}
