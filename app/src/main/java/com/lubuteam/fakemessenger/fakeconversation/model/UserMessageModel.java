package com.lubuteam.fakemessenger.fakeconversation.model;

import com.lubuteam.fakemessenger.fakeconversation.model.realmModel.UserMessageObject;

import java.io.Serializable;

public class UserMessageModel implements Serializable {
    private long id;
    private String avatar;
    private String name;

    public UserMessageModel(UserMessageObject userMessageObject) {
        this.id = userMessageObject.getId();
        this.avatar = userMessageObject.getAvatar();
        this.name = userMessageObject.getName();
    }

    public UserMessageModel() {
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
