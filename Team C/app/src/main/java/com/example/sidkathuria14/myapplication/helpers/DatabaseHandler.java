package com.example.sidkathuria14.myapplication.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.sidkathuria14.myapplication.models.Friend;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sidkathuria14 on 4/2/18.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "FriendsDB";
    public static final String DB_TABLE = "friends";

    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_UID = "uid";

    public DatabaseHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + DB_TABLE + "( " + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT," + KEY_UID + " TEXT"
                + " ) ";
        sqLiteDatabase.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
        onCreate(sqLiteDatabase);
    }

    public void addFriend(Friend friend) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, friend.getName());
        values.put(KEY_UID, friend.getUid());
        db.insert(DB_TABLE, null, values);
        db.close();

    }

    public Friend getFriend(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(DB_TABLE, new String[]{
                KEY_ID, KEY_NAME, KEY_UID
        }, KEY_ID + " =? ", new String[]{
                String.valueOf(id)}, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        Friend friend = new Friend(cursor.getString(0), cursor.getString(1),
                Integer.parseInt(String.valueOf(cursor.getString(2))));
        return friend;
    }

    public void deleteFriend(Friend friend) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DB_TABLE, KEY_ID + " =? ", new String[]{
                String.valueOf(friend.getId())
        });

        db.close();
    }
    public List<Friend> getAllFriends() {
        List<Friend> contactList = new ArrayList<Friend>();
        String selectQuery = "SELECT * FROM " + DB_TABLE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Friend friend = new Friend();
                friend.setId(Integer.parseInt(cursor.getString(0)));
                friend.setName(cursor.getString(1));
                friend.setUid(cursor.getString(2));

            } while (cursor.moveToNext());
        }
        return contactList;

    }

    public int getFriendsCount() {

        String countQuery = "SELECT  * FROM " + DB_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;

    }
}
