package com.jerey.keepgank.net;


import com.jerey.keepgank.douban.bean.MovieInfoBean;
import com.jerey.keepgank.douban.bean.TypeInfoBean;
import com.jerey.keepgank.douban.bean.USBean;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;


/**
 * @author xiamin
 * @date 8/17/17.
 */
public class DoubanApi {
    public static final String BASE_URL = "http://api.douban.com";
    public static final int LOAD_LIMIT = 20;

    public static DoubanApi INSTACE;
    private static DoubanInterface doubanInterface;

    public static DoubanApi getInstance() {
        if (INSTACE == null) {
            synchronized (DoubanApi.class) {
                if (INSTACE == null) {
                    INSTACE = new DoubanApi();
                }
            }
        }
        return INSTACE;
    }

    public DoubanApi() {

        OkHttpClient client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)//连接失败后是否重新连接
                .connectTimeout(15, TimeUnit.SECONDS)//超时时间15S
                .addInterceptor(new HttpInterceptor())
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        doubanInterface = retrofit.create(DoubanInterface.class);
    }

    public interface DoubanInterface {
        //http://api.douban.com/v2/movie/top250?start=10&count=10
        @GET("/v2/movie/top250")
        Observable<TypeInfoBean> getTop250(@Query("start") int start,
                                           @Query("count") int count);

        /**
         * http://api.douban.com/v2/movie/coming_soon?start=10&count=10
         * 即将上映
         */
        @GET("/v2/movie/coming_soon")
        Observable<TypeInfoBean> getCommingSoon(@Query("start") int start,
                                                @Query("count") int count);

        /**
         * http://api.douban.com/v2/movie/us_box
         * 欧美热映
         */
        @GET("/v2/movie/us_box")
        Observable<USBean> getUSMovie();

        /**
         * http://api.douban.com/v2/movie/in_theaters?start=10&count=10
         * 正在上映的电影
         */
        @GET("/v2/movie/coming_soon")
        Observable<TypeInfoBean> getInTheaters(@Query("start") int start,
                                                @Query("count") int count);

        /**
         * http://api.douban.com/v2/movie/subject/26363254
         * 某个电影信息
         */
        @GET("/v2/movie/subject/{id}")
        Observable<MovieInfoBean> getMonvieInfo(@Path("id") int id);

    }

    public DoubanInterface getDoubanInterface() {
        return doubanInterface;
    }

}
