package com.example.music_mobile_app.service.mydatabase.myinterface;

public interface DownloadService {
    public void downloadMp3(String url, String id, DownloadCallback downloadCallback);
}
