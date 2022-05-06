package com.merryblue.fakemessenger.ui.detailScreen;

import com.merryblue.fakemessenger.database.repository.AppDataRepository;
import com.merryblue.fakemessenger.model.ConversationModel;
import com.merryblue.fakemessenger.model.realmModel.ConversationObject;
import com.merryblue.fakemessenger.Config;

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
