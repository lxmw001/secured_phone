package com.lx.secured.securedphone.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.util.List;

public class WifiBroadcastReceiver extends BroadcastReceiver {
    private static String TAG = "WifiBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("check wifi conection");
        String action = intent.getAction();
        if (WifiManager.SUPPLICANT_STATE_CHANGED_ACTION .equals(action)) {
            SupplicantState state = intent.getParcelableExtra(WifiManager.EXTRA_NEW_STATE);
            if (SupplicantState.isValidState(state)
                    && state == SupplicantState.COMPLETED) {

                boolean isOpenWifi = checkConnectedToOpenWifi(context);

                if(isOpenWifi) {
                    //start service
                    System.out.println("Check connections");
                } else {
                    System.out.println("No Check connections");
                    //stop serv
                }
            }
        }
    }

    /** Detect you are connected to a open network. */
    private boolean checkConnectedToOpenWifi(Context context) {
        boolean connectedToOpenNetwork = false;

        WifiManager wifiManager =
                (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        wifiManager.startScan();

        List<ScanResult> networkList = wifiManager.getScanResults();

        WifiInfo wifi = wifiManager.getConnectionInfo();
        String currentSSID = wifi.getSSID().replace("\"", "");

        if (networkList != null) {
            for (ScanResult network : networkList) {
                //check if current connected SSID
                if (currentSSID.equals(network.SSID)) {
                    //get capabilities of current connection
                    String capabilities =  network.capabilities;
                    connectedToOpenNetwork = capabilities.contains("ESS") && capabilities.length() == 5;
                }
            }
        }

        return connectedToOpenNetwork;
    }
}