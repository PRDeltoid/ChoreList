package com.taylor.chorelist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class ChoreItemDatabase extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "chorelist.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ChoreItemContract.ChoreItem.TABLE_NAME + " (" +
                    ChoreItemContract.ChoreItem._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    ChoreItemContract.ChoreItem.COLUMN_NAME_CHORE_INTERVAL + " INTEGER NOT NULL," +
                    ChoreItemContract.ChoreItem.COLUMN_NAME_CHORE_NAME + " TEXT NOT NULL)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ChoreItemContract.ChoreItem.TABLE_NAME;

    private static ChoreItemDatabase mDbHelper = null;

    private ChoreItemDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    static public SQLiteDatabase get_readable_db(Context context) {
        acquire_instance(context);
        return mDbHelper.getReadableDatabase();
    }

    static public SQLiteDatabase get_writable_db(Context context) {
        acquire_instance(context);
        return mDbHelper.getWritableDatabase();
    }

    static private void acquire_instance(Context context) {
        //Simple method to create our Singleton if it is un-initialized
        if(mDbHelper == null) {
            mDbHelper = new ChoreItemDatabase(context);
        }
    }
}
