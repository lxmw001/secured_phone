package com.lx.secured.securedphone.observers;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.lx.secured.securedphone.activities.DialogActivity;
import com.lx.secured.securedphone.receivers.WifiBroadcastReceiver;
import com.lx.secured.securedphone.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class FileObserver {

    private static String TAG = FileObserver.class.toString();

    private List<File> prevFiles;
    private String path;
    private Context context;
    private Timer timer;

    public static File currentApkFile;

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
            boolean isAPK = FileUtils.isAPK(file);
            if (isAPK) {//
                openDialog(file);
            }
        }
    }

    public void openDialog(File apkFile) {
        currentApkFile = apkFile;
        Intent intent = new Intent(context, DialogActivity.class);
        context.startActivity(intent);
        Log.d(TAG, "Apk found");
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