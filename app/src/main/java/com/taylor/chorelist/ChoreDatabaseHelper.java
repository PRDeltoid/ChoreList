package com.taylor.chorelist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class ChoreDatabaseHelper {
    private static final String[] ENTRY_PROJECTION = {
            ChoreItemContract.ChoreItem._ID,
            ChoreItemContract.ChoreItem.COLUMN_NAME_CHORE_NAME,
            ChoreItemContract.ChoreItem.COLUMN_NAME_CHORE_INTERVAL};

    static public ArrayList<ChoreItem> pull_chore_items(Context context) {
        SQLiteDatabase db = ChoreItemDatabase.get_readable_db(context);
        Cursor cursor = create_cursor(db, null);
        ArrayList<ChoreItem> chore_list = new ArrayList<>();

        while (cursor.moveToNext()) {
            ChoreItem chore_item = create_item_from_cursor(cursor);
            chore_list.add(chore_item);
        }

        //Cleanup
        cursor.close();

        return chore_list;
    }

    static public ChoreItem pull_chore_item(Context context, long index) {
        //Get a database
        SQLiteDatabase db = ChoreItemDatabase.get_readable_db(context);
        //Create our SQL query (ID = index passed)
        String selection = ChoreItemContract.ChoreItem._ID + " = " + index;

        //Query the database.
        Cursor cursor = create_cursor(db, selection);
        cursor.moveToNext();

        //Create the item from the cursor
        ChoreItem chore_item = create_item_from_cursor(cursor);

        //Cleanup and return
        cursor.close();
        return chore_item;
    }

    static public void update_entry(Context context, long chore_id, ChoreItem chore_item) {
        SQLiteDatabase db = ChoreItemDatabase.get_writable_db(context);

        ContentValues values = create_entry_content_values(chore_item);

        db.update(ChoreItemContract.ChoreItem.TABLE_NAME, values, ChoreItemContract.ChoreItem._ID + "=" + chore_id, null);
    }

    //Returns the record ID if successful, -1 otherwise
    static public void new_entry(Context context, ChoreItem chore_item) {
        SQLiteDatabase db = ChoreItemDatabase.get_writable_db(context);
        ContentValues values = create_entry_content_values(chore_item);

        long record_id;
        //Insert information into the database
        try {
            record_id = db.insertOrThrow(ChoreItemContract.ChoreItem.TABLE_NAME, null, values);
        } catch (SQLException sql_exception) {
            //Failed to insert. Show error message and exit method
        }
    }

    static private ContentValues create_entry_content_values(ChoreItem chore_item) {
        ContentValues values = new ContentValues();

        //Store the database information!
        values.put(ChoreItemContract.ChoreItem.COLUMN_NAME_CHORE_NAME,  chore_item.get_name());
        values.put(ChoreItemContract.ChoreItem.COLUMN_NAME_CHORE_INTERVAL,  chore_item.get_interval());

        return values;
    }

    static public void delete_chore_item(Context context, long index) {
        SQLiteDatabase db = ChoreItemDatabase.get_writable_db(context);

        String[] index_string = {Long.toString(index)};


        db.delete(ChoreItemContract.ChoreItem.TABLE_NAME, ChoreItemContract.ChoreItem._ID + " = ?", index_string);
    }

    //Creates a database cursor from a query (null query pulls all)
    static private Cursor create_cursor(SQLiteDatabase db, String query) {
        return db.query(
                ChoreItemContract.ChoreItem.TABLE_NAME,
                ENTRY_PROJECTION,
                query,
                null,
                null,
                null,
                ChoreItemContract.ChoreItem.COLUMN_NAME_CHORE_NAME + " desc"
        );
    }

    //Helper method to create a ChoreItem object from a database cursor
    static private ChoreItem create_item_from_cursor(Cursor cursor) {
        ChoreItem chore_item;

        String chore_name;
        int chore_interval,
            chore_id;

        try {
            chore_id = cursor.getInt(
                    cursor.getColumnIndexOrThrow(ChoreItemContract.ChoreItem._ID));
            chore_name = cursor.getString(
                    cursor.getColumnIndexOrThrow(ChoreItemContract.ChoreItem.COLUMN_NAME_CHORE_NAME));
            chore_interval = cursor.getInt(
                    cursor.getColumnIndexOrThrow(ChoreItemContract.ChoreItem.COLUMN_NAME_CHORE_INTERVAL));

            chore_item = new ChoreItem(chore_name, chore_interval, chore_id);
        } catch (Exception e) {
            chore_item = new ChoreItem();
        }

        return chore_item;
    }
}
