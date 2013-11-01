package com.missionse.commandablemodel;

import java.util.ArrayList;
import java.util.List;

import com.missionse.wifidirect.WifiDirectConnector;

import android.app.ListFragment;
import android.content.Context;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class PeersListFragment extends ListFragment {

	private static final String TAG = PeersListFragment.class.getSimpleName();
	
	private View contentView;
	private final List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();
	
	private WifiP2pDevice thisDevice;
	
	@Override
	public void onActivityCreated(final Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		setListAdapter(new WiFiPeerListAdapter(getActivity(),
				R.layout.peer_entry, peers));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.e(TAG, "on create view");
		contentView =  inflater.inflate(R.layout.commandable_model_peer_list, null);
		
		getActivity().setTitle(R.string.app_name);
		
		if (thisDevice != null) {
			TextView view = (TextView) contentView.findViewById(R.id.this_device_name);
	        view.setText(thisDevice.deviceName);
	        view = (TextView) contentView.findViewById(R.id.this_device_status);
	        view.setText(WifiDirectConnector.deviceStatuses.get(thisDevice.status));
		}
		
		return contentView;
	}
	
    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        WifiP2pDevice device = (WifiP2pDevice) getListAdapter().getItem(position);
        ((CommandableModelActivity) getActivity()).showPeerDetails(device);
    }
	
    public void setThisDeviceInfo(WifiP2pDevice device) {
    	thisDevice = device;
    }
    
    public void discoveryInitiated() {
    	contentView.findViewById(R.id.peer_discovery_progress).setVisibility(View.VISIBLE);
    }
    
	public void setAvailablePeers(WifiP2pDeviceList peerList) {
		contentView.findViewById(R.id.peer_discovery_progress).setVisibility(View.GONE);
		
		peers.clear();
        peers.addAll(peerList.getDeviceList());
        ((WiFiPeerListAdapter) getListAdapter()).notifyDataSetChanged();
	}
    
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
                TextView deviceName = (TextView) convertView.findViewById(R.id.device_name);
                TextView deviceStatus = (TextView) convertView.findViewById(R.id.device_status);
                if (deviceName != null) {
                	deviceName.setText(device.deviceName);
                }
                if (deviceStatus != null) {
                	deviceStatus.setText(WifiDirectConnector.deviceStatuses.get(device.status));
                }
            }
            return convertView;
        }
    }
}
