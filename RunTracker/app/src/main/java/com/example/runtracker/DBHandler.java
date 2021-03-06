package com.example.runtracker;

import com.example.runtracker.ContentProvider.MyContentProvider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentResolver;

public class DBHandler extends SQLiteOpenHelper {

    //Initialize DBHandler Variables

    private ContentResolver myCR;

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "runtrackerDB.db";
    public static final String TABLE_RUNNER = "Runner";


    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_RUNDISTANCE = "distance";
    public static final String COLUMN_RUNDURATION = "duration";
    public static final String COLUMN_RUNSPEED = "speed";
    public static final String COLUMN_WORKOUTTYPE = "workoutType";
    public static final String COLUMN_INITIALLOCATIONLAT = "initialLocationLAT";
    public static final String COLUMN_INITIALLOCATIONLONG = "initialLocationLONG";
    public static final String COLUMN_FINALLOCATIONLAT = "finalLocationLAT";
    public static final String COLUMN_FINALLOCATIONLONG = "finalLocationLONG";
    private static final String COLUMN_DATE = "date";


    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        myCR = context.getContentResolver();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {                                         //Create SQL Database onCreate

        String CREATE_RUN_TABLE = "CREATE TABLE " +
                TABLE_RUNNER + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY," +
                COLUMN_RUNDISTANCE + " DOUBLE," +
                COLUMN_RUNDURATION + " INTEGER," +
                COLUMN_RUNSPEED + " DOUBLE," +
                COLUMN_WORKOUTTYPE + " TEXT," +
                COLUMN_INITIALLOCATIONLAT + " DOUBLE," +
                COLUMN_INITIALLOCATIONLONG + " DOUBLE," +
                COLUMN_FINALLOCATIONLAT + " DOUBLE," +
                COLUMN_FINALLOCATIONLONG + " DOUBLE," +
                COLUMN_DATE + " INTEGER" +")";

        db.execSQL(CREATE_RUN_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {        //Deleting and Remaking Database on Version Change
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RUNNER);
        onCreate(db);
    }

    public void addRun(Run run) {                                                     //Function to Add a Run to the table
        ContentValues values = new ContentValues();
        values.put(COLUMN_RUNDISTANCE, run.getDistance());
        values.put(COLUMN_RUNDURATION, run.getDuration());
        values.put(COLUMN_RUNSPEED, run.getSpeed());
        values.put(COLUMN_WORKOUTTYPE, run.getType());
        values.put(COLUMN_INITIALLOCATIONLAT, run.getInitialLocationLAT());
        values.put(COLUMN_INITIALLOCATIONLONG, run.getInitialLocationLONG());
        values.put(COLUMN_FINALLOCATIONLAT, run.getFinalLocationLAT());
        values.put(COLUMN_FINALLOCATIONLONG, run.getFinalLocationLONG());
        values.put(COLUMN_DATE, run.getLongDate());
        myCR.insert(MyContentProvider.CONTENT_URI, values);
    }

    public boolean deleteRun(int id) {                                                 //Function to delete run from table
        boolean result = false;
        String selection = "_id = \"" + id + "\"";
        int rowsDeleted = myCR.delete(MyContentProvider.CONTENT_URI,
                selection, null);
        if (rowsDeleted > 0)
            result = true;
        return result;
    }

    public Run findRun(int id){                                                        //Function to find run in table
        String [] projection = {COLUMN_ID, COLUMN_RUNDISTANCE,
                COLUMN_RUNDURATION, COLUMN_RUNSPEED, COLUMN_WORKOUTTYPE, COLUMN_INITIALLOCATIONLAT,COLUMN_INITIALLOCATIONLONG, COLUMN_FINALLOCATIONLAT,COLUMN_FINALLOCATIONLONG,COLUMN_DATE };
        String selection = "_id = \"" + id + "\"";
        Cursor cursor = myCR.query(MyContentProvider.CONTENT_URI,
                projection, selection, null,
                null);
        Run workout = new Run();
        if (cursor.moveToFirst()) {
            workout.setID((cursor.getInt(0)));
            workout.setDistance(cursor.getFloat(1));
            workout.setDuration(cursor.getLong(2));
            workout.setSpeed(cursor.getFloat(3));
            workout.setType(cursor.getString(4));
            workout.setInitialLocationLAT(cursor.getDouble(5));
            workout.setInitialLocationLONG(cursor.getDouble(6));
            workout.setFinalLocationLAT(cursor.getDouble(7));
            workout.setFinalLocationLONG(cursor.getDouble(8));
            workout.setDate(cursor.getLong(9));
            cursor.close();
        } else {
            workout = null;
        }
        return workout;
    }

    public Cursor showList(){                                                           //Cursor to run SQL queries
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * from " + TABLE_RUNNER;
        Cursor cursor = db.rawQuery(query, null);

        return cursor;
    }
}
