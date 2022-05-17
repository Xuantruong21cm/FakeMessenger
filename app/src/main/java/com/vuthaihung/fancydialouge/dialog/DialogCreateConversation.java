package com.vuthaihung.fancydialouge.dialog;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.vuthaihung.fancydialouge.Config;
import com.bumptech.glide.Glide;
import com.vuthaihung.fancydialouge.R;
import com.vuthaihung.fancydialouge.databinding.DialogCreateConversationBinding;

import java.util.HashMap;

public class DialogCreateConversation extends BaseDialog<DialogCreateConversation.ExtendBuilder> {

    private DialogCreateConversationBinding conversationBinding;
    private ExtendBuilder extendBuilder;
    private String urlAvatar;

    public DialogCreateConversation(ExtendBuilder builder) {
        super(builder);
        this.extendBuilder = builder;
    }

    @Override
    protected View getLayoutResource() {
        conversationBinding = DialogCreateConversationBinding.inflate(LayoutInflater.from(getContext()));
        return conversationBinding.getRoot();
    }

    @Override
    protected TextView getTitle() {
        return conversationBinding.tvTitle;
    }

    @Override
    protected TextView getNegativeButton() {
        return conversationBinding.tvNegative;
    }

    @Override
    protected TextView getPositiveButton() {
        return conversationBinding.tvPositive;
    }

    @Override
    protected void handleClickPositiveButton(HashMap<String, Object> datas) {
        if (validate()) {
            datas.put(Config.KEY_TITLE, conversationBinding.edtInput.getText().toString());
            datas.put(Config.KEY_AVATAR, urlAvatar);
            datas.put(Config.KEY_GROUP, extendBuilder.isCreateGroup);
            super.handleClickPositiveButton(datas);
        }
    }

    @Override
    protected void initControl() {
        conversationBinding.imAvatar.setOnClickListener(v -> {
            if (extendBuilder.selectImageListener != null)
                extendBuilder.selectImageListener.onSelectImage(this);
        });
    }

    private boolean validate() {
        if (TextUtils.isEmpty(conversationBinding.edtInput.getText().toString())) {
            Toast.makeText(getContext()
                    , getString(R.string.you_must_enter, getString(R.string.convesation_name))
                    , Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public void setImageConversation(String url) {
        this.urlAvatar = url;
        Glide.with(this)
                .load(urlAvatar)
                .skipMemoryCache(true)
                .placeholder(R.drawable.ic_user_default)
                .into(conversationBinding.imAvatar);
    }

    public static class ExtendBuilder extends BuilderDialog {

        private SelectImageListener selectImageListener;
        private boolean isCreateGroup;

        public ExtendBuilder setCreateGroup(boolean createGroup) {
            isCreateGroup = createGroup;
            return this;
        }

        public interface SelectImageListener {
            void onSelectImage(DialogCreateConversation dialog);
        }

        public ExtendBuilder onSelectImageListener(SelectImageListener selectImageListener) {
            this.selectImageListener = selectImageListener;
            return this;
        }

        @Override
        public BaseDialog build() {
            return new DialogCreateConversation(this);
        }
    }
}
