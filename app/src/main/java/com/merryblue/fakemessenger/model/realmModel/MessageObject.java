package com.merryblue.fakemessenger.model.realmModel;

import com.merryblue.fakemessenger.model.MessageModel;

import io.realm.RealmObject;

public class MessageObject extends RealmObject {
    private long dateTime;
    private String chatContent;
    private boolean isSend;
    private int type;
    private long userOwn;
    private int emoji;

    public MessageObject() {
    }

    public MessageObject(MessageModel messageModel) {
        this.dateTime = messageModel.getDateTime();
        this.chatContent = messageModel.getChatContent();
        this.type = messageModel.getType();
        this.userOwn = messageModel.getUserOwn();
        this.isSend = messageModel.isSend();
        this.emoji = messageModel.getEmoji();
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }

    public String getChatContent() {
        return chatContent;
    }

    public void setChatContent(String chatContent) {
        this.chatContent = chatContent;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getUserOwn() {
        return userOwn;
    }

    public void setUserOwn(long userOwn) {
        this.userOwn = userOwn;
    }

    public boolean isSend() {
        return isSend;
    }

    public void setSend(boolean send) {
        isSend = send;
    }

    public int getEmoji() {
        return emoji;
    }

    public void setEmoji(int emoji) {
        this.emoji = emoji;
    }
}
