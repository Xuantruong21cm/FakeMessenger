package com.vuthaihung.fancydialouge.model;

import com.vuthaihung.fancydialouge.model.realmModel.MessageObject;

import java.io.Serializable;


public class MessageModel implements Serializable {
    private long dateTime;
    private String chatContent;
    private boolean isSend;
    /*1: Text, 2: DateTime, 3: Emoji*/
    private int type;
    private long userOwn;
    private int emoji;
    /* 1: seeen - 2: Received - 3: Not seen - 4: Not received*/
    private int status;

    public MessageModel() {
    }

    public MessageModel(MessageObject messageObject) {
        this.dateTime = messageObject.getDateTime();
        this.chatContent = messageObject.getChatContent();
        this.type = messageObject.getType();
        this.userOwn = messageObject.getUserOwn();
        this.isSend = messageObject.isSend();
        this.emoji = messageObject.getEmoji();
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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
