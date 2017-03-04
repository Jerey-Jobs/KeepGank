package com.jerey.keepgank.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;


/**
 * Created by xiamin on 3/3/17.
 */

public class ImageSave {

    private ScheduledExecutorService mScheduledThreadPool;
    private WeakReference<Context> mContextWeakReference;

    public ImageSave() {
        mScheduledThreadPool = Executors.newScheduledThreadPool(4);
    }

    private static class SingletonHolder {
        private final static ImageSave INSTANCE = new ImageSave();
    }

    public static ImageSave getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "Gank");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
    }

    private static class SaveTask extends AsyncTask<Bitmap,Void,Boolean>{

        @Override
        protected Boolean doInBackground(Bitmap... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
        }
    }

    public void save(Bitmap bitmap){
        new SaveTask().executeOnExecutor(mScheduledThreadPool).execute(bitmap);
    }

    public ImageSave with(Context context){
        mContextWeakReference = new WeakReference<Context>(context);
        return this;
    }
}
