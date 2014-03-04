package com.missionse.kestrelweather.util;

import android.content.Context;

import com.missionse.kestrelweather.R;

/**
 *
 */
public final class UnitConverter {

	private UnitConverter() {
	}

	public static float celsiusToFahrenheit(float celsius) {
		return 32.0F + 9.0F * celsius / 5.0F;
	}

	public static float cmToInches(float cm) {
		return 0.393701F * cm;
	}

	public static float cmToMm(float cm) {
		return 10.0F * cm;
	}

	public static float fahrenheitToCelsius(float fahrenheit) {
		return 5.0F * (fahrenheit - 32.0F) / 9.0F;
	}

	public static float kmphToKnots(float kmph) {
		return 0.539957F * kmph;
	}

	public static float kmphToMph(float kmph) {
		return kmph / 1.609344F;
	}

	public static float kmphToMps(float kmph) {
		return kmph / 3.6F;
	}

	public static float mphToKmph(float mph) {
		return 1.609344F * mph;
	}

	public static float mphToMps(float mph) {
		return 1000.0F * (1.609344F * mph) / 3600.0F;
	}

	public static float mpsToKmph(float mps) {
		return 3.6F * mps;
	}

	public static float mpsToMph(float mps) {
		return 3.6F * mps / 1.609344F;
	}

	public static String degreeToCardinal(Context context, float degrees) {
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

}
