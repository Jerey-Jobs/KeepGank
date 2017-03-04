package com.jerey.lruCache;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import static com.jerey.lruCache.DiskLruCache.TAG;

/**
 * Created by xiamin on 3/4/17.
 */

public class DiskLruCacheManager {
    private static final String DEFAULT_DIR_NAME = "diskCache";
    private static final int MAX_COUNT = 10 * 1024 * 1024;
    private static final int DEFAULT_APP_VERSION = 1;
    private static final int DEFAULT_VALUE_COUNT = 1;

    private static DiskLruCache mDiskLruCache = null;

    public DiskLruCacheManager(Context context) throws IOException {
        this(context, DEFAULT_DIR_NAME, MAX_COUNT);
    }

    public DiskLruCacheManager(Context context, String dirName) throws IOException {
        this(context, dirName, MAX_COUNT);
    }

    public DiskLruCacheManager(Context context, String dirName, int maxCount) throws IOException {
        mDiskLruCache = generateCache(context, dirName, maxCount);
    }

    //custom cache dir
    public DiskLruCacheManager(File dir) throws IOException {
        this(null, dir, MAX_COUNT);
    }

    public DiskLruCacheManager(Context context, File dir) throws IOException {
        this(context, dir, MAX_COUNT);
    }

    public DiskLruCacheManager(Context context, File dir, int maxCount) throws IOException {
        mDiskLruCache = generateCache(context, dir, maxCount);
    }

    private DiskLruCache generateCache(Context context, String dirName, int maxCount) throws IOException {
        DiskLruCache diskLruCache = DiskLruCache.open(
                getDiskCacheDir(context, dirName),
                Utils.getAppVersion(context),
                DEFAULT_VALUE_COUNT,
                maxCount);
        return diskLruCache;
    }

    private DiskLruCache generateCache(Context context, File dir, int maxCount) throws IOException {
        if (!dir.exists() || !dir.isDirectory()) {
            throw new IllegalArgumentException(
                    dir + " is not a directory or does not exists. ");
        }

        int appVersion = context == null ? DEFAULT_APP_VERSION : Utils.getAppVersion(context);

        DiskLruCache diskLruCache = DiskLruCache.open(
                dir,
                appVersion,
                DEFAULT_VALUE_COUNT,
                maxCount);

        return diskLruCache;
    }

    /**
     * 获取缓存的路径 两个路径在卸载程序时都会删除，因此不会在卸载后还保留乱七八糟的缓存
     * 有SD卡时获取  /sdcard/Android/data/<application package>/cache
     * 无SD卡时获取  /data/data/<application package>/cache
     *
     * @param context    上下文
     * @param uniqueName 缓存目录下的细分目录，用于存放不同类型的缓存
     * @return 缓存目录 File
     */
    private File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }

    // =======================================
    // ===遇到文件比较大的，可以直接通过流读写 =====
    // =======================================
    //basic editor
    public DiskLruCache.Editor editor(String key) {
        try {
            key = Utils.hashKeyForDisk(key);
            //wirte DIRTY
            DiskLruCache.Editor edit = mDiskLruCache.edit(key);
            //edit maybe null :the entry is editing
            if (edit == null) {
                Log.w(TAG, "the entry spcified key:" + key + " is editing by other . ");
            }
            return edit;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    //basic get
    public InputStream get(String key) {
        try {
            DiskLruCache.Snapshot snapshot = mDiskLruCache.get(Utils.hashKeyForDisk(key));
            if (snapshot == null) //not find entry , or entry.readable = false
            {
                Log.e(TAG, "not find entry , or entry.readable = false");
                return null;
            }
            //write READ
            return snapshot.getInputStream(0);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    // =======================================
    // ============== String 数据 读写 =============
    // =======================================

    public void put(String key, String value) {
        DiskLruCache.Editor edit = null;
        BufferedWriter bw = null;
        try {
            edit = editor(key);
            if (edit == null) return;
            OutputStream os = edit.newOutputStream(0);
            bw = new BufferedWriter(new OutputStreamWriter(os));
            bw.write(value);
            edit.commit();//write CLEAN
        } catch (IOException e) {
            e.printStackTrace();
            try {
                edit.abort();//write REMOVE
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } finally {
            try {
                if (bw != null)
                    bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getAsString(String key) {
        InputStream inputStream = null;
        inputStream = get(key);
        if (inputStream == null) return null;
        String str = null;
        try {
            str = Util.readFully(new InputStreamReader(inputStream, Util.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
            try {
                inputStream.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return str;
    }
}
