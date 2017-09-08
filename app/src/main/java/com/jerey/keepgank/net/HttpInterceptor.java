package com.jerey.keepgank.net;

import android.util.Log;

import com.jerey.keepgank.utils.ApplicationUtils;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * HttpInterceptor for cache
 * @author xiamin
 */
public class HttpInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        String TAG_REQUEST = "request";
        Log.e(TAG_REQUEST, "request" + request.url().toString());
        /**
         * no network
         */
        if (!ApplicationUtils.isNetworkAvailable()) {
            request = request.newBuilder()
                             .cacheControl(CacheControl.FORCE_CACHE)
                             .url(request.url())
                             .build();
        }
        Response response = chain.proceed(request);
        /**
         * after process, cache the reponse
         */
        if (ApplicationUtils.isNetworkAvailable()) {
            // read from cache for 10 minute
            int maxAge = 60 * 60 * 10;
            response.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", "public, max-age=" + maxAge)
                    .build();
        } else {
            int maxStale = 60 * 60 * 24 * 30;
            response.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                    .build();
        }
        return response;
    }
}
