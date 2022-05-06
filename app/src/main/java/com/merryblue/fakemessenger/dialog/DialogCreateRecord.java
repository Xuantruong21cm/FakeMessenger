package com.merryblue.fakemessenger.dialog;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.merryblue.fakemessenger.Config;
import com.merryblue.fakemessenger.R;
import com.merryblue.fakemessenger.databinding.DialogCreateRecordBinding;

import java.util.HashMap;

public class DialogCreateRecord extends BaseDialog<DialogCreateRecord.ExtendBuilder> {

    private DialogCreateRecordBinding recordBinding;

    public DialogCreateRecord(DialogCreateRecord.ExtendBuilder builder) {
        super(builder);
    }

    @Override
    protected void initControl() {

    }

    @Override
    protected TextView getTitle() {
        return recordBinding.tvTitle;
    }

    @Override
    protected TextView getNegativeButton() {
        return recordBinding.tvNegative;
    }

    @Override
    protected TextView getPositiveButton() {
        return recordBinding.tvPositive;
    }

    @Override
    protected void handleClickPositiveButton(HashMap<String, Object> datas) {
        if (validate()) {
            datas.put(Config.KEY_CONTENT, recordBinding.edtSetTime.getText().toString());
            datas.put(Config.KEY_TYPE_SEND, recordBinding.rdSend.isChecked());
            super.handleClickPositiveButton(datas);
        }
    }

    private boolean validate() {
        if (TextUtils.isEmpty(recordBinding.edtSetTime.getText().toString())) {
            Toast.makeText(getContext()
                    , getString(R.string.you_must_enter, getString(R.string.full_data))
                    , Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    @Override
    protected View getLayoutResource() {
        recordBinding = DialogCreateRecordBinding.inflate(LayoutInflater.from(getContext()));
        return recordBinding.getRoot();
    }

    public static class ExtendBuilder extends BuilderDialog {
        @Override
        public BaseDialog build() {
            return new DialogCreateRecord(this);
        }
    }
}
