package com.example.music_mobile_app.util;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.provider.Settings;

public class WifiStateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(action)) {
            int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, -1);
            if (wifiState == WifiManager.WIFI_STATE_DISABLED) {
                // Gọi hàm bạn muốn chạy khi tắt Wi-Fi ở đây
                doSomethingWhenWifiTurnedOff();
            }
        }
    }

    private void doSomethingWhenWifiTurnedOff() {
        // Thực hiện các hành động bạn muốn khi tắt Wi-Fi
//        showWifiConnectDialog();
    }

    public static void showWifiConnectDialog(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Yêu cầu kết nối Wi-Fi");
        builder.setMessage("Ứng dụng cần kết nối Wi-Fi để tiếp tục. Vui lòng bật Wi-Fi.");
        builder.setPositiveButton("Cài đặt", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Chuyển đến cài đặt Wi-Fi
                context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            }
        });
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Đóng Dialog nếu người dùng không muốn kết nối Wi-Fi
                dialog.dismiss();
            }
        });
        builder.setCancelable(false); // Ngăn người dùng nhấn nút Back để đóng Dialog
        builder.show();
    }
}