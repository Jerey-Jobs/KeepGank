package com.jerey.lruCache;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;

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

    /******************************************************
     * 重载支持 Json数据读写
     ****************************************************/
    public void put(String key, JSONObject jsonObject) {
        put(key, jsonObject.toString());
    }

    public JSONObject getAsJson(String key) {
        String val = getAsString(key);
        try {
            if (val != null)
                return new JSONObject(val);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /******************************************************
     * 重载支持 JSONArray 读写
     ****************************************************/
    public void put(String key, JSONArray jsonArray) {
        put(key, jsonArray.toString());
    }

    public JSONArray getAsJSONArray(String key) {
        String JSONString = getAsString(key);
        try {
            JSONArray obj = new JSONArray(JSONString);
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /******************************************************
     * 重载支持 byte 读写
     ****************************************************/
    public void put(String key, byte[] value) {
        OutputStream outputStream = null;
        DiskLruCache.Editor editor = null;

        editor = editor(key);
        if (editor == null) return;
        try {
            outputStream = editor.newOutputStream(0);
            outputStream.write(value);
            outputStream.flush();
            editor.commit();
        } catch (IOException e) {
            e.printStackTrace();
            try {
                editor.abort();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public byte[] getAsBytes(String key) {
        byte[] ret = null;
        InputStream inputStram = get(key);
        if (inputStram == null) return null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            byte[] buf = new byte[1024];
            int len = 0;
            while ((len = inputStram.read(buf)) != -1) {
                byteArrayOutputStream.write(buf, 0, len);
            }
            ret = byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStram.close();
                byteArrayOutputStream.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return ret;
    }

    /******************************************************
     * 重载支持 Serializable 读写
     ****************************************************/
    public void put(String key, Serializable value) {
        ObjectOutputStream outputStream = null;
        DiskLruCache.Editor editor = null;

        editor = editor(key);
        if (editor == null) return;
        try {
            outputStream = new ObjectOutputStream(editor.newOutputStream(0));
            outputStream.writeObject(value);
            outputStream.flush();
            editor.commit();
        } catch (IOException e) {
            e.printStackTrace();
            try {
                editor.abort();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public <T> T getAsSerializable(String key) {
        T ret = null;
        InputStream inputStream = get(key);
        if (inputStream == null) return ret;
        ObjectInputStream objInputStream = null;
        try {
            objInputStream = new ObjectInputStream(inputStream);
            ret = (T) objInputStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                objInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    public void close() throws IOException {
        mDiskLruCache.close();
    }

    public void delete() throws IOException {
        mDiskLruCache.delete();
    }

    public void flush() throws IOException {
        mDiskLruCache.flush();
    }

    public boolean isClosed() {
        return mDiskLruCache.isClosed();
    }

    public long size() {
        return mDiskLruCache.size();
    }

    public void setMaxSize(long maxSize) {
        mDiskLruCache.setMaxSize(maxSize);
    }

    public File getDirectory() {
        return mDiskLruCache.getDirectory();
    }

    public long getMaxSize() {
        return mDiskLruCache.getMaxSize();
    }

    public DiskLruCache getDiskLruCache() {
        return mDiskLruCache;
    }

}
