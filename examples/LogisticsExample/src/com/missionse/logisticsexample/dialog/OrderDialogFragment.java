package com.missionse.logisticsexample.dialog;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableLayout;

import com.missionse.logisticsexample.LogisticsExample;
import com.missionse.logisticsexample.R;
import com.missionse.logisticsexample.database.LocalDatabaseHelper;
import com.missionse.logisticsexample.model.ItemName;
import com.missionse.logisticsexample.model.Order;
import com.missionse.logisticsexample.model.Site;
import com.missionse.logisticsexample.view.OrderItemTableRow;

/**
 * This will be a dialog that a user can create and modify a Orders.
 */
public class OrderDialogFragment extends DialogFragment implements OnTableRowDataSetChange {

	private static final String SITE_ID = "site_id";
	private static final String ORDER_MODIFY = "modify_order";
	private static final String ORDER_ID = "order_id";
	private static final String ORDER_AT = "order_at";
	private static final String SEVERITY_ID = "severity_id";
	private static final String STATUS_ID = "status_id";

	private Activity mActivity;
	private boolean mModify;
	private int mSiteId;
	private int mSeverity;
	private int mStatus;
	private long mOrderAt;
	private TableLayout mTableLayout;
	private List<ItemName> mItemNames;
	private List<OrderItemTableRow> mTableRows = new ArrayList<OrderItemTableRow>();

	public static OrderDialogFragment newInstance(Site site) {
		OrderDialogFragment fragment = new OrderDialogFragment();
		Bundle bundle = new Bundle();
		bundle.putInt(SITE_ID, site.getId());
		bundle.putBoolean(ORDER_MODIFY, false);
		fragment.setArguments(bundle);

		return fragment;
	}

	public static OrderDialogFragment newInstance(Site site, Order order) {
		OrderDialogFragment fragment = new OrderDialogFragment();
		Bundle bundle = new Bundle();
		bundle.putInt(SITE_ID, site.getId());
		bundle.putBoolean(ORDER_MODIFY, true);
		bundle.putInt(ORDER_ID, site.getId());
		bundle.putLong(ORDER_AT, order.getTimeStamp().getMillis());
		bundle.putInt(SEVERITY_ID, order.getSeverityId());
		bundle.putInt(STATUS_ID, order.getStatusId());

		fragment.setArguments(bundle);

		return fragment;
	}

	@Override
	public void onAttach(final Activity activity) {
		super.onAttach(activity);
		mActivity = activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mActivity = null;
	}

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		updateState(savedInstanceState);
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		updateState(savedInstanceState);
		updateItemNames();
		View view = inflater.inflate(R.layout.fragment_order_dialog, container);
		setupTable(view);
		setupButtons(view);
		setupTitle();

		return view;
	}

	private void updateItemNames() {
		mItemNames = new LinkedList<ItemName>();
		try {
			LocalDatabaseHelper database = ((LogisticsExample) mActivity).getDatabaseHelper();
			mItemNames.addAll(database.getItemNames());
		} catch (ClassCastException exception) {
			exception.printStackTrace();
		}
	}

	private void setupTable(View root) {
		mTableLayout = (TableLayout) root.findViewById(R.id.item_table);

		addBlankRow();
		addBlankRow();
	}

	private void addRowToTable(OrderItemTableRow row) {
		mTableLayout.addView(row);
		mTableRows.add(row);
	}

	private OrderItemTableRow createEmptyRow() {
		OrderItemTableRow row = new OrderItemTableRow(mActivity, mItemNames, this);
		return row;
	}

	private void setupTitle() {
		getDialog().getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		if (mModify) {
			getDialog().setTitle(R.string.title_order_modify);
		} else {
			getDialog().setTitle(R.string.title_order_new);
		}
	}

	private void updateState(Bundle state) {
		if (state != null) {
			if (state.getBoolean(ORDER_MODIFY)) {
				mModify = true;
				mSiteId = state.getInt(SITE_ID);
				mOrderAt = state.getLong(ORDER_AT);
				mStatus = state.getInt(STATUS_ID);
				mSeverity = state.getInt(SEVERITY_ID);
			} else {
				mModify = false;
				mSiteId = state.getInt(SITE_ID);
			}
		}
	}

	private void setupButtons(View root) {
		Button accept = (Button) root.findViewById(R.id.ok_button);
		Button cancel = (Button) root.findViewById(R.id.cancel_button);
		ImageButton addrow = (ImageButton) root.findViewById(R.id.add_order_row);

		if (mModify) {
			accept.setText(R.string.update);
		} else {
			accept.setText(R.string.create);
		}

		accept.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onAccept();
			}

		});
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				OrderDialogFragment.this.dismiss();
			}
		});
		addrow.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				addBlankRow();
			}
		});
	}

	private void addBlankRow() {
		addRowToTable(createEmptyRow());
	}

	private void onAccept() {

	}

	@Override
	public void onRemove(OrderItemTableRow row) {
		mTableRows.remove(row);
		mTableLayout.removeView(row);
	}

}
