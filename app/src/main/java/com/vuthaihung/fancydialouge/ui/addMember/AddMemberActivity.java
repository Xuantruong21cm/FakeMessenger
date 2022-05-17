package com.vuthaihung.fancydialouge.ui.addMember;
import android.app.Activity;
import android.content.Intent;
import android.view.View;

import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.DialogFragment;

import com.ads.control.AdmobHelp;
import com.vuthaihung.fancydialouge.R;
import com.vuthaihung.fancydialouge.adapter.MemberAdapter;
import com.vuthaihung.fancydialouge.databinding.ActivityAddMemberBinding;
import com.vuthaihung.fancydialouge.dialog.DialogCreateConversation;
import com.vuthaihung.fancydialouge.dialog.DialogEditMessage;
import com.vuthaihung.fancydialouge.model.ConversationModel;
import com.vuthaihung.fancydialouge.model.UserMessageModel;
import com.vuthaihung.fancydialouge.ui.BaseActivity;
import com.vuthaihung.fancydialouge.ui.detailScreen.ConversationDetailActivity;
import com.vuthaihung.fancydialouge.Config;

import java.util.ArrayList;
import java.util.List;

public class AddMemberActivity extends BaseActivity implements View.OnClickListener {

    private ActivityAddMemberBinding addMemberBinding;

    private MemberAdapter memberAdapter;
    ConversationModel conversationModel;
    private List<UserMessageModel> lstMember = new ArrayList<>();

    @Override
    protected View getLayoutResource() {
        addMemberBinding = ActivityAddMemberBinding.inflate(getLayoutInflater());
        return addMemberBinding.getRoot();
    }

    @Override
    protected void initView() {
        AdmobHelp.getInstance().loadNative(this);
    }

    @Override
    protected void initData() {
        if (getIntent() != null) {
            conversationModel = (ConversationModel) getIntent().getSerializableExtra(ConversationDetailActivity.DATA_SELECT);
//            for (int i = 0; i < userMessageModels.size(); i++) {
//                for (int j = i + 1; j < userMessageModels.size(); j++) {
//                    if (userMessageModels.get(i).equals(userMessageModels.get(j))) {
//                        userMessageModels.remove(j);
//                        j--;
//                    }
//                }
//            }
            lstMember = conversationModel.getUserMessageModels();
//            lstMember = userMessageModels;
        }

        memberAdapter = new MemberAdapter(this, lstMember);
        addMemberBinding.rcvMember.setAdapter(memberAdapter);
    }

    @Override
    protected void initControl() {
        addMemberBinding.imBack.setOnClickListener(this);
        addMemberBinding.imAddUser.setOnClickListener(this);

        memberAdapter.setItemClickListener(new MemberAdapter.ItemClickListener() {
            @Override
            public void onItemClickListener(int position, View view) {
                PopupMenu popup = new PopupMenu(AddMemberActivity.this, view);
                popup.getMenuInflater().inflate(R.menu.menu_item_member, popup.getMenu());
                popup.setOnMenuItemClickListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.menu_delete:
                            lstMember.remove(position);
                            memberAdapter.notifyDataSetChanged();
                            break;
                        case R.id.menu_edit:
                            new DialogEditMessage.ExtendBuilder()
                                    .setMessageContent(lstMember.get(position).getName())
                                    .setTitle(getString(R.string.edit))
                                    .onSetPositiveButton(getString(R.string.ok), (baseDialog, data) -> {
                                        lstMember.get(position).setName((String) data.get(Config.KEY_CONTENT));
                                        memberAdapter.notifyItemChanged(position);
                                        baseDialog.dismiss();
                                    })
                                    .onSetNegativeButton(getString(R.string.cancel), baseDialog -> {
                                        baseDialog.dismiss();
                                    })
                                    .build()
                                    .show(getSupportFragmentManager(), DialogEditMessage.class.getName());
                            break;
                    }
                    return true;
                });
                popup.show();
            }

            @Override
            public void onAvatarClickListener(int position) {
                try {
                    askPermissionStorage(() -> {
                        requestGetGallery(path -> {
                            lstMember.get(position).setAvatar(path);
                            memberAdapter.notifyItemChanged(position);

                        });
                        return null;
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected View getViewPadding() {
        return addMemberBinding.container;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(Config.RESULRT_DATA, conversationModel);
        setResult(Activity.RESULT_OK, intent);
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.im_back:
                onBackPressed();
                break;
            case R.id.im_add_user:
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
                        .setTitle(getString(R.string.member_infor))
                        .onSetPositiveButton(getString(R.string.ok), (baseDialog, data) -> {
                            String avatar = (String) data.get(Config.KEY_AVATAR);
                            String name = (String) data.get(Config.KEY_TITLE);
                            UserMessageModel userMessageModel = new UserMessageModel();
                            userMessageModel.setId(System.currentTimeMillis());
                            userMessageModel.setAvatar(avatar);
                            userMessageModel.setName(name);
                            lstMember.add(userMessageModel);
                            memberAdapter.notifyDataSetChanged();
                            baseDialog.dismiss();
                        })
                        .onSetNegativeButton(getString(R.string.cancel), DialogFragment::dismiss)
                        .build()
                        .show(getSupportFragmentManager(), DialogCreateConversation.class.getName());
                break;
        }
    }
}
