package com.jerey.searchview.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * @author xiamin
 * @date 8/24/17.
 */
public class HistoryDataBaseHelper extends SQLiteOpenHelper {
    private static final String TAG = HistoryDataBaseHelper.class.getSimpleName();

    private static final String HISTORY_TABLE = "search_history";
    static final String SEARCH_HISTORY_ID = "id";
    static final String SEARCH_TYPE = "type";
    static final String SEARCH_HISTORY_TIME = "time";
    static final String SEARCH_HISTORY_CONTENT = "content";
    static final String SEARCH_HISTORY_URL = "url";

    private static final String DATABASE_NAME = "search_history_info.db";
    private static final int DATABASE_VERSION = 3;

    private static String CREATE_TABLE_SEARCH_HISTORY = "CREATE TABLE IF NOT EXISTS "
            + HISTORY_TABLE + " ( "
            + SEARCH_HISTORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + SEARCH_TYPE + " TEXT,"
            + SEARCH_HISTORY_TIME + " DATETIME,"
            + SEARCH_HISTORY_CONTENT + " TEXT UNIQUE, "
            + SEARCH_HISTORY_URL + " TEXT " + ");";

    public String getHistoryTableName() {
        return HISTORY_TABLE;
    }

    public HistoryDataBaseHelper(Context context) {
        this(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public HistoryDataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory
            factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.i(TAG, "onCreate SQL : " + CREATE_TABLE_SEARCH_HISTORY);
        sqLiteDatabase.execSQL(CREATE_TABLE_SEARCH_HISTORY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + HISTORY_TABLE);
        Log.i(TAG, "onUpgrade SQL : " + "DROP TABLE IF EXISTS " + HISTORY_TABLE);
        onCreate(sqLiteDatabase);
    }
}
