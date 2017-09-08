package com.jerey.keepgank.net;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author Xiamin
 * @date 2017/9/8
 */
public class HttpDiskLruCacheInterceptor implements Interceptor {
    public static final String TAG = HttpDiskLruCacheInterceptor.class.getSimpleName();

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Log.i(TAG, "request" + request.url().toString());
        Response response = chain.proceed(request);
        return response;
    }
}
