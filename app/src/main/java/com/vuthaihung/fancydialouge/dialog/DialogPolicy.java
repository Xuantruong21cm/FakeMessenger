package com.vuthaihung.fancydialouge.dialog;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.vuthaihung.fancydialouge.databinding.DialogPolicyBinding;

public class DialogPolicy extends BaseDialog<DialogPolicy.ExtendBuilder> {

    private DialogPolicyBinding policyBinding;
    private ExtendBuilder extendBuilder;

    public DialogPolicy(DialogPolicy.ExtendBuilder builder) {
        super(builder);
        this.extendBuilder = builder;
    }

    @Override
    protected void initControl() {

    }

    @Override
    protected void initView() {
        super.initView();
        policyBinding.webview.loadUrl("file:///android_asset/privacy_policy.html");
    }

    @Override
    protected TextView getNegativeButton() {
        return policyBinding.tvNegative;
    }

    @Override
    protected TextView getPositiveButton() {
        return policyBinding.tvPositive;
    }

    @Override
    protected View getLayoutResource() {
        policyBinding = DialogPolicyBinding.inflate(LayoutInflater.from(getContext()));
        return policyBinding.getRoot();
    }

    public static class ExtendBuilder extends BuilderDialog {
        @Override
        public BaseDialog build() {
            return new DialogPolicy(this);
        }
    }
}
