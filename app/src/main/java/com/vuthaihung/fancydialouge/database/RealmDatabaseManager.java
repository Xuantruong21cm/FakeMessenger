package com.vuthaihung.fancydialouge.database;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmQuery;

public class RealmDatabaseManager<T extends RealmObject> implements DatabaseManager<T> {
    private Realm realm;

    public RealmDatabaseManager(Realm realm) {
        this.realm = realm;
    }

    @Override
    public void create(T object) {
        realm.executeTransaction(realmModel -> {
            realmModel.createObject(object.getClass());
        });
    }

    @Override
    public void save(T object) {
        realm.executeTransaction(realmModel -> {
            realmModel.insert(object);
        });
    }

    @Override
    public void update(T object) {
        realm.executeTransaction(realmModel -> {
            realmModel.insertOrUpdate(object);
        });
    }

    @Override
    public void delete(T object) {
        realm.executeTransaction(realmModel -> {
            object.deleteFromRealm();
        });
    }

    @Override
    public void deleteAll() {
        realm.executeTransaction(realmModel -> {
            realmModel.deleteAll();
        });
    }

    @Override
    public RealmQuery realmQuery(Class<T> type) {
        return realm.where(type);
    }

    @Override
    public List<T> fetch(RealmQuery<T> query) {
        return query.findAll();
    }
}
