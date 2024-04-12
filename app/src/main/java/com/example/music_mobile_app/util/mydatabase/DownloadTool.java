package com.example.music_mobile_app.util.mydatabase;

public class DownloadTool{

    public static String getFileName(String urlString) {
        String fileName = "";
        if (urlString.contains("/")) {
            fileName = urlString.substring(urlString.lastIndexOf("/") + 1);
        }
        return fileName;
    }
}
