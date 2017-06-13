package com.jerey.downloadmanager;

import android.os.Handler;

import java.security.InvalidParameterException;

public class DownloadHelper implements DownloadManager {

    /**
     * Download request queue takes care of handling the request based on priority.
     */
    private DownloadRequestQueue mRequestQueue;

    /**
     * Default constructor
     */
    private DownloadHelper() {
        this(true);
    }

    /**
     * Construct with logging Enabled.
     */
    public DownloadHelper(boolean loggingEnabled) {
        mRequestQueue = new DownloadRequestQueue();
        mRequestQueue.start();
        setLoggingEnabled(loggingEnabled);
    }

    /**
     * Construct with provided callback handler
     *
     * @param callbackHandler
     */
    public DownloadHelper(Handler callbackHandler) throws InvalidParameterException {
        mRequestQueue = new DownloadRequestQueue(callbackHandler);
        mRequestQueue.start();
        setLoggingEnabled(true);
    }

    /**
     * Constructor taking MAX THREAD POOL SIZE  Allows maximum of 4 threads.
     * Any number higher than four or less than one wont be respected.
     * <p>
     * Deprecated use Default Constructor. As the thread pool size will not respected anymore through this constructor.
     * Thread pool size is determined with the number of available processors on the device.
     **/
    public DownloadHelper(int threadPoolSize) {
        mRequestQueue = new DownloadRequestQueue(threadPoolSize);
        mRequestQueue.start();
        setLoggingEnabled(true);
    }

    /**
     * Add a new download.  The download will start automatically once the download manager is
     * ready to execute it and connectivity is available.
     *
     * @param request the parameters specifying this download
     * @return an ID for the download, unique across the application.  This ID is used to make future
     * calls related to this download.
     * @throws IllegalArgumentException
     */
    @Override
    public int add(DownloadRequest request) throws IllegalArgumentException {
        checkReleased("add(...) called on a released ThinDownloadManager.");
        if (request == null) {
            throw new IllegalArgumentException("DownloadRequest cannot be null");
        }
        return mRequestQueue.add(request);
    }

    @Override
    public int cancel(int downloadId) {
        checkReleased("cancel(...) called on a released ThinDownloadManager.");
        return mRequestQueue.cancel(downloadId);
    }

    @Override
    public void cancelAll() {
        checkReleased("cancelAll() called on a released ThinDownloadManager.");
        mRequestQueue.cancelAll();
    }

    @Override
    public int query(int downloadId) {
        checkReleased("query(...) called on a released ThinDownloadManager.");
        return mRequestQueue.query(downloadId);
    }

    @Override
    public void release() {
        if (!isReleased()) {
            mRequestQueue.release();
            mRequestQueue = null;
        }
    }

    @Override
    public boolean isReleased() {
        return mRequestQueue == null;
    }

    /**
     * This is called by methods that want to throw an exception if the DownloadManager
     * has already been released.
     */
    private void checkReleased(String errorMessage) {
        if (isReleased()) {
            throw new IllegalStateException(errorMessage);
        }
    }

    private static void setLoggingEnabled(boolean enabled) {
        Log.setEnabled(enabled);
    }

    private static volatile DownloadHelper instance;

    public static DownloadHelper getInstance() {
        if (instance == null) {
            synchronized (DownloadHelper.class) {
                if (instance == null) {
                    instance = new DownloadHelper();
                }
            }
        }
        return instance;
    }
}


