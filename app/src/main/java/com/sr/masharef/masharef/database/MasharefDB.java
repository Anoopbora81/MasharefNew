package com.sr.masharef.masharef.database;


public class MasharefDB {
  /*  public static final String COLUMN_NAME = "name";

    private static final String TAG = "EventsTable";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_NAME = "Database";
    private static final String EVENTS_TABLE = "EventsTable";
    private static final int DATABASE_VERSION = 1;
    private static DatabaseHelper databaseHelper;

    //Create a FTS3 Virtual Table for fast searches
  *//*  private static final String DATABASE_CREATE =
            "CREATE VIRTUAL TABLE " + EVENTS_TABLE + " USING fts3("
                    + COLUMN_NAME
                    + " UNIQUE (" + COLUMN_NAME + "));";*//*

    private static final String TABLE_EVENTS = "create table " + EVENTS_TABLE +
            "(  android:layout_margin="10dp" INTEGER primary key, event_name text, description text, venue text, date text, time text, gender text, cost INTEGER, dead_line text, status text)"; // primary key

    private final Context context;

    public static SQLiteDatabase getDataBase() {
        return databaseHelper.getWritableDatabase();
    }

    public static SQLiteDatabase getReadableDB() {
        return databaseHelper.getReadableDatabase();
    }

    public static class DatabaseHelper extends SQLiteOpenHelper {

        public  DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.w(TAG, TABLE_EVENTS);
            db.execSQL(TABLE_EVENTS);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + EVENTS_TABLE);
            onCreate(db);
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

    public MasharefDB(Context ctx) {
        this.context = ctx;
        databaseHelper = new DatabaseHelper(context);
    }

    public MasharefDB open() throws SQLException {
        mDbHelper = new DatabaseHelper(context);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }


    public long insertEventData(String   android:layout_margin="10dp", String event_name, String description, String venue,
                             String date, String time, String gender,int cost, String dead_line, String status)
    {
        ContentValues contValues = new ContentValues();
        contValues.put("event_id", _id);
        contValues.put("event_name", event_name);
        contValues.put("description", description);
        contValues.put("venue", venue);
        contValues.put("date", date);
        contValues.put("time", time);
        contValues.put("gender", gender);
        contValues.put("cost", cost);
        contValues.put("dead_line", dead_line);
        contValues.put("status", status);
        return mDb.insert(EVENTS_TABLE, null, contValues);

    }

    public ArrayList<EventObjects> getEventData() throws SQLException {
        Cursor cursor = mDb.query(EVENTS_TABLE, null,
              null, null, null, null, null);
        ArrayList<EventObjects> events = new ArrayList<>();
        if(cursor.moveToFirst()){
            do{
                int id = cursor.getInt(0);
              //  String event_id = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
                String event_name = cursor.getString(cursor.getColumnIndexOrThrow("event_name"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                String venue = cursor.getString(cursor.getColumnIndexOrThrow("venue"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                String time = cursor.getString(cursor.getColumnIndexOrThrow("time"));


                String gender = cursor.getString(cursor.getColumnIndexOrThrow("gender"));
                int cost = cursor.getInt(cursor.getColumnIndexOrThrow("cost"));
                String dead_line = cursor.getString(cursor.getColumnIndexOrThrow("dead_line"));
                String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));
                //convert start date to date object
                Date reminderDate = convertStringToDate(date);
                Date deadlineDate = convertStringToDate(dead_line);

                events.add(new EventObjects(id, event_name, description, venue, reminderDate, time, gender, cost, deadlineDate, status));
               // }
            }while (cursor.moveToNext());
        }
        cursor.close();
        return events;

      *//*  HashMap passesData = new HashMap();
        if (null != c) {
            if (c.getCount() > 0) {
                while (c.moveToNext()) {
                    passesData.put("pass_id", c.getString(c.getColumnIndex("pass_id")));
                    passesData.put("name", c.getString(c.getColumnIndex("name")));
                    passesData.put("stamp", c.getString(c.getColumnIndex("stamp")));
                    passesData.put("pass_no", c.getString(c.getColumnIndex("pass_no")));
                    passesData.put("title", c.getString(c.getColumnIndex("title")));
                    passesData.put("status", c.getString(c.getColumnIndex("status")));
                }
            }
            c.close();
        }
//        return passesData;
       return cursor;*//*
    }
    private Date convertStringToDate(String dateInString){
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date date = null;
        try {
            date = format.parse(dateInString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }*/
}
