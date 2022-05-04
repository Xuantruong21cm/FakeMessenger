package com.lubuteam.fakemessenger.fakeconversation.dialog;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lubuteam.fakemessenger.fakeconversation.R;
import com.lubuteam.fakemessenger.fakeconversation.databinding.DialogEditProfileBinding;
import com.lubuteam.fakemessenger.fakeconversation.utils.Config;

import java.util.HashMap;

public class DialogEditProfile extends BaseDialog<DialogEditProfile.ExtendBuilder> {

    private DialogEditProfileBinding profileBinding;
    private ExtendBuilder extendBuilder;

    public DialogEditProfile(DialogEditProfile.ExtendBuilder builder) {
        super(builder);
        this.extendBuilder = builder;
    }

    @Override
    protected void initView() {
        super.initView();
        if (!TextUtils.isEmpty(extendBuilder.friendOn))
            profileBinding.edtFriendOn.setText(extendBuilder.friendOn);
        if (!TextUtils.isEmpty(extendBuilder.liveIn))
            profileBinding.edtLiveIn.setText(extendBuilder.liveIn);
    }

    @Override
    protected void initControl() {

    }

    @Override
    protected TextView getTitle() {
        return profileBinding.tvTitle;
    }

    @Override
    protected TextView getNegativeButton() {
        return profileBinding.tvNegative;
    }

    @Override
    protected TextView getPositiveButton() {
        return profileBinding.tvPositive;
    }

    @Override
    protected void handleClickPositiveButton(HashMap<String, Object> datas) {
        if (validate()) {
            datas.put(Config.KEY_FRIEND_ON, profileBinding.edtFriendOn.getText().toString());
            datas.put(Config.KEY_LIVE_IN, profileBinding.edtLiveIn.getText().toString());
            super.handleClickPositiveButton(datas);
        }
    }

    private boolean validate() {
        if (TextUtils.isEmpty(profileBinding.edtFriendOn.getText().toString())
                || TextUtils.isEmpty(profileBinding.edtLiveIn.getText().toString())) {
            Toast.makeText(getContext()
                    , getString(R.string.you_must_enter, getString(R.string.full_data))
                    , Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    @Override
    protected View getLayoutResource() {
        profileBinding = DialogEditProfileBinding.inflate(LayoutInflater.from(getContext()));
        return profileBinding.getRoot();
    }

    public static class ExtendBuilder extends BuilderDialog {

        private String friendOn;
        private String liveIn;

        public ExtendBuilder setFriendOn(String friendOn) {
            this.friendOn = friendOn;
            return this;
        }

        public ExtendBuilder setLiveIn(String liveIn) {
            this.liveIn = liveIn;
            return this;
        }

        @Override
        public BaseDialog build() {
            return new DialogEditProfile(this);
        }
    }
}
