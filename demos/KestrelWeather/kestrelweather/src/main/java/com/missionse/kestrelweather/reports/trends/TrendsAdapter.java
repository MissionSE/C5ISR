package com.missionse.kestrelweather.reports.trends;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.missionse.kestrelweather.R;
import com.missionse.kestrelweather.preferences.UnitPrefs;

/**
 * Provides an adapter for the trends.
 */
public class TrendsAdapter extends ArrayAdapter<CharSequence> {
	private Context mContext;
	private int mResource;
	private CharSequence mTemperatureTrend;
	private CharSequence mPressureTrend;

	/**
	 * Constructor.
	 * @param context The current context.
	 * @param resource The resource ID for a layout file containing a layout to use when instantiating views.
	 * @param objects The objects to represent in the ListView.
	 */
	public TrendsAdapter(final Context context, final int resource, CharSequence[] objects) {
		super(context, resource, objects);
		mContext = context;
		mResource = resource;

		mTemperatureTrend = mContext.getString(R.string.kestrel_simulation_temperature);
		mPressureTrend = mContext.getString(R.string.kestrel_simulation_pressure);
	}

	@Override
	public View getView(final int position, View view, ViewGroup viewGroup) {
		return getCustomView(position, view);
	}

	@Override
	public View getDropDownView(final int position, View view, ViewGroup viewGroup) {
		return getCustomView(position, view);
	}

	private View getCustomView(final int position, View view) {
		if (view == null) {
			LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = layoutInflater.inflate(mResource, null);
		}

		if (view != null) {
			CharSequence units = "";
			CharSequence trend = getItem(position);
			if (trend.equals(mTemperatureTrend)) {
				units = UnitPrefs.getPreferredTemperatureUnit(mContext);
			} else if (trend.equals(mPressureTrend)) {
				units = UnitPrefs.getPreferredPressureUnit(mContext);
			}
			((TextView) view).setText(trend + " (" + units + ")");
		}

		return view;
	}
}
