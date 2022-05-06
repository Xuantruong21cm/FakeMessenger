package com.merryblue.fakemessenger;

import static com.merryblue.fakemessenger.Config.NOTIFICATION_ID;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import io.realm.Realm;

public class App extends Application {

    private static App instance;

    public static App getInstace() {
        if (instance == null)
            instance = new App();
        return instance;
    }

    private static void setInstance(App instance) {
        App.instance = instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (instance == null)
            setInstance(App.this);
        PreferencesHelper.init(this);
        createNotificationChannel();
        Realm.init(this);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence charSequence = "FakeMessenger2022";
            String description = "Channel of notification";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_ID, charSequence, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
