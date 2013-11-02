package com.missionse.commandablemodel.network;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;

import com.missionse.modelviewer.ModelViewerFragment;
import com.missionse.wifidirect.WifiUtilities;

public class ModelControllerClient implements ModelChangeRecipient {

	private Context context;
	private ModelViewerFragment modelFragment;

	private WifiP2pInfo connectionInfo;
	private WifiP2pDevice targetDevice;

	// We should only send ModelState changes at most every 20ms.
	// Note: In the future, this logic should be replaced by some sort of timed task that automatically sends
	// the current state at a fixed rate, to avoid jitter. For now, rate limiting works fine.
	private static final long PERIODIC = 20L;
	private long lastStateSentTime = 0L;

	public ModelControllerClient(final Context context) {
		this.context = context;
	}

	public void setModelViewerFragment(final ModelViewerFragment fragment) {
		modelFragment = fragment;
	}

	public void onConnectionSuccessful(final WifiP2pInfo p2pInfo, final WifiP2pDevice p2pDevice) {
		connectionInfo = p2pInfo;
		targetDevice = p2pDevice;
	}

	public void onDisconnect() {
		connectionInfo = null;
		targetDevice = null;
	}

	@Override
	public void onModelChange() {
		if (System.currentTimeMillis() - lastStateSentTime > PERIODIC) {
			if (connectionInfo != null && targetDevice != null) {
				String address = "";
				if (connectionInfo.isGroupOwner) {
					address = WifiUtilities.getIPAddressFromMacAddress(targetDevice.deviceAddress);
				} else {
					address = connectionInfo.groupOwnerAddress.getHostAddress();
				}

				Intent modelStatusIntent = new Intent(context, ModelStatusIntentService.class);
				modelStatusIntent.setAction(ModelStatusIntentService.ACTION_SEND_MODEL_STATUS);

				ModelState currentModelState = new ModelState(modelFragment.getController());
				modelStatusIntent.putExtra(ModelStatusIntentService.EXTRAS_STATUS, currentModelState.toString());
				modelStatusIntent.putExtra(ModelStatusIntentService.EXTRAS_HOST, address);
				modelStatusIntent.putExtra(ModelStatusIntentService.EXTRAS_PORT, ModelControllerServer.PORT);

				context.startService(modelStatusIntent);

				lastStateSentTime = System.currentTimeMillis();
			}
		}
	}
}
