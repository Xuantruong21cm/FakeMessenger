package com.lubuteam.fakemessenger.fakeconversation.ui.detailScreen;

import com.lubuteam.fakemessenger.fakeconversation.database.repository.AppDataRepository;
import com.lubuteam.fakemessenger.fakeconversation.model.ConversationModel;
import com.lubuteam.fakemessenger.fakeconversation.model.realmModel.ConversationObject;
import com.lubuteam.fakemessenger.fakeconversation.utils.Config;

public class DetailPresenter implements IActionDetail.IPresenter {

    private ConversationDetailActivity view;
    private AppDataRepository repository;

    public DetailPresenter(ConversationDetailActivity view) {
        this.view = view;
        repository = AppDataRepository.getRepository();
    }

    @Override
    public void updateConvesation(ConversationModel model) {
        ConversationObject modelObject = new ConversationObject(model);

        if (!modelObject.getMessageObjects().isEmpty() && modelObject.getMessageObjects().get(0).getType() == Config.TYPE_HEAEDER)
            modelObject.getMessageObjects().remove(0);
        repository.update(modelObject);
    }
}
