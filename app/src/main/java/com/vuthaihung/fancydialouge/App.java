package com.vuthaihung.fancydialouge;

import static com.vuthaihung.fancydialouge.Config.NOTIFICATION_ID;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.merryblue.fakemessenger.utils.AppOpenManager;

import io.realm.Realm;

public class App extends Application {

    private static App instance;
    private AppOpenManager appOpenManager;

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
        appOpenManager = new AppOpenManager(this);
        appOpenManager.fetchAd();
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
