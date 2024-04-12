package com.example.music_mobile_app.service.mydatabase.impl;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import com.example.music_mobile_app.service.mydatabase.myinterface.DownloadService;
import com.example.music_mobile_app.service.mydatabase.myinterface.DownloadCallback;
import com.example.music_mobile_app.util.mydatabase.DownloadTool;

public class DownloadServiceImpl implements DownloadService {

    public Context context;
    public DownloadServiceImpl(Context context)
    {
        this.context = context;
    }
    public void downloadMp3(String urlString, DownloadCallback downloadCallback) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(urlString));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE |
                DownloadManager.Request.NETWORK_WIFI);
        request.setTitle("Download");
        request.setDescription("Downloading File...");

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, DownloadTool.getFileName(urlString) + String.valueOf(System.currentTimeMillis()) );

        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        if (downloadManager!=null)
        {
            downloadManager.enqueue(request);
            if (downloadCallback != null) {
                downloadCallback.onDownloadComplete();
            }
        }

    }
}
