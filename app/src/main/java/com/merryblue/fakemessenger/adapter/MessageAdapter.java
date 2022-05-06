package com.merryblue.fakemessenger.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.merryblue.fakemessenger.R;
import com.merryblue.fakemessenger.databinding.ItemHeaderBinding;
import com.merryblue.fakemessenger.databinding.ItemMessageDatetimeBinding;
import com.merryblue.fakemessenger.databinding.ItemMessageImageBinding;
import com.merryblue.fakemessenger.databinding.ItemMessageRecordBinding;
import com.merryblue.fakemessenger.databinding.ItemMessageRemoveBinding;
import com.merryblue.fakemessenger.databinding.ItemMessageStickerBinding;
import com.merryblue.fakemessenger.databinding.ItemMessageTextBinding;
import com.merryblue.fakemessenger.model.ConversationModel;
import com.merryblue.fakemessenger.model.MessageModel;
import com.merryblue.fakemessenger.model.UserMessageModel;
import com.merryblue.fakemessenger.Config;
import com.merryblue.fakemessenger.ImageUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class MessageAdapter extends BaseRecyclerAdapter<MessageModel, RecyclerView.ViewHolder> {

    private ConversationModel conversationModel;
    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClickListener(int position, int itemType, View view);
    }

    public MessageAdapter(Context context, ConversationModel conversationModel, List<MessageModel> list) {
        super(context, list);
        this.conversationModel = conversationModel;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case Config.TYPE_HEAEDER:
                ((HeaderViewHolder) holder).bindData();
                break;
            case Config.TYPE_TEXT:
                ((MessagerViewHolder) holder).bindData(list.get(position));
                break;
            case Config.TYPE_DATETIME:
                ((DateTimeViewHolder) holder).bindData(list.get(position));
                break;
            case Config.TYPE_RECORD:
                ((RecordViewHolder) holder).bindData(list.get(position));
                break;
            case Config.TYPE_STICKER:
                ((StickerViewHolder) holder).bindData(list.get(position));
                break;
            case Config.TYPE_IMAGE:
                ((ImageViewHolder) holder).bindData(list.get(position));
                break;
            case Config.TYPE_REMOVE:
                ((RemoveViewHolder) holder).bindData(list.get(position));
                break;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        View view;
        if (viewType == Config.TYPE_HEAEDER) {
            view = layoutInflater.inflate(R.layout.item_header, parent, false);
            viewHolder = new HeaderViewHolder(view);
        } else if (viewType == Config.TYPE_TEXT) {
            view = layoutInflater.inflate(R.layout.item_message_text, parent, false);
            viewHolder = new MessagerViewHolder(view);
        } else if (viewType == Config.TYPE_DATETIME) {
            view = layoutInflater.inflate(R.layout.item_message_datetime, parent, false);
            viewHolder = new DateTimeViewHolder(view);
        } else if (viewType == Config.TYPE_RECORD) {
            view = layoutInflater.inflate(R.layout.item_message_record, parent, false);
            viewHolder = new RecordViewHolder(view);
        } else if (viewType == Config.TYPE_STICKER) {
            view = layoutInflater.inflate(R.layout.item_message_sticker, parent, false);
            viewHolder = new StickerViewHolder(view);
        } else if (viewType == Config.TYPE_IMAGE) {
            view = layoutInflater.inflate(R.layout.item_message_image, parent, false);
            viewHolder = new ImageViewHolder(view);
        } else if (viewType == Config.TYPE_REMOVE) {
            view = layoutInflater.inflate(R.layout.item_message_remove, parent, false);
            viewHolder = new RemoveViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).getType();
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {

        private ItemHeaderBinding binding;

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemHeaderBinding.bind(itemView);
        }

        public void bindData() {
            if (!conversationModel.isGroup()) {
                binding.llInfor.setVisibility(View.VISIBLE);
                ImageUtils.loadImage(binding.imAvatar, conversationModel.getImage());
                if (!TextUtils.isEmpty(conversationModel.getName()))
                    binding.tvName2.setText(conversationModel.getName());
                if (!TextUtils.isEmpty(conversationModel.getLiveIn()))
                    binding.tvLiveIn.setText(conversationModel.getLiveIn());
                if (!TextUtils.isEmpty(conversationModel.getFriendOn()))
                    binding.tvFriendOn.setText(conversationModel.getFriendOn());
            } else {
                binding.llInfor.setVisibility(View.GONE);
            }

            binding.llInfor.setOnClickListener(v -> {
                if (itemClickListener != null)
                    itemClickListener.onItemClickListener(getAdapterPosition(), Config.TYPE_HEAEDER, binding.llInfor);
            });
        }
    }

    public class MessagerViewHolder extends RecyclerView.ViewHolder {

        private ItemMessageTextBinding textBinding;

        public MessagerViewHolder(@NonNull View itemView) {
            super(itemView);
            textBinding = ItemMessageTextBinding.bind(itemView);
        }

        public void bindData(MessageModel messageModel) {
            if (messageModel == null)
                return;
            if (!TextUtils.isEmpty(messageModel.getChatContent()))
                textBinding.tvContent.setText(messageModel.getChatContent());
            textBinding.tvContent.setTextColor(messageModel.isSend() ? Color.WHITE : Color.BLACK);
            textBinding.tvContent.setBackgroundTintList(ColorStateList.valueOf(messageModel.isSend()
                    ? Color.parseColor(conversationModel.getColor())
                    : mContext.getResources().getColor(R.color.color_f1f1f1)));

            textBinding.tvContent.setBackgroundResource(Config.getBackgroundChatResoure(conversationModel, getAdapterPosition()));

            setStatusCommon(textBinding.imAvatar, textBinding.imStatus, getAdapterPosition()
                    , textBinding.llContent, messageModel, textBinding.tvMember);

            textBinding.tvContent.setOnClickListener(v -> {
                if (itemClickListener != null)
                    itemClickListener.onItemClickListener(getAdapterPosition(), Config.TYPE_TEXT, textBinding.tvContent);
            });
        }

    }

    public class DateTimeViewHolder extends RecyclerView.ViewHolder {

        private ItemMessageDatetimeBinding datetimeBinding;

        public DateTimeViewHolder(@NonNull View itemView) {
            super(itemView);
            datetimeBinding = ItemMessageDatetimeBinding.bind(itemView);
        }

        public void bindData(MessageModel messageModel) {
            if (messageModel == null)
                return;
            if (!TextUtils.isEmpty(messageModel.getChatContent()))
                datetimeBinding.tvDatetime.setText(messageModel.getChatContent());

            datetimeBinding.tvDatetime.setOnClickListener(v -> {
                if (itemClickListener != null)
                    itemClickListener.onItemClickListener(getAdapterPosition(), Config.TYPE_DATETIME, datetimeBinding.tvDatetime);
            });
        }

    }

    public class RecordViewHolder extends RecyclerView.ViewHolder {

        private ItemMessageRecordBinding recordBinding;

        public RecordViewHolder(@NonNull View itemView) {
            super(itemView);
            recordBinding = ItemMessageRecordBinding.bind(itemView);
        }

        public void bindData(MessageModel messageModel) {
            if (messageModel == null)
                return;

            if (!TextUtils.isEmpty(messageModel.getChatContent()))
                recordBinding.tvData.setText(messageModel.getChatContent());
            String colorDefault = conversationModel.getColor();
            recordBinding.llContent.setBackgroundTintList(ColorStateList.valueOf(messageModel.isSend()
                    ? Color.parseColor(colorDefault)
                    : mContext.getResources().getColor(R.color.color_f1f1f1)));
            recordBinding.tvData.setTextColor(messageModel.isSend()
                    ? Color.parseColor(colorDefault)
                    : Color.WHITE);
            recordBinding.tvData.setBackgroundDrawable(mContext.getResources().getDrawable(messageModel.isSend()
                    ? R.drawable.bg_white_radius_50
                    : R.drawable.bg_8x8b90_radius_50));
            recordBinding.llTime.setBackgroundTintList(ColorStateList.valueOf(messageModel.isSend()
                    ? Color.parseColor(colorDefault)
                    : mContext.getResources().getColor(R.color.color_e8e8e8)));
            recordBinding.imRecord.setColorFilter(messageModel.isSend()
                    ? Color.parseColor(colorDefault)
                    : Color.WHITE, PorterDuff.Mode.SRC_IN);
            recordBinding.imRecord.setBackgroundDrawable(mContext.getResources().getDrawable(messageModel.isSend()
                    ? R.drawable.bg_round_white
                    : R.drawable.bg_round_8c8b90));
            recordBinding.seekbar.setBackgroundColor(messageModel.isSend()
                    ? Color.WHITE
                    : mContext.getResources().getColor(R.color.color_8c8b90));
            recordBinding.imShareLeft.setVisibility(messageModel.isSend() ? View.VISIBLE : View.GONE);
            recordBinding.imShareRight.setVisibility(messageModel.isSend() ? View.GONE : View.VISIBLE);

            setStatusCommon(recordBinding.imAvatar, recordBinding.imStatus, getAdapterPosition()
                    , recordBinding.llContent, messageModel, recordBinding.tvMember);

            recordBinding.llTime.setOnClickListener(v -> {
                if (itemClickListener != null)
                    itemClickListener.onItemClickListener(getAdapterPosition(), Config.TYPE_RECORD, recordBinding.llTime);
            });
        }

    }

    public class StickerViewHolder extends RecyclerView.ViewHolder {

        private ItemMessageStickerBinding stickerBinding;

        public StickerViewHolder(@NonNull View itemView) {
            super(itemView);
            stickerBinding = ItemMessageStickerBinding.bind(itemView);
        }

        public void bindData(MessageModel messageModel) {
            if (messageModel == null)
                return;

            int drawableResourceId = mContext.getResources().getIdentifier(messageModel.getChatContent(), "drawable", mContext.getPackageName());
            int imgageSize;
            if (messageModel.getChatContent().equals(Config.IC_LIKE_NAME)) {
                stickerBinding.imSticker.setColorFilter(Color.parseColor(conversationModel.getColor()));
                imgageSize = mContext.getResources().getDimensionPixelSize(R.dimen._30sdp);
            } else {
                stickerBinding.imSticker.clearColorFilter();
                imgageSize = mContext.getResources().getDimensionPixelSize(R.dimen._110sdp);
            }
            Glide.with(stickerBinding.imSticker)
                    .load(drawableResourceId)
                    .apply(new RequestOptions().override(imgageSize))
                    .skipMemoryCache(false)
                    .into(stickerBinding.imSticker);

            setStatusCommon(stickerBinding.imAvatar, stickerBinding.imStatus, getAdapterPosition()
                    , stickerBinding.llContent, messageModel, null);

            stickerBinding.imSticker.setOnClickListener(v -> {
                if (itemClickListener != null)
                    itemClickListener.onItemClickListener(getAdapterPosition(), Config.TYPE_STICKER, stickerBinding.imSticker);
            });
        }

    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        private ItemMessageImageBinding imageBinding;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageBinding = ItemMessageImageBinding.bind(itemView);
        }

        public void bindData(MessageModel messageModel) {
            if (messageModel == null)
                return;

            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(messageModel.getChatContent(), options);
            int width = 0;
            int heigth = 0;
            if (options.outWidth >= options.outHeight) {
                width = mContext.getResources().getDimensionPixelSize(R.dimen._190sdp);
                heigth = mContext.getResources().getDimensionPixelSize(R.dimen._120sdp);
            } else {
                width = mContext.getResources().getDimensionPixelSize(R.dimen._150sdp);
                heigth = mContext.getResources().getDimensionPixelSize(R.dimen._250sdp);
            }

            Glide.with(mContext)
                    .load(messageModel.getChatContent())
                    .error(R.drawable.bg_no_image)
                    .apply(new RequestOptions().override(width, heigth))
                    .into(imageBinding.imImage);

            imageBinding.llIconLeft.setVisibility(messageModel.isSend() ? View.VISIBLE : View.GONE);
            imageBinding.llIconRight.setVisibility(messageModel.isSend() ? View.GONE : View.VISIBLE);

            setStatusCommon(imageBinding.imAvatar, imageBinding.imStatus, getAdapterPosition()
                    , imageBinding.llContent, messageModel, imageBinding.tvMember);

            imageBinding.imImage.setOnClickListener(v -> {
                if (itemClickListener != null)
                    itemClickListener.onItemClickListener(getAdapterPosition()
                            , Config.TYPE_IMAGE, imageBinding.imImage);
            });
        }

    }

    public class RemoveViewHolder extends RecyclerView.ViewHolder {

        private ItemMessageRemoveBinding removeBinding;

        public RemoveViewHolder(@NonNull View itemView) {
            super(itemView);
            removeBinding = ItemMessageRemoveBinding.bind(itemView);
        }

        public void bindData(MessageModel messageModel) {
            if (messageModel == null)
                return;
            removeBinding.tvContent.setText(mContext.getString(R.string.removed_a_message
                    , messageModel.isSend()
                            ? mContext.getString(R.string.you)
                            : conversationModel.getName()));

            removeBinding.tvContent.setBackgroundResource(Config.getBackgroundRemoveResoure(conversationModel.getMessageModels(), getAdapterPosition()));

            setStatusCommon(removeBinding.imAvatar, removeBinding.imStatus, getAdapterPosition()
                    , removeBinding.llContent, messageModel, null);

            removeBinding.tvContent.setOnClickListener(v -> {
                if (itemClickListener != null)
                    itemClickListener.onItemClickListener(getAdapterPosition()
                            , Config.TYPE_REMOVE, removeBinding.tvContent);
            });

        }

    }

    public void setStatusCommon(ImageView imAvatar, ImageView imStatus, int position
            , LinearLayout llContent, MessageModel messageModel, TextView tvMember) {
        if (position == 0)
            /*0 là header*/
            return;
        UserMessageModel userMessageCurrent = null;
        /** Xử lý logic ẩn hiện avatar trong đoạn chat */
        if (conversationModel.isGroup()) {
            userMessageCurrent = Config.getMemberSendMessage(messageModel.getUserOwn()
                    , conversationModel.getUserMessageModels());
            ImageUtils.loadImage(imAvatar, userMessageCurrent != null ? userMessageCurrent.getAvatar() : "");
        } else {
            ImageUtils.loadImage(imAvatar, conversationModel.getImage());
        }
        llContent.setGravity(messageModel.isSend() ? Gravity.END : Gravity.START);
        imAvatar.setVisibility(messageModel.isSend() ? View.INVISIBLE : View.VISIBLE);
        if (position + 2 <= list.size()) {
            MessageModel itemNext = list.get(position + 1);
            if (itemNext.isSend() == messageModel.isSend() && !messageModel.isSend()) {
                if (!conversationModel.isGroup()) {
                    imAvatar.setVisibility(View.INVISIBLE);
                } else {
                    UserMessageModel userNext = Config.getMemberSendMessage(itemNext.getUserOwn()
                            , conversationModel.getUserMessageModels());
                    imAvatar.setVisibility(userNext.getId() == userMessageCurrent.getId() ? View.INVISIBLE : View.VISIBLE);
                }
            }
        }
        /** Xử lý logic ẩn hiện tên member trong đoạn chat */
        if (tvMember != null) {
            if (messageModel.getType() != Config.TYPE_STICKER &&
                    messageModel.getType() != Config.TYPE_DATETIME
                    && conversationModel.isGroup() &&
                    !messageModel.isSend() && userMessageCurrent != null) {
                tvMember.setText(!TextUtils.isEmpty(userMessageCurrent.getName())
                        ? userMessageCurrent.getName().split(" ")[0]
                        : "");
                if (position == 1) {
                    tvMember.setVisibility(View.VISIBLE);
                } else {
                    MessageModel messagePreivous = list.get(position - 1);
                    if (messagePreivous.isSend() || messageModel.getType() == Config.TYPE_DATETIME) {
                        tvMember.setVisibility(View.VISIBLE);
                    } else {
                        UserMessageModel userPrevious = Config.getMemberSendMessage(messagePreivous.getUserOwn()
                                , conversationModel.getUserMessageModels());
                        tvMember.setVisibility(userPrevious.getId() == userMessageCurrent.getId() ? View.GONE : View.VISIBLE);
                    }
                }
            } else {
                tvMember.setVisibility(View.GONE);
            }
        }

        /**Set status cho item gui*/
        if (messageModel.isSend()) {
            imStatus.setVisibility(View.VISIBLE);
            if (messageModel.getStatus() == Config.STATUS_SEEN) {
                ImageUtils.loadImage(imStatus, conversationModel.getImage());
            } else if (messageModel.getStatus() == Config.STATUS_RECEIVED) {
                imStatus.setImageResource(R.drawable.bg_round_0084f0);
            } else if (messageModel.getStatus() == Config.STATUS_NOT_SEND) {
                imStatus.setImageResource(R.drawable.ic_check_2);
            } else if (messageModel.getStatus() == Config.STATUS_NOT_RECEIVED) {
                imStatus.setImageResource(R.drawable.ic_check);
            } else {
                ImageUtils.loadImage(imStatus, conversationModel.getImage());
            }
        }

        /** Xử lý logic ẩn hiện status trong đoạn chat */
        if (messageModel.isSend() && position + 1 == list.size()) {
            imStatus.setVisibility(View.VISIBLE);
        } else {
            imStatus.setVisibility(View.INVISIBLE);
        }
    }
}
