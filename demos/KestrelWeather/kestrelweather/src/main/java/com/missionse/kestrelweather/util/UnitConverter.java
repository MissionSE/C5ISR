package com.missionse.kestrelweather.util;

import android.content.Context;

import com.missionse.kestrelweather.R;

/**
 * Provides utilities to convert between units.
 */
public final class UnitConverter {
	//Disable Checks: MagicNumber
	private UnitConverter() {
	}

	/**
	 * Converts Kelvin into Celsius.
	 * @param kelvin The value in Kelvin.
	 * @return The value in Celsius.
	 */
	public static float kelvinToCelsius(final float kelvin) {
		return kelvin - 272.15f;
	}

	/**
	 * Converts Celsius into Fahrenheit.
	 * @param celsius The value in Celsius.
	 * @return The value in Fahrenheit.
	 */
	public static float celsiusToFahrenheit(final float celsius) {
		return 32.0F + 9.0F * celsius / 5.0F;
	}

	/**
	 * Converts Pascal to Kilo-pascal.
	 * @param pascal The value in Pascals.
	 * @return The value in Kilo-pascals.
	 */
	public static float pascalToKilopascal(final float pascal) {
		return pascal * 0.1f;
	}

	/**
	 * Converts Kilo-pascals into Milli-bars.
	 * @param kilopascal The value in Kilo-pascals.
	 * @return The value in Milli-bars.
	 */
	public static float kilopascalToMillibar(final float kilopascal) {
		return kilopascal * 10.0f;
	}

	/**
	 * Converts Kilo-pascals into Inches of Mercury.
	 * @param kilopascal The value in Kilo-pascals.
	 * @return The value in Inches of Mercury.
	 */
	public static float kilopascalToInchesOfMercury(final float kilopascal) {
		return kilopascal * 0.295299830714f;
	}

	/**
	 * Converts Centimeters into Inches.
	 * @param cm The value in Centimeters.
	 * @return The value in Inches.
	 */
	public static float cmToInches(final float cm) {
		return 0.393701F * cm;
	}

	/**
	 * Converts Centimeters into Millimeters.
	 * @param cm The value in Centimeters.
	 * @return The value in Millimeters.
	 */
	public static float cmToMm(final float cm) {
		return 10.0F * cm;
	}

	/**
	 * Converts Fahrenheit into Celsius.
	 * @param fahrenheit The value of Fahrenheit.
	 * @return The value in Celsius.
	 */
	public static float fahrenheitToCelsius(final float fahrenheit) {
		return 5.0F * (fahrenheit - 32.0F) / 9.0F;
	}

	/**
	 * Converts Kilometers per hour into Knots.
	 * @param kmph The value in Kilometers per hour.
	 * @return The value in Knots.
	 */
	public static float kmphToKnots(final float kmph) {
		return 0.539957F * kmph;
	}

	/**
	 * Converts Kilometers per hour into Miles per hour.
	 * @param kmph The value in Kilometers per hour.
	 * @return The value in Miles per hour.
	 */
	public static float kmphToMph(final float kmph) {
		return kmph / 1.609344F;
	}

	/**
	 * Converts Kilometers per hour into Meters per second.
	 * @param kmph The value in Kilometers per hour.
	 * @return The value in Meters per second.
	 */
	public static float kmphToMps(final float kmph) {
		return kmph / 3.6F;
	}

	/**
	 * Converts Miles per hour into Kilometers per hour.
	 * @param mph The value in Miles per hour.
	 * @return The value in Kilometers per hour.
	 */
	public static float mphToKmph(final float mph) {
		return 1.609344F * mph;
	}

	/**
	 * Converts Miles per hour into Meters per second.
	 * @param mph The value in Miles per hour.
	 * @return The value in Meters per second.
	 */
	public static float mphToMps(final float mph) {
		return 1000.0F * (1.609344F * mph) / 3600.0F;
	}

	/**
	 * Converts Meters per second into Kilometers per hour.
	 * @param mps The value in Meters per second.
	 * @return The value in Kilometers per hour.
	 */
	public static float mpsToKmph(final float mps) {
		return 3.6F * mps;
	}

	/**
	 * Converts Meters per second into Miles per hour.
	 * @param mps The value in Meters per second.
	 * @return The value in Miles per hour.
	 */
	public static float mpsToMph(final float mps) {
		return 3.6F * mps / 1.609344F;
	}

	/**
	 * Converts Meters per second into Knots.
	 * @param mps The value in Meters per second.
	 * @return The value in Knots.
	 */
	public static float mpsToKnots(final float mps) {
		return 1.9438444924406f * mps;
	}

	/**
	 * Converts Degrees into Cardinal directions.
	 * @param context The current context.
	 * @param degrees The value in degrees.
	 * @return The value in Cardinal directions.
	 */
	public static String degreeToCardinal(final Context context, final float degrees) {
		if ((degrees >= 0.0F) && (degrees < 22.5F)) {
			return context.getString(R.string.N);
		}
		if ((degrees >= 22.5F) && (degrees < 45.0F)) {
			return context.getString(R.string.NNE);
		}
		if ((degrees >= 45.0F) && (degrees < 67.5F)) {
			return context.getString(R.string.NE);
		}
		if ((degrees >= 67.5F) && (degrees < 90.0F)) {
			return context.getString(R.string.ENE);
		}
		if ((degrees >= 90.0F) && (degrees < 112.5F)) {
			return context.getString(R.string.E);
		}
		if ((degrees >= 112.5F) && (degrees < 135.0F)) {
			return context.getString(R.string.ESE);
		}
		if ((degrees >= 135.0F) && (degrees < 157.5F)) {
			return context.getString(R.string.SE);
		}
		if ((degrees >= 157.5F) && (degrees < 180.0F)) {
			return context.getString(R.string.SSE);
		}
		if ((degrees >= 180.0F) && (degrees < 202.5F)) {
			return context.getString(R.string.S);
		}
		if ((degrees >= 202.5F) && (degrees < 225.0F)) {
			return context.getString(R.string.SSW);
		}
		if ((degrees >= 225.0F) && (degrees < 247.5F)) {
			return context.getString(R.string.SW);
		}
		if ((degrees >= 247.5F) && (degrees < 270.0F)) {
			return context.getString(R.string.WSW);
		}
		if ((degrees >= 270.0F) && (degrees < 292.5F)) {
			return context.getString(R.string.W);
		}
		if ((degrees >= 292.5F) && (degrees < 315.0F)) {
			return context.getString(R.string.WNW);
		}
		if ((degrees >= 315.0F) && (degrees < 337.5F)) {
			return context.getString(R.string.NW);
		}
		if ((degrees >= 337.5F) && (degrees < 360.0F)) {
			return context.getString(R.string.NNW);
		}
		return "";
	}
	//Enable Checks: MagicNumber
}
