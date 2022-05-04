package com.lubuteam.fakemessenger.fakeconversation.model.realmModel;

import com.lubuteam.fakemessenger.fakeconversation.model.UserMessageModel;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class UserMessageObject extends RealmObject {

    @PrimaryKey
    private long id;
    private String avatar;
    private String name;

    public UserMessageObject() {
    }

    public UserMessageObject(UserMessageModel messengerModel) {
        this.id = messengerModel.getId();
        this.avatar = messengerModel.getAvatar();
        this.name = messengerModel.getName();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
