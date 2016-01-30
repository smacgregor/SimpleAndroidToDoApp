package com.example.smacgregor.simpletodo.core;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by smacgregor on 1/28/16.
 */

public class ToDoItemDatabase extends SQLiteOpenHelper {
    // Database Info
    private static final String DATABASE_NAME = "toDoItemDatabase";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_TODO = "todos";

    // ToDo Table Columns
    private static final String KEY_TODO_ID = "id";
    private static final String KEY_TODO_NAME = "name";
    private static final String KEY_TODO_POSITION = "position";

    private static ToDoItemDatabase ourInstance;

    public static synchronized ToDoItemDatabase getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new ToDoItemDatabase(context);
        }
        return ourInstance;
    }

    private ToDoItemDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TODO_TABLE = "CREATE TABLE " + TABLE_TODO +
                "(" +
                    KEY_TODO_ID + " INTEGER PRIMARY KEY," + // Our primary key
                    KEY_TODO_NAME + " TEXT," +
                    KEY_TODO_POSITION + " INTEGER" +
                ")";
        db.execSQL(CREATE_TODO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO);
            onCreate(db);
        }
    }

    public long addToDoItem(ToDoItem toDoItem) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        long key = -1;
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_TODO_NAME, toDoItem.name);
            values.put(KEY_TODO_POSITION, toDoItem.position);

            key = db.insertOrThrow(TABLE_TODO, null, values);
            db.setTransactionSuccessful();
        } catch (Exception ex) {

        } finally {
            db.endTransaction();
        }

        return key;
    }

    public List<ToDoItem> getAllToDoItems() {
        List<ToDoItem> toDos = new ArrayList<>();

        String TODOS_SELECT_QUERY = String.format("SELECT * FROM %s", TABLE_TODO);
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(TODOS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    ToDoItem toDoItem = new ToDoItem();
                    toDoItem.key = cursor.getLong(cursor.getColumnIndex(KEY_TODO_ID));
                    toDoItem.name = cursor.getString(cursor.getColumnIndex(KEY_TODO_NAME));
                    toDoItem.position = cursor.getInt(cursor.getColumnIndex(KEY_TODO_POSITION));
                    toDos.add(toDoItem);
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return toDos;
    }

    public int updateToDoItem(ToDoItem toDoItem) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TODO_NAME, toDoItem.name);
        values.put(KEY_TODO_POSITION, toDoItem.position);

        // should update be wrapped in a transaction
        return db.update(TABLE_TODO, values, KEY_TODO_ID + " = " + Long.toString(toDoItem.key), null);
    }

    public void deleteToDo(ToDoItem toDoItem) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            int rowsEffected = db.delete(TABLE_TODO, KEY_TODO_ID + " = " + Long.toString(toDoItem.key), null);
            db.setTransactionSuccessful();
        } catch (Exception ex) {

        } finally {
            db.endTransaction();
        }
    }
}
