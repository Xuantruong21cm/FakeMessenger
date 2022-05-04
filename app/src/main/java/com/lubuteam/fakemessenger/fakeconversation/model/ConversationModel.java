package com.lubuteam.fakemessenger.fakeconversation.model;

import com.lubuteam.fakemessenger.fakeconversation.model.realmModel.ConversationObject;
import com.lubuteam.fakemessenger.fakeconversation.model.realmModel.MessageObject;
import com.lubuteam.fakemessenger.fakeconversation.model.realmModel.UserMessageObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ConversationModel implements Serializable {

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
    private List<MessageModel> messageModels = new ArrayList<>();
    private List<UserMessageModel> userMessageModels = new ArrayList<>();

    public ConversationModel() {
    }

    public ConversationModel(ConversationObject conversationObject) {
        this.id = conversationObject.getId();
        this.color = conversationObject.getColor();
        this.name = conversationObject.getName();
        this.image = conversationObject.getImage();
        this.friendOn = conversationObject.getFriendOn();
        this.liveIn = conversationObject.getLiveIn();
        this.isGroup = conversationObject.isGroup();
        this.lastTimeChat = conversationObject.getLastTimeChat();
        this.isActive = conversationObject.isActive();
        this.status = conversationObject.getStatus();
        this.timeActiveAgo = conversationObject.getTimeActiveAgo();
        this.isBlock = conversationObject.isBlock();
        for (MessageObject messageObject : conversationObject.getMessageObjects()) {
            this.messageModels.add(new MessageModel(messageObject));
        }
        for (UserMessageObject userMessageObject : conversationObject.getUserMessageObjects()) {
            this.userMessageModels.add(new UserMessageModel(userMessageObject));
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

    public List<MessageModel> getMessageModels() {
        return messageModels;
    }

    public void setMessageModels(List<MessageModel> messageModels) {
        this.messageModels = messageModels;
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

    public List<UserMessageModel> getUserMessageModels() {
        return userMessageModels;
    }

    public void setUserMessageModels(List<UserMessageModel> userMessageModels) {
        this.userMessageModels = userMessageModels;
    }
}
