package com.merryblue.fakemessenger.dialog;

import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.FileProvider;

import com.merryblue.fakemessenger.ImageUtils;
import com.merryblue.fakemessenger.R;
import com.merryblue.fakemessenger.Toolbox;
import com.merryblue.fakemessenger.databinding.DialogImageScreenshortBinding;

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
                    String path = extendBuilder.path;
                    new File(path).delete();
                    Log.e("path_delete", path);
                    try {
                        MediaScannerConnection.scanFile(getContext(), new String[]{Environment.getExternalStorageDirectory().toString()}, null, new MediaScannerConnection.OnScanCompletedListener() {
                            public void onScanCompleted(String path, Uri uri) {

                            }
                        });
                    } catch (Exception ex) {

                    }
                    dismiss();
                } catch (Exception e) {
                    Log.e("delete_Image", String.valueOf(e));
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
