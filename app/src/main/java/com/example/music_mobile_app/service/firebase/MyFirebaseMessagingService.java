package com.example.music_mobile_app.service.firebase;

import android.Manifest;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.music_mobile_app.R;
import com.example.music_mobile_app.service.mydatabase.impl.MyFirebaseServiceImpl;
import com.example.music_mobile_app.service.mydatabase.myinterface.MyFirebaseService;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private MyFirebaseService myFirebaseService;

    public MyFirebaseMessagingService()
    {
        myFirebaseService = new MyFirebaseServiceImpl();
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.d("TOKEN FIREBASE", token);
        myFirebaseService.postToken(token);
    }



    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
//        Log.d("FCM", "Message AAAAAA");
//        System.out.println(remoteMessage.getData());
        super.onMessageReceived(remoteMessage);

        String title = "default";
        String body = "default";
        if (remoteMessage.getData().size() > 0) {
            Map<String, String> data = remoteMessage.getData();
            title = data.get("title");
            body = data.get("body");
        } else {
            Log.d("FCM", "Message without data payload received.");
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "firebase's notification")
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.ic_add_circle_white);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        notificationManager.notify(0, builder.build());
    }
}