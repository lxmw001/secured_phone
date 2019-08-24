package com.lx.secured.securedphone.system;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

public class Gps {

    private static final String TAG = "GpsHandler";
    private Context context;

    public Gps(Context context) {
        this.context = context;
    }

//    public void turnGPSOnRoot() {
//        String grantPermissions = "pm grant com.your_app_packagename android.permission.WRITE_SECURE_SETTINGS";
//        String activeGps = "settings put secure location_providers_allowed gps,network,wifi";
//        String[] cmds = {grantPermissions, activeGps};
////        Terminal.runAsRoot(cmds);
//        System.out.println("GPS encendido");
//    }
//
//    public void turnGPSOffRoot() {
//        String grantPermissions  = "pm grant com.your_app_packagename android.permission.WRITE_SECURE_SETTINGS";
//        String offGps = "settings put secure location_providers_allowed ''";
//        String[] cmds = {grantPermissions, offGps};
////        Terminal.runAsRoot(cmds);
//    }

    public void turnGPSOn(){
        String provider = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if(!provider.contains("gps")){ //if gps is disabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            context.sendBroadcast(poke);
        }

        // Intent intent=new Intent("android.location.GPS_ENABLED_CHANGE");
        // intent.putExtra("enabled", true);
        // context.sendBroadcast(intent);
    }

    public void turnGPSOff(){
        String provider = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if(provider.contains("gps")){ //if gps is enabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            context.sendBroadcast(poke);
        }

        // Intent intent=new Intent("android.location.GPS_ENABLED_CHANGE");
        // intent.putExtra("enabled", true);
        // context.sendBroadcast(intent);
    }
}
