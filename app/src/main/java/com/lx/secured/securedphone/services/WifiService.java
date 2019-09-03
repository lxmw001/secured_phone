package com.lx.secured.securedphone.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.lx.secured.securedphone.utils.WifiInfo;

public class WifiService extends Service {

    private static String TAG = WifiService.class.toString();

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.d(TAG, "Service started");
        WifiInfo.detectOpenWifi(getApplicationContext());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Service destroyed");
    }
}
