package com.missionse.nfcexample;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MessageSenderFragment extends Fragment {

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_message_sender, null);
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.e(MessageSenderFragment.class.getSimpleName(), "on resume");
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.e(MessageSenderFragment.class.getSimpleName(), "on pause");
	}
}
