package com.lubuteam.fakemessenger.fakeconversation.ui.mainScreen;

import android.content.Intent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.DialogFragment;

import com.lubuteam.fakemessenger.fakeconversation.R;
import com.lubuteam.fakemessenger.fakeconversation.adapter.ConversationHorizontalAdapter;
import com.lubuteam.fakemessenger.fakeconversation.adapter.ConversationVerticalAdapter;
import com.lubuteam.fakemessenger.fakeconversation.databinding.ActivityMainBinding;
import com.lubuteam.fakemessenger.fakeconversation.databinding.LayoutMainBottomBinding;
import com.lubuteam.fakemessenger.fakeconversation.databinding.LayoutMainTopBinding;
import com.lubuteam.fakemessenger.fakeconversation.dialog.DialogCreateConversation;
import com.lubuteam.fakemessenger.fakeconversation.dialog.DialogSetActionConversation;
import com.lubuteam.fakemessenger.fakeconversation.model.ConversationModel;
import com.lubuteam.fakemessenger.fakeconversation.ui.BaseActivity;
import com.lubuteam.fakemessenger.fakeconversation.ui.detailScreen.ConversationDetailActivity;
import com.lubuteam.fakemessenger.fakeconversation.utils.Config;
import com.lubuteam.fakemessenger.fakeconversation.utils.ImageUtils;
import com.lubuteam.fakemessenger.fakeconversation.utils.PreferencesHelper;
import com.lubuteam.fakemessenger.fakeconversation.utils.ViewUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends BaseActivity implements IActionMain.IView, View.OnClickListener {

    private ActivityMainBinding activityMainBinding;
    private LayoutMainBottomBinding bottomBinding;
    private LayoutMainTopBinding topBinding;
    private MainPresenter presenter;
    private ConversationHorizontalAdapter horizontalAdapter;
    private ConversationVerticalAdapter verticalAdapter;

    @Override
    protected View getLayoutResource() {
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        bottomBinding = activityMainBinding.layoutBottom;
        topBinding = activityMainBinding.layoutTop;
        return activityMainBinding.getRoot();
    }

    @Override
    protected void initView() {
        ViewUtils.setupUI(activityMainBinding.container, this);
        presenter = new MainPresenter(this);
        horizontalAdapter = new ConversationHorizontalAdapter(this, new ArrayList<>());
        activityMainBinding.rcvHorizontal.setAdapter(horizontalAdapter);

        verticalAdapter = new ConversationVerticalAdapter(this, new ArrayList<>(), this::showPopupItemConversation);
        activityMainBinding.rcvVertical.setAdapter(verticalAdapter);
    }

    @Override
    protected void initData() {
        if (presenter != null)
            presenter.getListConversation();
        setImageAvatar(PreferencesHelper.getString(PreferencesHelper.PHOTO_AVATAR_PATH));
    }

    @Override
    protected void initControl() {
        topBinding.imAvatar.setOnClickListener(this);
        topBinding.imEdit.setOnClickListener(this);
        activityMainBinding.swipeRefreshLayout.setOnRefreshListener(() -> {
            activityMainBinding.swipeRefreshLayout.setRefreshing(false);
            if (presenter != null)
                presenter.getListConversation();
        });

        horizontalAdapter.setOnItemClickListener(position -> {
            if (position != 0)
                return;
            List<ConversationModel> lstData = new ArrayList<>();
            for (ConversationModel model : verticalAdapter.list) {
                if (!model.isGroup()) {
                    lstData.add(model);
                }
            }
            new DialogSetActionConversation.ExtendBuilder()
                    .setLstConversation(lstData)
                    .onSetPositiveButton(getString(R.string.ok), (baseDialog, data) -> {
                        if (presenter != null) {
                            presenter.getListConversation();
                        }
                        baseDialog.dismiss();
                    })
                    .onSetNegativeButton(getString(R.string.cancel), baseDialog -> {
                        if (presenter != null) {
                            presenter.getListConversation();
                        }
                        baseDialog.dismiss();
                    })
                    .build()
                    .show(getSupportFragmentManager(), DialogSetActionConversation.class.getName());
        });

        verticalAdapter.setOnItemClickListener(position -> {
            Intent intent = new Intent(this, ConversationDetailActivity.class);
            intent.putExtra(ConversationDetailActivity.DATA_SELECT, verticalAdapter.list.get(position));
            startActivityForResult(intent, Config.REQUEST_CODE_ACT);
        });
    }

    @Override
    protected View getViewPadding() {
        return activityMainBinding.container;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.im_avatar:
                try {
                    askPermissionStorage(() -> {
                        requestGetGallery(path -> {
                            PreferencesHelper.putString(PreferencesHelper.PHOTO_AVATAR_PATH, path);
                            setImageAvatar(path);
                            if (verticalAdapter != null)
                                verticalAdapter.notifyDataSetChanged();
                        });
                        return null;
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.im_edit:
                showPopupSelectCreateChat();
                break;
        }
    }

    @Override
    public void showPopupSelectCreateChat() {
        PopupMenu popup = new PopupMenu(MainActivity.this, topBinding.imEdit);
        popup.getMenuInflater()
                .inflate(R.menu.menu_create_conversation, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_create_chat:
                    showDialogCreateChat(false);
                    break;
                case R.id.menu_create_group:
                    showDialogCreateChat(true);
                    break;
                case R.id.menu_add_contact:
                    try {
                        askPermissionContact(() -> {
                            requestGetContact((name, image) -> {
                                HashMap<String, Object> datas = new HashMap<>();
                                datas.put(Config.KEY_TITLE, name);
                                datas.put(Config.KEY_AVATAR, image);
                                datas.put(Config.KEY_GROUP, false);
                                if (presenter != null) {
                                    presenter.createConvesation(datas);
                                }
                            });
                            return null;
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
            }
            return true;
        });
        popup.show();
    }

    @Override
    public void showPopupItemConversation(int index, View itemView) {
        PopupMenu popup = new PopupMenu(MainActivity.this, itemView);
        popup.getMenuInflater()
                .inflate(R.menu.menu_item_conversation, popup.getMenu());
        ConversationModel conversationModel = verticalAdapter.list.get(index);
        popup.setOnMenuItemClickListener(item -> {
            if (presenter == null)
                return true;
            switch (item.getItemId()) {
                case R.id.menu_delete:
                    presenter.deleteConvesation(conversationModel);
                    break;
                case R.id.menu_set_seen:
                    presenter.setStatusConversation(conversationModel, Config.STATUS_SEEN);
                    verticalAdapter.notifyItemChanged(index);
                    if (presenter != null)
                        presenter.getCountReceived();
                    break;
                case R.id.menu_set_received:
                    presenter.setStatusConversation(conversationModel, Config.STATUS_RECEIVED);
                    verticalAdapter.notifyItemChanged(index);
                    if (presenter != null)
                        presenter.getCountReceived();
                    break;
                case R.id.menu_not_send:
                    presenter.setStatusConversation(conversationModel, Config.STATUS_NOT_SEND);
                    verticalAdapter.notifyItemChanged(index);
                    if (presenter != null)
                        presenter.getCountReceived();
                    break;
                case R.id.menu_not_received:
                    presenter.setStatusConversation(conversationModel, Config.STATUS_NOT_RECEIVED);
                    verticalAdapter.notifyItemChanged(index);
                    if (presenter != null)
                        presenter.getCountReceived();
                    break;
            }
            return true;
        });
        popup.show();
    }

    @Override
    public void setImageAvatar(String url) {
        ImageUtils.loadImage(topBinding.imAvatar, url);
    }

    @Override
    public void showDialogCreateChat(boolean isCreateGroup) {
        new DialogCreateConversation.ExtendBuilder()
                .onSelectImageListener(dialog -> {
                    try {
                        askPermissionStorage(() -> {
                            requestGetGallery(dialog::setImageConversation);
                            return null;
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                })
                .setCreateGroup(isCreateGroup)
                .setTitle(getString(isCreateGroup ? R.string.create_group : R.string.create_chat))
                .onSetPositiveButton(getString(R.string.ok), (baseDialog, data) -> {
                    if (presenter != null) {
                        presenter.createConvesation(data);
                    }
                    baseDialog.dismiss();
                })
                .onSetNegativeButton(getString(R.string.cancel), DialogFragment::dismiss)
                .build()
                .show(getSupportFragmentManager(), DialogCreateConversation.class.getName());
    }

    @Override
    public void fillListDataHorizontal(List<ConversationModel> lstData) {
        horizontalAdapter.clear();
        horizontalAdapter.addAll(lstData);
    }

    @Override
    public void fillListDataVertical(List<ConversationModel> lstData) {
        verticalAdapter.clear();
        verticalAdapter.addAll(lstData);
    }

    @Override
    public void setCountReceiverd(int count) {
        bottomBinding.tvCountReceived.setVisibility(count == 0 ? View.GONE : View.VISIBLE);
        if (count < 100) {
            bottomBinding.tvCountReceived.setText(String.valueOf(count));
        } else {
            bottomBinding.tvCountReceived.setText("99+");
        }
    }

    @Override
    public void deleteConversationSuccess() {
        toast(getString(R.string.delete_conversation_success));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Config.REQUEST_CODE_ACT) {
            if (presenter != null)
                presenter.getListConversation();
        }
    }
}