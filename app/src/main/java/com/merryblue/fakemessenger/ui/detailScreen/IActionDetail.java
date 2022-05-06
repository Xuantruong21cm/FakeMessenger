package com.merryblue.fakemessenger.ui.detailScreen;

import android.view.View;

import com.merryblue.fakemessenger.model.ConversationModel;
import com.merryblue.fakemessenger.model.UserMessageModel;

public interface IActionDetail {
    interface IView {
        void showPopupInfor(View view);

        void showPopupOptSend();

        void showDialogEditInfor();

        void showPopupOptItemMessager(int position, int type, View view);

        void setImageConversation();

        void setNameConversation();

        void setDataScrollView();

        void setStatusItemBottom(boolean isChating, boolean foreShow);

        void setColorConversation();

        void setStatusBlock(boolean isBlock);

        void cleanChatAfterSend();

        void cleanImageAfterSend();

        void scrollLastItemMessage();

        void validateMemberSend(boolean isSend, MemberSelectLisnter memberSelectLisnter);

        void saveImage();
    }

    interface IPresenter {
        void updateConvesation(ConversationModel model);
    }

    interface MemberSelectLisnter {
        void onMemberSelectListener(UserMessageModel userMessageModel);
    }
}
