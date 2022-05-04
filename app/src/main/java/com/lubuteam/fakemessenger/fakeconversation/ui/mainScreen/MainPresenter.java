package com.lubuteam.fakemessenger.fakeconversation.ui.mainScreen;

import com.lubuteam.fakemessenger.fakeconversation.database.repository.AppDataRepository;
import com.lubuteam.fakemessenger.fakeconversation.model.ConversationModel;
import com.lubuteam.fakemessenger.fakeconversation.model.UserMessageModel;
import com.lubuteam.fakemessenger.fakeconversation.model.realmModel.ConversationObject;
import com.lubuteam.fakemessenger.fakeconversation.utils.Config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainPresenter implements IActionMain.IPresenter {

    private MainActivity view;
    private AppDataRepository repository;

    public MainPresenter(MainActivity view) {
        this.view = view;
        repository = AppDataRepository.getRepository();
    }

    @Override
    public void createConvesation(HashMap<String, Object> data) {
        ConversationModel model = Config.setConverstationDefault();
        model.setName(String.valueOf(data.get(Config.KEY_TITLE)));
        model.setImage(String.valueOf(data.get(Config.KEY_AVATAR)));
        model.setGroup((Boolean) data.get(Config.KEY_GROUP));
        if (!model.isGroup()) {
            List<UserMessageModel> lstMemeber = new ArrayList<>();
            UserMessageModel userMessageModel = new UserMessageModel();
            userMessageModel.setId(model.getId());
            userMessageModel.setName(model.getName());
            userMessageModel.setAvatar(model.getImage());
            lstMemeber.add(userMessageModel);
            model.setUserMessageModels(lstMemeber);
        }
        repository.saveConversation(model);
        getListConversation();
    }

    @Override
    public void getListConversation() {
        List<ConversationModel> lstDataHorizontal = repository.getListDataHorizontal();
        List<ConversationModel> lstDataVertical = repository.getListDataVertical();

        if (view != null) {
            lstDataHorizontal.add(0, null);
            view.fillListDataHorizontal(lstDataHorizontal);
            view.fillListDataVertical(lstDataVertical);
        }

        getCountReceived();
    }

    @Override
    public void deleteConvesation(ConversationModel conversationModel) {
        repository.deleteConversation(conversationModel);
        getListConversation();
        if (view != null)
            view.deleteConversationSuccess();
    }

    @Override
    public void setStatusConversation(ConversationModel conversation, int status) {
        conversation.setStatus(status);
        repository.update(new ConversationObject(conversation));
    }

    @Override
    public void getCountReceived() {
        int countReceived = repository.getCountReceived();
        if (view != null) {
            view.setCountReceiverd(countReceived);
        }
    }
}
