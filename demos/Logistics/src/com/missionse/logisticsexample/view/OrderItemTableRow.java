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
import com.missionse.logisticsexample.model.OrderItem;

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
	private int mOrderItemId = 0;
	private int mOrderItemNameId = 0;
	private double mOrderItemQuantity = 0.0;

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
	public OrderItemTableRow(Activity activity, OrderItem orderItem, List<ItemName> itemNames, OnTableRowDataSetChange dataSetChange) {
		super(activity);
		mActivity = activity;
		mItemNames = itemNames;
		mDataSetChange = dataSetChange;

		if (orderItem != null) {
			mOrderItemId = orderItem.getId();
			mOrderItemNameId = orderItem.getNameId();
			mOrderItemQuantity = orderItem.getQuantity();
		}

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
		populate();
		addView(view);
	}

	private void populate() {
		if (mOrderItemId > 0) {
			mEditText.setText(String.valueOf(mOrderItemQuantity));
			mRemoveButton.setVisibility(View.GONE);

			for (ItemName name : mItemNames) {
				if (name.getId() == mOrderItemNameId) {
					mSpinner.setSelection(mItemNames.indexOf(name));
				}
			}
		}
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

	/**
	 * Get the selected item name id.
	 * 
	 * @return The ID of the item name.
	 */
	public int getSelectedNameId() {
		return ((ItemName) mSpinner.getSelectedItem()).getId();
	}

	/**
	 * Get the selected item quantity.
	 * 
	 * @return The quantity of the selected item.
	 */
	public double getSelectedQuantity() {
		return Double.valueOf(mEditText.getText().toString().trim());
	}
}
