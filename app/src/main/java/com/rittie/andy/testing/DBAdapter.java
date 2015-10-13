package com.rittie.andy.testing;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by James on 6/09/2015.
 * Lasted Edited on 4/10/2015.
 */
public class DBAdapter {

    public static final String USER_ID = "userID";
    public static final String USER_NAME = "userName";
    public static final String USER_EMAIL = "userEmail";
    public static final String USER_PASSWORD = "userPassword";

    public static final String BASELINE_ID = "baselineID";
    public static final String USER_ID_FK = "userIDFK";
    public static final String BASELINE_NAME = "baselineName";
    public static final String AVERAGE_HEART_RATE = "averageHeartRate";

    public static final String HEART_RATE_ID = "heartRateID";
    public static final String BASELINE_ID_FK = "baselineIDFK";
    public static final String HEART_RATE = "heartRate";
    public static final String START_TIME = "startTime";

    private static final String TAG = "DBAdapter";

    private static final String DATABASE_NAME = "ArousalDB";
    private static final String DATABASE_USERS_TABLE = "users";
    private static final String DATABASE_BASELINES_TABLE = "baselines";
    private static final String DATABASE_HEART_RATE_TABLE = "heartRateValues";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE_T1 =
            "CREATE TABLE IF NOT EXISTS users (userID INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "userName TEXT UNIQUE NOT NULL, userEmail TEXT, userPassword TEXT);";

    private static final String DATABASE_CREATE_T2 =
            "CREATE TABLE IF NOT EXISTS baselines (baselineID INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "userIDFK INTEGER NOT NULL, baselineName TEXT NOT NULL, averageHeartRate REAL, "
                    + "FOREIGN KEY(userIDFK) REFERENCES users(userID) ON DELETE CASCADE);";

    private static final String DATABASE_CREATE_T3 =
            "CREATE TABLE IF NOT EXISTS heartRateValues ("
                    + "heartRateID INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "baselineIDFK INTEGER NOT NULL, heartRate REAL NOT NULL, "
                    + "startTime DATETIME NOT NULL, "
                    + "FOREIGN KEY(baselineIDFK) REFERENCES baselines(baselineID) ON DELETE CASCADE)";

    private final Context context;

    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBAdapter(Context ctx) {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(DATABASE_CREATE_T1);
                db.execSQL(DATABASE_CREATE_T2);
                db.execSQL(DATABASE_CREATE_T3);
                db.execSQL("PRAGMA foreign_keys=ON;");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS heartRateValues");
            db.execSQL("DROP TABLE IF EXISTS baselines");
            db.execSQL("DROP TABLE IF EXISTS users");
            onCreate(db);
        }
    }

    //---opens the database---
    public DBAdapter open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    //---closes the database---
    public void close() {
        DBHelper.close();
    }

    //---insert a record into the database---
    public long insertUser(String userName, String userEmail, String userPassword) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(USER_NAME, userName);
        initialValues.put(USER_EMAIL, userEmail);
        initialValues.put(USER_PASSWORD, userPassword);
        return db.insert(DATABASE_USERS_TABLE, null, initialValues);
    }

    public long insertBaseline(long userIDFK, String baselineName, double averageHeartRate) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(USER_ID_FK, userIDFK);
        initialValues.put(BASELINE_NAME, baselineName);
        initialValues.put(AVERAGE_HEART_RATE, averageHeartRate);
        return db.insert(DATABASE_BASELINES_TABLE, null, initialValues);
    }

    public long insertHeartRate(long baselineIDFK, double heartRate, String startTime) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(BASELINE_ID_FK, baselineIDFK);
        initialValues.put(HEART_RATE, heartRate);
        initialValues.put(START_TIME, startTime);
        return db.insert(DATABASE_HEART_RATE_TABLE, null, initialValues);
    }

    //---deletes a particular record---
    public boolean deleteUser(long userID) {
        return db.delete(DATABASE_USERS_TABLE, USER_ID + "=" + userID, null) > 0;
    }

    public boolean deleteBaseline(long baselineID) {
        return db.delete(DATABASE_BASELINES_TABLE, BASELINE_ID + "=" + baselineID, null) > 0;
    }

    public boolean deleteHeartRate(long heartRateID) {
        return db.delete(DATABASE_HEART_RATE_TABLE, HEART_RATE_ID + "=" + heartRateID, null) > 0;
    }

    //---retrieves all the records---
    public Cursor getAllUserRecords() {
        return db.query(DATABASE_USERS_TABLE, new String[] {USER_ID, USER_NAME,
                USER_EMAIL, USER_PASSWORD}, null, null, null, null, null);
    }

    public Cursor getAllBaselineRecords() {
        return db.query(DATABASE_BASELINES_TABLE, new String[] {BASELINE_ID, USER_ID_FK,
                BASELINE_NAME, AVERAGE_HEART_RATE}, null, null, null, null, null);
    }

    public Cursor getAllHeartRateRecords() {
        return db.query(DATABASE_HEART_RATE_TABLE, new String[] {HEART_RATE_ID, BASELINE_ID_FK,
                HEART_RATE, START_TIME}, null, null, null, null, null);
    }

    //---retrieves all baseline records with a specific userIDFK---
    public Cursor getUsersBaselineRecords(long userIDFK) {
        return db.query(DATABASE_BASELINES_TABLE, new String[] {BASELINE_ID, USER_ID_FK,
                BASELINE_NAME, AVERAGE_HEART_RATE}, USER_ID_FK + "=" + userIDFK, null, null, null, null, null);
    }

    //---retrieves all heart rate records with a specific baselineIDFK---
    public Cursor getUsersHeartRateRecords(long baselineIDFK) {
        return db.query(DATABASE_HEART_RATE_TABLE, new String[] {HEART_RATE_ID, BASELINE_ID_FK,
                HEART_RATE, START_TIME}, BASELINE_ID_FK + "=" + baselineIDFK, null, null, null, null, null);
    }

    //---retrieves a particular record---
    public Cursor getUserRecord(long userID) throws SQLException {
        Cursor mCursor =
                db.query(true, DATABASE_USERS_TABLE, new String[] {USER_ID,
                                USER_NAME, USER_EMAIL, USER_PASSWORD},
                        USER_ID + "=" + userID, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor getBaselineRecord(long baselineID) throws SQLException {
        Cursor mCursor =
                db.query(true, DATABASE_BASELINES_TABLE, new String[] {BASELINE_ID,
                                USER_ID_FK, BASELINE_NAME, AVERAGE_HEART_RATE},
                        BASELINE_ID + "=" + baselineID, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor getHeartRateRecord(long heartRateID) throws SQLException {
        Cursor mCursor =
                db.query(true, DATABASE_HEART_RATE_TABLE, new String[] {HEART_RATE_ID,
                                BASELINE_ID_FK, HEART_RATE, START_TIME},
                        HEART_RATE_ID + "=" + heartRateID, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor getBaselineWithUserIDRecord(long userIDFK) throws SQLException {
        Cursor mCursor =
                db.query(true, DATABASE_BASELINES_TABLE, new String[] {BASELINE_ID,
                                USER_ID_FK, BASELINE_NAME, AVERAGE_HEART_RATE},
                        USER_ID_FK + "=" + userIDFK, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor getHeartRateWithBaselineIDRecord(long baselineIDFK) throws SQLException {
        Cursor mCursor =
                db.query(true, DATABASE_HEART_RATE_TABLE, new String[] {HEART_RATE_ID,
                                BASELINE_ID_FK, HEART_RATE, START_TIME},
                        BASELINE_ID_FK + "=" + baselineIDFK, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    //---updates a record---
    public boolean updateUserRecord(long userID, String userName, String userEmail, String userPassword) {
        ContentValues args = new ContentValues();
        args.put(USER_NAME, userName);
        args.put(USER_EMAIL, userEmail);
        args.put(USER_PASSWORD, userPassword);
        return db.update(DATABASE_USERS_TABLE, args, USER_ID + "=" + userID, null) > 0;
    }

    public boolean updateBaselineRecord(long baselineID, long userIDFK, String baselineName, double averageHeartRate) {
        ContentValues args = new ContentValues();
        args.put(USER_ID_FK, userIDFK);
        args.put(BASELINE_NAME, baselineName);
        args.put(AVERAGE_HEART_RATE, averageHeartRate);
        return db.update(DATABASE_BASELINES_TABLE, args, BASELINE_ID + "=" + baselineID, null) > 0;
    }

    public boolean updateHeartRateRecord(long heartRateID, long baselineIDFK, double heartRate, String startTime) {
        ContentValues args = new ContentValues();
        args.put(USER_ID_FK, baselineIDFK);
        args.put(HEART_RATE, heartRate);
        args.put(START_TIME, startTime);
        return db.update(DATABASE_HEART_RATE_TABLE, args, HEART_RATE_ID + "=" + heartRateID, null) > 0;
    }
}
