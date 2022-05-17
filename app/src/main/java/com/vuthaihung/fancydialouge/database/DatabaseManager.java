package com.vuthaihung.fancydialouge.database;

import java.util.List;

import io.realm.RealmQuery;

public interface DatabaseManager<T> {
    void create(T object);
    void save(T object);
    void update(T object);
    void delete(T object);
    void deleteAll();
    RealmQuery realmQuery(Class<T> type);
    List<T> fetch(RealmQuery<T> query);
}
