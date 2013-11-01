package com.missionse.commandablemodel;

import com.missionse.wifidirect.WifiDirectConnector;

import android.app.Fragment;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class PeerDetailFragment extends Fragment {
	
	private static final String TAG = PeerDetailFragment.class.getSimpleName();
	
	private View contentView;
	
	private WifiP2pDevice targetDevice;
	private WifiP2pInfo connectionSuccessfulInfo;
	
	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		contentView = inflater.inflate(R.layout.commandable_model_peer_detail, null);
		contentView.findViewById(R.id.peer_connection_progress).setVisibility(View.GONE);
		
		contentView.findViewById(R.id.btn_connect).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				WifiP2pConfig config = new WifiP2pConfig();
                config.deviceAddress = targetDevice.deviceAddress;
                config.wps.setup = WpsInfo.PBC;
                config.groupOwnerIntent = 0;
				((CommandableModelActivity) getActivity()).connect(config);
			}
		});
		contentView.findViewById(R.id.btn_disconnect).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				((CommandableModelActivity) getActivity()).disconnect();
			}
		});
		
		return contentView;
	}
	
	public void setTargetDevice(WifiP2pDevice device) {
		targetDevice = device;
	}
	
	public void setConnectionSuccessfulInformation(WifiP2pInfo info) {
		connectionSuccessfulInfo = info;
	}
	
	public void refresh() {
		if (targetDevice != null) {
			getActivity().setTitle(targetDevice.deviceName);
			
	        TextView view = (TextView) contentView.findViewById(R.id.device_detail_status);
	        view.setText("Status: " + WifiDirectConnector.deviceStatuses.get(targetDevice.status));
	        
	        view = (TextView) contentView.findViewById(R.id.device_detail_primarytype);
	        view.setText("Primary type: " + targetDevice.primaryDeviceType);
	        
			view = (TextView) contentView.findViewById(R.id.device_detail_address);
	        view.setText("Address: " + targetDevice.deviceAddress);
	        
	        contentView.findViewById(R.id.btn_connect).setEnabled(true);
			contentView.findViewById(R.id.btn_disconnect).setEnabled(true);
			
			if (connectionSuccessfulInfo != null) {
				contentView.findViewById(R.id.device_detail_success_info).setVisibility(View.VISIBLE);
				
				view = (TextView) contentView.findViewById(R.id.host_address);
			    view.setText("Host address: " + connectionSuccessfulInfo.groupOwnerAddress.getHostAddress());
				
			    view = (TextView) contentView.findViewById(R.id.group_owner);
			    view.setText("Group owner: " + ((connectionSuccessfulInfo.isGroupOwner == true) ? "yes" : "no"));
			    
			    contentView.findViewById(R.id.btn_connect).setEnabled(false);
				contentView.findViewById(R.id.btn_disconnect).setEnabled(true);
			}
			else {
				contentView.findViewById(R.id.device_detail_success_info).setVisibility(View.GONE);
				contentView.findViewById(R.id.btn_connect).setEnabled(true);
				contentView.findViewById(R.id.btn_disconnect).setEnabled(false);
			}
		}
		else {
			contentView.findViewById(R.id.btn_connect).setEnabled(false);
			contentView.findViewById(R.id.btn_disconnect).setEnabled(false);
		}
		
		
	}
}
