package com.lx.secured.securedphone.observers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.lx.secured.securedphone.activities.MyDialog;
import com.lx.secured.securedphone.receivers.WifiBroadcastReceiver;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileObserver {

    private static String TAG = FileObserver.class.toString();

    private List<File> prevFiles;
    private String path;
    private Context context;
    private Timer timer;

    public FileObserver(String path, Context context) {
        this.path = path;
        this.context = context;
    }

    public List<File> getCurrentFiles() {
        File filePath = new File(path);
        File[] files = filePath.listFiles();
        List<File> lastFiles = new ArrayList<>();

        if (files == null) return lastFiles;
        for (int i = 0; i < files.length; ++i) {
            File file = files[i];
            if (!file.isDirectory()) {
                lastFiles.add(file);
            }
        }

        return lastFiles;
    }

    public void checkApkFiles(Set<File> newFiles) {
        Object[] files = newFiles.toArray();
        for (int i = 0; i < files.length; i++) {
            File file = (File) files[i];
            boolean isAPK = isAPK(file);
            if (isAPK) {
                context.startActivity(new Intent(context, MyDialog.class));
                Log.d(TAG, "Apk found");
//                System.out.println(file.toString());
//                try {
//                    file.getCanonicalFile().delete();
//                    if (file.exists()) {
//                        context.deleteFile(file.getName());
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                if (file.delete()) {
//                    System.out.println("file Deleted :" + file.getPath());
//                } else {
//                    System.out.println("file not Deleted :" + file.getPath());
//                }
            }
        }
    }

    public boolean isAPK(File file) {
        if (isTempFile(file)) {
            return false;
        }

        FileInputStream fis = null;
        ZipInputStream zipIs = null;
        ZipEntry zEntry = null;
        String dexFile = "classes.dex";
        String manifestFile = "AndroidManifest.xml";
        boolean hasDex = false;
        boolean hasManifest = false;

        try {
            fis = new FileInputStream(file);
            zipIs = new ZipInputStream(new BufferedInputStream(fis));
            while ((zEntry = zipIs.getNextEntry()) != null) {
                if (zEntry.getName().equalsIgnoreCase(dexFile)) {
                    hasDex = true;
                } else if (zEntry.getName().equalsIgnoreCase(manifestFile)) {
                    hasManifest = true;
                }
                if (hasDex && hasManifest) {
                    zipIs.close();
                    fis.close();
                    return true;
                }
            }
            zipIs.close();
            fis.close();
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
        return false;
    }

    public boolean isTempFile(File file) {
        String fileExt = MimeTypeMap.getFileExtensionFromUrl(file.toString());
        return fileExt.length() == 6;
    }

    public void getNewFiles() {
        if (prevFiles == null) {
            prevFiles = getCurrentFiles();
        }

        List<File> currentFiles = getCurrentFiles();
        Set<File> prev = new HashSet<>(prevFiles);
        Set<File> current = new HashSet<>(currentFiles);
        current.removeAll(prev);
        if (current.size() > 0) {
            checkApkFiles(current);
        }
        prevFiles = currentFiles;
    }


    public void startWatching() {
        timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(WifiBroadcastReceiver.stopFileObserver){
                    stopWatching();
                    return;
                }
                getNewFiles();
            }
        }, 0, 1000);
    }

    public void stopWatching() {
        timer.cancel();
    }
}