package com.merryblue.fakemessenger.ui.settingScreen;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;

import com.ads.control.AdmobHelp;
import com.merryblue.fakemessenger.R;
import com.merryblue.fakemessenger.adapter.ColorAdapter;
import com.merryblue.fakemessenger.database.repository.AppDataRepository;
import com.merryblue.fakemessenger.databinding.ActivitySettingConvesationBinding;
import com.merryblue.fakemessenger.model.ConversationModel;
import com.merryblue.fakemessenger.model.realmModel.ConversationObject;
import com.merryblue.fakemessenger.ui.BaseActivity;
import com.merryblue.fakemessenger.ui.detailScreen.ConversationDetailActivity;
import com.merryblue.fakemessenger.Config;
import com.merryblue.fakemessenger.Toolbox;
import com.merryblue.fakemessenger.ViewUtils;

import java.util.Arrays;

public class SettingConvesationActivity extends BaseActivity implements View.OnClickListener {

    private ActivitySettingConvesationBinding binding;
    private AppDataRepository repository;
    private ConversationModel model;
    private ColorAdapter colorAdapter;

    @Override
    protected View getLayoutResource() {
        binding = ActivitySettingConvesationBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    protected void initView() {
        AdmobHelp.getInstance().loadBanner(this);
        ViewUtils.setupUI(binding.container, this);
        repository = AppDataRepository.getRepository();
    }

    @Override
    protected void initData() {
        model = (ConversationModel) getIntent().getSerializableExtra(ConversationDetailActivity.DATA_SELECT);
        if (model == null)
            return;
        colorAdapter = new ColorAdapter(this, Arrays.asList(Config.colorHexString));
        colorAdapter.setColorSelected(model.getColor());
        binding.rcvColor.setAdapter(colorAdapter);
        fillColorDefault();
        if (!TextUtils.isEmpty(model.getName()))
            binding.edtInputName.setText(model.getName());
        if (model.isActive()) {
            binding.rdActive.setChecked(true);
        } else {
            binding.rdActiveAgo.setChecked(true);
            binding.edtTimeAgo.setVisibility(View.VISIBLE);
        }
        if (!TextUtils.isEmpty(model.getTimeActiveAgo()))
            binding.edtTimeAgo.setText(model.getTimeActiveAgo());
    }

    @Override
    protected void initControl() {
        binding.imBack.setOnClickListener(this);
        binding.btnSave.setOnClickListener(this);

        binding.rdActiveAgo.setOnCheckedChangeListener((compoundButton, b) -> {
            binding.edtTimeAgo.setVisibility(b ? View.VISIBLE : View.GONE);
        });

        colorAdapter.setOnItemClickListener(position -> {
            Toolbox.hideSoftKeyboard(this);
            String colorSelected = Config.colorHexString[position];
            colorAdapter.setColorSelected(colorSelected);
            colorAdapter.notifyDataSetChanged();
            model.setColor(colorSelected);
            fillColorDefault();
        });
    }

    @Override
    protected View getViewPadding() {
        return binding.container;
    }

    private void fillColorDefault() {
        int color = Color.parseColor(model.getColor());
        binding.tvEditName.setTextColor(color);
        binding.tvOnlineStatus.setTextColor(color);
        binding.tvChatColor.setTextColor(color);
        binding.btnSave.setBackgroundColor(color);
        binding.rdActiveAgo.setButtonTintList(ColorStateList.valueOf(color));
        binding.rdActive.setButtonTintList(ColorStateList.valueOf(color));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.im_back:
                finish();
                break;
            case R.id.btn_save:
                if (validate()) {
                    saveData();
                    backScreen();
                }
                break;
        }
    }

    private boolean validate() {
        if (TextUtils.isEmpty(binding.edtInputName.getText().toString())) {
            toast(getString(R.string.you_must_enter, getString(R.string.convesation_name)));
            return false;
        }
        return true;
    }

    private void saveData() {
        model.setName(binding.edtInputName.getText().toString());
        model.setActive(binding.rdActive.isChecked());
        model.setTimeActiveAgo(binding.edtTimeAgo.getText().toString());
        repository.update(new ConversationObject(model));
    }

    private void backScreen() {
        Intent intent = new Intent();
        intent.putExtra(Config.RESULRT_DATA, model);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
