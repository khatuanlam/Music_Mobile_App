package com.example.music_mobile_app.receiver;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.example.music_mobile_app.util.MyAlertDialog;

public class MyCorruptInternetReceiver extends BroadcastReceiver {
    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (isConnected) {
            if(MyAlertDialog.justCorrupt)
            {
                MyAlertDialog.showAlertConnect(context, "Internet","Đã có kết nối Internet");
                MyAlertDialog.justCorrupt = false;
            }
        } else {
            MyAlertDialog.justCorrupt = true;
            MyAlertDialog.showAlertDisconnect(context, "Internet","Vui lòng kết nối mạng để sử dụng ứng dụng");
        }
    }
}
