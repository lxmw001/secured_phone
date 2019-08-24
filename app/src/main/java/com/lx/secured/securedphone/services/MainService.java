package com.lx.secured.securedphone.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.lx.secured.securedphone.system.WifiInfo;

public class MainService extends Service {

    private static String TAG = "Secured phone >>>>>>>>>>>";

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        // TODO Auto-generated method stub
        super.onStart(intent, startId);
        Log.d(TAG, "MainService started");
        WifiInfo.detectOpenWifi(getApplicationContext());
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        Log.d(TAG, "MainService destroyed");
    }

}
