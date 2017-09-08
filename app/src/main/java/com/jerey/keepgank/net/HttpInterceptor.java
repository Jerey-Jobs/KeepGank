package com.jerey.keepgank.net;

import android.util.Log;

import com.jerey.keepgank.utils.NetworkManager;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * HttpInterceptor for cache
 * 需要服务端配合处理缓存请求头，不然会抛出： HTTP 504 Unsatisfiable Request (only-if-cached)
 * <pre>
 *     File httpCacheDirectory = new File(GankApp.getmCachePath(), "networkcache");
 *     OkHttpClient client = new OkHttpClient.Builder()
 *                              .retryOnConnectionFailure(true)
 *                              .connectTimeout(15, TimeUnit.SECONDS)
 *                              .cache(new Cache(httpCacheDirectory, 10 * 1024 * 1024))
 *                              .addInterceptor(new HttpInterceptor())
 *                              .build();
 * </pre>
 * @author xiamin
 */

public class HttpInterceptor implements Interceptor {
    public static final String TAG = HttpInterceptor.class.getSimpleName();

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Log.i(TAG, "request" + request.url().toString());
        /**
         * no network
         */
        if (!NetworkManager.isNetWorkConnect()) {
            Log.i(TAG, "isNetWorkConnect : " + NetworkManager.isNetWorkConnect() +
                    " use cache");
            request = request.newBuilder()
                             .cacheControl(CacheControl.FORCE_CACHE)
                             .url(request.url())
                             .build();
        }
        Response response = chain.proceed(request);

        /** 设置max-age为5分钟之后，这5分钟之内不管有没有网, 都读缓存。
         * max-stale设置为5天，意思是，网络未连接的情况下设置缓存时间为15天 */
        CacheControl cacheControl = new CacheControl.Builder()
                .maxAge(5, TimeUnit.MINUTES)
                .maxStale(15, TimeUnit.DAYS)
                .build();


        Log.i(TAG, "response" + response.message().toString());
        return response.newBuilder()
                       //在这里生成新的响应并修改它的响应头
                       .header("Cache-Control", cacheControl.toString())
                       .removeHeader("Pragma")
                       .build();

    }
}
