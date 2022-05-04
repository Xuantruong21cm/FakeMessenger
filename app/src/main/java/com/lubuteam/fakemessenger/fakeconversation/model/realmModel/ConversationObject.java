package com.lubuteam.fakemessenger.fakeconversation.model.realmModel;

import com.lubuteam.fakemessenger.fakeconversation.model.ConversationModel;
import com.lubuteam.fakemessenger.fakeconversation.model.MessageModel;
import com.lubuteam.fakemessenger.fakeconversation.model.UserMessageModel;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ConversationObject extends RealmObject {

    @PrimaryKey
    private long id;
    private String name;
    private String image;
    private String color;
    private String friendOn;
    private String liveIn;
    private boolean isGroup;
    private boolean isBlock;
    private long lastTimeChat;
    private boolean isActive;
    private String timeActiveAgo;
    /* 1: seeen - 2: Received - 3: Not seen - 4: Not received*/
    private int status;
    private RealmList<MessageObject> messageObjects = new RealmList<>();
    private RealmList<UserMessageObject> userMessageObjects = new RealmList<>();

    public ConversationObject() {
    }

    public ConversationObject(ConversationModel conversationModel) {
        this.id = conversationModel.getId();
        this.color = conversationModel.getColor();
        this.name = conversationModel.getName();
        this.image = conversationModel.getImage();
        this.friendOn = conversationModel.getFriendOn();
        this.liveIn = conversationModel.getLiveIn();
        this.isGroup = conversationModel.isGroup();
        this.lastTimeChat = conversationModel.getLastTimeChat();
        this.isActive = conversationModel.isActive();
        this.status = conversationModel.getStatus();
        this.timeActiveAgo = conversationModel.getTimeActiveAgo();
        this.isBlock = conversationModel.isBlock();
        for (MessageModel messageModel : conversationModel.getMessageModels()) {
            this.messageObjects.add(new MessageObject(messageModel));
        }
        for (UserMessageModel userMessageModel : conversationModel.getUserMessageModels()) {
            this.userMessageObjects.add(new UserMessageObject(userMessageModel));
        }
    }

    public boolean isBlock() {
        return isBlock;
    }

    public void setBlock(boolean block) {
        isBlock = block;
    }

    public String getTimeActiveAgo() {
        return timeActiveAgo;
    }

    public void setTimeActiveAgo(String timeActiveAgo) {
        this.timeActiveAgo = timeActiveAgo;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public long getLastTimeChat() {
        return lastTimeChat;
    }

    public void setLastTimeChat(long lastTimeChat) {
        this.lastTimeChat = lastTimeChat;
    }

    public boolean isGroup() {
        return isGroup;
    }

    public void setGroup(boolean group) {
        isGroup = group;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getFriendOn() {
        return friendOn;
    }

    public void setFriendOn(String friendOn) {
        this.friendOn = friendOn;
    }

    public String getLiveIn() {
        return liveIn;
    }

    public void setLiveIn(String liveIn) {
        this.liveIn = liveIn;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public RealmList<MessageObject> getMessageObjects() {
        return messageObjects;
    }

    public void setMessageObjects(RealmList<MessageObject> messageObjects) {
        this.messageObjects = messageObjects;
    }

    public RealmList<UserMessageObject> getUserMessageObjects() {
        return userMessageObjects;
    }

    public void setUserMessageObjects(RealmList<UserMessageObject> userMessageObjects) {
        this.userMessageObjects = userMessageObjects;
    }
}
