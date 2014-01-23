package com.missionse.gsonexample.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.missionse.gsonexample.R;
import com.missionse.gsonexample.model.shopboard.ShopBoardItem;
import com.missionse.gsonexample.model.shopboard.ShopBoardResponse;
/**
 * List adapter for displaying shop board items. 
 * @author rvieras
 *
 */
public class ShopBoardItemAdapter extends ArrayAdapter<ShopBoardResponse> {
	
	private List<ShopBoardResponse> mData = new ArrayList<ShopBoardResponse>();
	private int mLayoutId;

	/**
	 * Constructor.
	 * @param context - base context
	 * @param pItems - initial list of shop board items
	 */
	public ShopBoardItemAdapter(final Context context, final List<ShopBoardResponse> pItems) {
		super(context, R.layout.list_entry, pItems);
		mLayoutId = R.layout.list_entry;
		mData = pItems;
	}

	@Override
	public View getView(final int position, View convertView, final ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = vi.inflate(mLayoutId, null);
		}
		ShopBoardItem item = mData.get(position).getItem();
		if (item != null) {
			TextView action = (TextView) convertView.findViewById(R.id.item_list_entry_name);
			action.setText(item.getItemName());
			TextView action1 = (TextView) convertView.findViewById(R.id.item_list_entry_price);
			action1.setText("$" + item.getPrice());
			TextView action2 = (TextView) convertView.findViewById(R.id.item_list_entry_description);
			action2.setText(item.getDescription());
		}
		return convertView;
	}

}
