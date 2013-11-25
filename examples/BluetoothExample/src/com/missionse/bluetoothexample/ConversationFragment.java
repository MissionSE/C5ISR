package com.missionse.bluetoothexample;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import com.missionse.bluetooth.network.BluetoothNetworkService;

public class ConversationFragment extends Fragment {

	public View contentView;

	private ArrayAdapter<String> conversationArrayAdapter;
	private ListView conversationList;

	private EditText inputField;
	private ImageButton sendButton;

	private String connectedDevice = "";

	private int chatServiceStatus;

	@SuppressLint("HandlerLeak")
	private final Handler bluetoothNetworkMessageHandler = new Handler() {
		@Override
		public void handleMessage(final Message msg) {
			switch (msg.what) {
				case BluetoothNetworkService.MESSAGE_STATE_CHANGE:
					chatServiceStatus = msg.arg1;
					switch (chatServiceStatus) {
						case BluetoothNetworkService.STATE_CONNECTED:
							conversationArrayAdapter.clear();
							break;
					}
					break;
				case BluetoothNetworkService.MESSAGE_DEVICE_NAME:
					connectedDevice = (String) msg.obj;
					break;
				case BluetoothNetworkService.MESSAGE_OUTGOING_DATA:
					byte[] writeBuf = (byte[]) msg.obj;
					String writeMessage = new String(writeBuf);
					conversationArrayAdapter.add("Me:  " + writeMessage);
					break;
				case BluetoothNetworkService.MESSAGE_INCOMING_DATA:
					byte[] readBuf = (byte[]) msg.obj;
					String readMessage = new String(readBuf, 0, msg.arg1);
					conversationArrayAdapter.add(connectedDevice + ":  " + readMessage);
					break;
			}
		}
	};

	public Handler getHandler() {
		return bluetoothNetworkMessageHandler;
	}

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		contentView = inflater.inflate(R.layout.fragment_conversation, null);

		// Initialize the array adapter for the conversation thread
		conversationArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.message);
		conversationList = (ListView) contentView.findViewById(R.id.conversation_list);
		conversationList.setAdapter(conversationArrayAdapter);

		// Initialize the compose field with a listener for the return key
		inputField = (EditText) contentView.findViewById(R.id.message_entry);
		inputField.setOnEditorActionListener(new OnEditorActionListener() {
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
		sendButton = (ImageButton) contentView.findViewById(R.id.send_button);
		sendButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View v) {
				// Send a message using content of the edit text widget
				TextView view = (TextView) contentView.findViewById(R.id.message_entry);
				String message = view.getText().toString();
				sendMessage(message);
			}
		});

		return contentView;
	}

	private void sendMessage(final String message) {
		// If valid message length and message sent successfully, clear the input field
		if (message.length() > 0) {
			if (((BluetoothExample) getActivity()).sendMessage(message)) {
				inputField.setText("");
			}
		}
	}
}
