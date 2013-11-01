package com.missionse.componentexample.wifidirect;

import java.util.ArrayList;
import java.util.List;

import com.missionse.componentexample.R;
import com.missionse.wifidirect.WifiDirectConnector;

import android.app.ListFragment;
import android.content.Context;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class DeviceListFragment extends ListFragment {
		
	private final List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();	
	
	@Override
	public void onActivityCreated(final Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		setListAdapter(new WiFiPeerListAdapter(getActivity(),
				R.layout.wifi_direct_device_list_entry, peers));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.wifi_direct_device_list, null);
	}
	
    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        WifiP2pDevice device = (WifiP2pDevice) getListAdapter().getItem(position);
        ((WifiDirectActivity) getActivity()).showPeerDetails(device);
    }
	
	public void setAvailablePeers(WifiP2pDeviceList peerList) {
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
