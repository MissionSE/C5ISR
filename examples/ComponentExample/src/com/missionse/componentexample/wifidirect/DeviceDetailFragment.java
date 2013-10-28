package com.missionse.componentexample.wifidirect;

import com.missionse.componentexample.R;

import android.app.Fragment;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class DeviceDetailFragment extends Fragment {
	
	private View contentView;	
	private WifiP2pDevice targetDevice;
	private WifiP2pDevice connectedDevice;
	
	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		contentView = inflater.inflate(R.layout.wifi_direct_device_details, null);
		contentView.findViewById(R.id.btn_connect).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				WifiP2pConfig config = new WifiP2pConfig();
                config.deviceAddress = targetDevice.deviceAddress;
                config.wps.setup = WpsInfo.PBC;
                config.groupOwnerIntent = 0;
                ((WifiDirectActivity) getActivity()).connect(config);
			}
		});
		contentView.findViewById(R.id.btn_disconnect).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				((WifiDirectActivity) getActivity()).disconnect();
			}
		});

		contentView.findViewById(R.id.btn_start_client).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(getActivity(), "Starting client...", Toast.LENGTH_SHORT).show();
			}
		});

		return contentView;
	}
	
	public void displayConnectionSuccessInfo(final WifiP2pInfo p2pInfo) {
		connectedDevice = targetDevice;
        String groupOwnerAddress = p2pInfo.groupOwnerAddress.getHostAddress();

        // The owner IP is now known.
	    TextView view = (TextView) contentView.findViewById(R.id.group_owner);
	    view.setText("Group owner: " + ((p2pInfo.isGroupOwner == true) ? "yes" : "no"));
	
	    // InetAddress from WifiP2pInfo.
	    view = (TextView) contentView.findViewById(R.id.device_info);
	    view.setText("Group Owner IP: " + groupOwnerAddress);
        
	    getView().setVisibility(View.VISIBLE);
	    contentView.findViewById(R.id.btn_connect).setVisibility(View.GONE);
	    contentView.findViewById(R.id.btn_disconnect).setVisibility(View.VISIBLE);
	    contentView.findViewById(R.id.btn_start_client).setVisibility(View.VISIBLE);
	    
        // After the group negotiation, we can determine the group owner.
        /*if (info.groupFormed && info.isGroupOwner) {
            // Do whatever tasks are specific to the group owner.
            // One common case is creating a server thread and accepting
            // incoming connections.
            if (!server_running){
	            new ServerAsyncTask(getActivity(), mContentView.findViewById(R.id.status_text)).execute();
	            server_running = true;
	    	}	    
        } else if (info.groupFormed) {
            // The other device acts as the client. In this case,
            // you'll want to create a client thread that connects to the group
            // owner.
        }*/
	}
	
	public void showDeviceDetails(WifiP2pDevice device) {
		targetDevice = device;
		
		TextView view = (TextView) contentView.findViewById(R.id.device_address);
        view.setText(device.deviceAddress);
		
		if (connectedDevice != null && connectedDevice == targetDevice) {
			setButtonStates(View.GONE, View.VISIBLE, View.GONE);
		}
		else {
			setButtonStates(View.VISIBLE, View.GONE, View.GONE);
		}
		
        getView().setVisibility(View.VISIBLE);
	}

	public void clearForDisconnect() {
		connectedDevice = null;
	    setButtonStates(View.GONE, View.GONE, View.GONE);
	    getView().setVisibility(View.GONE);
        /*TextView view = (TextView) contentView.findViewById(R.id.device_address);
        view.setText("");
        view = (TextView) contentView.findViewById(R.id.group_owner);
        view.setText("");
        view = (TextView) contentView.findViewById(R.id.status_text);
        view.setText("");*/
	}
	
	private void setButtonStates(int connectState, int disconnectState, int actionState) {
		contentView.findViewById(R.id.btn_connect).setVisibility(connectState);
	    contentView.findViewById(R.id.btn_disconnect).setVisibility(disconnectState);
	    contentView.findViewById(R.id.btn_start_client).setVisibility(actionState);
	}
}
