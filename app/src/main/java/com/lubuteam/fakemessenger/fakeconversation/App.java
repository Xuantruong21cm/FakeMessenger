package com.lubuteam.fakemessenger.fakeconversation;

import android.app.Application;

import com.lubuteam.fakemessenger.fakeconversation.utils.PreferencesHelper;

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
        Realm.init(this);

    }

}
