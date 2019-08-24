package com.lx.secured.securedphone.system;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;

import com.lx.secured.securedphone.receivers.WifiBroadcastReceiver;

public class WifiInfo {

    public static void detectOpenWifi(Context context) {
        BroadcastReceiver broadcastReceiver = new WifiBroadcastReceiver();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
        context.registerReceiver(broadcastReceiver, intentFilter);
    }
}
