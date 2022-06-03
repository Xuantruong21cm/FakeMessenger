package com.vuthaihung.fancydialouge.ui.selectSticker;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import androidx.appcompat.widget.PopupMenu;

import com.vuthaihung.fancydialouge.R;
import com.vuthaihung.fancydialouge.adapter.StickerAdapter;
import com.vuthaihung.fancydialouge.databinding.ActivitySelectStickerBinding;
import com.vuthaihung.fancydialouge.ui.BaseActivity;
import com.vuthaihung.fancydialouge.Config;

import java.util.ArrayList;
import java.util.List;

public class StickerActivity extends BaseActivity {

    private ActivitySelectStickerBinding stickerBinding;
    private StickerAdapter stickerAdapter;
    private List<String> lstResName = new ArrayList<>();

    @Override
    protected View getLayoutResource() {
        stickerBinding = ActivitySelectStickerBinding.inflate(getLayoutInflater());
        return stickerBinding.getRoot();
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        for (int i = 1; i < 277; i++) {
            lstResName.add("z" + i);
        }
        stickerAdapter = new StickerAdapter(this, lstResName);
        stickerBinding.rcvSticker.setAdapter(stickerAdapter);
    }

    @Override
    protected void initControl() {
        stickerBinding.imBack.setOnClickListener(v -> finish());
        stickerAdapter.setItemClickListener((position, view) -> {
            PopupMenu popup = new PopupMenu(StickerActivity.this, view);
            popup.getMenuInflater()
                    .inflate(R.menu.menu_send_message, popup.getMenu());
            popup.setOnMenuItemClickListener(item -> {
                boolean isSend = item.getItemId() == R.id.menu_send;
                Intent intent = new Intent();
                intent.putExtra(Config.KEY_CONTENT, lstResName.get(position));
                intent.putExtra(Config.KEY_TYPE_SEND, isSend);
                setResult(Activity.RESULT_OK, intent);
                finish();
                return true;
            });
            popup.show();
        });
    }

    @Override
    protected View getViewPadding() {
        return stickerBinding.container;
    }
}
