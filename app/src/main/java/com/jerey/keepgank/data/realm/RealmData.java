package com.jerey.keepgank.data.realm;

import android.content.Context;

import com.jerey.keepgank.data.bean.Bookmark;
import com.jerey.keepgank.data.IData;

import java.util.List;

import io.realm.DynamicRealm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import rx.Observable;

/**
 * Created by Xiamin on 2017/2/12.
 */

public class RealmData implements IData{

    private Context mContext;
    private RealmConfiguration mConfiguration;


    public RealmData(Context context){
        this(context, null);
    }

    public RealmData(Context context, RealmConfiguration configuration){
        this.mContext = context;
        if(configuration == null){
            this.mConfiguration = new RealmConfiguration.Builder(context)
                    .name("Gank.realm")
                    .schemaVersion(0)
                    .setModules(new GankModule())
                    .migration(new RealmMigration() {
                        @Override
                        public void migrate(DynamicRealm dynamicRealm, long l, long l1) {
                            if (l == 0) {
                                l++;
                            }
                        }
                    })
                    .build();
        } else {
            this.mConfiguration = configuration;
        }
    }

    @Override
    public Observable<Bookmark> addBookmark(Bookmark bookmark) {
        return null;
    }

    @Override
    public Observable<Bookmark> removeBookmark(String id) {
        return null;
    }

    @Override
    public Observable<Bookmark> findBookmarkById(String id) {
        return null;
    }

    @Override
    public Observable<List<Bookmark>> getBookmarkList() {
        return null;
    }

    @Override
    public Observable<List<Bookmark>> getBookmarkList(String type) {
        return null;
    }
}
