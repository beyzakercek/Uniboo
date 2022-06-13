package com.example.uniboo;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;


public class NotificationB extends Application {
    static final String channelID = "c";
    @Override
    public void onCreate(){
        super.onCreate();

        createNotificaton();
    }

    private void createNotificaton() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(
                    channelID, "Notification", NotificationManager.IMPORTANCE_HIGH
            );
            notificationChannel.setDescription("Channel");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

}


