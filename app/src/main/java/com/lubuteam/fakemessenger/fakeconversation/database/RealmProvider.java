package com.lubuteam.fakemessenger.fakeconversation.database;


import com.lubuteam.fakemessenger.fakeconversation.model.realmModel.RealmMessengerModule;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class RealmProvider {
    private RealmConfiguration realmConfiguration;

    public RealmProvider(RealmConfiguration realmConfiguration) {
        this.realmConfiguration = realmConfiguration;
    }

    private Realm realm = Realm.getInstance(realmConfiguration);

    public Realm getRealm() {
        return realm;
    }

    private static RealmConfiguration fakeMessengereConfig = new RealmConfiguration.Builder()
            .name("FakeMessenger.realm")
            .schemaVersion(2)
            .modules(new RealmMessengerModule())
            .deleteRealmIfMigrationNeeded()
            .build();

    public static Realm realmMessenger = Realm.getInstance(fakeMessengereConfig);
}
