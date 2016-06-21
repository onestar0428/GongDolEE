package example.com.mptermui;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by cjf90 on 2016-06-10.
 */
public class resDB {
    static final String KEY_TIME = "time";
    static final String KEY_BUILDING = "building";
    static final String KEY_CLASS = "classroom";
    static final String KEY_DDAY="dday";
    static final String TAG = "DBAdapter";
    static final String DATABASE_NAME = "MyDB";
    static final String DATABASE_TABLE = "reser";
    static final int DATABASE_VERSION = 1;
    static final String DATABASE_CREATE =
            "create table " + DATABASE_TABLE + " (time text primary key autoincrement, "
                    + "buliding text not null unique , classroom text not null, dday text not null);";

    final Context context;
    DatabaseHelper DBHelper;
    SQLiteDatabase db;
    public resDB(Context ctx) {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);

    }
    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {

            String drop = "drop table if exists "+DATABASE_TABLE;
            try {

                db.execSQL(drop);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {

                db.execSQL(DATABASE_CREATE);
                //초기 인서트부분
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS Classroom");
            onCreate(db);
        }
    }
    public resDB open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }
    // ---closes the database---
    public void close() {
        DBHelper.close();
    }
    // --- find a day`s Reservation
    public Cursor getdday(String cl) {
        return db.query(DATABASE_TABLE, new String[] { KEY_TIME, KEY_BUILDING,
                KEY_CLASS }, KEY_DDAY+ "=" +cl, null, null, null, null);
    }
    public long insertContact(String da, String time, String bd, String cl) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TIME, time);
        initialValues.put(KEY_BUILDING, bd);
        initialValues.put(KEY_CLASS, cl);
        initialValues.put(KEY_DDAY, da);
        return db.insert(DATABASE_TABLE, null, initialValues);
    }
}
