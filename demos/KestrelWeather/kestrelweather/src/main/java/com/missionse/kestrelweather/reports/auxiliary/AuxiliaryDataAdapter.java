package com.missionse.kestrelweather.reports.auxiliary;

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
 * Provides an adapter to manage the items in the auxiliary data list.
 */
public class AuxiliaryDataAdapter extends ArrayAdapter<AuxiliaryDataListItem> {
	private int mResource;

	/**
	 * Constructor.
	 * @param context The current context.
	 * @param resource The resource ID for a layout file containing a View to use when instantiating views.
	 * @param objects The initial objects in the adapter.
	 */
	public AuxiliaryDataAdapter(final Context context, final int resource, final List<AuxiliaryDataListItem> objects) {
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
			AuxiliaryDataListItem listItem = getItem(position);

			ImageView auxiliaryDataIcon = (ImageView) view.findViewById(R.id.auxiliary_data_item_icon);
			if (auxiliaryDataIcon != null) {
				auxiliaryDataIcon.setImageResource(listItem.getIcon());
			}

			TextView auxiliaryDataCount = (TextView) view.findViewById(R.id.auxiliary_data_item_count);
			if (auxiliaryDataCount != null) {
				auxiliaryDataCount.setText(Integer.toString(listItem.getCount()));
			}

			TextView auxiliaryDataType = (TextView) view.findViewById(R.id.auxiliary_data_item_type);
			if (auxiliaryDataType != null) {
				auxiliaryDataType.setText(listItem.getType());
			}
		}

		return view;
	}
}
