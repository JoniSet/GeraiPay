package com.optima.gerai_pay.Helper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.optima.gerai_pay.Model.ListNotif;

import java.util.ArrayList;


public class SqliteHelper extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION       = 2;
    static final String DB_NAME                     = "notif.db";

    public static final String TABLE_NOTIF          = "notification";

    public static final String ID_NOTIF             = "id";
    public static final String STATUS               = "status";
    public static final String TITLE                = "title";
    public static final String BODY                 = "body";
    public static final String TANGGAL              = "tanggal";


    public SqliteHelper(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String CREATE_TABLE_NOTIF   = "CREATE TABLE " + TABLE_NOTIF + " (" +
                ID_NOTIF + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                STATUS + " TEXT NOT NULL, " +
                TITLE + " TEXT NOT NULL, " +
                BODY + " TEXT NOT NULL, " +
                TANGGAL  + " TEXT NOT NULL " +
                " )";

        db.execSQL(CREATE_TABLE_NOTIF);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIF);

        onCreate(db);
    }

    //================================Notification=================================================
    public void add_notif(String status, String title, String body, String tanggal) {
        SQLiteDatabase database = this.getWritableDatabase();
        String queryValues = "INSERT INTO " + TABLE_NOTIF + " (status, title, body, tanggal) " +
                "VALUES ('" + status + "', '" + title + "', '" + body + "', '" + tanggal + "')";

        Log.d("insert kategori ", "" + queryValues);
        database.execSQL(queryValues);
        database.close();
    }

    public void delete_notif(String id) {
        SQLiteDatabase database = this.getWritableDatabase();

        String updateQuery = "DELETE FROM " + TABLE_NOTIF + " WHERE id = '" + id + "'";
        Log.e("update sqlite ", updateQuery);
        database.execSQL(updateQuery);
        database.close();
    }

    public ArrayList<ListNotif> readKategoriSqlite(){
        SQLiteDatabase db       = this.getReadableDatabase();
        Cursor cursorCourses    = db.rawQuery("SELECT * FROM " + TABLE_NOTIF + " ORDER BY id DESC", null);

        ArrayList<ListNotif> courseModalArrayList = new ArrayList<>();

        if (cursorCourses.moveToFirst()) {
            do {
                courseModalArrayList.add(new ListNotif(
                        cursorCourses.getString(0),
                        cursorCourses.getString(1),
                        cursorCourses.getString(2),
                        cursorCourses.getString(3),
                        cursorCourses.getString(4)));
            } while (cursorCourses.moveToNext());
        }
        cursorCourses.close();
        return courseModalArrayList;
    }

    public int readJmlPesanMasuk(){
        SQLiteDatabase db       = this.getReadableDatabase();
        Cursor cursorCourses    = db.rawQuery("SELECT * FROM " + TABLE_NOTIF + " WHERE status = 'baru'", null);

        ArrayList<ListNotif> courseModalArrayList = new ArrayList<>();

        if (cursorCourses.moveToFirst()) {
            do {
                courseModalArrayList.add(new ListNotif(
                        cursorCourses.getString(0),
                        cursorCourses.getString(1),
                        cursorCourses.getString(2),
                        cursorCourses.getString(3),
                        cursorCourses.getString(4)));
            } while (cursorCourses.moveToNext());
        }

        int jumlah              = cursorCourses.getCount();

        cursorCourses.close();
        return jumlah;
    }

    public void updateStokBahan(String id) {
        SQLiteDatabase database = this.getWritableDatabase();

        String query = "UPDATE " + TABLE_NOTIF + " SET status = 'dibaca' WHERE id = '" + id + "'";
        database.execSQL(query);
        Log.d("Update", query);
        database.close();
    }

}
