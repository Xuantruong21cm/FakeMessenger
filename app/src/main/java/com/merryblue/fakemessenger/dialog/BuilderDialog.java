package com.merryblue.fakemessenger.dialog;

import java.util.HashMap;

public abstract class BuilderDialog {
    protected String title;
    protected String positiveButton;
    protected String negativeButton;
    protected boolean cancelable = true;
    protected boolean canOntouchOutside = true;

    protected DialogActionListener.PositiveButtonListener positiveButtonListener;
    protected DialogActionListener.SetNegativeButtonListener negativeButtonListener;
    protected DialogActionListener.DismissDialogListener dismissDialogListener;

    public BuilderDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public BuilderDialog setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
        return this;
    }

    public BuilderDialog setCanOntouchOutside(boolean canOntouchOutside) {
        this.canOntouchOutside = canOntouchOutside;
        return this;
    }

    public BuilderDialog onSetPositiveButton(String positiveButton
            , DialogActionListener.PositiveButtonListener positiveButtonListener) {
        this.positiveButton = positiveButton;
        this.positiveButtonListener = positiveButtonListener;
        return this;
    }

    public BuilderDialog onSetNegativeButton(String negativeButton
            , DialogActionListener.SetNegativeButtonListener negativeButtonListener) {
        this.negativeButton = negativeButton;
        this.negativeButtonListener = negativeButtonListener;
        return this;
    }

    public BuilderDialog onDismissListener(DialogActionListener.DismissDialogListener dismissDialogListener) {
        this.dismissDialogListener = dismissDialogListener;
        return this;
    }

    public abstract BaseDialog build();

    public interface DialogActionListener {

        interface PositiveButtonListener {
            void onPositiveButtonListener(BaseDialog baseDialog, HashMap<String, Object> datas);
        }

        interface SetNegativeButtonListener {
            void onNegativeButtonListener(BaseDialog baseDialog);
        }

        interface DismissDialogListener {
            void onDismissDialogListner();
        }
    }
}
