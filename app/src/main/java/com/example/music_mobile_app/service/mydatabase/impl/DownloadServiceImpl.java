package com.example.music_mobile_app.service.mydatabase.impl;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.music_mobile_app.R;
import com.example.music_mobile_app.service.mydatabase.extension_interface.DownloadCallback;
import com.example.music_mobile_app.service.mydatabase.extension_interface.DownloadService;

public class DownloadServiceImpl implements DownloadService {

    private Context context;

    public DownloadServiceImpl(Context context) {
        this.context = context;
    }

    public void downloadMp3(String urlString, String id, DownloadCallback downloadCallback) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(urlString));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE |
                DownloadManager.Request.NETWORK_WIFI);
        request.setTitle("Download");
        request.setDescription("Downloading File...");

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

//        String fileName = DownloadTool.getFileName(urlString);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, id+".mp3");

        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "firebase's notification")
                .setContentTitle("Tải nhạc")
                .setContentText("Bắt đầu tải, vui lòng chờ")
                .setSmallIcon(R.drawable.ic_add_circle_white);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        // Check and request permission if needed
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
            return;
        }

        if (downloadManager != null) {
            long downloadId = downloadManager.enqueue(request);
            if (downloadCallback != null) {
                downloadCallback.onDownloadComplete(id);
            }
        }
    }
}

