package com.missionse.wifidirectexample;

import android.app.Fragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.missionse.wifidirect.listener.IncomingDataListener;

/**
 * Provides a user interface for sending and displaying a text conversation.
 */
public class ConversationFragment extends Fragment implements IncomingDataListener {

	private View mContentView;

	private ArrayAdapter<String> mConversationArrayAdapter;
	private ListView mConversationList;

	private EditText mInputField;
	private ImageButton mSendButton;

	@Override
	public void processReceivedData(final byte[] data) {
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				String readMessage = new String(data, 0, data.length);
				mConversationArrayAdapter.add("Incoming:  " + readMessage);
			}
		});

	}

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		mContentView = inflater.inflate(R.layout.fragment_conversation, null);

		// Initialize the array adapter for the conversation thread
		mConversationArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.message);
		mConversationList = (ListView) mContentView.findViewById(R.id.conversation_list);
		mConversationList.setAdapter(mConversationArrayAdapter);

		// Initialize the compose field with a listener for the return key
		mInputField = (EditText) mContentView.findViewById(R.id.message_entry);
		mInputField.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(final TextView view, final int actionId, final KeyEvent event) {
				// If the action is a key-up event on the return key, send the message
				if (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_UP) {
					String message = view.getText().toString();
					sendMessage(message);
				}
				return true;
			}
		});

		// Initialize the send button with a listener that for click events
		mSendButton = (ImageButton) mContentView.findViewById(R.id.send_button);
		mSendButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View v) {
				// Send a message using content of the edit text widget
				TextView view = (TextView) mContentView.findViewById(R.id.message_entry);
				String message = view.getText().toString();
				sendMessage(message);
			}
		});

		return mContentView;
	}

	private void sendMessage(final String message) {
		// If valid message length and message sent successfully, clear the input field
		if (message.length() > 0) {
			if (((WifiDirectExample) getActivity()).sendMessage(message)) {
				mInputField.setText("");
			}
		}
	}
}
