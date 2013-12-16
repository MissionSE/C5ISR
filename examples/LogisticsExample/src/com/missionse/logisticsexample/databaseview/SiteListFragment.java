package com.missionse.logisticsexample.databaseview;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.missionse.logisticsexample.R;
import com.missionse.logisticsexample.model.Site;

/**
 * Displays a search-able list of Sites, with section headers for the first letter of each site name.
 */
public class SiteListFragment extends DatabaseEntryListFragment {

	private SiteAdapter mSiteAdapter;

	private static final String LEXICON = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	private static final java.util.Random RANDOM = new java.util.Random();
	private static final int NAME_LENGTH = 5;
	private static final int MAX_NAMES = 30;

	private static String randomIdentifier() {
		StringBuilder builder = new StringBuilder();
		while (builder.toString().length() == 0) {
			int length = RANDOM.nextInt(NAME_LENGTH) + NAME_LENGTH;
			for (int i = 0; i < length; i++) {
				builder.append(LEXICON.charAt(RANDOM.nextInt(LEXICON.length())));
			}
		}
		return builder.toString();
	}

	@Override
	public void onSearchInitiated() {
		// TODO Auto-generated method stub

	}

	@Override
	public StickyListHeadersAdapter getEntryAdapter() {
		mSiteAdapter = new SiteAdapter(getActivity(), R.layout.list_entry, R.layout.list_entry_header);

		List<String> siteNames = new ArrayList<String>();
		for (int count = 0; count < MAX_NAMES; count++) {
			siteNames.add(randomIdentifier());

		}
		Collections.sort(siteNames);
		for (String name : siteNames) {
			Site site = new Site();
			site.setName(name);
			mSiteAdapter.add(site);
		}
		return mSiteAdapter;
	}

	@Override
	public void onItemSelected(final int position) {
		// TODO Auto-generated method stub

	}

	/**
	 * Acts as a adapter displaying Sites, with sticky headers.
	 */
	private class SiteAdapter extends ArrayAdapter<Site> implements StickyListHeadersAdapter {
		private Context mContext;
		private int mEntryResource;
		private int mHeaderResource;

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
			entryTitle.setText(siteName);
			return convertView;
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
	}
}
