package com.vuthaihung.fancydialouge.ui.mainScreen;

import android.view.View;

import com.vuthaihung.fancydialouge.model.ConversationModel;

import java.util.HashMap;
import java.util.List;

public interface IActionMain {
    interface IView {
        void showPopupSelectCreateChat();

        void showPopupItemConversation(int index, View item);

        void setImageAvatar(String url);

        void showDialogCreateChat(boolean isCreateGroup);

        void fillListDataHorizontal(List<ConversationModel> lstData);

        void fillListDataVertical(List<ConversationModel> lstData);

        void deleteConversationSuccess();

        void setCountReceiverd(int count);

    }

    interface IPresenter {
        void createConvesation(HashMap<String, Object> data);

        void getListConversation();

        void deleteConvesation(ConversationModel conversationModel);

        void setStatusConversation(ConversationModel conversation, int status);

        void getCountReceived();
    }
}
