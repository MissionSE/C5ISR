package com.missionse.logisticsexample.databaseview.order;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import com.missionse.logisticsexample.R;
import com.missionse.logisticsexample.database.LocalDatabaseHelper;
import com.missionse.logisticsexample.model.Order;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Displays a search-able list of Orders, with section headers dependent on sort criteria.
 */
public class OrderListFragment extends Fragment {

	/**
	 * The type of sorts.
	 */
	public enum SortType {
		DEFAULT, SEVERITY, STATUS, SITE
	}

	private StickyListHeadersListView mEntryList;
	private OrderAdapter mOrderAdapter;
	private int mSelectedPosition = -1;
	private OrderViewerContainerFragment mContainer;
	private LocalDatabaseHelper mDatabaseHelper;
	private SortType mCurrentSortType = SortType.DEFAULT;

	/**
	 * Attaches a parent container to this fragment, to be called back on certain events.
	 * @param container the parent container
	 */
	public void setContainer(final OrderViewerContainerFragment container) {
		mContainer = container;
	}

	/**
	 * Sets the DatabaseHelper, allowing database access.
	 * @param databaseHelper the database helper
	 */
	public void setDatabaseHelper(final LocalDatabaseHelper databaseHelper) {
		mDatabaseHelper = databaseHelper;
	}

	/**
	 * Gets the entry adapter.
	 * @return The adapter.
	 */
	public StickyListHeadersAdapter getEntryAdapter() {
		mOrderAdapter = new OrderAdapter(getActivity(), R.layout.list_entry, R.layout.list_entry_header);
		List<Order> orders = mDatabaseHelper.getOrders();
		sortOrders(orders);
		for (Order order : orders) {
			mOrderAdapter.add(order);
		}
		return mOrderAdapter;
	}

	private void sortOrders(final List<Order> orders) {
		switch (mCurrentSortType) {
			case DEFAULT:
				Collections.sort(orders);
				break;
			case SEVERITY:
				Collections.sort(orders, new OrderSeverityComparator(mDatabaseHelper));
				break;
			case STATUS:
				Collections.sort(orders, new OrderStatusComparator(mDatabaseHelper));
				break;
			case SITE:
				Collections.sort(orders, new OrderSiteComparator(mDatabaseHelper));
				break;
			default:
				break;
		}
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup parent, final Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.fragment_order_database_list, parent, false);
		Button sortByButton = (Button) contentView.findViewById(R.id.list_sort_by_btn);
		sortByButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				// TODO Auto-generated method stub

			}

		});
		Button filterByButton = (Button) contentView.findViewById(R.id.list_filter_by_btn);
		filterByButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				// TODO Auto-generated method stub

			}

		});

		mEntryList = (StickyListHeadersListView) contentView.findViewById(R.id.entry_list);
		mEntryList.setAdapter(getEntryAdapter());
		mEntryList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
				mEntryList.setItemChecked(position, true);
				mSelectedPosition = position;
				mEntryList.invalidate();
				mContainer.displayOrder(mOrderAdapter.getItem(position));
			}
		});

		return contentView;
	}

	/**
	 * Acts as a adapter displaying Sites, with sticky headers.
	 */
	private class OrderAdapter extends ArrayAdapter<Order> implements StickyListHeadersAdapter {
		private Context mContext;
		private int mEntryResource;
		private int mHeaderResource;

		private OrderFilter mOrderFilter;

		public OrderAdapter(final Context context, final int entryResource, final int headerResource) {
			super(context, entryResource);
			mContext = context;
			mEntryResource = entryResource;
			mHeaderResource = headerResource;
		}

		@Override
		public View getView(final int position, View convertView, final ViewGroup parent) {
			if (convertView == null) {
				LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = vi.inflate(mEntryResource, null);
			}

			String orderId = getItem(position).toString();
			TextView entryTitle = (TextView) convertView.findViewById(R.id.entry_title);
			entryTitle.setText(orderId);
			return convertView;
		}

		@Override
		public View getHeaderView(final int position, View convertView, final ViewGroup parent) {
			if (convertView == null) {
				LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = vi.inflate(mHeaderResource, null);
			}

			String headerText = "" + getItem(position).toString().subSequence(0, 1).charAt(0);
			TextView headerTitle = (TextView) convertView.findViewById(R.id.header_title);
			headerTitle.setText(headerText);
			return convertView;
		}

		@Override
		public long getHeaderId(final int position) {
			return getItem(position).toString().subSequence(0, 1).charAt(0);
		}

		@Override
		public Filter getFilter() {
			if (mOrderFilter == null) {
				mOrderFilter = new OrderFilter(this);
			}
			return mOrderFilter;
		}

		public ArrayList<Order> getItems() {
			ArrayList<Order> allItems = new ArrayList<Order>();
			for (int index = 0; index < getCount(); index++) {
				allItems.add(getItem(index));
			}
			return allItems;
		}

		/**
		 * Extension of the Filter class, to filter on types of Orders.
		 * 
		 * There are several types of filters:
		 * By Status: asdasd,
		 * By Severity: asdasd,
		 * By Item type: asdasd,
		 */
		private class OrderFilter extends Filter {
			private ArrayList<Order> mOriginalValues;
			private OrderAdapter mAdapter;
			private CharSequence mConstraint;

			public OrderFilter(final OrderAdapter adapter) {
				super();
				mAdapter = adapter;
			}

			public CharSequence getConstraint() {
				return mConstraint;
			}

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				mConstraint = constraint;
				FilterResults results = new FilterResults();

				if (mOriginalValues == null) {
					mOriginalValues = mAdapter.getItems();
				}

				if (constraint != null && constraint.toString().length() > 0) {
					constraint = constraint.toString().toLowerCase(Locale.getDefault());
					ArrayList<Order> filteredOrders = new ArrayList<Order>();

					synchronized (this) {
						for (Order order : mOriginalValues) {
							if (constraint.equals("")) {
								if (true) {
									filteredOrders.add(order);
								}
							}

						}
					}

					results.count = filteredOrders.size();
					results.values = filteredOrders;
				} else {
					synchronized (this) {
						results.count = mOriginalValues.size();
						results.values = mOriginalValues;
					}
				}
				return results;
			}

			@Override
			protected void publishResults(final CharSequence constraint, final FilterResults results) {
				@SuppressWarnings("unchecked")
				ArrayList<Order> filteredOrders = (ArrayList<Order>) results.values;
				notifyDataSetChanged();
				clear();
				for (Order order : filteredOrders) {
					add(order);
				}
				notifyDataSetInvalidated();
			}
		}
	}
}
