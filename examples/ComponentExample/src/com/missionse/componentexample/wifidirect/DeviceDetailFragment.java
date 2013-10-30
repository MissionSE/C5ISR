package com.missionse.componentexample.wifidirect;

import com.missionse.componentexample.R;

import android.app.Fragment;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;
import android.os.Bundle;
//import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class DeviceDetailFragment extends Fragment {
	
	//private static final String TAG = DeviceDetailFragment.class.getSimpleName();
	
	private View contentView;
	
	private WifiP2pInfo connectionInfo;
	private WifiP2pDevice targetDevice;
	
	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		contentView = inflater.inflate(R.layout.wifi_direct_device_details, null);
		
		contentView.findViewById(R.id.peer_connection_progress).setVisibility(View.GONE);
		
		TextView view = (TextView) contentView.findViewById(R.id.target_peer_name);
        view.setText(targetDevice.deviceName);
		
        view = (TextView) contentView.findViewById(R.id.device_detail_status);
        view.setText("Status: " + ((WifiDirectActivity)getActivity()).deviceStatuses.get(targetDevice.status));
        
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
		
		showConnectionInfo();

		return contentView;
	}
	
	public void setConnectionSuccessInfo(final WifiP2pInfo p2pInfo) {
		connectionInfo = p2pInfo;
		
		if (isVisible()) {
			contentView.findViewById(R.id.peer_connection_progress).setVisibility(View.GONE);
		}
	}
	
	public void showConnectionInfo() {
		if (targetDevice.status == WifiP2pDevice.CONNECTED || connectionInfo != null) {
			contentView.findViewById(R.id.device_detail_success_info).setVisibility(View.VISIBLE);
			
			TextView view = (TextView) contentView.findViewById(R.id.host_address);
		    view.setText("Host address: " + connectionInfo.groupOwnerAddress.getHostAddress());
			
		    view = (TextView) contentView.findViewById(R.id.group_owner);
		    view.setText("Group owner: " + ((connectionInfo.isGroupOwner == true) ? "yes" : "no"));

		    setButtonStates(false, true);
		}
		else {
			contentView.findViewById(R.id.device_detail_success_info).setVisibility(View.INVISIBLE);
			setButtonStates(true, false);
		}
    
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
	
	public void setTargetPeer(WifiP2pDevice device) {
		targetDevice = device;	
	}

	public void clearForDisconnect() {
		connectionInfo = null;
	    setButtonStates(false, false);
	}
	
	private void setButtonStates(boolean connectState, boolean disconnectState) {
		contentView.findViewById(R.id.btn_connect).setEnabled(connectState);
	    contentView.findViewById(R.id.btn_disconnect).setEnabled(disconnectState);
	}
}
