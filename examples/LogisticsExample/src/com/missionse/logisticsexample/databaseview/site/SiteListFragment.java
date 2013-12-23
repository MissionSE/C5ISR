package com.missionse.logisticsexample.databaseview.site;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.TextView;

import com.missionse.logisticsexample.R;
import com.missionse.logisticsexample.database.LocalDatabaseHelper;
import com.missionse.logisticsexample.model.Site;

/**
 * Displays a search-able list of Sites, with section headers for the first letter of each site name.
 */
public class SiteListFragment extends Fragment {

	private EditText mSearchField;
	private StickyListHeadersListView mEntryList;
	private SiteAdapter mSiteAdapter;
	private int mSelectedPosition = -1;
	private SiteViewerContainerFragment mContainer;
	private LocalDatabaseHelper mDatabaseHelper;

	/**
	 * Attaches a parent container to this fragment, to be called back on certain events.
	 * @param container the parent container
	 */
	public void setContainer(final SiteViewerContainerFragment container) {
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
	 * Retrieves the entry adapter, that provides the entries to list.
	 * @return an adapter
	 */
	public StickyListHeadersAdapter getEntryAdapter() {
		mSiteAdapter = new SiteAdapter(getActivity(), R.layout.list_entry, R.layout.list_entry_header);
		List<Site> sites = mDatabaseHelper.getSites();
		Collections.sort(sites);
		for (Site site : sites) {
			mSiteAdapter.add(site);
		}
		return mSiteAdapter;
	}

	/**
	 * Gets the watcher for a given text field (namely, the search text field).
	 * @return the text watcher
	 */
	public TextWatcher getTextWatcher() {
		return new TextWatcher() {
			@Override
			public void afterTextChanged(final Editable s) {
			}

			@Override
			public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {
			}

			@Override
			public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
				mSiteAdapter.getFilter().filter(s);
			}
		};
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup parent, final Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.fragment_site_database_list, parent, false);
		mSearchField = (EditText) contentView.findViewById(R.id.search_field);
		mSearchField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(final TextView v, final int actionId, final KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(
							Context.INPUT_METHOD_SERVICE);
					inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
					return true;
				}
				return false;
			}
		});

		mSearchField.addTextChangedListener(getTextWatcher());

		mEntryList = (StickyListHeadersListView) contentView.findViewById(R.id.entry_list);
		mEntryList.setAdapter(getEntryAdapter());
		mEntryList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
				mEntryList.setItemChecked(position, true);
				mSelectedPosition = position;
				mEntryList.invalidate();
				mContainer.displaySite(mSiteAdapter.getItem(position));
			}
		});

		return contentView;
	}

	/**
	 * Acts as a adapter displaying Sites, with sticky headers.
	 */
	private class SiteAdapter extends ArrayAdapter<Site> implements StickyListHeadersAdapter {
		private Context mContext;
		private int mEntryResource;
		private int mHeaderResource;

		private SiteNameFilter mSiteNameFilter;

		public SiteAdapter(final Context context, final int entryResource, final int headerResource) {
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

			String siteName = getItem(position).getName();
			TextView entryTitle = (TextView) convertView.findViewById(R.id.entry_title);

			String siteNameInLowerCase = siteName.toLowerCase(Locale.getDefault());
			SpannableString spannableSiteName = new SpannableString(siteName);
			if (listIsBeingFiltered()) {
				if (siteNameInLowerCase.contains(mSiteNameFilter.getConstraint())) {
					int startOfFoundConstraint = siteNameInLowerCase
							.indexOf(mSiteNameFilter.getConstraint().toString());
					int endOfFoundConstraint = startOfFoundConstraint + mSiteNameFilter.getConstraint().length();
					spannableSiteName.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), startOfFoundConstraint,
							endOfFoundConstraint, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					if (position != mSelectedPosition) {
						spannableSiteName.setSpan(
								new ForegroundColorSpan(mContext.getResources().getColor(R.color.holo_blue_dark)),
								startOfFoundConstraint, endOfFoundConstraint, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					}
				}
			}
			entryTitle.setText(spannableSiteName);
			return convertView;
		}

		private boolean listIsBeingFiltered() {
			return (mSiteNameFilter != null && mSiteNameFilter.getConstraint() != null && mSiteNameFilter
					.getConstraint().length() > 0);
		}

		@Override
		public View getHeaderView(final int position, View convertView, final ViewGroup parent) {
			if (convertView == null) {
				LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = vi.inflate(mHeaderResource, null);
			}

			String headerText = "" + getItem(position).getName().subSequence(0, 1).charAt(0);
			TextView headerTitle = (TextView) convertView.findViewById(R.id.header_title);
			headerTitle.setText(headerText);
			return convertView;
		}

		@Override
		public long getHeaderId(final int position) {
			return getItem(position).getName().subSequence(0, 1).charAt(0);
		}

		@Override
		public Filter getFilter() {
			if (mSiteNameFilter == null) {
				mSiteNameFilter = new SiteNameFilter(this);
			}
			return mSiteNameFilter;
		}

		public ArrayList<Site> getItems() {
			ArrayList<Site> allItems = new ArrayList<Site>();
			for (int index = 0; index < getCount(); index++) {
				allItems.add(getItem(index));
			}
			return allItems;
		}

		/**
		 * Extension of the Filter class, to filter on Site names.
		 */
		private class SiteNameFilter extends Filter {
			private ArrayList<Site> mOriginalValues;
			private SiteAdapter mAdapter;
			private CharSequence mConstraint;

			public SiteNameFilter(final SiteAdapter adapter) {
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
					ArrayList<Site> filteredSites = new ArrayList<Site>();

					synchronized (this) {
						for (Site site : mOriginalValues) {
							if (site.getName().toLowerCase(Locale.getDefault()).contains(constraint)) {
								filteredSites.add(site);
							}
						}
					}

					results.count = filteredSites.size();
					results.values = filteredSites;
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
				ArrayList<Site> filteredSites = (ArrayList<Site>) results.values;
				notifyDataSetChanged();
				clear();
				for (Site site : filteredSites) {
					add(site);
				}
				notifyDataSetInvalidated();
			}
		}
	}
}
