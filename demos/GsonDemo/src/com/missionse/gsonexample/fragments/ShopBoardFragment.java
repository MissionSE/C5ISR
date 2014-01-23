package com.missionse.gsonexample.fragments;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.missionse.gsonexample.R;
import com.missionse.gsonexample.adapters.ShopBoardItemAdapter;
import com.missionse.gsonexample.model.shopboard.ShopBoardResponse;
import com.missionse.gsonexample.util.HttpHandler;

/**
 * Displays a list of mShopBoardItems.
 */
public class ShopBoardFragment extends Fragment {

	private ArrayAdapter<ShopBoardResponse> mListAdapter;
	private final List<ShopBoardResponse> mShopBoardItems = new ArrayList<ShopBoardResponse>();

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.fragment_shopboard_list, container, false);

		ListView shopBoardItemList = (ListView) contentView.findViewById(R.id.shopboarditem_list);

		mListAdapter = new ShopBoardItemAdapter(getActivity(), mShopBoardItems);
		shopBoardItemList.setAdapter(mListAdapter);
		
		(new GetDataClass(this)).execute("");

		return contentView;
	}
	
	private void update(List<ShopBoardResponse> items) {
		mShopBoardItems.clear();
		mShopBoardItems.addAll(items);
		mListAdapter.notifyDataSetChanged();
	}
	
	/**
	 * Internal class/thread to handle the execution of http. 
	 */
	private class GetDataClass extends AsyncTask<String, String, String> {

		private ShopBoardFragment mActivity;
		private List<ShopBoardResponse> mItems = new ArrayList<ShopBoardResponse>();

		public GetDataClass(ShopBoardFragment activity) {
			this.mActivity = activity;
		}
		
		@SuppressWarnings("unchecked")
		@Override
		protected String doInBackground(String... params) {
			String jsonResponse = HttpHandler.getResponse("https://www.cs.drexel.edu/~gl89/serviceProvider.php");
			Gson gson = new Gson();
			Type listType = (new TypeToken<List<ShopBoardResponse>>() { }).getType();
			mItems.clear();
			mItems.addAll((ArrayList<ShopBoardResponse>) gson.fromJson(jsonResponse, listType));
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			mActivity.update(mItems);
		}
	}
}
