package com.lx.secured.securedphone.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.lx.secured.securedphone.observers.FileObserver;
import com.lx.secured.securedphone.utils.FileUtils;


public class DialogActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showDialog();
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Se ha detectado un nuevo archivo apk, verifica si es una aplicacion segura para instalarla?")
                .setCancelable(false)
                .setPositiveButton("Eliminar APK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            FileUtils.deleteFile(FileObserver.currentApkFile, getApplicationContext());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.setTitle("Alerta");
        alert.show();
    }
}