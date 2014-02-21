package com.missionse.kestrelweather.reports.readings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.missionse.kestrelweather.R;

import java.util.List;

/**
 * Provides an adapter to manage the items in the readings list.
 */
public class ReadingsAdapter extends ArrayAdapter<ReadingsListItem> {
	private int mResource;

	/**
	 * Constructor.
	 * @param context The current context.
	 * @param resource The resource ID for a layout file containing a View to use when instantiating views.
	 * @param objects The initial objects in the adapter.
	 */
	public ReadingsAdapter(final Context context, final int resource, final List<ReadingsListItem> objects) {
		super(context, resource, objects);
		mResource = resource;
	}

	@Override
	public View getView(int position, View view, ViewGroup viewGroup) {
		if (view == null) {
			LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = layoutInflater.inflate(mResource, null);
		}

		if (view != null) {
			ReadingsListItem listItem = getItem(position);

			ImageView readingIcon = (ImageView) view.findViewById(R.id.readings_item_icon);
			if (readingIcon != null) {
				readingIcon.setImageResource(listItem.getIcon());
			}

			TextView readingValue = (TextView) view.findViewById(R.id.readings_item_value);
			if (readingValue != null) {
				readingValue.setText(listItem.getReading());
			}

			TextView readingType = (TextView) view.findViewById(R.id.readings_item_type);
			if (readingType != null) {
				readingType.setText(listItem.getType());
			}

			TextView readingUnits = (TextView) view.findViewById(R.id.readings_item_units);
			if (readingUnits != null) {
				readingUnits.setText(listItem.getUnits());
			}
		}

		return view;
	}
}
