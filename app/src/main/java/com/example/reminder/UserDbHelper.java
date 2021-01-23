package com.example.reminder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class UserDbHelper {
    //these are the column names
    public static final String COL_ID = "_id";
    public static final String COL_NAME = "name";
    public static final String COL_EMAIL = "email";
    public static final String COL_PASSWORD = "password";

    //these are the corresponding indices
    public static final int INDEX_ID = 0;
    public static final int INDEX_NAME = INDEX_ID + 1;
    public static final int INDEX_EMAIL = INDEX_ID + 2;
    public static final int INDEX_PASSWORD = INDEX_ID + 3;

    //used for logging
    private static final String TAG = "UserDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_NAME = "UserManager";
    private static final String TABLE_NAME = "user";
    private static final int DATABASE_VERSION = 1;

    private final Context mCtx;

    //SQL statement used to create the database
    private static final String DATABASE_CREATE =
            "CREATE TABLE if not exists " + TABLE_NAME + " ( " +
                    COL_ID + " INTEGER PRIMARY KEY autoincrement, " +
                    COL_NAME + " TEXT, " +
                    COL_EMAIL + " TEXT, " +
                    COL_PASSWORD + " TEXT );";

    public UserDbHelper(Context ctx) {
        this.mCtx = ctx;
    }

    //open
    public void open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
    }

    //close
    public void close(){
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }
    /**
     * This method is to create user record
     *
     // @param user
     */
    //CRUD

    //CREATE
    public  void  addUser(String name, String email, String password){
        System.out.println("Buoc vao addUser");
        ContentValues values = new ContentValues();
        values.put(COL_NAME,name);
        values.put(COL_EMAIL,email);
        values.put(COL_PASSWORD,password);
        mDb.insert(TABLE_NAME, null, values);
        System.out.println("Ra khoi addUser");
    }
    public void addUser(User user){
        ContentValues values = new ContentValues();
        values.put(COL_NAME,user.getName());
        values.put(COL_EMAIL,user.getEmail());
        values.put(COL_PASSWORD,user.getPassword());
        mDb.insert(TABLE_NAME,null,values);
    }

    //READ
    public  User fetchUserById(int id){
        Cursor cursor = mDb.query(TABLE_NAME, new String[]{COL_ID,
                        COL_NAME,COL_EMAIL,COL_PASSWORD}, COL_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null
        );
        if (cursor != null)
            cursor.moveToFirst();
        return new User(cursor.getInt(INDEX_ID),
                cursor.getString(INDEX_NAME),
                cursor.getString(INDEX_EMAIL),
                cursor.getString(INDEX_PASSWORD));

    }

    public Cursor fetchAllUser(){
        Cursor cursor = mDb.query(TABLE_NAME, new String[]{COL_ID,
                        COL_NAME, COL_EMAIL, COL_PASSWORD},
                null, null, null, null, null, null);
        if (cursor != null){
            cursor.moveToFirst();
        }
        return  cursor;
    }

    /* Phương thưc nhận tất cả user và trả về list record
     * @return list
     */
    public ArrayList<User> getAllUser(){

        System.out.println("Buoc 3");
        // Mãng chứa các cột
        String[] columns = {
                COL_ID,
                COL_EMAIL,
                COL_NAME,
                COL_PASSWORD
        };

        // sorting orders
        String sortOrder =
                COL_NAME + " ASC";
        ArrayList<User>  userList = new ArrayList<>();

        // query the user table
        /**

         * SELECT user_id,user_name,user_email,user_password FROM user ORDER BY user_name;
         */
        Cursor cursor = mDb.query(TABLE_NAME, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order
        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COL_ID))));
                user.setName(cursor.getString(cursor.getColumnIndex(COL_NAME)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(COL_EMAIL)));
                user.setPassword(cursor.getString(cursor.getColumnIndex(COL_PASSWORD)));
                // Adding user record to list
                System.out.println("Buoc 4" + cursor.getString(cursor.getColumnIndex(COL_NAME)));
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        //db.close();
        System.out.println("Buoc 5");
        return userList;
    }

    //UPDATE
    public void updateUser(User user){
        ContentValues values = new ContentValues();
        values.put(COL_NAME,user.getName());
        values.put(COL_EMAIL,user.getEmail());
        values.put(COL_PASSWORD,user.getPassword());
        mDb.update(TABLE_NAME, values,
                COL_ID + "=?", new String[]{String.valueOf(user.getId())});
    }
    //DELETE
    public void deleteUserById(int nId) {
        mDb.delete(TABLE_NAME, COL_ID + "=?", new String[]{String.valueOf(nId)});
    }

    public void deleteAllUser() {
        mDb.delete(TABLE_NAME, null, null);
    }

    /**
     * This method to check user exist or not
     *
     // @param email
     * @return true/false
     */

    public boolean checkUser(String email){
        // array of columns to fetch
        String[] columns = {
                COL_ID
        };

        // selection criteria
        String selection = COL_EMAIL + " = ?";

        // selection argument
        String[] selectionArgs = {email};

        // query user table with condition
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com';
         */
        Cursor cursor = mDb.query(TABLE_NAME, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        mDb.close();
        if (cursorCount > 0) {
            return true;
        }
        return false;
    }

    /**
     * This method to check user exist or not
     *
     // @param email
     // @param password
     * @return true/false
     */
    public boolean checkUser(String email, String  password){
        // array of columns to fetch
        String[] columns = {
                COL_ID
        };

        // selection criteria
        String selection = COL_EMAIL + " = ?" + " AND " + COL_PASSWORD + " = ?";

        // selection arguments
        String[] selectionArgs = {email, password};

        // query user table with conditions
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com' AND user_password = 'qwerty';
         */
        Cursor cursor = mDb.query(TABLE_NAME, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        mDb.close();
        if (cursorCount > 0) {
            return true;
        }
        return false;
    }
    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.w(TAG, DATABASE_CREATE);
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }
}

