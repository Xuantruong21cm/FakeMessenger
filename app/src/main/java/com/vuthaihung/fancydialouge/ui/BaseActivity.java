package com.vuthaihung.fancydialouge.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.vuthaihung.fancydialouge.Toolbox;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.concurrent.Callable;

public abstract class BaseActivity extends AppCompatActivity {

    private Callable<Void> callable;
    private SelectImageListener selectImageListener;
    private SelectContactListener selectContactListener;
    private static final int PICK_FROM_GALLERY = 1000;
    private static final int PICK_FROM_GALLERY_NOT_CROP = 1002;
    private static final int MY_PERMISSIONS_REQUEST_STORAGE = 1001;
    private static final int MY_REQUEST_CONTACT = 2000;
    private static final int MY_PERMISSIONS_REQUEST_CONTACT = 2001;
    private View viewContainer;

    private Long timeClick;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewContainer = getLayoutResource();
        setContentView(viewContainer);
        View layoutPadding = getViewPadding();
        if (layoutPadding != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && Toolbox.getHeightStatusBar(this) > 0) {
                layoutPadding.setPadding(0, Toolbox.getHeightStatusBar(this), 0, 0);
            }
        }
        Toolbox.setStatusBarHomeWhite(this);
        initView();
        initData();
        initControl();
    }

    protected View getViewPadding() {
        return null;
    }

    protected abstract View getLayoutResource();

    protected abstract void initView();

    protected abstract void initData();

    protected abstract void initControl();

    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        super.onAttachFragment(fragment);
    }

    public void toast(String content) {
        if (!TextUtils.isEmpty(content))
            Toast.makeText(this, content, Toast.LENGTH_LONG).show();
    }

    public void askPermissionContact(Callable<Void> callable) throws Exception {
        this.callable = callable;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CONTACTS}, MY_PERMISSIONS_REQUEST_CONTACT);
            } else {
                callable.call();
            }
        } else {
            callable.call();
        }
    }

    public void askPermissionStorage(Callable<Void> callable) throws Exception {
        this.callable = callable;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE
                                , Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_STORAGE);
            } else {
                callable.call();
            }
        } else {
            callable.call();
        }
    }

    public void requestGetContact(SelectContactListener selectContactListener) {
        this.selectContactListener = selectContactListener;
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(intent, MY_REQUEST_CONTACT);
    }

    public void requestGetGallery(SelectImageListener selectImageListener) {
        this.selectImageListener = selectImageListener;
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(galleryIntent, PICK_FROM_GALLERY);
    }

    public void requestGetGalleryNotCrop(SelectImageListener selectImageListener) {
        this.selectImageListener = selectImageListener;
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(galleryIntent, PICK_FROM_GALLERY_NOT_CROP);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_STORAGE:
            case MY_PERMISSIONS_REQUEST_CONTACT:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    try {
                        this.callable.call();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FROM_GALLERY_NOT_CROP && resultCode == RESULT_OK && null != data) {
            if (selectImageListener != null)
                selectImageListener.onImageSelectListener(data.getData().getPath());
        }
        if (requestCode == PICK_FROM_GALLERY && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            CropImage.activity(selectedImage)
                    .start(this);
            // String picturePath contains the path of selected Image
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && null != data) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            Uri resultUri = result.getUri();
            if (selectImageListener != null)
                selectImageListener.onImageSelectListener(resultUri.getPath());
        } else if (requestCode == MY_REQUEST_CONTACT && resultCode == RESULT_OK) {
            Cursor query = getContentResolver().query(data.getData()
                    , new String[]{ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts.PHOTO_THUMBNAIL_URI}, null, null, null);
            if (query.moveToFirst()) {
                String contactName = query.getString(query.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String imageContact = query.getString(query.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI));
                if (selectContactListener != null)
                    selectContactListener.onContactSelectListener(contactName, imageContact);
            }
            query.close();
        }
    }

    public interface SelectImageListener {
        void onImageSelectListener(String path);
    }

    public interface SelectContactListener {
        void onContactSelectListener(String name, String image);
    }
}
