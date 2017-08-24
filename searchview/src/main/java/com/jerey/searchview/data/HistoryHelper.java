package com.jerey.searchview.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.jerey.searchview.HistoryBean;

import java.util.ArrayList;
import java.util.List;

import static com.jerey.searchview.data.HistoryDataBaseHelper.HISTORY_TABLE;

/**
 * @author xiamin
 * @date 8/24/17.
 */
public class HistoryHelper {
    private static final String TAG = HistoryHelper.class.getSimpleName();

    private HistoryDataBaseHelper mHistoryDataBaseHelper;

    public HistoryHelper(Context context) {
        mHistoryDataBaseHelper = new HistoryDataBaseHelper(context);
    }

    public void insertHistory(HistoryBean historyBean) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(HistoryDataBaseHelper.SEARCH_HISTORY_CONTENT,
                historyBean.getContent().trim());
        if (historyBean.getDrawableURL() != null) {
            contentValues.put(HistoryDataBaseHelper.SEARCH_HISTORY_URL, historyBean
                    .getDrawableURL());
        }
        SQLiteDatabase sqLiteDatabase = mHistoryDataBaseHelper.getWritableDatabase();
        sqLiteDatabase.insert(HISTORY_TABLE, null, contentValues);
        mHistoryDataBaseHelper.close();
    }


    public List<HistoryBean> getHistoryList() {
        String dbString = "SELECT * FROM " + HISTORY_TABLE;
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

    public List<HistoryBean> searchHistoryList(String search) {
        String dbString = "SELECT * FROM " + HISTORY_TABLE +
                " WHERE " + HistoryDataBaseHelper.SEARCH_HISTORY_CONTENT +
                " LIKE " + "'%" + search + "%'";
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

    public void clearHistory() {
        SQLiteDatabase sqLiteDatabase = mHistoryDataBaseHelper.getWritableDatabase();
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + HISTORY_TABLE);
        mHistoryDataBaseHelper.close();
    }
}
