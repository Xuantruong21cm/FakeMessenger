package com.vuthaihung.fancydialouge.dialog;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.vuthaihung.fancydialouge.adapter.SelectMemberAdapter;
import com.vuthaihung.fancydialouge.databinding.DialogSelectMemberBinding;
import com.vuthaihung.fancydialouge.model.UserMessageModel;

import java.util.ArrayList;
import java.util.List;

public class DialogSelectMember extends BaseDialog<DialogSelectMember.ExtendBuilder> {

    private DialogSelectMemberBinding selectMemberBinding;
    private SelectMemberAdapter selectMemberAdapter;
    private ExtendBuilder extendBuilder;

    public DialogSelectMember(DialogSelectMember.ExtendBuilder builder) {
        super(builder);
        this.extendBuilder = builder;
    }

    @Override
    protected void initControl() {

    }

    @Override
    protected void initView() {
        super.initView();
        selectMemberAdapter = new SelectMemberAdapter(getContext(), extendBuilder.lstMember);
        selectMemberAdapter.setOnItemClickListener(position -> {
            if (extendBuilder.itemSelectListener != null)
                extendBuilder.itemSelectListener.onItemSelectListner(extendBuilder.lstMember.get(position));
            dismiss();
        });
        selectMemberBinding.rcvMember.setAdapter(selectMemberAdapter);
    }

    @Override
    protected TextView getTitle() {
        return selectMemberBinding.tvTitle;
    }

    @Override
    protected View getLayoutResource() {
        selectMemberBinding = DialogSelectMemberBinding.inflate(LayoutInflater.from(getContext()));
        return selectMemberBinding.getRoot();
    }

    public static class ExtendBuilder extends BuilderDialog {

        private List<UserMessageModel> lstMember = new ArrayList<>();
        private ItemSelectListener itemSelectListener;

        public interface ItemSelectListener {
            void onItemSelectListner(UserMessageModel userMessageModel);
        }

        public ExtendBuilder setItemSelectListener(ItemSelectListener itemSelectListener) {
            this.itemSelectListener = itemSelectListener;
            return this;
        }

        public ExtendBuilder setLstMember(List<UserMessageModel> lstMember) {
            this.lstMember.clear();
            this.lstMember.addAll(lstMember);
            return this;
        }

        @Override
        public BaseDialog build() {
            return new DialogSelectMember(this);
        }
    }
}
