package com.lubuteam.fakemessenger.fakeconversation.database.repository;

import com.lubuteam.fakemessenger.fakeconversation.database.DatabaseManager;

import java.util.List;

import io.realm.RealmQuery;

public class BaseRepository<T> {
    private DatabaseManager<T> databaseManager;

    public BaseRepository(DatabaseManager<T> databaseManager) {
        this.databaseManager = databaseManager;
    }

    public void create(T object) {
        this.databaseManager.create(object);
    }

    public void save(T object) {
        this.databaseManager.save(object);
    }

    public void update(T object) {
        this.databaseManager.update(object);
    }

    public void delete(T object) {
        this.databaseManager.delete(object);
    }

    public void deleteAll() {
        this.databaseManager.deleteAll();
    }

    public RealmQuery realmQuery(Class<T> type) {
        return this.databaseManager.realmQuery(type);
    }

    public List<T> fetch(RealmQuery<T> query) {
        return this.databaseManager.fetch(query);
    }
}
