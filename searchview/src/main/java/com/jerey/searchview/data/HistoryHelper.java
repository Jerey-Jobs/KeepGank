package com.jerey.searchview.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.jerey.searchview.HistoryBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiamin
 * @date 8/24/17.
 */
public class HistoryHelper {
    private static final String TAG = HistoryHelper.class.getSimpleName();

    private HistoryDataBaseHelper mHistoryDataBaseHelper;

    public HistoryHelper(Context context) {
        mHistoryDataBaseHelper = new HistoryDataBaseHelper(context);
        mHistoryDataBaseHelper.onCreate(mHistoryDataBaseHelper.getWritableDatabase());
    }

    public void insertHistory(HistoryBean historyBean) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(HistoryDataBaseHelper.SEARCH_HISTORY_CONTENT,
                historyBean.getContent().trim());
        contentValues.put(HistoryDataBaseHelper.SEARCH_TYPE, historyBean.getType());
        if (historyBean.getDrawableURL() != null) {
            contentValues.put(HistoryDataBaseHelper.SEARCH_HISTORY_URL, historyBean
                    .getDrawableURL());
        }
        contentValues.put(HistoryDataBaseHelper.SEARCH_HISTORY_TIME, System.currentTimeMillis());
        SQLiteDatabase sqLiteDatabase = mHistoryDataBaseHelper.getWritableDatabase();
        sqLiteDatabase.insert(mHistoryDataBaseHelper.getHistoryTableName(), null, contentValues);
        mHistoryDataBaseHelper.close();
    }


    public List<HistoryBean> getHistoryList() {
        String dbString = "SELECT * FROM " + mHistoryDataBaseHelper.getHistoryTableName();
        SQLiteDatabase sqLiteDatabase = mHistoryDataBaseHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(dbString, null);
        List<HistoryBean> list = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                HistoryBean historyBean = new HistoryBean();
                int contentColumn = cursor.getColumnIndex(HistoryDataBaseHelper
                        .SEARCH_HISTORY_CONTENT);
                int urlColumn = cursor.getColumnIndex(HistoryDataBaseHelper.SEARCH_HISTORY_URL);
                historyBean.setContent(cursor.getString(contentColumn));
                historyBean.setDrawableURL(cursor.getString(urlColumn));
                Log.i(TAG, "searchHistoryList: " + historyBean.getContent() + " " + historyBean
                        .getDrawableURL());
                list.add(historyBean);
            } while (cursor.moveToNext());
        }
        cursor.close();
        mHistoryDataBaseHelper.close();
        return list;
    }

    /**
     * 根据文字从数据库搜索最近的十条包含其内容的历史记录
     * @param search
     * @return
     */
    public List<HistoryBean> searchHistoryList(String search, String type, int count) {
        String dbString;
        if (TextUtils.isEmpty(type)) {
            dbString = "select * from " + mHistoryDataBaseHelper.getHistoryTableName() +
                    " where " + HistoryDataBaseHelper.SEARCH_HISTORY_CONTENT +
                    " like " + "'%" + search + "%'" + " order by " + HistoryDataBaseHelper
                    .SEARCH_HISTORY_TIME + " desc limit " + count;
        } else {
            dbString = "select * from " + mHistoryDataBaseHelper.getHistoryTableName() +
                    " where " + HistoryDataBaseHelper.SEARCH_HISTORY_CONTENT +
                    " like " + "'%" + search + "%'" +
                    " and " + HistoryDataBaseHelper.SEARCH_TYPE + " = '" + type
                    + "' order by " + HistoryDataBaseHelper
                    .SEARCH_HISTORY_TIME + " desc limit " + count;
        }
        Log.d(TAG, "searchHistoryList: " + dbString);
        SQLiteDatabase sqLiteDatabase = mHistoryDataBaseHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(dbString, null);
        List<HistoryBean> list = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                HistoryBean historyBean = new HistoryBean();
                int contentColumn = cursor.getColumnIndex(HistoryDataBaseHelper
                        .SEARCH_HISTORY_CONTENT);
                int urlColumn = cursor.getColumnIndex(HistoryDataBaseHelper.SEARCH_HISTORY_URL);
                historyBean.setContent(cursor.getString(contentColumn));
                historyBean.setDrawableURL(cursor.getString(urlColumn));
                Log.i(TAG, "searchHistoryList: " + historyBean.getContent() + " " + historyBean
                        .getDrawableURL());
                list.add(historyBean);
            } while (cursor.moveToNext());
        }
        cursor.close();
        mHistoryDataBaseHelper.close();
        return list;
    }

    public void deleteAllHistory() {
        SQLiteDatabase sqLiteDatabase = mHistoryDataBaseHelper.getWritableDatabase();
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + mHistoryDataBaseHelper
                .getHistoryTableName());
        mHistoryDataBaseHelper.close();
    }

    public void deleteHistoryByType(String type) {
        try {
            String sql = "DELETE FROM " + mHistoryDataBaseHelper.getHistoryTableName() +
                    " WHERE " + HistoryDataBaseHelper.SEARCH_TYPE + " = '" + type + "'";
            Log.i(TAG, "deleteHistoryByType SQL: " + sql);
            SQLiteDatabase sqLiteDatabase = mHistoryDataBaseHelper.getWritableDatabase();
            sqLiteDatabase.execSQL(sql);
            mHistoryDataBaseHelper.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
