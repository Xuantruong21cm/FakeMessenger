package com.vuthaihung.fancydialouge.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import java.util.HashMap;

public abstract class BaseDialog<B extends BuilderDialog> extends DialogFragment {

    private B builder;

    public BaseDialog(B builder) {
        this.builder = builder;
    }

    @Override
    public void show(@NonNull FragmentManager manager, @Nullable String tag) {
        String t = getClass().getSimpleName();
        if (manager.findFragmentByTag(t) == null) {
            super.show(manager, t);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setView(getLayoutResource());
        initView();
        initControl();
        return dialogBuilder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            Window window = dialog.getWindow();
            WindowManager.LayoutParams windowParams = window.getAttributes();
            windowParams.dimAmount = 0.7f;
            windowParams.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE;
            window.setAttributes(windowParams);
            dialog.setCancelable(builder.cancelable);
            dialog.setCanceledOnTouchOutside(builder.canOntouchOutside);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

    }

    protected void initView() {
        if (!TextUtils.isEmpty(builder.title) && getTitle() != null)
            getTitle().setText(builder.title);

        if (!TextUtils.isEmpty(builder.positiveButton) && getPositiveButton() != null) {
            getPositiveButton().setText(builder.positiveButton);
            getPositiveButton().setOnClickListener(v -> {
                handleClickPositiveButton(new HashMap<>());
            });
        }

        if (!TextUtils.isEmpty(builder.negativeButton) && getNegativeButton() != null) {
            getNegativeButton().setText(builder.negativeButton);
            getNegativeButton().setOnClickListener(v -> {
                handleClickNegativeButton();
            });
        }
    }

    protected abstract void initControl();

    protected abstract View getLayoutResource();

    protected TextView getPositiveButton() {
        return null;
    }

    protected TextView getNegativeButton() {
        return null;
    }

    protected TextView getTitle() {
        return null;
    }

    protected void handleClickNegativeButton() {
        if (builder.negativeButtonListener != null)
            builder.negativeButtonListener.onNegativeButtonListener(this);
    }

    protected void handleClickPositiveButton(HashMap<String, Object> datas) {
        if (builder.positiveButtonListener != null) {
            builder.positiveButtonListener.onPositiveButtonListener(this
                    , datas);
        }
    }

}
