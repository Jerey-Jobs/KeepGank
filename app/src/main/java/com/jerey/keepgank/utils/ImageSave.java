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
    private static final String TAG = "ImageSave";
    private static final String TYPE = ".jpg";

    private static ScheduledExecutorService mScheduledThreadPool;
    //降低一下与context的耦合度,避免内存泄漏
    private WeakReference<Context> mContextWeakReference;
    private ImageSaveListener mListener;

    private ImageSave(Context context) {
        //双重校验锁,保证线程安全和惰加载
        if (mScheduledThreadPool == null) {
            synchronized (ImageSave.class) {
                if (mScheduledThreadPool == null) {
                    mScheduledThreadPool = Executors.newScheduledThreadPool(4);
                }
            }
        }
        mContextWeakReference = new WeakReference<Context>(context);
    }


    private class SaveTask extends AsyncTask<Bitmap, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Bitmap... params) {
            for (Bitmap bmp : params) {
                // 首先保存图片
                File appDir = new File(Environment.getExternalStorageDirectory(), TAG);
                if (!appDir.exists()) {
                    appDir.mkdir();
                }
                String fileName = System.currentTimeMillis() + TYPE;
                File file = new File(appDir, fileName);
                try {
                    FileOutputStream fos = new FileOutputStream(file);
                    bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.flush();
                    fos.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    return false;
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }

                Context context = mContextWeakReference.get();
                if (context != null) {
                    // 其次把文件插入到系统图库
                    try {
                        MediaStore.Images.Media.insertImage(context.getContentResolver(),
                                file.getAbsolutePath(), fileName, null);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        return false;
                    }
                    // 最后通知图库更新
                    context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                            Uri.parse("file://" + Environment.getExternalStorageDirectory())));
                }
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (mListener == null) {
                return;
            }
            if (aBoolean == true) {
                mListener.onSuccess();
            } else {
                mListener.onError();
            }
        }
    }

    /**
     * This method will be executed by a ThreadPool
     *
     * @param bitmaps
     * @return
     */
    public ImageSave save(Bitmap... bitmaps) {
        new SaveTask().executeOnExecutor(mScheduledThreadPool, bitmaps);
        return this;
    }

    public static ImageSave with(Context context) {
        return new ImageSave(context);
    }

    public interface ImageSaveListener {
        public void onSuccess();

        public void onError();
    }

    /**
     * The listener will be in UI thread, so you can do some UI hint;
     *
     * @param listener
     */
    public void setImageSaveListener(ImageSaveListener listener) {
        mListener = listener;
    }
}
