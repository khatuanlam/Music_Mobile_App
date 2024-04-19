package com.example.music_mobile_app.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class MyAlertDialog {
    public static boolean justCorrupt = false;
    private static AlertDialog dialogConnect;
    private static AlertDialog dialogDisconnect;
    public static void showAlertDisconnect(Context context, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(false);
        dialogDisconnect = builder.create();
        if(dialogConnect != null)
            dialogConnect.dismiss();
        dialogDisconnect.show();
    }
    public static void showAlertConnect(Context context, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setCancelable(false);
        dialogConnect = builder.create();
        if(dialogDisconnect != null)
            dialogDisconnect.dismiss();
        dialogConnect.show();
    }
}