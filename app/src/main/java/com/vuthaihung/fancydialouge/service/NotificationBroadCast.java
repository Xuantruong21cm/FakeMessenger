package com.vuthaihung.fancydialouge.service;

import static com.vuthaihung.fancydialouge.Config.NOTIFICATION_ID;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.vuthaihung.fancydialouge.R;
import com.vuthaihung.fancydialouge.ui.mainScreen.SplashActivity;

public class NotificationBroadCast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent _intent = new Intent(context, SplashActivity.class);
        _intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, _intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_ID)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText("Hey, Are you ready for so much FUN now? Let's start right away")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationCompat = NotificationManagerCompat.from(context);
        notificationCompat.notify(200, builder.build());
    }
}