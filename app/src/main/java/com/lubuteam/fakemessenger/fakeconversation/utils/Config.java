package com.lubuteam.fakemessenger.fakeconversation.utils;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;

import com.lubuteam.fakemessenger.fakeconversation.App;
import com.lubuteam.fakemessenger.fakeconversation.R;
import com.lubuteam.fakemessenger.fakeconversation.model.ConversationModel;
import com.lubuteam.fakemessenger.fakeconversation.model.MessageModel;
import com.lubuteam.fakemessenger.fakeconversation.model.UserMessageModel;

import java.util.List;

public class Config {

    public static final String URL_ABOUT = "https://www.google.com/";
    public static final String URL_POLICY = "https://www.google.com/";
    public static final String EMAIL = "dienMailVaoDay@gmail.com";

    public static final String KEY_TITLE = "title conversation";
    public static final String KEY_AVATAR = "avatar conversation";
    public static final String KEY_GROUP = "conversation is group";
    public static final String KEY_FRIEND_ON = "friend on";
    public static final String KEY_LIVE_IN = "live in";
    public static final String KEY_CONTENT = "message content";
    public static final String KEY_TYPE_SEND = "type send";

    public static final int REQUEST_CODE_ACT = 1257;
    public static final int REQUEST_CODE_ACT_DATETIME = 1258;
    public static final int REQUEST_CODE_ACT_STICKER = 1259;
    public static final int REQUEST_CODE_ACT_SETTING = 1260;
    public static final int REQUEST_CODE_ACT_MEMBER = 1261;
    public static final String RESULRT_DATA = "result data";

    public static final String IC_LIKE_NAME = "sticker_like";

    public static final int STATUS_SEEN = 1;
    public static final int STATUS_RECEIVED = 2;
    public static final int STATUS_NOT_SEND = 3;
    public static final int STATUS_NOT_RECEIVED = 4;

    public static final int TYPE_HEAEDER = -1;
    public static final int TYPE_TEXT = 1;
    public static final int TYPE_DATETIME = 2;
    public static final int TYPE_RECORD = 3;
    public static final int TYPE_STICKER = 4;
    public static final int TYPE_IMAGE = 5;
    public static final int TYPE_REMOVE = 6;

    public static ConversationModel setConverstationDefault() {
        Resources resources = App.getInstace().getResources();
        ConversationModel conversationModel = new ConversationModel();
        conversationModel.setId(System.currentTimeMillis());
        conversationModel.setLastTimeChat(System.currentTimeMillis());
        conversationModel.setActive(true);
        conversationModel.setStatus(STATUS_SEEN);
        conversationModel.setColor("#0084f0");
        conversationModel.setFriendOn(resources.getString(R.string.friend_on_facebook));
        conversationModel.setLiveIn(resources.getString(R.string.live_in_uk));
        return conversationModel;
    }

    public static MessageModel createMessage(String data, int type) {
        MessageModel messageModel = new MessageModel();
        messageModel.setChatContent(data);
        messageModel.setDateTime(System.currentTimeMillis());
        messageModel.setType(type);
        return messageModel;
    }

    public static String getLastMessage(Context context, ConversationModel conversationModel) {
        StringBuilder out = new StringBuilder();
        if (conversationModel == null)
            return "";
        List<MessageModel> lstChat = conversationModel.getMessageModels();
        if (lstChat.isEmpty())
            return "";
        MessageModel messageModel = lstChat.get(lstChat.size() - 1);
        if (messageModel.getType() == Config.TYPE_DATETIME)
            return "";
        if (messageModel.isSend()) {
            out.append(context.getString(R.string.you));
        } else {
            String name;
            if (conversationModel.isGroup()) {
                UserMessageModel userMessageModel = getMemberSendMessage(messageModel.getUserOwn(), conversationModel.getUserMessageModels());
                name = userMessageModel == null ? "" : userMessageModel.getName();
            } else {
                name = conversationModel.getName();
            }
            out.append(!TextUtils.isEmpty(name) ? name.split(" ")[0] : "");
        }
        out.append(" ");
        if (messageModel.getType() == Config.TYPE_TEXT || messageModel.getType() == Config.TYPE_DATETIME) {
            if (!messageModel.isSend()) {
                out.append(": ");
            } else {
                out = new StringBuilder();
            }
            out.append(TextUtils.isEmpty(messageModel.getChatContent()) ? "" : messageModel.getChatContent());
        } else if (messageModel.getType() == Config.TYPE_STICKER)
            out.append(context.getString(R.string.sent_a_sticker));
        else if (messageModel.getType() == Config.TYPE_IMAGE)
            out.append(context.getString(R.string.sent_a_photo));
        else if (messageModel.getType() == Config.TYPE_RECORD)
            out.append(context.getString(R.string.sent_a_voice_clip));
        else if (messageModel.getType() == Config.TYPE_REMOVE)
            out.append(context.getString(R.string.removed_a_message, ""));
        return out.toString();
    }

    public static UserMessageModel getMemberSendMessage(long id, List<UserMessageModel> lstMember) {
        UserMessageModel userMessageModel = new UserMessageModel();
        userMessageModel.setId(System.currentTimeMillis());
        if (lstMember.isEmpty())
            return userMessageModel;
        for (UserMessageModel model : lstMember)
            if (model.getId() == id)
                return model;
        return userMessageModel;
    }

    public static String[] colorHexString = new String[]{
            "#0084f0", "#c43030", "#971670", "#ffb683", "#cc7cff",
            "#2b51bc", "#217ca9", "#2ca9bf", "#59c19c", "#94109a", "#15b9b0",
            "#ee534f", "#f03d81", "#ab46bc", "#7e57c2", "#5e6ac0", "#28b6f6",
            "#25c6da", "#66bb6a", "#9ccc66", "#d4e056", "#ffee58",
            "#ffa827", "#ff7143", "#8c6e63", "#bdbdbd", "#78909c"
    };

    public static int getBackgroundChatResoure(ConversationModel conversationModel, int currentIndex) {
        List<MessageModel> listData = conversationModel.getMessageModels();
        boolean isSend = listData.get(currentIndex).isSend();
        MessageModel messageCurrent = listData.get(currentIndex);
        if (isSend) {
            if (currentIndex == listData.size() - 1) {
                MessageModel messagePrevious = listData.get(currentIndex - 1);
                if (messagePrevious.isSend()) {
                    if (messagePrevious.getType() != Config.TYPE_TEXT)
                        return R.drawable.bg_chat_single;
                    else
                        return R.drawable.bg_chat_right_bottom;
                } else {
                    return R.drawable.bg_chat_single;
                }
            } else {
                MessageModel messageNext = listData.get(currentIndex + 1);
                MessageModel messagePrevious = listData.get(currentIndex - 1);
                if (!messageNext.isSend()) {
                    if (!messagePrevious.isSend() || messagePrevious.getType() != Config.TYPE_TEXT)
                        return R.drawable.bg_chat_single;
                    else
                        return R.drawable.bg_chat_right_bottom;
                } else if (!messagePrevious.isSend()) {
                    if (!messageNext.isSend() || messageNext.getType() != Config.TYPE_TEXT)
                        return R.drawable.bg_chat_single;
                    else
                        return R.drawable.bg_chat_right_top;
                } else {
                    if (messagePrevious.getType() != Config.TYPE_TEXT) {
                        if (messageNext.getType() != Config.TYPE_TEXT)
                            return R.drawable.bg_chat_single;
                        else
                            return R.drawable.bg_chat_right_top;
                    } else if (messageNext.getType() != Config.TYPE_TEXT) {
                        if (messagePrevious.getType() != Config.TYPE_TEXT)
                            return R.drawable.bg_chat_single;
                        else
                            return R.drawable.bg_chat_right_bottom;
                    } else {
                        return R.drawable.bg_chat_right_center;
                    }
                }
            }
        } else {
            MessageModel messagePrevious = listData.get(currentIndex - 1);
            UserMessageModel userPrevious = getMemberSendMessage(messagePrevious.getUserOwn()
                    , conversationModel.getUserMessageModels());
            UserMessageModel userCurrent = getMemberSendMessage(messageCurrent.getUserOwn()
                    , conversationModel.getUserMessageModels());
            if (listData.size() == 2) {
                return R.drawable.bg_chat_single;
            } else if (currentIndex == listData.size() - 1) {
                if (messagePrevious.isSend()) {
                    return R.drawable.bg_chat_single;
                } else {
                    if (userPrevious.getId() == userCurrent.getId()) {
                        if (messagePrevious.getType() == Config.TYPE_TEXT) {
                            return R.drawable.bg_chat_left_bottom;
                        } else {
                            return R.drawable.bg_chat_single;
                        }
                    } else {
                        return R.drawable.bg_chat_single;
                    }
                }
            } else {
                MessageModel messageNext = listData.get(currentIndex + 1);
                UserMessageModel userNext = getMemberSendMessage(messageNext.getUserOwn()
                        , conversationModel.getUserMessageModels());
                if (messageNext.isSend() && messagePrevious.isSend()) {
                    /*tin nhắn trước và sau đều là tin gửi*/
                    return R.drawable.bg_chat_single;
                } else if (!messagePrevious.isSend() && messageNext.isSend()) {
                    /*tin nhắn sau là tin nhận*/
                    if (userPrevious.getId() == userCurrent.getId()) {
                        /*tin sau với tin hiện tại cùng người gửi*/
                        if (messageCurrent.getType() == Config.TYPE_TEXT) {
                            /*tin hiện tại có phải là tin văn bản*/
                            return R.drawable.bg_chat_left_bottom;
                        } else {
                            return R.drawable.bg_chat_single;
                        }
                    } else {
                        return R.drawable.bg_chat_single;
                    }
                } else if (!messageNext.isSend() && messagePrevious.isSend()) {
                    /*Tin nhắn trước là tin nhận*/
                    if (userNext.getId() == userCurrent.getId()) {
                        /*tin trước với tin hiện tại cùng người gửi*/
                        if (messageCurrent.getType() == Config.TYPE_TEXT) {
                            /*tin hiện tại có phải là tin văn bản*/
                            return R.drawable.bg_chat_left_top;
                        } else {
                            return R.drawable.bg_chat_single;
                        }
                    } else {
                        return R.drawable.bg_chat_single;
                    }
                } else {
                    /*tin trước và tin sau đều là tin nhận*/
                    if (userCurrent.getId() == userPrevious.getId()
                            && userCurrent.getId() == userNext.getId()) {
                        /*Cùng 1 người gửi*/
                        if (messageNext.getType() == Config.TYPE_TEXT
                                && messagePrevious.getType() == Config.TYPE_TEXT) {
                            return R.drawable.bg_chat_left_center;
                        } else if (messageNext.getType() == Config.TYPE_TEXT) {
                            return R.drawable.bg_chat_left_top;
                        } else if (messagePrevious.getType() == Config.TYPE_TEXT) {
                            return R.drawable.bg_chat_left_bottom;
                        } else {
                            return R.drawable.bg_chat_single;
                        }
                    } else if (userCurrent.getId() != userPrevious.getId()
                            && userCurrent.getId() == userNext.getId()) {
                        /*Tin sau với tin hiện tại ko cùng người gửi*/
                        if (messageNext.getType() != Config.TYPE_TEXT) {
                            /*Tin truoc ko phải tin văn bản*/
                            return R.drawable.bg_chat_single;
                        } else {
                            return R.drawable.bg_chat_left_top;
                        }
                    } else if (userCurrent.getId() != userNext.getId()
                            && userCurrent.getId() == userPrevious.getId()) {
                        /*Tin trước với tin hiện tại ko cùng người gửi*/
                        if (messagePrevious.getType() != Config.TYPE_TEXT) {
                            /*Tin sau ko phải tin văn bản*/
                            return R.drawable.bg_chat_single;
                        } else {
                            return R.drawable.bg_chat_left_bottom;
                        }
                    } else {
                        return R.drawable.bg_chat_single;
                    }
                }
            }
        }
    }

    public static int getBackgroundRemoveResoure(List<MessageModel> listData, int currentIndex) {
        MessageModel messageCurrent = listData.get(currentIndex);
        if (messageCurrent.isSend()) {
            if (currentIndex == listData.size() - 1) {
                MessageModel messagePrevious = listData.get(currentIndex - 1);
                if (messagePrevious.isSend()) {
                    if (messagePrevious.getType() != Config.TYPE_REMOVE)
                        return R.drawable.bg_remove_single;
                    else
                        return R.drawable.bg_remove_right_bottom;
                } else {
                    return R.drawable.bg_remove_single;
                }
            } else {
                MessageModel messageNext = listData.get(currentIndex + 1);
                MessageModel messagePrevious = listData.get(currentIndex - 1);
                if (!messageNext.isSend()) {
                    if (!messagePrevious.isSend() || messagePrevious.getType() != Config.TYPE_REMOVE)
                        return R.drawable.bg_remove_single;
                    else
                        return R.drawable.bg_remove_right_bottom;
                } else if (!messagePrevious.isSend()) {
                    if (!messageNext.isSend() || messageNext.getType() != Config.TYPE_REMOVE)
                        return R.drawable.bg_remove_single;
                    else
                        return R.drawable.bg_remove_right_top;
                } else {
                    if (messagePrevious.getType() != Config.TYPE_REMOVE) {
                        if (messageNext.getType() != Config.TYPE_REMOVE)
                            return R.drawable.bg_remove_single;
                        else
                            return R.drawable.bg_remove_right_top;
                    } else if (messageNext.getType() != Config.TYPE_REMOVE) {
                        if (messagePrevious.getType() != Config.TYPE_REMOVE)
                            return R.drawable.bg_remove_single;
                        else
                            return R.drawable.bg_remove_right_bottom;
                    } else {
                        return R.drawable.bg_remove_right_center;
                    }
                }
            }
        } else {
            if (currentIndex == listData.size() - 1) {
                MessageModel messagePrevious = listData.get(currentIndex - 1);
                if (!messagePrevious.isSend()) {
                    if (messagePrevious.getType() != Config.TYPE_REMOVE)
                        return R.drawable.bg_remove_single;
                    else
                        return R.drawable.bg_remove_left_bottom;
                } else {
                    return R.drawable.bg_remove_single;
                }
            } else {
                MessageModel messageNext = listData.get(currentIndex + 1);
                MessageModel messagePrevious = listData.get(currentIndex - 1);
                if (messageNext.isSend()) {
                    if (messagePrevious.isSend() || messagePrevious.getType() != Config.TYPE_REMOVE)
                        return R.drawable.bg_remove_single;
                    else
                        return R.drawable.bg_remove_left_bottom;
                } else if (messagePrevious.isSend()) {
                    if (messageNext.isSend() || messageNext.getType() != Config.TYPE_REMOVE)
                        return R.drawable.bg_remove_single;
                    else
                        return R.drawable.bg_remove_left_top;
                } else {
                    if (messagePrevious.getType() != Config.TYPE_REMOVE) {
                        if (messageNext.getType() != Config.TYPE_REMOVE)
                            return R.drawable.bg_remove_single;
                        else
                            return R.drawable.bg_remove_left_top;
                    } else if (messageNext.getType() != Config.TYPE_REMOVE) {
                        if (messagePrevious.getType() != Config.TYPE_REMOVE)
                            return R.drawable.bg_remove_single;
                        else
                            return R.drawable.bg_remove_left_bottom;
                    } else {
                        return R.drawable.bg_remove_left_center;
                    }
                }
            }
        }
    }
}
