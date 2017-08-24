package com.jerey.searchview.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jerey.searchview.HistoryBean;

import java.util.List;

/**
 * @author xiamin
 * @date 8/24/17.
 */
public class HistoryHelper {

    private HistoryDataBaseHelper mHistoryDataBaseHelper;

    public HistoryHelper(Context context) {
        mHistoryDataBaseHelper = new HistoryDataBaseHelper(context);
    }

    public void insertHistory(HistoryBean historyBean) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(HistoryDataBaseHelper.SEARCH_HISTORY_CONTENT, historyBean.getContent());
        if (historyBean.getDrawableURL() != null) {
            contentValues.put(HistoryDataBaseHelper.SEARCH_HISTORY_URL, historyBean
                    .getDrawableURL());
        }
        SQLiteDatabase sqLiteDatabase = mHistoryDataBaseHelper.getWritableDatabase();
        sqLiteDatabase.insert(HistoryDataBaseHelper.HISTORY_TABLE, null, contentValues);
        mHistoryDataBaseHelper.close();
    }


    public List<HistoryBean> getHistoryList() {

    }

    public List<HistoryBean> searchHistoryList(String search) {
        String dbString = "SELECT * FROM " + HistoryDataBaseHelper.HISTORY_TABLE +
                " WHERE " + HistoryDataBaseHelper.SEARCH_HISTORY_CONTENT +
                " LIKE " + "'%" + search + "%'";
        SQLiteDatabase sqLiteDatabase = mHistoryDataBaseHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(dbString, null);
    }
}
