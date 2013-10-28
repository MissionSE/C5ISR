package com.missionse.componentexample.wifidirect;

import com.missionse.componentexample.R;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager.ConnectionInfoListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class DeviceDetailFragment extends Fragment implements ConnectionInfoListener {

	private View contentView;
	private ProgressDialog progressDialog = null;
	
	private WifiP2pDevice targetDevice;
	
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
                if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                }
                progressDialog = ProgressDialog.show(getActivity(), "Press back to cancel",
                		"Connecting to :" + targetDevice.deviceAddress, true, true);
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
	
	@Override
	public void onConnectionInfoAvailable(final WifiP2pInfo p2pInfo) {
//		// InetAddress from WifiP2pInfo struct.
//        InetAddress groupOwnerAddress = info.groupOwnerAddress.getHostAddress());
//
//        // After the group negotiation, we can determine the group owner.
//        if (info.groupFormed && info.isGroupOwner) {
//            // Do whatever tasks are specific to the group owner.
//            // One common case is creating a server thread and accepting
//            // incoming connections.
//        } else if (info.groupFormed) {
//            // The other device acts as the client. In this case,
//            // you'll want to create a client thread that connects to the group
//            // owner.
//        }
		
		if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
	    }
	    getView().setVisibility(View.VISIBLE);
	
	    // The owner IP is now known.
	    TextView view = (TextView) contentView.findViewById(R.id.group_owner);
	    view.setText("Group owner: " + ((p2pInfo.isGroupOwner == true) ? "yes" : "no"));
	
	    // InetAddress from WifiP2pInfo.
	    view = (TextView) contentView.findViewById(R.id.device_info);
	    view.setText("Group Owner IP: " + p2pInfo.groupOwnerAddress.getHostAddress());
	
	    contentView.findViewById(R.id.btn_start_client).setVisibility(View.VISIBLE);
	
	    //if (!server_running){
	    //        new ServerAsyncTask(getActivity(), mContentView.findViewById(R.id.status_text)).execute();
	    //        server_running = true;
	    //}
	
	    // hide the connect button
	    contentView.findViewById(R.id.btn_connect).setVisibility(View.GONE);
	}
	
	public void showDetails(WifiP2pDevice device) {
		targetDevice = device;
        getView().setVisibility(View.VISIBLE);
        TextView view = (TextView) contentView.findViewById(R.id.device_address);
        view.setText(device.deviceAddress);
        view = (TextView) contentView.findViewById(R.id.device_info);
        view.setText(device.toString());
	}

	public void resetViews() {
        contentView.findViewById(R.id.btn_connect).setVisibility(View.VISIBLE);
        TextView view = (TextView) contentView.findViewById(R.id.device_address);
        view.setText("");
        view = (TextView) contentView.findViewById(R.id.device_info);
        view.setText("");
        view = (TextView) contentView.findViewById(R.id.group_owner);
        view.setText("");
        view = (TextView) contentView.findViewById(R.id.status_text);
        view.setText("");
        //contentView.findViewById(R.id.btn_start_client).setVisibility(View.GONE);
        //getView().setVisibility(View.GONE);
	}
	
	public void endProgressBar() {
		if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
	    }
	}
}
