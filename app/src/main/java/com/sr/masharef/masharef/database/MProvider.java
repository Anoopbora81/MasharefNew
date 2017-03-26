package com.sr.masharef.masharef.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.net.Uri;
import android.util.Log;

import com.sr.masharef.masharef.constants.Constants;

import net.sqlcipher.Cursor;
import net.sqlcipher.MatrixCursor;
import net.sqlcipher.SQLException;
import net.sqlcipher.database.SQLiteConstraintException;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

//import com.sr.masharef.masharef.masharef.utility.LogUtil;

public class MProvider extends ContentProvider {

    public final static String AUTHORITY = "com.sr.masharef.masharef";
    private static final String DATABASE_NAME = "masharef.db";
    private static final int DATABASE_VERSION = 1;

    private static DataBaseHelper databaseHelper;

    // To-Do make all to public, so that can be used other places also
    public static final String TABLE_NAME_EVENTS = "EventsTable";
    public static final String TABLE_NAME_PHONE_CATEGEORY = "PhoneCategeoryTable";
    public static final String TABLE_NAME_GALLERY = "GalleryTable";
    public static final String TABLE_NAME_CONTACT_LIST = "ContactListTable";


    private static final String CREATE_TABLE_EVENTS = "create table " + TABLE_NAME_EVENTS +
            "(event_id INTEGER primary key, event_name text, description text, venue text, date text, time text, gender text, cost INTEGER, dead_line text, status text, not_going_to_event INTEGER, may_be_gooing_to_event INTEGER, going_to_event INTEGER, currency text)"; // primary key
    private static final String CREATE_TABLE_PHONE_CATEGEORY = "create table " + TABLE_NAME_PHONE_CATEGEORY +
            "(category_id INTEGER primary key, categeory_name text)";
    private static final String CREATE_GALLERY = "create table " + TABLE_NAME_GALLERY +
            "(is_private INTEGER,  media_type text, media_image text, media_url text)";
    private static final String CREATE_TABLE_CONTACT_LIST= "create table " + TABLE_NAME_CONTACT_LIST +
            "(contact_id INTEGER primary key, categeory_id INTEGER, categeory_name text, first_name text, last_name text, occupation text, work_place text, rating text, country_name text, country_code text, country_ISN text, national_number text, interNational_number text)";



    /**
         * Table Name:- 1. Acc_guid (primary key) 2. Email/username 3. Password 4.
         * Requ_auth_token
         */
//    private static final String TABLE_LOGIN = "create table login(acc_guid text primary key, req_auth_token text, "
//            + "user_name text , password text)";





    private static final int EVENT = 1;
    private static final int PHONE_CATEGEORY = 2;
    private static final int CONTACT_LIST = 3;
    private static final int GALLERY_LIST = 4;



    private static final UriMatcher matcher = new UriMatcher(
            UriMatcher.NO_MATCH);

    static {
        matcher.addURI(AUTHORITY, TABLE_NAME_EVENTS, EVENT);
        matcher.addURI(AUTHORITY, TABLE_NAME_PHONE_CATEGEORY, PHONE_CATEGEORY);
        matcher.addURI(AUTHORITY, TABLE_NAME_CONTACT_LIST, CONTACT_LIST);
        matcher.addURI(AUTHORITY, TABLE_NAME_GALLERY, GALLERY_LIST);

    }

    public static SQLiteDatabase getDataBase() {
        return databaseHelper.getWritableDatabase(Constants.CIPHER_KEY);
    }

    public static SQLiteDatabase getReadableDB() {
        return databaseHelper.getReadableDatabase(Constants.CIPHER_KEY);
    }

    public static class DataBaseHelper extends SQLiteOpenHelper {

        private Context mContext = null;

        public DataBaseHelper(Context context) {
            super(context, getDbPath(context) + "/" + DATABASE_NAME, null,
                    DATABASE_VERSION);
            mContext = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.beginTransaction();
              //  db.execSQL(TABLE_LOGIN);
                db.execSQL(CREATE_TABLE_EVENTS);
                db.execSQL(CREATE_TABLE_PHONE_CATEGEORY);
                db.execSQL(CREATE_TABLE_CONTACT_LIST);
                db.execSQL(CREATE_GALLERY);
                db.setTransactionSuccessful();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                db.endTransaction();
            }

        }

        private static String getDbPath(Context context) {
            File dbPath = new File(context.getExternalFilesDir(null),
                    "databases");
            if (!dbPath.exists()) {
                dbPath.mkdir();
            }
            return dbPath.getPath();
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            if (oldVersion > newVersion) {
                throw new UnsupportedOperationException(
                        "DB upgrade failed, due to lower new verion : "
                                + newVersion);
            }

          /*  boolean val = isTableExists(db, TABLE_EVENTS);
            if (!val) {
                db.execSQL(TABLE_EVENTS);
            }

            List<String> columns = getColumnNames(db, INSTANCE_TABLE_NAME);
            addColumnIfNotExists(db, INSTANCE_TABLE_NAME, columns,
                    InstanceCount.MAP_COORDINATES, "STRING");*/
        }

        public ArrayList<Cursor> getData(String Query){
            //get writable database
            SQLiteDatabase sqlDB = getDataBase();
            String[] columns = new String[] { "mesage" };
            //an array list of cursor to save two cursors one has results from the query
            //other cursor stores error message if any errors are triggered
            ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
            MatrixCursor Cursor2= new MatrixCursor(columns);
            alc.add(null);
            alc.add(null);

            try{
                String maxQuery = Query ;
                //execute the query results will be save in Cursor c
                Cursor c = sqlDB.rawQuery(maxQuery, null);

                //add value to cursor2
                Cursor2.addRow(new Object[] { "Success" });

                alc.set(1,Cursor2);
                if (null != c && c.getCount() > 0) {
                    alc.set(0,c);
                    c.moveToFirst();
                    return alc ;
                }
                return alc;
            } catch(SQLException sqlEx){
                Log.d("printing exception", sqlEx.getMessage());
                //if any exceptions are triggered save the error message to cursor an return the arraylist
                Cursor2.addRow(new Object[] { ""+sqlEx.getMessage() });
                alc.set(1,Cursor2);
                return alc;
            } catch(Exception ex){
                Log.d("printing exception", ex.getMessage());
                //if any exceptions are triggered save the error message to cursor an return the arraylist
                Cursor2.addRow(new Object[] { ""+ex.getMessage() });
                alc.set(1,Cursor2);
                return alc;
            }
            finally {
                Cursor2.close();
            }
        }
    }


    @Override
    public String getType(Uri uri) {
        return null;
    }


    @Override
    public boolean onCreate() {
        SQLiteDatabase.loadLibs(getContext());
//        SQLiteDatabase.loadLibs(getContext(), getContext().getFilesDir());
        databaseHelper = new DataBaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteDatabase database = null;
        database = getDataBase();
        Cursor cursor = null;
        switch (matcher.match(uri)) {
         /*   case USER:
                cursor = database.query(USER_TABLE_NAME, projection, selection,
                        selectionArgs, null, null, null);
                break;*/

            case EVENT:
                cursor = database.query(TABLE_NAME_EVENTS, projection,
                        selection, selectionArgs, null, null, null);
                break;
            case PHONE_CATEGEORY:
                cursor = database.query(TABLE_NAME_PHONE_CATEGEORY, projection,
                        selection, selectionArgs, null, null, null);
                break;
            case CONTACT_LIST:
                cursor = database.query(TABLE_NAME_CONTACT_LIST, projection,
                        selection, selectionArgs, null, null, null);
                break;
            case GALLERY_LIST:
                cursor = database.query(TABLE_NAME_GALLERY, projection,
                        selection, selectionArgs, null, null, null);
                break;

            default:
                break;
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        SQLiteDatabase database = getDataBase();
        int count = 0;

        switch (matcher.match(uri)) {
         /*   case USER:
                count = database.update(USER_TABLE_NAME, values, selection,
                        selectionArgs);
                break;*/
            case EVENT:
                count = database.update(TABLE_NAME_EVENTS, values, selection,
                        selectionArgs);
                break;
            case PHONE_CATEGEORY:
                count = database.update(TABLE_NAME_PHONE_CATEGEORY, values, selection,
                        selectionArgs);
                break;
            case CONTACT_LIST:
                count = database.update(TABLE_NAME_CONTACT_LIST, values, selection,
                        selectionArgs);
                break;
            case GALLERY_LIST:
                count = database.update(TABLE_NAME_GALLERY, values, selection,
                        selectionArgs);
                break;
            default:
                break;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = getDataBase();
        int count = 0;
        boolean val = false;
        switch (matcher.match(uri)) {
        /*   case USER:
               count = database.delete(USER_TABLE_NAME, selection, selectionArgs);

               break;*/

            case EVENT: val = isTableExists(database, TABLE_NAME_PHONE_CATEGEORY);
                if(val)
                count = database.delete(TABLE_NAME_EVENTS, selection, selectionArgs);
                break;
            case PHONE_CATEGEORY:
               val = isTableExists(database, TABLE_NAME_PHONE_CATEGEORY);
                if(val)
                count = database.delete(TABLE_NAME_PHONE_CATEGEORY, selection, selectionArgs);
                break;
            case CONTACT_LIST:
                 val = isTableExists(database, TABLE_NAME_CONTACT_LIST);
                if(val)
                count = database.delete(TABLE_NAME_CONTACT_LIST, selection,  selectionArgs);
                break;

            case GALLERY_LIST:
                val = isTableExists(database, TABLE_NAME_GALLERY);
                if(val)
                    count = database.delete(TABLE_NAME_GALLERY, selection,  selectionArgs);
                break;

            default:
                break;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri returnUri = null;

        if (null != values) {
            long rowId = -1;
            SQLiteDatabase database = getDataBase();
            switch (matcher.match(uri)) {
               /* case USER:
                    rowId = database.insert(USER_TABLE_NAME, null, values);
                    if (0 < rowId) {
                        returnUri = ContentUris.withAppendedId(
                                UserCount.CONTENT_URI, rowId);
                    }
                    break;*/
                case EVENT:
                    try {
                        rowId = database.insert(TABLE_NAME_EVENTS, null, values);
                        if (0 < rowId) {
                            returnUri = ContentUris.withAppendedId(
                                    ContentUri.Event.CONTENT_URI, rowId);
                        }
                    }
                    catch(SQLiteConstraintException ex)
                    {

                    }
                    break;
                case PHONE_CATEGEORY:
                    try {
                        rowId = database.insert(TABLE_NAME_PHONE_CATEGEORY, null, values);
                        if (0 < rowId) {
                            returnUri = ContentUris.withAppendedId(
                                    ContentUri.PhoneCategeory.CONTENT_URI, rowId);
                        }
                    }
                    catch(SQLiteConstraintException ex)
                    {

                    }
                    break;
                case CONTACT_LIST:
                    try {
                        rowId = database.insert(TABLE_NAME_CONTACT_LIST, null, values);
                        if (0 < rowId) {
                            returnUri = ContentUris.withAppendedId(
                                    ContentUri.ContactList.CONTENT_URI, rowId);
                        }
                    }
                    catch(SQLiteConstraintException ex)
                    {

                    }
                    break;

                case GALLERY_LIST:
                    try {
                        rowId = database.insert(TABLE_NAME_GALLERY, null, values);
                        if (0 < rowId) {
                            returnUri = ContentUris.withAppendedId(
                                    ContentUri.GalleryList.CONTENT_URI, rowId);
                        }
                    }
                    catch(SQLiteConstraintException ex)
                    {

                    }
                    break;

                default:
                    break;
            }
            if (null != returnUri) {
                getContext().getContentResolver().notifyChange(returnUri, null);
            }
        }
        return returnUri;
    }


    private static boolean isTableExists(SQLiteDatabase db, String tablename) {
        Cursor c = db.rawQuery(
                "SELECT name FROM sqlite_master WHERE type='table' AND name=?",
                new String[]{tablename});
        try {
            return c.getCount() > 0;
        } finally {
            c.close();
        }
    }

    private static void addColumnIfNotExists(SQLiteDatabase db, String table,
                                             List<String> tableColumns, String col, String type) {
        if (!tableColumns.contains(col)) {
            db.execSQL("ALTER TABLE " + table + " ADD COLUMN " + col + " "
                    + type);
        }
    }

    private static List<String> getColumnNames(SQLiteDatabase db, String tableName) {
        List<String> ret = new ArrayList<String>();
        Cursor cur = db.rawQuery("PRAGMA table_info( " + tableName + " )",
                null);
        try {
            if (cur.moveToFirst()) {

                int nameColumn = cur.getColumnIndex("name");

                do {
                    ret.add(cur.getString(nameColumn));

                } while (cur.moveToNext());
            }
        } finally {
            cur.close();
        }

        return ret;
    }
}
