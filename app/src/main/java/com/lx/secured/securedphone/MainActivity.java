package com.lx.secured.securedphone;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lx.secured.securedphone.constans.AppConstants;
import com.lx.secured.securedphone.services.MainService;
import com.lx.secured.securedphone.system.Gps;
import com.lx.secured.securedphone.utils.GpsUtils;

public class MainActivity extends Activity {

    private static final int LOCATION = 1;
    private boolean isGPS = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isGPS) {
            moveTaskToBack(true);
            return;
        }
        new GpsUtils(this).turnGPSOn(new GpsUtils.onGpsListener() {
            @Override
            public void gpsStatus(boolean isGPSEnable) {
                // turn on GPS
                isGPS = isGPSEnable;
                startService(new Intent(getApplicationContext(), MainService.class));
                moveTaskToBack(true);

            }
        });
    }
}
