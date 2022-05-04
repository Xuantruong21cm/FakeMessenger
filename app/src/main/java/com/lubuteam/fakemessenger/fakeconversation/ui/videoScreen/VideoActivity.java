package com.lubuteam.fakemessenger.fakeconversation.ui.videoScreen;

import android.view.View;
import android.widget.ImageView;

import com.lubuteam.fakemessenger.fakeconversation.R;
import com.lubuteam.fakemessenger.fakeconversation.databinding.ActivityVideoBinding;
import com.lubuteam.fakemessenger.fakeconversation.ui.BaseActivity;
import com.lubuteam.fakemessenger.fakeconversation.utils.ImageUtils;
import com.lubuteam.fakemessenger.fakeconversation.utils.Toolbox;
import com.lubuteam.fakemessenger.fakeconversation.utils.ViewUtils;

public class VideoActivity extends BaseActivity implements View.OnClickListener {

    private ActivityVideoBinding videoBinding;

    @Override
    protected View getLayoutResource() {
        videoBinding = ActivityVideoBinding.inflate(getLayoutInflater());
        return videoBinding.getRoot();
    }

    @Override
    protected void initView() {
        ViewUtils.setupUI(videoBinding.container, this);
        Toolbox.setStatusBarHomeTransfer(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initControl() {
        videoBinding.imAddUser.setOnClickListener(this);
        videoBinding.imBubbleChat.setOnClickListener(this);
        videoBinding.imCamera.setOnClickListener(this);
        videoBinding.imMicrophone.setOnClickListener(this);
        videoBinding.imPhone.setOnClickListener(this);
        videoBinding.imRotateCamera.setOnClickListener(this);
        videoBinding.imTakePhoto.setOnClickListener(this);
        videoBinding.imShow2.setOnClickListener(this);
        videoBinding.imShow1.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.im_phone:
                finish();
                break;
            case R.id.im_show_1:
            case R.id.im_show_2:
                try {
                    askPermissionStorage(() -> {
                        requestGetGallery(path -> {
                            ImageUtils.loadImage((ImageView) v, path);
                        });
                        return null;
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
