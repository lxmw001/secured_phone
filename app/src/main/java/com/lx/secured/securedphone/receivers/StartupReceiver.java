package com.lx.secured.securedphone.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.lx.secured.securedphone.activities.MainActivity;

public class StartupReceiver extends BroadcastReceiver {

    private static String TAG = StartupReceiver.class.toString();

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent myIntent = new Intent(context, MainActivity.class);
        context.startActivity(myIntent);
        Log.d(TAG, "Starting secured app");
    }
}