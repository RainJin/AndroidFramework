package com.youyi.appframework.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class NetworkHelper {
	public static final String TAG = "NetworkHelper";

	public static boolean isAirplaneModeEnabled(Context context) {
		int mode = Settings.System.getInt(context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0);
		return mode == 1;
	}

	public static NetworkInfo getActiveNetworkInfo(Context context) {
		final ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = null;
		int preferenceNetwork = cm.getNetworkPreference();
		NetworkInfo[] info = cm.getAllNetworkInfo();
		for (NetworkInfo nwi : info) {
			if (nwi.getType() == preferenceNetwork && nwi.isConnected()) {
				ni = nwi;
				break;
			}
		}
		if (ni == null) {
			ni = cm.getActiveNetworkInfo();
		}
		return ni;
	}

	public static boolean isNetworkAvailable(Context context) {
		if (context == null) {
			return false;
		}
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			return false;
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static NetworkInfo[] getActiveNetworkInfos(Context context) {
		final ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		ArrayList<NetworkInfo> infos = new ArrayList<NetworkInfo>();
		NetworkInfo[] info = cm.getAllNetworkInfo();
		for (NetworkInfo ni : info) {
			if (ni.isConnected()) {
				infos.add(ni);
			}
		}
		if (infos.size() == 0) {
			return null;
		}
		NetworkInfo[] nis = new NetworkInfo[infos.size()];
		infos.toArray(nis);
		return nis;
	}

	public interface NetworkChangedNotifier {
		void onNetworkChanged(NetworkConnectivityListener sender, NetworkInfo ni);

		void onAnyDataConnectionChanged(NetworkConnectivityListener sender, int state);
	}

	public enum State {
		UNKNOWN,

		/** This state is returned if there is connectivity to any network **/
		CONNECTED,
		/**
		 * This state is returned if there is no connectivity to any network.
		 * This is set to true under two circumstances:
		 * <ul>
		 * <li>When connectivity is lost to one network, and there is no other
		 * available network to attempt to switch to.</li>
		 * <li>When connectivity is lost to one network, and the attempt to
		 * switch to another network fails.</li>
		 */
		NOT_CONNECTED
	}

	public static class NetworkConnectivityListener {
		// private static final String TAG = "NetworkConnectivityListener";
		private static final String ACTION_ANY_DATA_CONNECTION_STATE_CHANGED = "android.intent.action.ANY_DATA_STATE";

		private Context mContext;
		private HashMap<Handler, NetworkChangedNotifier> mHandlers = new HashMap<Handler, NetworkChangedNotifier>();

		private State mState;
		private boolean mListening;
		private String mReason;
		private boolean mIsFailover;

		/** Network connectivity information */
		private NetworkInfo mNetworkInfo;
		private NetworkInfo mOtherNetworkInfo;
		private ConnectivityBroadcastReceiver mReceiver;
		private ConnectivityManager mConnectivityManager = null;

		private class NetworkChangedRunnable implements Runnable {
			public NetworkChangedNotifier mNotifier;
			public NetworkInfo mNetInfo;

			public NetworkChangedRunnable(NetworkChangedNotifier notifier, NetworkInfo ni) {
				mNotifier = notifier;
				mNetInfo = ni;
			}

			@Override
			public void run() {
				if (mNetInfo != null) {
					mNotifier.onNetworkChanged(NetworkConnectivityListener.this, mNetInfo);
				}
			}
		}

		private class AnyDataConnectionChangedRunnable implements Runnable {
			private NetworkChangedNotifier mNotifier;
			private int mState;

			public AnyDataConnectionChangedRunnable(NetworkChangedNotifier notifier, int state) {
				mNotifier = notifier;
				mState = state;
			}

			@Override
			public void run() {
				mNotifier.onAnyDataConnectionChanged(NetworkConnectivityListener.this, mState);
			}
		}

		private class ConnectivityBroadcastReceiver extends BroadcastReceiver {
			@Override
			public synchronized void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
					boolean noConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);

					if (noConnectivity) {
						mState = State.NOT_CONNECTED;
					} else {
						mState = State.CONNECTED;
					}

					mNetworkInfo = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
					if (mNetworkInfo == null || !mNetworkInfo.isConnected()) {
						NetworkInfo ani = getActiveNetworkInfo(context);
						if (ani != null) {
							mNetworkInfo = ani;
						}
					} else {
						final ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
						int preferenceNetwork = cm.getNetworkPreference();
						if (mNetworkInfo.getType() != preferenceNetwork) {
							NetworkInfo ani = getActiveNetworkInfo(context);
							if (ani != null) {
								String extra = ani.getExtraInfo();
								if (extra == null) {
									extra = "";
								}
								if (ani.getType() != mNetworkInfo.getType() || !extra.equals(mNetworkInfo.getExtraInfo())) {
									mNetworkInfo = ani;
								}
							}
						}
					}
					mOtherNetworkInfo = intent.getParcelableExtra(ConnectivityManager.EXTRA_OTHER_NETWORK_INFO);

					mReason = intent.getStringExtra(ConnectivityManager.EXTRA_REASON);
					mIsFailover = intent.getBooleanExtra(ConnectivityManager.EXTRA_IS_FAILOVER, false);
					Iterator<Handler> it = mHandlers.keySet().iterator();
					while (it.hasNext()) {
						Handler target = it.next();
						NetworkChangedNotifier notifier = mHandlers.get(target);
						boolean result = target.postDelayed(new NetworkChangedRunnable(notifier, mNetworkInfo), 10);
						if (!result) {
							notifier.onNetworkChanged(NetworkConnectivityListener.this, mNetworkInfo);
						}
					}
				} else if (ACTION_ANY_DATA_CONNECTION_STATE_CHANGED.equals(action)) {
					String apn = intent.getStringExtra("apn");
					// String ifname = intent.getStringExtra("iface");
					// String key = "gsm." + apn;
					Iterator<Handler> it = mHandlers.keySet().iterator();
					while (it.hasNext()) {
						Handler target = it.next();
						NetworkChangedNotifier notifier = mHandlers.get(target);
						boolean result = target.post(new AnyDataConnectionChangedRunnable(notifier, 0));
						if (!result) {
							notifier.onAnyDataConnectionChanged(NetworkConnectivityListener.this, 0);
						}
					}
				}
			}
		}

		/**
		 * Create a new NetworkConnectivityListener.
		 */
		public NetworkConnectivityListener(Context context) {
			mContext = context;
			mState = State.UNKNOWN;
			mReceiver = new ConnectivityBroadcastReceiver();
		}

		/**
		 * This method starts listening for network connectivity state changes.
		 */
		public synchronized void startListening() {
			if (!mListening) {
				IntentFilter filter = new IntentFilter();
				filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
				// mul_apn_modify
				// TODO
				// filter.addAction(ConnectivityManager.MPDP_CONNECTIVITY_ACTION);
				filter.addAction(ACTION_ANY_DATA_CONNECTION_STATE_CHANGED);
				mContext.registerReceiver(mReceiver, filter);
				mListening = true;
			}
		}

		/**
		 * This method stops this class from listening for network changes.
		 */
		public synchronized void stopListening() {
			if (mListening) {
				mContext.unregisterReceiver(mReceiver);
				mListening = false;
			}
		}

		/**
		 * This methods registers a Handler to be called back onto with the
		 * specified what code when the network connectivity state changes.
		 * 
		 * @param target
		 *            The target handler.
		 * @param what
		 *            The what code to be used when posting a message to the
		 *            handler.
		 */
		public void registerHandler(Handler target, NetworkChangedNotifier notifier) {
			mHandlers.put(target, notifier);
		}

		/**
		 * This methods unregisters the specified Handler.
		 * 
		 * @param target
		 */
		public void unregisterHandler(Handler target) {
			mHandlers.remove(target);
		}

		public State getState() {
			return mState;
		}

		/**
		 * Return the NetworkInfo associated with the most recent connectivity
		 * event.
		 * 
		 * @return {@code NetworkInfo} for the network that had the most recent
		 *         connectivity event.
		 */
		public NetworkInfo getNetworkInfo() {
			return mNetworkInfo;
		}

		/**
		 * If the most recent connectivity event was a DISCONNECT, return any
		 * information supplied in the broadcast about an alternate network that
		 * might be available. If this returns a non-null value, then another
		 * broadcast should follow shortly indicating whether connection to the
		 * other network succeeded.
		 * 
		 * @return NetworkInfo
		 */
		public NetworkInfo getOtherNetworkInfo() {
			return mOtherNetworkInfo;
		}

		/**
		 * Returns true if the most recent event was for an attempt to switch
		 * over to a new network following loss of connectivity on another
		 * network.
		 * 
		 * @return {@code true} if this was a failover attempt, {@code false}
		 *         otherwise.
		 */
		public boolean isFailover() {
			return mIsFailover;
		}

		/**
		 * An optional reason for the connectivity state change may have been
		 * supplied. This returns it.
		 * 
		 * @return the reason for the state change, if available, or
		 *         {@code null} otherwise.
		 */
		public String getReason() {
			return mReason;
		}

		public boolean isNetworkAvailable() {
			if (mConnectivityManager == null) {
				mConnectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
			}
			if (mConnectivityManager == null) {
				return false;
			}
			NetworkInfo ni = getActiveNetworkInfo(mContext);
			return ni != null && ni.isConnected();
		}
	}

	public static String getIMEI(Context context) {
		TelephonyManager telmanager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = telmanager.getDeviceId();
		imei = imei == null ? "000000000000000" : imei;
		return imei;
	}

	public static String getMACaddress(Context context) {
		String macAddress = null;
		WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = null == wifiMgr ? null : wifiMgr.getConnectionInfo();
		if (null != info) {
			macAddress = info.getMacAddress();
			// ip = int2ip(info.getIpAddress());
		}
		if (null != macAddress) {
			macAddress = macAddress.replace(":", "");
		}
		return macAddress;
	}
}
