package com.example.todolist6;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper  extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "TASK.DB";

    private static final String TABLE_TASK = "task_Infor";

    private static final String COLUMN_TASK_ID = "task_id";
    private static final String COLUMN_TASK_NAME = "task_name";
    private static final String COLUMN_TASK_STATUS = "task_status";


    private String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_TASK + "("
            + COLUMN_TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_TASK_NAME + " TEXT," + COLUMN_TASK_STATUS + " TEXT" + ")";



    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_TASK;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_USER_TABLE);
        onCreate(db);
    }


    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK_NAME, user.getName());
        values.put(COLUMN_TASK_STATUS, user.getStatus());

        db.insert(TABLE_TASK, null, values);
        db.close();
    }





    public ArrayList getTask(String status) {
        ArrayList result =  new ArrayList();
        String[] columns = {
                COLUMN_TASK_NAME
        };
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_TASK_STATUS + " = ?";
        String[] selectionArgs = {status};

        Cursor cursor = db.query(TABLE_TASK,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);

        while (cursor.moveToNext()) {
            String r = cursor.getString(cursor.getColumnIndex(COLUMN_TASK_NAME));
            result.add(r);
        }
        cursor.close();
        db.close();
        return result;
    }



    public boolean updatePendingToComplete(String sta) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TASK_STATUS,"Completed");

        db.update(TABLE_TASK, contentValues, "task_name = ?",new String[] { sta });
        return true;
    }


    public boolean updateCompleteToPending(String sta) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TASK_STATUS,"Pending");

        db.update(TABLE_TASK, contentValues, "task_name = ?",new String[] { sta });
        return true;
    }



    public void deleteTask(String tsk) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TASK,
                COLUMN_TASK_NAME + " = ?",
                new String[]{tsk});
        db.close();
    }



}



