package com.merryblue.fakemessenger.model.realmModel;

@io.realm.annotations.RealmModule(library = true, classes = {ConversationObject.class, MessageObject.class, UserMessageObject.class})
public class RealmMessengerModule {
}
