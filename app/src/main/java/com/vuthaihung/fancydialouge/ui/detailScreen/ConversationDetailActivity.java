package com.vuthaihung.fancydialouge.ui.detailScreen;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;

import com.ads.control.AdmobHelp;
import com.vuthaihung.fancydialouge.Config;
import com.vuthaihung.fancydialouge.FileUtil;
import com.vuthaihung.fancydialouge.ImageUtils;
import com.vuthaihung.fancydialouge.R;
import com.vuthaihung.fancydialouge.SoftInputAssist;
import com.vuthaihung.fancydialouge.Toolbox;
import com.vuthaihung.fancydialouge.adapter.MessageAdapter;
import com.vuthaihung.fancydialouge.databinding.ActivityDetailConversationBinding;
import com.vuthaihung.fancydialouge.databinding.LayoutDetailBottomBinding;
import com.vuthaihung.fancydialouge.databinding.LayoutDetailSelectImageBinding;
import com.vuthaihung.fancydialouge.databinding.LayoutDetailTopBinding;
import com.vuthaihung.fancydialouge.dialog.DialogCreateRecord;
import com.vuthaihung.fancydialouge.dialog.DialogEditMessage;
import com.vuthaihung.fancydialouge.dialog.DialogEditProfile;
import com.vuthaihung.fancydialouge.dialog.DialogImageScreenshort;
import com.vuthaihung.fancydialouge.dialog.DialogSelectMember;
import com.vuthaihung.fancydialouge.model.ConversationModel;
import com.vuthaihung.fancydialouge.model.MessageModel;
import com.vuthaihung.fancydialouge.model.UserMessageModel;
import com.vuthaihung.fancydialouge.ui.BaseActivity;
import com.vuthaihung.fancydialouge.ui.addDateTimeScreen.DateTimeActivity;
import com.vuthaihung.fancydialouge.ui.addMember.AddMemberActivity;
import com.vuthaihung.fancydialouge.ui.callScreen.CallActivity;
import com.vuthaihung.fancydialouge.ui.selectSticker.StickerActivity;
import com.vuthaihung.fancydialouge.ui.settingScreen.SettingConvesationActivity;
import com.vuthaihung.fancydialouge.ui.videoScreen.VideoActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class ConversationDetailActivity extends BaseActivity implements IActionDetail.IView, View.OnClickListener {

    public static final String DATA_SELECT = "data select";

    private ActivityDetailConversationBinding binding;
    private LayoutDetailTopBinding topBinding;
    private LayoutDetailBottomBinding bottomBinding;
    private LayoutDetailSelectImageBinding selectImageBinding;
    private DetailPresenter presenter;
    private ConversationModel model;
    private List<MessageModel> lstMessage = new ArrayList<>();
    private MessageAdapter messageAdapter;
    private String pathImageSelect;
    private SoftInputAssist softInputAssist;

    @Override
    protected View getLayoutResource() {
        binding = ActivityDetailConversationBinding.inflate(getLayoutInflater());
        topBinding = binding.layoutTop;
        bottomBinding = binding.layoutBottom;
        selectImageBinding = binding.layoutSelectImage;
        return binding.getRoot();
    }

    @Override
    protected View getViewPadding() {
        return binding.container;
    }

    @Override
    protected void initView() {
        softInputAssist = new SoftInputAssist(this);
        presenter = new DetailPresenter(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.tvBlock.setText(Html.fromHtml(getString(R.string.block_content), Html.FROM_HTML_MODE_COMPACT));
        } else {
            binding.tvBlock.setText(Html.fromHtml(getString(R.string.block_content)));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        softInputAssist.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        softInputAssist.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        softInputAssist.onPause();
    }

    @Override
    protected void initData() {
        model = (ConversationModel) getIntent().getSerializableExtra(DATA_SELECT);
        if (model == null)
            return;
        lstMessage.clear();
        lstMessage = model.getMessageModels();
        loadViewStatus();
    }

    private void loadViewStatus() {
        setColorConversation();
        setNameConversation();
        setColorConversation();
        setImageConversation();
        setDataScrollView();
        if (model.isBlock())
            setStatusBlock(true);
    }

    @Override
    public void setDataScrollView() {
        /*add header*/
        if (lstMessage.isEmpty() || lstMessage.get(0).getType() != Config.TYPE_HEAEDER) {
            MessageModel messageModel = new MessageModel();
            messageModel.setType(Config.TYPE_HEAEDER);
            lstMessage.add(0, messageModel);
        }
        messageAdapter = new MessageAdapter(this, model, lstMessage);
        binding.rcvMessage.setAdapter(messageAdapter);
        messageAdapter.setItemClickListener((position, itemType, view) -> {
            if (itemType == Config.TYPE_HEAEDER) {
                showDialogEditInfor();
            } else {
                showPopupOptItemMessager(position, itemType, view);
            }
        });
        scrollLastItemMessage();
    }

    @Override
    public void showDialogEditInfor() {
        new DialogEditProfile.ExtendBuilder()
                .setFriendOn(model.getFriendOn())
                .setLiveIn(model.getLiveIn())
                .setTitle(getString(R.string.edit_profile))
                .onSetPositiveButton(getString(R.string.ok), (baseDialog, data) -> {
                    model.setFriendOn((String) data.get(Config.KEY_FRIEND_ON));
                    model.setLiveIn((String) data.get(Config.KEY_LIVE_IN));
                    if (messageAdapter != null)
                        messageAdapter.notifyItemChanged(0);
                    if (presenter != null)
                        presenter.updateConvesation(model);
                    baseDialog.dismiss();
                })
                .onSetNegativeButton(getString(R.string.cancel), baseDialog -> {
                    baseDialog.dismiss();
                })
                .build()
                .show(getSupportFragmentManager(), DialogEditProfile.class.getName());
    }

    @Override
    public void showPopupOptItemMessager(int position, int type, View view) {
        MessageModel messageModel = lstMessage.get(position);
        PopupMenu popup = new PopupMenu(this, view);
        popup.getMenuInflater()
                .inflate(R.menu.menu_item_message, popup.getMenu());
        if (type == Config.TYPE_RECORD || type == Config.TYPE_STICKER || type == Config.TYPE_IMAGE
                || type == Config.TYPE_REMOVE)
            popup.getMenu().findItem(R.id.menu_edit).setVisible(false);
        if (type == Config.TYPE_REMOVE || type == Config.TYPE_DATETIME)
            popup.getMenu().findItem(R.id.menu_remove_message).setVisible(false);
        if (model.isGroup() || type == Config.TYPE_DATETIME) {
            popup.getMenu().findItem(R.id.menu_remove_message).setVisible(false);
            popup.getMenu().findItem(R.id.menu_set_seen).setVisible(false);
            popup.getMenu().findItem(R.id.menu_set_received).setVisible(false);
            popup.getMenu().findItem(R.id.menu_not_send).setVisible(false);
            popup.getMenu().findItem(R.id.menu_not_received).setVisible(false);
        }
        popup.setOnMenuItemClickListener(item -> {
            if (presenter == null)
                return true;
            switch (item.getItemId()) {
                case R.id.menu_delete:
                    lstMessage.remove(position);
                    if (presenter != null)
                        presenter.updateConvesation(model);
                    if (messageAdapter != null)
                        messageAdapter.notifyDataSetChanged();
                    break;
                case R.id.menu_edit:
                    new DialogEditMessage.ExtendBuilder()
                            .setMessageContent(messageModel.getChatContent())
                            .setTitle(getString(R.string.edit))
                            .onSetPositiveButton(getString(R.string.ok), (baseDialog, data) -> {
                                messageModel.setChatContent((String) data.get(Config.KEY_CONTENT));
                                if (presenter != null)
                                    presenter.updateConvesation(model);
                                if (messageAdapter != null)
                                    messageAdapter.notifyDataSetChanged();
                                baseDialog.dismiss();
                            })
                            .onSetNegativeButton(getString(R.string.cancel), baseDialog -> {
                                baseDialog.dismiss();
                            })
                            .build()
                            .show(getSupportFragmentManager(), DialogEditMessage.class.getName());
                    break;
                case R.id.menu_set_seen:
                    messageModel.setStatus(Config.STATUS_SEEN);
                    if (messageAdapter != null)
                        messageAdapter.notifyItemChanged(position);
                    break;
                case R.id.menu_set_received:
                    messageModel.setStatus(Config.STATUS_RECEIVED);
                    if (messageAdapter != null)
                        messageAdapter.notifyItemChanged(position);
                    break;
                case R.id.menu_not_send:
                    messageModel.setStatus(Config.STATUS_NOT_SEND);
                    if (messageAdapter != null)
                        messageAdapter.notifyItemChanged(position);
                    break;
                case R.id.menu_not_received:
                    messageModel.setStatus(Config.STATUS_NOT_RECEIVED);
                    if (messageAdapter != null)
                        messageAdapter.notifyItemChanged(position);
                    break;
                case R.id.menu_remove_message:
                    messageModel.setType(Config.TYPE_REMOVE);
                    if (messageAdapter != null)
                        messageAdapter.notifyDataSetChanged();
                    break;
            }
            return true;
        });
        popup.show();
    }

    @Override
    protected void initControl() {
        topBinding.imBack.setOnClickListener(this);
        topBinding.imCall.setOnClickListener(this);
        topBinding.imVideo.setOnClickListener(this);
        topBinding.imInfor.setOnClickListener(this);
//        topBinding.imOutline.setOnClickListener(this);
        bottomBinding.edtInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setStatusItemBottom(count != 0, false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        bottomBinding.imMore.setOnClickListener(this);
        bottomBinding.imEmoji.setOnClickListener(this);
        bottomBinding.imSend.setOnClickListener(this);
        bottomBinding.imPicture.setOnClickListener(this);
        bottomBinding.imRecord.setOnClickListener(this);
        bottomBinding.imTakePhoto.setOnClickListener(this);

        selectImageBinding.imRemoveImage.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        if (presenter != null) {
            presenter.updateConvesation(model);
        }
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.im_more:
                if (!bottomBinding.llOpt.isShown()) {
                    setStatusItemBottom(false, true);
                }
                break;
            case R.id.im_back:
                onBackPressed();
                break;
            case R.id.im_call:
                Intent intent = new Intent(this, CallActivity.class);
                intent.putExtra(ConversationDetailActivity.DATA_SELECT, model);
                startActivity(intent);
                break;
            case R.id.im_video:
                startActivity(new Intent(this, VideoActivity.class));
                break;
            case R.id.im_infor:
            case R.id.im_outline:
                showPopupInfor(v);
                break;
            case R.id.im_send:
                showPopupOptSend();
                break;
            case R.id.im_record:
                new DialogCreateRecord.ExtendBuilder()
                        .setTitle(getString(R.string.voice_message))
                        .onSetPositiveButton(getString(R.string.ok), (baseDialog, data) -> {
                            Boolean isSend = (Boolean) data.get(Config.KEY_TYPE_SEND);
                            String time = (String) data.get(Config.KEY_CONTENT);
                            validateMemberSend(isSend, userMessageModel -> {
                                MessageModel message = Config.createMessage(time, Config.TYPE_RECORD);
                                message.setUserOwn(userMessageModel != null
                                        ? userMessageModel.getId()
                                        : model.getId());
                                message.setSend(isSend);
                                lstMessage.add(message);
                                if (presenter != null) {
                                    presenter.updateConvesation(model);
                                }
                                scrollLastItemMessage();
                            });
                            baseDialog.dismiss();
                        })
                        .onSetNegativeButton(getString(R.string.cancel), baseDialog -> {
                            baseDialog.dismiss();
                        })
                        .build()
                        .show(getSupportFragmentManager(), DialogEditMessage.class.getName());
                break;
            case R.id.im_emoji:
                Intent intentEmoji = new Intent(this, StickerActivity.class);
                startActivityForResult(intentEmoji, Config.REQUEST_CODE_ACT_STICKER);
                break;
            case R.id.im_picture:
                try {
                    askPermissionStorage(() -> {
                        requestGetGallery(path -> {
                            pathImageSelect = path;
                            selectImageBinding.container.setVisibility(View.VISIBLE);
                            ImageUtils.loadImageDefault(selectImageBinding.imPreview, path);
                            bottomBinding.imSend.setImageResource(R.drawable.ic_send);
                        });
                        return null;
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.im_remove_image:
                cleanImageAfterSend();
                break;
            case R.id.im_take_photo:
                new AlertDialog.Builder(this)
                        .setMessage(getString(R.string.ask_save_image))
                        .setPositiveButton(R.string.ok, (dialog, which) -> {
                            saveImage();
                            dialog.cancel();
                        })
                        .setNegativeButton(R.string.cancel, (dialog, which) -> {
                            dialog.cancel();
                        })
                        .show();
                break;
        }
    }

    @Override
    public void saveImage() {
        try {
            askPermissionStorage(() -> {
                File file = FileUtil.createFolder(ConversationDetailActivity.this, getString(R.string.app_name));
                Log.e("file_storage", file.getAbsolutePath());
                String path = FileUtil.saveImageToGallery(ConversationDetailActivity.this
                        , file
                        , Toolbox.screenShortView(binding.container));
                new DialogImageScreenshort.ExtendBuilder()
                        .setPath(path)
                        .setTitle(getString(R.string.screenshort_success))
                        .build().show(getSupportFragmentManager(), DialogImageScreenshort.class.getName());
                return null;
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showPopupInfor(View view) {
        Toolbox.hideSoftKeyboard(this);
        PopupMenu popup = new PopupMenu(this, view);
        popup.getMenuInflater()
                .inflate(R.menu.menu_infor_detail_conversation, popup.getMenu());
        popup.getMenu().getItem(0)
                .setTitle(model.isBlock() ? getString(R.string.unblock) : getString(R.string.block));
        if (!model.isGroup())
            popup.getMenu().findItem(R.id.menu_add_member).setVisible(false);
        popup.setOnMenuItemClickListener(item -> {
            if (presenter == null)
                return true;
            switch (item.getItemId()) {
                case R.id.menu_block:
                    model.setBlock(!model.isBlock());
                    setStatusBlock(model.isBlock());
                    if (presenter != null)
                        presenter.updateConvesation(model);
                    break;
                case R.id.menu_profile_picture:
                    try {
                        askPermissionStorage(() -> {
                            requestGetGallery(path -> {
                                model.setImage(path);
                                setImageConversation();
                                if (!model.isGroup() && messageAdapter != null)
                                    messageAdapter.notifyDataSetChanged();
                                if (presenter != null)
                                    presenter.updateConvesation(model);
                            });
                            return null;
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.menu_add_date_time:
                    AdmobHelp.getInstance().showInterstitialAd(() -> {
                        Intent intent2 = new Intent(ConversationDetailActivity.this, DateTimeActivity.class);
                        intent2.putExtra(DATA_SELECT, model.getColor());
                        startActivityForResult(intent2, Config.REQUEST_CODE_ACT_DATETIME);
                    });
                    break;
                case R.id.menu_chat_setting:
                    AdmobHelp.getInstance().showInterstitialAd(() -> {
                        Intent intent = new Intent(ConversationDetailActivity.this, SettingConvesationActivity.class);
                        intent.putExtra(ConversationDetailActivity.DATA_SELECT, model);
                        startActivityForResult(intent, Config.REQUEST_CODE_ACT_SETTING);
                    });
                    break;
                case R.id.menu_add_member:
                    AdmobHelp.getInstance().showInterstitialAd(() -> {
                        Intent intent3 = new Intent(this, AddMemberActivity.class);
                        intent3.putExtra(ConversationDetailActivity.DATA_SELECT, model);
                        startActivityForResult(intent3, Config.REQUEST_CODE_ACT_MEMBER);
                    });
                    break;
            }
            return true;
        });
        popup.show();
    }

    @Override
    public void setStatusBlock(boolean isBlock) {
        if (isBlock) {
            binding.tvBlock.setVisibility(View.VISIBLE);
            bottomBinding.container.setVisibility(View.GONE);
            topBinding.imCall.setVisibility(View.GONE);
            topBinding.imVideo.setVisibility(View.GONE);
            topBinding.tvContent.setVisibility(View.GONE);
            topBinding.imStatus.setVisibility(View.GONE);
            if (!model.isGroup()) {
//                ImageUtils.loadImage(topBinding.imOutline, model.getImage());
                topBinding.imInline.setVisibility(View.GONE);
//                topBinding.imOutline.clearColorFilter();
            }
        } else {
            binding.tvBlock.setVisibility(View.GONE);
            bottomBinding.container.setVisibility(View.VISIBLE);
            topBinding.imCall.setVisibility(View.VISIBLE);
            topBinding.imVideo.setVisibility(View.VISIBLE);
            topBinding.imStatus.setVisibility(View.VISIBLE);
            if (!model.isGroup()) {
                topBinding.tvContent.setVisibility(View.VISIBLE);
                topBinding.imInline.setVisibility(View.VISIBLE);
//                topBinding.imOutline.setImageResource(R.drawable.bg_round_0084f0);
//                topBinding.imOutline.setColorFilter(Color.parseColor(model.getColor()), PorterDuff.Mode.SRC_IN);
                ImageUtils.loadImage(topBinding.imInline, model.getImage());
            }
        }
    }

    @Override
    public void setImageConversation() {
        if (model.isGroup()) {
//            topBinding.imOutline.setVisibility(View.GONE);
            topBinding.imInline.setVisibility(View.GONE);
            topBinding.llAvatarGroup.setVisibility(View.VISIBLE);
            topBinding.tvContent.setVisibility(View.GONE);
            List<UserMessageModel> lstMember = model.getUserMessageModels();
            if (!TextUtils.isEmpty(model.getImage()) && !"null".equals(model.getImage())) {
                ImageUtils.loadImage(topBinding.imAvaGroup1, model.getImage());
                if (lstMember.size() > 0) {
                    ImageUtils.loadImage(topBinding.imAvaGroup2, lstMember.get(0).getAvatar());
                }
            } else {
                if (lstMember.size() > 0) {
                    ImageUtils.loadImage(topBinding.imAvaGroup1, lstMember.get(0).getAvatar());
                }
                if (lstMember.size() > 1) {
                    ImageUtils.loadImage(topBinding.imAvaGroup2, lstMember.get(1).getAvatar());
                }
            }

        } else {
//            topBinding.imOutline.setVisibility(View.VISIBLE);
            topBinding.llAvatarGroup.setVisibility(View.GONE);
            if (!model.isActive()) {
                topBinding.imStatus.setVisibility(View.GONE);
                ImageUtils.loadImage(topBinding.imOutline, model.getImage());
                topBinding.imInline.setVisibility(View.GONE);
//                topBinding.imOutline.clearColorFilter();
                if (!TextUtils.isEmpty(model.getTimeActiveAgo())) {
                    topBinding.tvContent.setText(model.getTimeActiveAgo());
                } else {
                    topBinding.tvContent.setVisibility(View.GONE);
                }
            } else {
                topBinding.imStatus.setVisibility(View.VISIBLE);
                topBinding.imInline.setVisibility(View.VISIBLE);
//                topBinding.imOutline.setImageResource(R.drawable.bg_round_0084f0);
//                topBinding.imOutline.setColorFilter(Color.parseColor(model.getColor()), PorterDuff.Mode.SRC_IN);
                ImageUtils.loadImage(topBinding.imInline, model.getImage());
            }
        }
    }

    @Override
    public void setNameConversation() {
        if (!TextUtils.isEmpty(model.getName()))
            topBinding.tvName.setText(model.getName());
    }

    @Override
    public void setStatusItemBottom(boolean isChating, boolean foreShow) {
        if (isChating) {
            bottomBinding.llOpt.setVisibility(View.GONE);
            bottomBinding.imSend.setImageResource(R.drawable.ic_send);
            bottomBinding.imMore.setImageResource(R.drawable.ic_next);
        } else {
            bottomBinding.llOpt.setVisibility(View.VISIBLE);
            bottomBinding.imMore.setImageResource(R.drawable.ic_more);
            if (foreShow) {
                if (TextUtils.isEmpty(bottomBinding.edtInput.getText().toString())) {
                    bottomBinding.imSend.setImageResource(R.drawable.ic_like);
                } else {
                    bottomBinding.imSend.setImageResource(R.drawable.ic_send);
                }
            } else {
                bottomBinding.imSend.setImageResource(R.drawable.ic_like);
            }
        }

        if (selectImageBinding.container.isShown()) {
            bottomBinding.imSend.setImageResource(R.drawable.ic_send);
        }
    }

    @Override
    public void setColorConversation() {
        int color = Color.parseColor(model.getColor());
        binding.tvBlock.setBackgroundColor(color);
        /**/
        topBinding.imBack.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        topBinding.imCall.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        topBinding.imVideo.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        topBinding.imInfor.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        /**/
        bottomBinding.imSend.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        bottomBinding.imMore.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        bottomBinding.imPicture.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        bottomBinding.imRecord.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        bottomBinding.imTakePhoto.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        bottomBinding.imEmoji.setColorFilter(color, PorterDuff.Mode.SRC_IN);
    }

    @Override
    public void showPopupOptSend() {
        PopupMenu popup = new PopupMenu(this, bottomBinding.imSend);
        popup.getMenuInflater()
                .inflate(R.menu.menu_send_message, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {
            boolean isSend = item.getItemId() == R.id.menu_send;
            validateMemberSend(isSend, userMessageModel -> {
                String data;
                int type;
                if (selectImageBinding.container.isShown()) {
                    data = pathImageSelect;
                    type = Config.TYPE_IMAGE;
                    cleanImageAfterSend();
                } else if (!TextUtils.isEmpty(bottomBinding.edtInput.getText().toString())) {
                    data = bottomBinding.edtInput.getText().toString();
                    type = Config.TYPE_TEXT;
                    cleanChatAfterSend();
                } else {
                    data = Config.IC_LIKE_NAME;
                    type = Config.TYPE_STICKER;
                }
                MessageModel message = Config.createMessage(data, type);
                message.setSend(isSend);
                message.setUserOwn(userMessageModel != null
                        ? userMessageModel.getId()
                        : model.getId());
                lstMessage.add(message);
                if (presenter != null) {
                    presenter.updateConvesation(model);
                }
                scrollLastItemMessage();
            });
            return true;
        });
        popup.show();
    }

    @Override
    public void validateMemberSend(boolean isSend, IActionDetail.
            MemberSelectLisnter memberSelectLisnter) {
        if (!model.isGroup() || isSend) {
            if (memberSelectLisnter != null)
                memberSelectLisnter.onMemberSelectListener(null);
        } else {
            List<UserMessageModel> lstMember = model.getUserMessageModels();
            if (lstMember.isEmpty()) {
                toast(getString(R.string.no_user_found));
                return;
            }
            new DialogSelectMember.ExtendBuilder()
                    .setLstMember(model.getUserMessageModels())
                    .setItemSelectListener(userMessageModel -> {
                        if (memberSelectLisnter != null)
                            memberSelectLisnter.onMemberSelectListener(userMessageModel);
                    })
                    .build()
                    .show(getSupportFragmentManager(), DialogSelectMember.class.getName());
        }
    }


    @Override
    public void scrollLastItemMessage() {
        if (messageAdapter != null)
            messageAdapter.notifyDataSetChanged();
        binding.rcvMessage.scrollToPosition(messageAdapter.getItemCount() - 1);
        bottomBinding.edtInput.setFocusableInTouchMode(true);
        bottomBinding.edtInput.requestFocus(0);
    }

    @Override
    public void cleanChatAfterSend() {
        bottomBinding.edtInput.setText("");
    }

    @Override
    public void cleanImageAfterSend() {
        selectImageBinding.container.setVisibility(View.GONE);
        pathImageSelect = "";
        if (!TextUtils.isEmpty(bottomBinding.edtInput.getText().toString())) {
            bottomBinding.imSend.setImageResource(R.drawable.ic_send);
        } else {
            bottomBinding.imSend.setImageResource(R.drawable.ic_like);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;
        if (requestCode == Config.REQUEST_CODE_ACT_SETTING) {
            model = (ConversationModel) data.getSerializableExtra(Config.RESULRT_DATA);
            loadViewStatus();
        } else if (requestCode == Config.REQUEST_CODE_ACT_DATETIME) {
            String dateTime = data.getStringExtra(Config.RESULRT_DATA);
            lstMessage.add(Config.createMessage(dateTime, Config.TYPE_DATETIME));
            scrollLastItemMessage();
        } else if (requestCode == Config.REQUEST_CODE_ACT_STICKER) {
            String resName = data.getStringExtra(Config.KEY_CONTENT);
            boolean isSend = data.getBooleanExtra(Config.KEY_TYPE_SEND, true);
            validateMemberSend(isSend, userMessageModel -> {
                MessageModel messageModel = Config.createMessage(resName, Config.TYPE_STICKER);
                messageModel.setSend(isSend);
                messageModel.setUserOwn(userMessageModel != null
                        ? userMessageModel.getId()
                        : model.getId());
                lstMessage.add(messageModel);
                scrollLastItemMessage();
            });
        } else if (requestCode == Config.REQUEST_CODE_ACT_MEMBER) {
            model = (ConversationModel) data.getSerializableExtra(Config.RESULRT_DATA);
            setImageConversation();
        }

        if (presenter != null) {
            presenter.updateConvesation(model);
        }
    }
}
