/*******************************************************************************
 * Copyright (C) 2014 Artem Yankovskiy (artemyankovskiy@gmail.com).
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package ru.neverdark.shufflelist;

import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DbAdapter {
    private static final String KEY_ID = "_id";
    private static final String KEY_IS_CHECKED = "is_checked";
    private static final String KEY_LIST_ID = "list_id";

    private static final String KEY_POSITION = "position";
    private static final String KEY_TEXT = "item";
    private static final String TABLE_NAME = "list";
    private Context mContext;
    private SQLiteDatabase mDb;
    private DatabaseHelper mDbHelper;

    public DbAdapter(Context context) {
        mContext = context;
    }

    private void clearList() {
        mDb.delete(TABLE_NAME, null, null);
    }

    public void close() {
        mDbHelper.close();
    }

    private ContentValues createContentValues(String text, boolean isChecked,
            int position, int list_id) {
        ContentValues values = new ContentValues();
        values.put(KEY_TEXT, text);
        values.put(KEY_IS_CHECKED, isChecked);
        values.put(KEY_POSITION, position);
        values.put(KEY_LIST_ID, list_id);
        return values;
    }

    public boolean isOpen() {
        return mDb.isOpen();
    }

    public void loadItemFromDb(List<Item> list) {
        String order = KEY_POSITION.concat(" ASC");
        Cursor cursor = mDb.query(TABLE_NAME, null, null, null, null, null,
                order);

        int idColumn = cursor.getColumnIndex(KEY_ID);
        int textColumn = cursor.getColumnIndex(KEY_TEXT);
        int isCheckedColumn = cursor.getColumnIndex(KEY_IS_CHECKED);
        int positionColumn = cursor.getColumnIndex(KEY_POSITION);
        // reserved for future use
        // int listIdColumn = cursor.getColumnIndex(KEY_LIST_ID);

        while (cursor.moveToNext()) {
            Item item = new Item(cursor.getString(textColumn));
            item.setIsChecked(cursor.getInt(isCheckedColumn) > 0);
            item.setRecordId(cursor.getLong(idColumn));
            item.setPosition(cursor.getInt(positionColumn));
            list.add(item);
        }

        cursor.close();
    }

    public DbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mContext);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void saveItemsToDb(List<Item> list) {
        mDb.beginTransaction();
        try {
            clearList();

            for (Item item : list) {
                ContentValues values = createContentValues(item.getText(),
                        item.isChecked(), item.getPosition(), 0);
                mDb.insert(TABLE_NAME, null, values);
            }

            mDb.setTransactionSuccessful();
        } finally {
            mDb.endTransaction();
        }
    }
}
