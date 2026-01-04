package com.example.musicplayerr;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor; // Added for reading data
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList; // Added for the list
import java.util.List;      // Added for the list

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "MusicApp.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Creates the table to store API data
        db.execSQL("CREATE TABLE posts (id INTEGER PRIMARY KEY, title TEXT, body TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS posts");
        onCreate(db);
    }

    // Section 4: Insert data from API into Local SQLite
    public void insertData(int id, String title, String body) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("title", title);
        values.put("body", body);

        // CONFLICT_REPLACE ensures we don't get "Duplicate ID" errors
        db.insertWithOnConflict("posts", null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    // NEW METHOD: Fetches all stored data for the RecyclerView
    public List<post> getAllPosts() {
        List<post> postList = new ArrayList<>();
        String selectQuery = "SELECT * FROM posts";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                // Creates a new 'post' object (small 'p') from database columns
                post p = new post(
                        cursor.getInt(0),    // Column 0 is ID
                        cursor.getString(1), // Column 1 is Title
                        cursor.getString(2)  // Column 2 is Body
                );
                postList.add(p);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return postList;
    }
}