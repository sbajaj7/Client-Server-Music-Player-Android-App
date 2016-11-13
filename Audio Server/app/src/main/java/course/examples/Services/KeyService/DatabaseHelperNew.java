package course.examples.Services.KeyService;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sahib on 4/6/2016.
 */
public class DatabaseHelperNew extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Track.db";
    public static final String TABLE_NAME = "tracks";
    public static final String COL_1 = "OPERATION";
    public static final String COL_2 = "TIME";

    public DatabaseHelperNew(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (OPERATION VARCHAR(50), TIME VARCHAR(50)) ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String operation,String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,operation);
        contentValues.put(COL_2, time);
        long result = db.insert(TABLE_NAME,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public String[] getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from" +" " + TABLE_NAME,null);

        String arr[] = new String[500];
        int i = 0;
        while (res.moveToNext()){
            arr[i] = res.getString(0) + " " + res.getString(1);
            i++;
        }
        return arr;
    }

    public String getLastEnteredRecord(){

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from" + " " + TABLE_NAME, null);

        String lstRecord;

        if(res!=null){

            res.moveToLast();

            lstRecord= res.getString(0);
        }
        else{
            lstRecord = "empty";
        }

        return  lstRecord;
    }

}
