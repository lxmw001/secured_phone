package com.lx.secured.securedphone.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.lx.secured.securedphone.R;
import com.lx.secured.securedphone.services.FileSystemObserverService;
import com.lx.secured.securedphone.services.MainService;
import com.lx.secured.securedphone.utils.GpsUtils;

public class MainActivity extends Activity {

    private static final int PERMISSIONS_REQUEST = 100;

    private boolean isGPS = false;
    private boolean permissionsOk = false;
    private boolean serviceStarted = false;
    private static Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permissionsOk = true;
                    initWifiService();
                }

                return;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (serviceStarted) {
            moveTaskToBack(true);
            return;
        }
        new GpsUtils(this).turnGPSOn(new GpsUtils.onGpsListener() {
            @Override
            public void gpsStatus(boolean isGPSEnable) {
                isGPS = isGPSEnable;
                checkRequiredPermissions();
            }
        });
    }

    private void initWifiService() {
        if (isGPS && permissionsOk && !serviceStarted) {
            startService(new Intent(getApplicationContext(), MainService.class));
            serviceStarted = true;
            moveTaskToBack(true);
        }
    }

    public static void initFileObserverService() {
        Intent myIntent = new Intent(context, FileSystemObserverService.class); // este servicio iniciar en otro lado cuando se detecta q esta en una red abierta
        context.startService(myIntent);
    }

    public static void stopFileObserverService() {
        Intent myIntent = new Intent(context, FileSystemObserverService.class); // este servicio iniciar en otro lado cuando se detecta q esta en una red abierta
        context.stopService(myIntent);
    }

    private void checkRequiredPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSIONS_REQUEST);
        } else {
            permissionsOk = true;
            initWifiService();
        }
    }
}
