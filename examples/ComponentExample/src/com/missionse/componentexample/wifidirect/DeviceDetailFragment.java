package com.missionse.componentexample.wifidirect;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import com.missionse.componentexample.R;
import com.missionse.wifidirect.WifiDirectConnector;
import com.missionse.wifidirect.WifiUtilities;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class DeviceDetailFragment extends Fragment {
	
	private static final String TAG = DeviceDetailFragment.class.getSimpleName();
	
	private static final int PORT = 3456;
	private boolean serverRunning = false;
	private ServerAsyncTask serverTask;
	
	private View contentView;
	
	private WifiP2pInfo connectionInfo;
	private WifiP2pDevice targetDevice;
	
	private boolean connectButtonEnabled = false;
	private boolean disconnectButtonEnabled = false;
	
	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		contentView = inflater.inflate(R.layout.wifi_direct_device_details, null);
		
		contentView.findViewById(R.id.peer_connection_progress).setVisibility(View.GONE);
		
		TextView view = (TextView) contentView.findViewById(R.id.target_peer_name);
        view.setText(targetDevice.deviceName);
		
        view = (TextView) contentView.findViewById(R.id.device_detail_status);
        view.setText("Status: " + WifiDirectConnector.deviceStatuses.get(targetDevice.status));
        
        view = (TextView) contentView.findViewById(R.id.device_detail_primarytype);
        view.setText("Primary type: " + targetDevice.primaryDeviceType);
        
		view = (TextView) contentView.findViewById(R.id.device_detail_address);
        view.setText("Address: " + targetDevice.deviceAddress);
		
		contentView.findViewById(R.id.btn_connect).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				WifiP2pConfig config = new WifiP2pConfig();
                config.deviceAddress = targetDevice.deviceAddress;
                config.wps.setup = WpsInfo.PBC;
                config.groupOwnerIntent = 0;
                ((WifiDirectActivity) getActivity()).connect(config);
                
                contentView.findViewById(R.id.peer_connection_progress).setVisibility(View.VISIBLE);
			}
		});
		contentView.findViewById(R.id.btn_disconnect).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				((WifiDirectActivity) getActivity()).disconnect();
			}
		});
		contentView.findViewById(R.id.send_button).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				EditText sendableTextView = (EditText)contentView.findViewById(R.id.sendable_text);
				Editable sendableText = (Editable) sendableTextView.getText();
				Log.e(TAG, "send button clicked");
				if (!sendableText.toString().equals("")) {
					
					Log.e(TAG, "sending [" + sendableText.toString() + "]");
					
					InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(
							Context.INPUT_METHOD_SERVICE); 
					inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
					
					Intent serviceIntent = new Intent(getActivity(), WifiDirectChatService.class);
					serviceIntent.setAction(WifiDirectChatService.ACTION_SEND_TEXT);
					serviceIntent.putExtra(WifiDirectChatService.EXTRAS_TEXT, sendableText.toString());
					
					if (connectionInfo.isGroupOwner) {
						String peerIP = WifiUtilities.getIPAddressFromMacAddress(targetDevice.deviceAddress);
		                serviceIntent.putExtra(WifiDirectChatService.EXTRAS_HOST, peerIP);
		                Log.e(TAG, "sending to device at " + peerIP);
					}
					else {
						serviceIntent.putExtra(WifiDirectChatService.EXTRAS_HOST, connectionInfo.groupOwnerAddress.getHostAddress());
						Log.e(TAG, "sending to device at " + connectionInfo.groupOwnerAddress.getHostAddress());
					}
					
					serviceIntent.putExtra(WifiDirectChatService.EXTRAS_PORT, PORT);
					getActivity().startService(serviceIntent);
					
					sendableTextView.setText("");
				}
			}
		});
		
		showConnectionInfo();
		
		refreshButtonState();

		return contentView;
	}
	
	public void setConnectionSuccessInfo(final WifiP2pInfo p2pInfo) {
		connectionInfo = p2pInfo;
		
		//If a group is formed, we are definitely ready to start sending data both ways.
        if (connectionInfo != null && connectionInfo.groupFormed) {
        	if (!serverRunning) {
        		serverRunning = true;
        		serverTask = new ServerAsyncTask();
        		serverTask.execute(getActivity());
        	}
        }
		
		if (isVisible()) {
			contentView.findViewById(R.id.peer_connection_progress).setVisibility(View.GONE);
		}
	}
	
	public void showConnectionInfo() {
		if (targetDevice.status == WifiP2pDevice.CONNECTED || connectionInfo != null) {
			contentView.findViewById(R.id.device_detail_success_info).setVisibility(View.VISIBLE);
			contentView.findViewById(R.id.send_data_section).setVisibility(View.VISIBLE);
			
			TextView view = (TextView) contentView.findViewById(R.id.host_address);
		    view.setText("Host address: " + connectionInfo.groupOwnerAddress.getHostAddress());
			
		    view = (TextView) contentView.findViewById(R.id.group_owner);
		    view.setText("Group owner: " + ((connectionInfo.isGroupOwner == true) ? "yes" : "no"));

		    setButtonStates(false, true);
		}
		else {
			contentView.findViewById(R.id.device_detail_success_info).setVisibility(View.INVISIBLE);
			contentView.findViewById(R.id.send_data_section).setVisibility(View.INVISIBLE);
			setButtonStates(true, false);
		}
	}
	
	public void setTargetPeer(final WifiP2pDevice device) {
		targetDevice = device;	
	}

	public void clearForDisconnect() {
		connectionInfo = null;
		if (serverTask != null) {
			serverTask.cancel(true);
		}
	    setButtonStates(true, false);
	}
	
	private void setButtonStates(boolean connectState, boolean disconnectState) {
		connectButtonEnabled = connectState;
		disconnectButtonEnabled = disconnectState;
	}
	
	private void refreshButtonState() {
		contentView.findViewById(R.id.btn_connect).setEnabled(connectButtonEnabled);
	    contentView.findViewById(R.id.btn_disconnect).setEnabled(disconnectButtonEnabled);
	}
	
	private class ServerAsyncTask extends AsyncTask<Activity, Void, String> {
		@Override
		protected String doInBackground(Activity... params) {
			try {
				ServerSocket serverSocket = new ServerSocket(PORT);
				while (true) {
					Socket connectingClient = serverSocket.accept();
					BufferedReader reader = new BufferedReader(new InputStreamReader(connectingClient.getInputStream()));
					final String textToDisplay = reader.readLine();
					
					params[0].runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(getActivity(), textToDisplay, Toast.LENGTH_SHORT).show();
						}
					});
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
	}
}
