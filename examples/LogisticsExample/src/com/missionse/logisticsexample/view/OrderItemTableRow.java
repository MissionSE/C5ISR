/**
 * 
 */
package com.missionse.logisticsexample.view;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TableRow;

import com.missionse.logisticsexample.R;
import com.missionse.logisticsexample.dialog.OnTableRowDataSetChange;
import com.missionse.logisticsexample.model.ItemName;

/**
 * Custom layout for displaying OrderItem data.
 */
public class OrderItemTableRow extends TableRow {

	private Activity mActivity;
	private List<ItemName> mItemNames;
	private Spinner mSpinner;
	private EditText mEditText;
	private ImageButton mRemoveButton;
	private OnTableRowDataSetChange mDataSetChange;

	/**
	 * Constructor.
	 * 
	 * @param activity
	 *            The base activity that this fragment comes from.
	 * @param itemNames
	 *            The list of names for items.
	 * @param dataSetChange
	 *            The base Table.
	 */
	public OrderItemTableRow(Activity activity, List<ItemName> itemNames, OnTableRowDataSetChange dataSetChange) {
		super(activity);
		mActivity = activity;
		mItemNames = itemNames;
		mDataSetChange = dataSetChange;
		init();
	}

	private void init() {
		View view = mActivity.getLayoutInflater().inflate(R.layout.orderitem_table_row, null, false);

		mEditText = (EditText) view.findViewById(R.id.item_quanity_et);
		mRemoveButton = (ImageButton) view.findViewById(R.id.remove_item_row);
		mRemoveButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				remove();
			}
		});
		setupParams();
		setupSpinner(view);
		addView(view);
	}

	private void setupParams() {
		TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
		params.span = 1;
		setLayoutParams(params);
	}

	private void setupSpinner(View root) {
		mSpinner = (Spinner) root.findViewById(R.id.item_name_spinner);
		mSpinner.setAdapter(new ArrayAdapter<ItemName>(mActivity, android.R.layout.simple_list_item_1, mItemNames));
	}

	private void remove() {
		mDataSetChange.onRemove(this);
	}


}
