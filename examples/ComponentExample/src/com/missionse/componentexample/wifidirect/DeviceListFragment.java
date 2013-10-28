package com.missionse.componentexample.wifidirect;

import java.util.ArrayList;
import java.util.List;

import com.missionse.componentexample.R;

import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class DeviceListFragment extends ListFragment implements PeerListListener{

	private ProgressDialog progressDialog = null;
	private View contentView;
	
	private final List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();
	private final SparseArray<String> deviceStatuses = new SparseArray<String>();

	public DeviceListFragment() {
	}
	
	@Override
	public void onActivityCreated(final Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		deviceStatuses.append(WifiP2pDevice.AVAILABLE, "Available");
		deviceStatuses.append(WifiP2pDevice.INVITED, "Invited");
		deviceStatuses.append(WifiP2pDevice.CONNECTED, "Connected");
		deviceStatuses.append(WifiP2pDevice.FAILED, "Failed");
		deviceStatuses.append(WifiP2pDevice.UNAVAILABLE, "Unavailable");
		
		setListAdapter(new WiFiPeerListAdapter(getActivity(),
				R.layout.wifi_direct_device_entry, peers));

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		contentView = inflater.inflate(R.layout.wifi_direct_device_list, null);
		return contentView;
	}
	
	@Override
	public void onPeersAvailable(WifiP2pDeviceList peerList) {
		if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        peers.clear();
        peers.addAll(peerList.getDeviceList());
        ((WiFiPeerListAdapter) getListAdapter()).notifyDataSetChanged();
        if (peers.size() == 0) {
            Toast.makeText(getActivity(), "No peers found.", Toast.LENGTH_SHORT).show();
        }
	}
	
	/**
     * Initiate a connection with the peer.
     */
    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        WifiP2pDevice device = (WifiP2pDevice) getListAdapter().getItem(position);
        ((WifiDirectActivity) getActivity()).showDetails(device);
    }
    
    public void onInitiateDiscovery() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        progressDialog = ProgressDialog.show(getActivity(), "Press back to cancel", "finding peers", true, true);
    }
	
    public void updateThisDevice(WifiP2pDevice device) {
        TextView view = (TextView) contentView.findViewById(R.id.my_name);
        view.setText(device.deviceName);
        view = (TextView) contentView.findViewById(R.id.my_status);
        view.setText(deviceStatuses.get(device.status));
    }
    
	/**
     * Array adapter for ListFragment that maintains WifiP2pDevice list.
     */
    private class WiFiPeerListAdapter extends ArrayAdapter<WifiP2pDevice> {

        private List<WifiP2pDevice> p2pDevices;
        private int textViewResource;

        public WiFiPeerListAdapter(final Context context, final int resource, final List<WifiP2pDevice> devices) {
            super(context, resource, devices);
            textViewResource = resource;
            p2pDevices = devices;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(textViewResource, null);
            }
            WifiP2pDevice device = p2pDevices.get(position);
            if (device != null) {
                TextView top = (TextView) convertView.findViewById(R.id.device_name);
                TextView bottom = (TextView) convertView.findViewById(R.id.device_details);
                if (top != null) {
                    top.setText(device.deviceName);
                }
                if (bottom != null) {
                    bottom.setText(deviceStatuses.get(device.status));
                }
            }
            return convertView;
        }
    }
	
}