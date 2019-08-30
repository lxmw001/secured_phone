package com.lx.secured.securedphone.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.lx.secured.securedphone.observers.FileObserver;

public class FileSystemObserverService extends Service {
    private static String TAG = FileSystemObserverService.class.toString();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.d(TAG, "Service started");
//        String path = Environment.getExternalStorageDirectory().getPath();

        FileObserver observer = new FileObserver("/storage/emulated/0/Download", getApplicationContext());
        observer.startWatching(); //START OBSERVING
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Service destroyed");
    }
}
