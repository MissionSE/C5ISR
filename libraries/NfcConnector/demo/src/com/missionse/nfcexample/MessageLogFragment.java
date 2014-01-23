package com.missionse.nfcexample;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Maintains a running list of incoming mMessages.
 */
public class MessageLogFragment extends Fragment {

	private List<String> mMessages;

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_message_log, null);
		mMessages = new ArrayList<String>();
		ListView messageList = (ListView) view.findViewById(R.id.message_log_list);
		messageList.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.message_entry, mMessages));
		return view;
	}

	/**
	 * Adds a message to the message list to be displayed.
	 * @param message the message to be displayed
	 */
	public void addMessage(final String message) {
		mMessages.add(message);
	}
}
