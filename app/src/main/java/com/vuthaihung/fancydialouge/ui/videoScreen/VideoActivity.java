package com.vuthaihung.fancydialouge.ui.videoScreen;

import android.view.View;
import android.widget.ImageView;

import com.vuthaihung.fancydialouge.R;
import com.vuthaihung.fancydialouge.databinding.ActivityVideoBinding;
import com.vuthaihung.fancydialouge.ui.BaseActivity;
import com.vuthaihung.fancydialouge.ImageUtils;
import com.vuthaihung.fancydialouge.Toolbox;
import com.vuthaihung.fancydialouge.ViewUtils;

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
//        videoBinding.imCamera.setOnClickListener(this);
        videoBinding.imMicrophone.setOnClickListener(this);
        videoBinding.imPhone.setOnClickListener(this);
//        videoBinding.imRotateCamera.setOnClickListener(this);
//        videoBinding.imTakePhoto.setOnClickListener(this);
        videoBinding.imShow2.setOnClickListener(this);
        videoBinding.imShow1.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.im_phone:
            case R.id.im_bubble_chat:
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
