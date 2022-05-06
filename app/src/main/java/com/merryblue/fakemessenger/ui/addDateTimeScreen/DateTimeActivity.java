package com.merryblue.fakemessenger.ui.addDateTimeScreen;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;

import com.ads.control.AdmobHelp;
import com.merryblue.fakemessenger.R;
import com.merryblue.fakemessenger.databinding.ActivityAddDateTimeBinding;
import com.merryblue.fakemessenger.ui.BaseActivity;
import com.merryblue.fakemessenger.ui.detailScreen.ConversationDetailActivity;
import com.merryblue.fakemessenger.Config;
import com.merryblue.fakemessenger.DateUtils;

import java.util.Calendar;
import java.util.Date;

public class DateTimeActivity extends BaseActivity implements View.OnClickListener {

    private ActivityAddDateTimeBinding binding;
    private String timePreview;
    private String datePreivew;
    private int hour;
    private int minute;
    private int day;
    private int month;
    private int year;

    @Override
    protected View getLayoutResource() {
        binding = ActivityAddDateTimeBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    protected void initView() {
        AdmobHelp.getInstance().loadNative(this);
        fillColorDefault();
    }

    @Override
    protected void initData() {
        Calendar calendar = Calendar.getInstance();
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);
        setDatePreview();
        setTimePreview();
    }

    @Override
    protected void initControl() {
        binding.imBack.setOnClickListener(this);
        binding.btnSave.setOnClickListener(this);
        binding.rdDaily.setOnClickListener(this);
        binding.rdMonthly.setOnClickListener(this);
        binding.rdYearly.setOnClickListener(this);
        binding.btnSetTime.setOnClickListener(this);
        binding.btnSetDate.setOnClickListener(this);
        binding.rdFormat12.setOnClickListener(this);
        binding.rdFormat24.setOnClickListener(this);
    }

    private void fillColorDefault() {
        String colorSelect = getIntent().getStringExtra(ConversationDetailActivity.DATA_SELECT);
        int color = Color.parseColor(colorSelect);
        binding.btnSave.setBackgroundColor(color);
        binding.btnSetTime.setBackgroundColor(color);
        binding.btnSetDate.setBackgroundColor(color);
        binding.rdDaily.setButtonTintList(ColorStateList.valueOf(color));
        binding.rdMonthly.setButtonTintList(ColorStateList.valueOf(color));
        binding.rdYearly.setButtonTintList(ColorStateList.valueOf(color));
        binding.rdFormat12.setButtonTintList(ColorStateList.valueOf(color));
        binding.rdFormat24.setButtonTintList(ColorStateList.valueOf(color));
    }

    @Override
    protected View getViewPadding() {
        return binding.container;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rd_daily:
                binding.llSetDate.setVisibility(View.GONE);
                setDatePreview();
                setTimePreview();
                break;
            case R.id.rd_monthly:
            case R.id.rd_yearly:
                binding.llSetDate.setVisibility(View.VISIBLE);
                setDatePreview();
                setTimePreview();
                break;
            case R.id.rd_format_12:
            case R.id.rd_format_24:
                setDatePreview();
                setTimePreview();
                break;
            case R.id.im_back:
                finish();
                break;
            case R.id.btn_save:
                Intent intent = new Intent();
                intent.putExtra(Config.RESULRT_DATA, binding.tvPreview.getText().toString());
                setResult(Activity.RESULT_OK, intent);
                finish();
                break;
            case R.id.btn_set_time:
                showDialogTimePicker();
                break;
            case R.id.btn_set_date:
                showDialogDatePicker();
                break;
        }
    }

    private void showDialogDatePicker() {
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            this.year = year;
            this.month = month;
            this.day = dayOfMonth;
            setDatePreview();
        }, year, month, day).show();
    }

    private void showDialogTimePicker() {
        new TimePickerDialog(this, (view, hourOfDay, minute) -> {
            this.hour = hourOfDay;
            this.minute = minute;
            setTimePreview();
        }, hour, minute, binding.rdFormat24.isChecked()).show();
    }

    private void setDatePreview() {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day);
        Date date = cal.getTime();
        String simpleFormat = "";
        if (binding.rdMonthly.isChecked())
            simpleFormat = DateUtils.DATE_FORMAT_6;
        else if (binding.rdYearly.isChecked())
            simpleFormat = DateUtils.DATE_FORMAT_7;
        datePreivew = DateUtils.getStampByDate(date, simpleFormat);
        if (!TextUtils.isEmpty(datePreivew))
            binding.tvSetDate.setText(datePreivew);
        String txtMid = TextUtils.isEmpty(datePreivew) ? "" : " " + getString(R.string.at) + " ";
        binding.tvPreview.setText(datePreivew + txtMid + timePreview);
    }

    private void setTimePreview() {
        Date date = new Date();
        date.setHours(hour);
        date.setMinutes(minute);
        timePreview = DateUtils.getStampByDate(date,
                binding.rdFormat24.isChecked() ? DateUtils.TIME_FORMAT_2 : DateUtils.TIME_FORMAT_1);
        if (!TextUtils.isEmpty(timePreview))
            binding.tvSetTime.setText(timePreview);

        String txtMid = TextUtils.isEmpty(datePreivew) ? "" : " " + getString(R.string.at) + " ";
        binding.tvPreview.setText(datePreivew + txtMid + timePreview);
    }
}
