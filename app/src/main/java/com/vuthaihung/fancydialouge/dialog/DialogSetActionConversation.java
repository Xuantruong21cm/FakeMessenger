package com.vuthaihung.fancydialouge.dialog;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.vuthaihung.fancydialouge.adapter.ActiveStatusAdapter;
import com.vuthaihung.fancydialouge.database.repository.AppDataRepository;
import com.vuthaihung.fancydialouge.databinding.DialogSetActiveConversationBinding;
import com.vuthaihung.fancydialouge.model.ConversationModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DialogSetActionConversation extends BaseDialog<DialogSetActionConversation.ExtendBuilder> {

    private DialogSetActiveConversationBinding binding;
    private ExtendBuilder extendBuilder;
    private ActiveStatusAdapter activeStatusAdapter;
    private List<ConversationModel> lstData = new ArrayList<>();

    public DialogSetActionConversation(ExtendBuilder builder) {
        super(builder);
        this.extendBuilder = builder;
    }

    @Override
    protected void initView() {
        super.initView();
        lstData.clear();
        lstData.addAll(extendBuilder.lstConversation);
        binding.tvNoData.setVisibility(lstData.isEmpty() ? View.VISIBLE : View.GONE);
        binding.rcvData.setVisibility(lstData.isEmpty() ? View.INVISIBLE : View.VISIBLE);

        activeStatusAdapter = new ActiveStatusAdapter(getContext(), lstData);
        binding.rcvData.setAdapter(activeStatusAdapter);
    }

    @Override
    protected TextView getNegativeButton() {
        return binding.tvNegative;
    }

    @Override
    protected TextView getPositiveButton() {
        return binding.tvPositive;
    }

    @Override
    protected void initControl() {

    }

    @Override
    protected void handleClickPositiveButton(HashMap<String, Object> datas) {
        AppDataRepository repository = AppDataRepository.getRepository();
        repository.saveAllConversation(lstData);
        super.handleClickPositiveButton(datas);
    }

    @Override
    protected View getLayoutResource() {
        binding = DialogSetActiveConversationBinding.inflate(LayoutInflater.from(getContext()));
        return binding.getRoot();
    }

    public static class ExtendBuilder extends BuilderDialog {

        private List<ConversationModel> lstConversation = new ArrayList<>();

        public ExtendBuilder setLstConversation(List<ConversationModel> lstConversation) {
            this.lstConversation.clear();
            this.lstConversation.addAll(lstConversation);
            return this;
        }

        @Override
        public BaseDialog build() {
            return new DialogSetActionConversation(this);
        }
    }
}
