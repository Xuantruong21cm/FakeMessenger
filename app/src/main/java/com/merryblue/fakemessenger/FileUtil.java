package com.merryblue.fakemessenger;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;


import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class FileUtil {

    public static File createFolder(Activity activity, String folderName) {
        String fileName = System.currentTimeMillis() + ".png";

        File path;
        File file;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            File rootFolder = new File(activity.getExternalFilesDir(null), folderName);
            if (!rootFolder.exists()) {
                rootFolder.mkdirs();
                Log.d("LOG_TAG", "Directory not created");
            }
            return new File(rootFolder, fileName);
//        } else {
//            path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
//            file = new File(path + "/" + "FakeMessenger");
//            if (!file.exists()) {
//                file.mkdirs();
//                Log.d("LOG_TAG", "Directory not created");
//            }
//
//            return new File(file, fileName);
//        }
    }

    public static String getFolderName(String name) {
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), name);
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return "";
            }
        }
        return mediaStorageDir.getAbsolutePath();
    }


    private static boolean isSDAvailable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static File getNewFile(Context context, String folderName) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH);
        String timeStamp = simpleDateFormat.format(new Date());
        String path;
        if (isSDAvailable()) {
            path = getFolderName(folderName) + File.separator + timeStamp + ".jpg";
        } else {
            path = context.getFilesDir().getPath() + File.separator + timeStamp + ".jpg";
        }

        if (TextUtils.isEmpty(path)) {
            return null;
        }

        return new File(path);
    }

    public static String saveImageToGallery(@NonNull Context context, @NonNull File file, @NonNull Bitmap bitmap) {
//        String fileName = System.currentTimeMillis() + ".png";
//        File rootFolder = new File(context.getExternalFilesDir(null), context.getString(R.string.app_name));
//        if (!rootFolder.exists()) {
//            rootFolder.mkdirs();
//            Log.d("LOG_TAG", "Directory not created");
//        }
//        return new File(rootFolder, fileName).getAbsolutePath();
        try {
            OutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());
            context.sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.fromFile(file)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("saveImageToGallery: the path of bmp is ");
        stringBuilder.append(file.getAbsolutePath());
        return file.getAbsolutePath();
    }

}
