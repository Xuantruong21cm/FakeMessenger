package com.merryblue.fakemessenger.ui.mainScreen;

import static com.merryblue.fakemessenger.Config.SHOW_SLIDE_HINT;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.viewpager.widget.ViewPager;

import com.android.library.coachmark.components.CoachMarkInfo;
import com.android.library.coachmark.components.CoachMarkOverlay;
import com.android.library.coachmark.components.CoachMarkSkipButton;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.koushikdutta.ion.Ion;
import com.merryblue.fakemessenger.Config;
import com.merryblue.fakemessenger.ImageUtils;
import com.merryblue.fakemessenger.PreferencesHelper;
import com.merryblue.fakemessenger.R;
import com.merryblue.fakemessenger.ViewUtils;
import com.merryblue.fakemessenger.adapter.ConversationHorizontalAdapter;
import com.merryblue.fakemessenger.adapter.ConversationVerticalAdapter;
import com.merryblue.fakemessenger.adapter.ViewPagerAdapter;
import com.merryblue.fakemessenger.databinding.ActivityMainBinding;
import com.merryblue.fakemessenger.databinding.LayoutMainBottomBinding;
import com.merryblue.fakemessenger.databinding.LayoutMainTopBinding;
import com.merryblue.fakemessenger.dialog.DialogCreateConversation;
import com.merryblue.fakemessenger.dialog.DialogSetActionConversation;
import com.merryblue.fakemessenger.model.ConversationModel;
import com.merryblue.fakemessenger.ui.BaseActivity;
import com.merryblue.fakemessenger.ui.detailScreen.ConversationDetailActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

import tourguide.tourguide.Overlay;
import tourguide.tourguide.Pointer;
import tourguide.tourguide.ToolTip;
import tourguide.tourguide.TourGuide;

public class MainActivity extends BaseActivity implements IActionMain.IView, View.OnClickListener {

    private ActivityMainBinding activityMainBinding;
    private LayoutMainBottomBinding bottomBinding;
    private LayoutMainTopBinding topBinding;
    private MainPresenter presenter;
    private ConversationHorizontalAdapter horizontalAdapter;
    private ConversationVerticalAdapter verticalAdapter;
    public static List<ConversationModel> lstData = new ArrayList<>();
    public TourGuide mTutorialHandler;
    private int dotscount;
    private ImageView[] dots;
    int currPos;
    int count = 0;

    @Override
    protected View getLayoutResource() {
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        bottomBinding = activityMainBinding.layoutBottom;
        topBinding = activityMainBinding.layoutTop;
        checkPermiss();
        return activityMainBinding.getRoot();
    }

    @Override
    protected void initView() {
        ViewUtils.setupUI(activityMainBinding.container, this);
        presenter = new MainPresenter(this);
        horizontalAdapter = new ConversationHorizontalAdapter(this, new ArrayList<>());
        activityMainBinding.rcvHorizontal.setAdapter(horizontalAdapter);

        verticalAdapter = new ConversationVerticalAdapter(this, new ArrayList<>(), this::showPopupItemConversation);
        activityMainBinding.rcvVertical.setAdapter(verticalAdapter);


        if (PreferencesHelper.getInt(SHOW_SLIDE_HINT, 0) < 3) {
            initSlideShow();
            count = PreferencesHelper.getInt(SHOW_SLIDE_HINT, 0) + 1;
            PreferencesHelper.putInt(SHOW_SLIDE_HINT, count);
            Log.e("SHOW_SLIDE_HINT", String.valueOf(PreferencesHelper.getInt(SHOW_SLIDE_HINT, 0)));
        } else activityMainBinding.guideView.setVisibility(View.GONE);
    }

    private void initSlideShow() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(MainActivity.this);
        activityMainBinding.slideView.setAdapter(viewPagerAdapter);

        activityMainBinding.slideView.setAdapter(viewPagerAdapter);
        dotscount = viewPagerAdapter.getCount();
        dots = new ImageView[dotscount];

        for (int i = 0; i < dotscount; i++) {

            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(10, 0, 10, 0);

            activityMainBinding.SliderDots.addView(dots[i], params);
        }
        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));
        activityMainBinding.slideView.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currPos = position;
                for (int i = 0; i < dotscount; i++) {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
                }

                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        currPos = activityMainBinding.slideView.getCurrentItem();
        activityMainBinding.btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityMainBinding.guideView.setVisibility(View.GONE);
            }
        });

        activityMainBinding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (currPos == dotscount - 1) {
                    activityMainBinding.guideView.setVisibility(View.GONE);
                } else {
                    activityMainBinding.slideView.setCurrentItem(currPos + 1);
                }
                Log.e("currPos == dotscount", String.valueOf(currPos) + " : " + dotscount);
            }
        });

    }


    @Override
    protected void initData() {
        if (presenter != null)
            presenter.getListConversation();
        setImageAvatar(PreferencesHelper.getString(PreferencesHelper.PHOTO_AVATAR_PATH));
    }

    @Override
    protected void initControl() {
        topBinding.imAvatar.setOnClickListener(this);
        topBinding.imEdit.setOnClickListener(this);
        activityMainBinding.swipeRefreshLayout.setOnRefreshListener(() -> {
            activityMainBinding.swipeRefreshLayout.setRefreshing(false);
            if (presenter != null)
                presenter.getListConversation();
        });

        horizontalAdapter.setOnItemClickListener(position -> {
            if (position != 0) {
                Intent intent = new Intent(this, ConversationDetailActivity.class);
                intent.putExtra(ConversationDetailActivity.DATA_SELECT, horizontalAdapter.list.get(position));
                startActivityForResult(intent, Config.REQUEST_CODE_ACT);
            } else {
                List<ConversationModel> lstData = new ArrayList<>();
                for (ConversationModel model : verticalAdapter.list) {
                    if (!model.isGroup()) {
                        lstData.add(model);
                    }
                }
                new DialogSetActionConversation.ExtendBuilder()
                        .setLstConversation(lstData)
                        .onSetPositiveButton(getString(R.string.ok), (baseDialog, data) -> {
                            if (presenter != null) {
                                presenter.getListConversation();
                            }
                            baseDialog.dismiss();
                        })
                        .onSetNegativeButton(getString(R.string.cancel), baseDialog -> {
                            if (presenter != null) {
                                presenter.getListConversation();
                            }
                            baseDialog.dismiss();
                        })
                        .build()
                        .show(getSupportFragmentManager(), DialogSetActionConversation.class.getName());
            }
//                return;

        });

        verticalAdapter.setOnItemClickListener(position -> {
            Intent intent = new Intent(this, ConversationDetailActivity.class);
            intent.putExtra(ConversationDetailActivity.DATA_SELECT, verticalAdapter.list.get(position));
            startActivityForResult(intent, Config.REQUEST_CODE_ACT);
        });
    }

    @Override
    protected View getViewPadding() {
        return activityMainBinding.container;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.im_avatar:
                mTutorialHandler.cleanUp();
                try {
                    askPermissionStorage(() -> {
                        requestGetGallery(path -> {
                            PreferencesHelper.putString(PreferencesHelper.PHOTO_AVATAR_PATH, path);
                            setImageAvatar(path);
                            if (verticalAdapter != null)
                                verticalAdapter.notifyDataSetChanged();
                        });
                        return null;
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.im_edit:
                showPopupSelectCreateChat();
                break;
        }
    }

    @Override
    public void showPopupSelectCreateChat() {
        PopupMenu popup = new PopupMenu(MainActivity.this, topBinding.imEdit);
        popup.getMenuInflater()
                .inflate(R.menu.menu_create_conversation, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_create_chat:
                    showDialogCreateChat(false);
                    break;
                case R.id.menu_create_group:
                    showDialogCreateChat(true);
                    break;
                case R.id.menu_add_contact:
                    try {
                        askPermissionContact(() -> {
                            requestGetContact((name, image) -> {
                                HashMap<String, Object> datas = new HashMap<>();
                                datas.put(Config.KEY_TITLE, name);
                                datas.put(Config.KEY_AVATAR, image);
                                datas.put(Config.KEY_GROUP, false);
                                if (presenter != null) {
                                    presenter.createConvesation(datas);
                                }
                            });
                            return null;
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
            }
            return true;
        });
        popup.show();
    }

    @Override
    public void showPopupItemConversation(int index, View itemView) {
        PopupMenu popup = new PopupMenu(MainActivity.this, itemView);
        popup.getMenuInflater()
                .inflate(R.menu.menu_item_conversation, popup.getMenu());
        ConversationModel conversationModel = verticalAdapter.list.get(index);
        popup.setOnMenuItemClickListener(item -> {
            if (presenter == null)
                return true;
            switch (item.getItemId()) {
                case R.id.menu_delete:
                    presenter.deleteConvesation(conversationModel);
                    break;
                case R.id.menu_set_seen:
                    presenter.setStatusConversation(conversationModel, Config.STATUS_SEEN);
                    verticalAdapter.notifyItemChanged(index);
                    if (presenter != null)
                        presenter.getCountReceived();
                    break;
                case R.id.menu_set_received:
                    presenter.setStatusConversation(conversationModel, Config.STATUS_RECEIVED);
                    verticalAdapter.notifyItemChanged(index);
                    if (presenter != null)
                        presenter.getCountReceived();
                    break;
                case R.id.menu_not_send:
                    presenter.setStatusConversation(conversationModel, Config.STATUS_NOT_SEND);
                    verticalAdapter.notifyItemChanged(index);
                    if (presenter != null)
                        presenter.getCountReceived();
                    break;
                case R.id.menu_not_received:
                    presenter.setStatusConversation(conversationModel, Config.STATUS_NOT_RECEIVED);
                    verticalAdapter.notifyItemChanged(index);
                    if (presenter != null)
                        presenter.getCountReceived();
                    break;
            }
            return true;
        });
        popup.show();
    }

    @Override
    public void setImageAvatar(String url) {
        ImageUtils.loadImage(topBinding.imAvatar, url);
    }

    @Override
    public void showDialogCreateChat(boolean isCreateGroup) {
        new DialogCreateConversation.ExtendBuilder()
                .onSelectImageListener(dialog -> {
                    try {
                        askPermissionStorage(() -> {
                            requestGetGallery(dialog::setImageConversation);
                            return null;
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                })
                .setCreateGroup(isCreateGroup)
                .setTitle(getString(isCreateGroup ? R.string.create_group : R.string.create_chat))
                .onSetPositiveButton(getString(R.string.ok), (baseDialog, data) -> {
                    if (presenter != null) {
                        presenter.createConvesation(data);
                    }
                    baseDialog.dismiss();
                })
                .onSetNegativeButton(getString(R.string.cancel), DialogFragment::dismiss)
                .build()
                .show(getSupportFragmentManager(), DialogCreateConversation.class.getName());
    }

    @Override
    public void fillListDataHorizontal(List<ConversationModel> lstData) {
        horizontalAdapter.clear();
        horizontalAdapter.addAll(lstData);
//        this.lstData = lstData;
    }

    @Override
    public void fillListDataVertical(List<ConversationModel> lstData) {
        verticalAdapter.clear();
        verticalAdapter.addAll(lstData);
    }

    @Override
    public void setCountReceiverd(int count) {
        bottomBinding.tvCountReceived.setVisibility(count == 0 ? View.GONE : View.VISIBLE);
        if (count < 100) {
            bottomBinding.tvCountReceived.setText(String.valueOf(count));
        } else {
            bottomBinding.tvCountReceived.setText("99+");
        }
    }

    @Override
    public void deleteConversationSuccess() {
        toast(getString(R.string.delete_conversation_success));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Config.REQUEST_CODE_ACT) {
            if (presenter != null)
                presenter.getListConversation();
        }
    }

    public void checkPermiss() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
        Dexter.withActivity(this).withPermissions("android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE").withListener(new MultiplePermissionsListener() {
            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                multiplePermissionsReport.areAllPermissionsGranted();
                if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle((CharSequence) "Change Permissions in Settings");
                    builder.setMessage((CharSequence) "\nClick SETTINGS to Manually Set\nPermissions to use this app").setCancelable(false).setPositiveButton((CharSequence) "SETTINGS", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
                            intent.setData(Uri.fromParts("package", MainActivity.this.getPackageName(), (String) null));
                            MainActivity.this.startActivityForResult(intent, 1000);
                        }
                    });
                    builder.create().show();
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }

        }).onSameThread().check();
//        } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
//            Dexter.withActivity(this).withPermissions("android.permission.READ_EXTERNAL_STORAGE").withListener(new MultiplePermissionsListener() {
//                public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
//                    multiplePermissionsReport.areAllPermissionsGranted();
//                    if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
//                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//                        builder.setTitle((CharSequence) "Change Permissions in Settings");
//                        builder.setMessage((CharSequence) "\nClick SETTINGS to Manually Set\nPermissions to use this app").setCancelable(false).setPositiveButton((CharSequence) "SETTINGS", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
//                                intent.setData(Uri.fromParts("package", MainActivity.this.getPackageName(), (String) null));
//                                MainActivity.this.startActivityForResult(intent, 1000);
//                            }
//                        });
//                        builder.create().show();
//                    }
//                }
//
//                @Override
//                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
//                    permissionToken.continuePermissionRequest();
//                }
//
//            }).onSameThread().check();
//        }

    }

    private boolean hasPermission(Activity ac) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ac.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ac.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

}