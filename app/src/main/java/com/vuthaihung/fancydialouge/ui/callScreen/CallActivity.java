package com.vuthaihung.fancydialouge.ui.callScreen;

import android.os.Handler;
import android.text.TextUtils;
import android.view.View;

import com.vuthaihung.fancydialouge.R;
import com.vuthaihung.fancydialouge.databinding.ActivityCallBinding;
import com.vuthaihung.fancydialouge.model.ConversationModel;
import com.vuthaihung.fancydialouge.ui.BaseActivity;
import com.vuthaihung.fancydialouge.ui.detailScreen.ConversationDetailActivity;
import com.vuthaihung.fancydialouge.ImageUtils;
import com.vuthaihung.fancydialouge.Toolbox;
import com.vuthaihung.fancydialouge.ViewUtils;

public class CallActivity extends BaseActivity implements View.OnClickListener {

    private ActivityCallBinding callBinding;
    private ConversationModel model;
    private Runnable runnable;
    private Handler handler;
    private int timeSecound = 0;

    @Override
    protected View getLayoutResource() {
        callBinding = ActivityCallBinding.inflate(getLayoutInflater());
        return callBinding.getRoot();
    }

    @Override
    protected void initView() {
        ViewUtils.setupUI(callBinding.container, this);
        Toolbox.setStatusBarHomeTransfer(this);
    }

    @Override
    protected void initData() {
        model = (ConversationModel) getIntent().getSerializableExtra(ConversationDetailActivity.DATA_SELECT);
        if (model == null)
            return;
        if (!TextUtils.isEmpty(model.getName()))
            callBinding.tvName.setText(model.getName());
        ImageUtils.loadImage(callBinding.imShow1, model.getImage());
        ImageUtils.loadImage(callBinding.imAvatar, model.getImage());
        handler = new Handler();
        lifeCycle();
    }

    private void lifeCycle() {
        timeSecound++;
        callBinding.tvTime.setText(Toolbox.secondToTime(timeSecound));
        runnable = () -> {
            lifeCycle();
        };
        handler.postDelayed(runnable, 1000);
    }

    @Override
    protected void initControl() {
        callBinding.imAddUser.setOnClickListener(this);
        callBinding.imBubbleChat.setOnClickListener(this);
//        callBinding.imMagicWard.setOnClickListener(this);
        callBinding.imMicrophone.setOnClickListener(this);
        callBinding.imPhone.setOnClickListener(this);
        callBinding.imShow1.setOnClickListener(this);
        callBinding.imVolume.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.im_phone:
            case R.id.im_bubble_chat:
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}