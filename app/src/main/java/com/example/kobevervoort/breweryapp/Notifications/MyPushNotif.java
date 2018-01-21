package com.example.kobevervoort.breweryapp.Notifications;

import android.app.NotificationManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.kobevervoort.breweryapp.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Helena on 21/01/18.
 */

public class MyPushNotif extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d("msg", "onMessageReceived: " + remoteMessage.getData().get("message"));
        NotificationCompat.Builder builder = new  NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Beware!")
                .setContentText(remoteMessage.getData().get("message"));

        NotificationManager manager = (NotificationManager)     getSystemService(NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());

    }
}
