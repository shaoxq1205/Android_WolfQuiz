package helper;

/**
 * Created by shaox on 2016/3/28.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandler.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "android_api";

    // Login table name
    private static final String TABLE_USER = "user";
    private static final String TABLE_QUESTION = "question";

    // Login Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_IDENTITY = "identity";
    private static final String KEY_UID = "uid";
    private static final String KEY_CREATED_AT = "created_at";


    private static final String KEY_Q_ID = "id";
    private static final String KEY_QUESTION = "question";
    private static final String KEY_OPTIONA = "optiona";
    private static final String KEY_OPTIONB = "optionb";
    private static final String KEY_OPTIONC = "optionc";
    private static final String KEY_OPTIOND = "optiond";
    private static final String KEY_Q_UID = "uid";
    private static final String KEY_Q_CREATED_AT = "created_at";

    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT," + KEY_IDENTITY + " TEXT,"
                + KEY_EMAIL + " TEXT UNIQUE," + KEY_UID + " TEXT,"
                + KEY_CREATED_AT + " TEXT" + ")";
        db.execSQL(CREATE_LOGIN_TABLE);
        String CREATE_QUESTION_TABLE = "CREATE TABLE " + TABLE_QUESTION + "("
                + KEY_Q_ID + " INTEGER PRIMARY KEY," + KEY_QUESTION + " TEXT," + KEY_OPTIONA
                + " TEXT," + KEY_OPTIONB + " TEXT," + KEY_OPTIONC + " TEXT,"
                + KEY_OPTIOND + " TEXT UNIQUE," + KEY_Q_UID + " TEXT,"
                + KEY_Q_CREATED_AT + " TEXT" + ")";
        db.execSQL(CREATE_QUESTION_TABLE);

        Log.d(TAG, "Database tables created");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTION);


        // Create tables again
        onCreate(db);
    }

    /**
     * Storing user details in database
     * */
    public void addUser(String name, String email, String uid, String identity, String created_at) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name); // Name
        values.put(KEY_EMAIL, email); // Email
        values.put(KEY_IDENTITY, identity); // Identity
        values.put(KEY_UID, uid);
        values.put(KEY_CREATED_AT, created_at); // Created At

        // Inserting Row
        long id = db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }
    /**
     * Storing user details in database
     * */
    public void addQuestion(String question, String optiona, String optionb, String optionc, String optiond, String uid, String created_at) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_QUESTION, question);
        values.put(KEY_OPTIONA, optiona);
        values.put(KEY_OPTIONB, optionb);
        values.put(KEY_OPTIONC, optionc);
        values.put(KEY_OPTIOND, optiond);
        values.put(KEY_Q_UID, uid);
        values.put(KEY_Q_CREATED_AT, created_at); // Created At

        // Inserting Row
        long id = db.insert(TABLE_QUESTION, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New question inserted into sqlite: " + id);
        //Potential problem: Long long enough?
    }
    /**
     * Getting user data from database
     * */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("name", cursor.getString(1));
            user.put("uid", cursor.getString(2));
            user.put("email", cursor.getString(3));
            user.put("identity", cursor.getString(4));
            user.put("created_at", cursor.getString(5));
            //           user.put("created_at", null);
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        return user;
    }
    /**
     * Getting question context
     */
    public HashMap<String, String> getQuestions() {
        HashMap<String, String> question = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_QUESTION;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            question.put("question", cursor.getString(1));
            question.put("optiona", cursor.getString(2));
            question.put("optionb", cursor.getString(3));
            question.put("optionc", cursor.getString(4));
            question.put("optiond", cursor.getString(5));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching question from Sqlite: " + question.toString());

        return question;
    }

    /**
     * Re crate database Delete all tables and create them again
     * */
    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }

}