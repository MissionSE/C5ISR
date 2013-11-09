package com.missionse.nfcexample;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MessageLogFragment extends Fragment {

	private List<String> messages;
	private ListView messageList;

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_message_log, null);
		messages = new ArrayList<String>();
		messageList = (ListView) view.findViewById(R.id.message_log_list);
		messageList.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.message_entry, messages));
		return view;
	}

	public void addMessage(final String message) {
		messages.add(message);
		((ArrayAdapter<?>) messageList.getAdapter()).notifyDataSetChanged();
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.e(MessageLogFragment.class.getSimpleName(), "on resume");
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.e(MessageLogFragment.class.getSimpleName(), "on pause");
	}

}
