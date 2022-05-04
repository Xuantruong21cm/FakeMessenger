package com.lubuteam.fakemessenger.fakeconversation.dialog;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.FileProvider;

import com.lubuteam.fakemessenger.fakeconversation.R;
import com.lubuteam.fakemessenger.fakeconversation.databinding.DialogImageScreenshortBinding;
import com.lubuteam.fakemessenger.fakeconversation.utils.ImageUtils;
import com.lubuteam.fakemessenger.fakeconversation.utils.Toolbox;

import java.io.File;

public class DialogImageScreenshort extends BaseDialog<DialogImageScreenshort.ExtendBuilder> implements View.OnClickListener {

    private DialogImageScreenshortBinding binding;
    private ExtendBuilder extendBuilder;

    public DialogImageScreenshort(DialogImageScreenshort.ExtendBuilder builder) {
        super(builder);
        this.extendBuilder = builder;
    }

    @Override
    protected void initControl() {
        ImageUtils.loadImageDefault(binding.imImage, extendBuilder.path);
        binding.imClose.setOnClickListener(this);
        binding.imShare.setOnClickListener(this);
        binding.imView.setOnClickListener(this);
        binding.imDelete.setOnClickListener(this);
    }

    @Override
    protected View getLayoutResource() {
        binding = DialogImageScreenshortBinding.inflate(LayoutInflater.from(getContext()));
        return binding.getRoot();
    }

    @Override
    protected TextView getTitle() {
        return binding.tvTitle;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.im_close:
                dismiss();
                break;
            case R.id.im_share:
                try {
                    Toolbox.shareImage(getContext(), extendBuilder.path);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.im_view:
                try {
                    Uri fileUri = FileProvider.getUriForFile(getContext(), getActivity().getPackageName() + ".provider", new File(extendBuilder.path));
                    Intent openPhotoIntent = new Intent();
                    openPhotoIntent.setAction(Intent.ACTION_VIEW)
                            .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            .setDataAndType(fileUri, getContext().getContentResolver().getType(fileUri));
                    getContext().startActivity(openPhotoIntent);
                    dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.im_delete:
                try {
                    new File(extendBuilder.path).delete();
                    dismiss();
                } catch (Exception e) {

                }
                break;
        }
    }

    public static class ExtendBuilder extends BuilderDialog {

        private String path;

        public ExtendBuilder setPath(String path) {
            this.path = path;
            return this;
        }

        @Override
        public BaseDialog build() {
            return new DialogImageScreenshort(this);
        }
    }
}
