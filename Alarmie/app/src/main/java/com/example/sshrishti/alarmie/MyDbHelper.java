package com.example.sshrishti.alarmie;

/**
 * Created by sshrishti on 11/30/2016.
 */
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.provider.Settings;

/**
 * Created by sshrishti on 11/28/2016.
 */
class  MyDataEntry implements BaseColumns {
    public static final String TABLE_NAME="alarms";
   // public static final String ROW_ID="_ID";
    public static final String HOUR_COLUMN="hour";
    public static final String MIN_COLUMN="minute";
}
public class MyDbHelper extends SQLiteOpenHelper {
    public static final String DB_NAME="CoolDatabse.db";

    public static final int DB_VERSION=1;
    private static final String SQL_CREATE_TABLE="CREATE TABLE "+ MyDataEntry.TABLE_NAME+ " (" + MyDataEntry._ID + " INTEGER PRIMARY KEY, " + MyDataEntry.HOUR_COLUMN+ " INTEGER," + MyDataEntry.MIN_COLUMN + " INTEGER )";
    private static final String SQL_DELETE_QUERY= "DROP TABLE IF EXISTS" ;

    static SQLiteDatabase database = null;
    static MyDbHelper instance = null;


    public MyDbHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println("Executing query: SQL_CREATE_TABLE " + SQL_CREATE_TABLE);
        db.execSQL(SQL_CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //shortcut: lets discard the table and start over
        db.execSQL(SQL_DELETE_QUERY);
        onCreate(db);

    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
        onUpgrade(db, oldVersion, newVersion);
    }
    public Cursor getListContents() {
        //   SQLiteDatabase db = this.getWritableDatabase();
        //  Cursor data = db.rawQuery("SELECT * FROM " + MyDataEntry.TABLE_NAME, null);
        //  return data;

        SQLiteDatabase db = this.getReadableDatabase();
        String[] query_columns = {MyDataEntry._ID, MyDataEntry.HOUR_COLUMN, MyDataEntry.MIN_COLUMN};
        String sortOrder = MyDataEntry.HOUR_COLUMN + " DESC";
        Cursor cursor = db.query(
                MyDataEntry.TABLE_NAME,
                query_columns,
                null,//provide a where clause
                null,//arguments for where clause
                null, // griuping
                null, //filter
                sortOrder //sorting order
        );
        if (cursor != null) {
            cursor.moveToNext();
        }
        return cursor;
    }


    public static SQLiteDatabase getDatabase(Context context) {
        if (null == database) {
            instance = new MyDbHelper(context);
            database = instance.getWritableDatabase();
        }
        return database;
    }

    public static int deleteEntry(int id , Context context){
        return getDatabase(context).delete(MyDataEntry.TABLE_NAME, MyDataEntry._ID + "=" + id, null) ;
    }

    public void deleteRow(int hour, int min , Context context) {
        getDatabase(context).delete(MyDataEntry.TABLE_NAME, MyDataEntry.HOUR_COLUMN + "=" + hour + " AND " + MyDataEntry.MIN_COLUMN + "=" + min, null) ;
    }
}