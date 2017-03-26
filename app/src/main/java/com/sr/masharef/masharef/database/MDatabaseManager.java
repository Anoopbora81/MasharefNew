package com.sr.masharef.masharef.database;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

import com.sr.masharef.masharef.database.ContentUri.Event;
import com.sr.masharef.masharef.database.ContentUri.PhoneCategeory;
import com.sr.masharef.masharef.manager.ApplicationManager;
import com.sr.masharef.masharef.model.event.EventObjects;
import com.sr.masharef.masharef.model.gallery.AGallery;
import com.sr.masharef.masharef.model.phonebook.ACategory;
import com.sr.masharef.masharef.model.phonebook.AContact;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MDatabaseManager {

    private static MDatabaseManager instance;
    private static Context mContext;
    private static final String TAG = "masharef Database Manager: ";

    private MDatabaseManager() {
        mContext = ApplicationManager.context;
    }

    public static MDatabaseManager getInstance() {
        if (null == instance) {
            instance = new MDatabaseManager();
        }
        return instance;
    }

    public void insertEventData(int event_id, String event_name, String description, String venue,
                                String date, String time, String gender,int cost, String dead_line, String status, int notGoingToEvent, int mayBeGoingToEvent, int goingToEvent, String currency)
    {

        ContentResolver resolver = mContext.getContentResolver();
        ContentValues contValues = new ContentValues();
        contValues.put("event_id", event_id);
        contValues.put("event_name", event_name);
        contValues.put("description", description);
        contValues.put("venue", venue);
        contValues.put("date", date);
        contValues.put("time", time);
        contValues.put("gender", gender);
        contValues.put("cost", cost);
        contValues.put("dead_line", dead_line);
        contValues.put("status", status);
        contValues.put("not_going_to_event", notGoingToEvent);
        contValues.put("may_be_gooing_to_event", mayBeGoingToEvent);
        contValues.put("going_to_event", goingToEvent);
        contValues.put("currency", currency);

        resolver.insert(Event.CONTENT_URI, contValues);
    }

    public ArrayList<EventObjects> getEventData() throws SQLException {
        ContentResolver resolver = mContext.getContentResolver();
        Cursor cursor = resolver.query(Event.CONTENT_URI, null, null, null,null);
        ArrayList<EventObjects> events = new ArrayList<>();
        if(cursor.moveToFirst()){
            do{
                int id = cursor.getInt(0);
                String event_name = cursor.getString(cursor.getColumnIndexOrThrow("event_name"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                String venue = cursor.getString(cursor.getColumnIndexOrThrow("venue"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                String time = cursor.getString(cursor.getColumnIndexOrThrow("time"));

                String gender = cursor.getString(cursor.getColumnIndexOrThrow("gender"));
                int cost = cursor.getInt(cursor.getColumnIndexOrThrow("cost"));
                String dead_line = cursor.getString(cursor.getColumnIndexOrThrow("dead_line"));
                String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));
                int notGoingToEvent = cursor.getInt(cursor.getColumnIndexOrThrow("not_going_to_event"));
                int mayBeGoingToEvent = cursor.getInt(cursor.getColumnIndexOrThrow("may_be_gooing_to_event"));
                int goingToEvent = cursor.getInt(cursor.getColumnIndexOrThrow("going_to_event"));
                String currency = cursor.getString(cursor.getColumnIndexOrThrow("currency"));
                //convert start date to date object
                Date reminderDate = convertStringToDate(date);
                Date deadlineDate = convertStringToDate(dead_line);

                events.add(new EventObjects(id, event_name, description, venue, reminderDate, time, gender, cost, deadlineDate, status, notGoingToEvent,mayBeGoingToEvent,goingToEvent, currency));
                // }
            }while (cursor.moveToNext());
        }
        cursor.close();
        return events;
    }

    public void setMembersJoiningToEvent(String evntId, int notGoing, int mayBeGoing, int going) {
        ContentResolver resolver = mContext.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(Event.NOTGOINGTOEVENT, notGoing);
        values.put(Event.MAYBEGOINGTOEVENT, mayBeGoing);
        values.put(Event.GOINGTOEVENT, going);
        resolver.update(Event.CONTENT_URI, values,
                Event.EVENT_id + "= ?", new String[]{evntId});
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
    }

    public void reset() {
        deleteEvent();

  /*      // Confirm whether we have to call the below statements or not
        File mediaStorageDir = new File(
                Main.mContext.getExternalFilesDir(null), "mDatabase");
        if (mediaStorageDir.exists()) {
            deleteDirFiles(mediaStorageDir);
        }*/
    }


    private void deleteDirFiles(File dir) {
        File[] list = dir.listFiles();
        if (list != null && list.length > 0) {
            for (File f : list) {
                if (!f.isDirectory()) {
                    f.delete();
                } else {
                    deleteDirFiles(f);
                }
            }
        }
    }

    public void deleteEvent() {
        ContentResolver resolver = mContext.getContentResolver();
        resolver.delete(Event.CONTENT_URI, null, null);
    }

    public void insertCategeory(int categeory_id, String categeory_name)
    {

        ContentResolver resolver = mContext.getContentResolver();
        ContentValues contValues = new ContentValues();
        contValues.put("category_id", categeory_id);
        contValues.put("categeory_name", categeory_name);
        resolver.insert(PhoneCategeory.CONTENT_URI, contValues);
    }

    public ArrayList<ACategory> getCategeoryList() throws SQLException {
        ContentResolver resolver = mContext.getContentResolver();
        Cursor cursor = resolver.query(PhoneCategeory.CONTENT_URI, null, null, null,null);
        ArrayList<ACategory> categeoryList = new ArrayList<>();
        if(cursor.moveToFirst()){
            do{
                int id = cursor.getInt(0);
                String categeory_name = cursor.getString(cursor.getColumnIndexOrThrow("categeory_name"));
                categeoryList.add(new ACategory(""+id, categeory_name));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return categeoryList;
    }

    public void deleteCategeory() {
        ContentResolver resolver = mContext.getContentResolver();
        resolver.delete(PhoneCategeory.CONTENT_URI, null, null);
    }

    public void insertGalleryCategeory(int is_private, String media_type,String media_image,String media_url)
    {

        ContentResolver resolver = mContext.getContentResolver();
        ContentValues contValues = new ContentValues();
        contValues.put("is_private", is_private);
        contValues.put("media_type", media_type);
        contValues.put("media_image", media_image);
        contValues.put("media_url", media_url);
        resolver.insert(ContentUri.GalleryCategeory.CONTENT_URI, contValues);
    }

    public ArrayList<AGallery> getGallelryImageList(String privateId,String type) throws SQLException {
        String selectionString = "is_private=? AND media_type=?";
        String[] selectionArgs = new String[]{""+privateId,type};
        ContentResolver resolver = mContext.getContentResolver();
        Cursor cursor = resolver.query(ContentUri.GalleryList.CONTENT_URI, null, selectionString, selectionArgs,null);
        ArrayList<AGallery> galleryList = new ArrayList<>();
        if(cursor.moveToFirst()){
            do{
                int id = cursor.getInt(0);
                String media_type = cursor.getString(cursor.getColumnIndexOrThrow("media_type"));
                String  media_image = cursor.getString(cursor.getColumnIndexOrThrow("media_image"));
                String  media_url = cursor.getString(cursor.getColumnIndexOrThrow("media_url"));
                if(media_type.equalsIgnoreCase("1")) {
                    galleryList.add(new AGallery("" + id, media_type, media_image, media_url));
                }
               /* else if(media_type.equalsIgnoreCase("2")) {
                    galleryList.add(new AGallery("" + id, media_type, media_image, media_url));
                }else if(media_type.equalsIgnoreCase("3")) {
                    galleryList.add(new AGallery("" + id, media_type, media_image, media_url));
                }*/
            }while (cursor.moveToNext());
        }
        cursor.close();
        return galleryList;
    }

    public void deleteGallery() {
        ContentResolver resolver = mContext.getContentResolver();
        resolver.delete(ContentUri.GalleryCategeory.CONTENT_URI, null, null);
    }


    public void insertContactList(int contact_id, int categeory_id, String categeory_name, String first_name,String last_name,
                                  String occupation,String work_place,String rating, String country_name,String country_code,String country_ISN,String national_number, String interNational_number)
    {
        ContentResolver resolver = mContext.getContentResolver();
        ContentValues contValues = new ContentValues();
        contValues.put("contact_id", contact_id);
        contValues.put("categeory_id", categeory_id);
        contValues.put("categeory_name", categeory_name);
        contValues.put("first_name", first_name);
        contValues.put("last_name", last_name);
        contValues.put("occupation", occupation);
        contValues.put("work_place", work_place);
        contValues.put("rating", rating);
        contValues.put("country_name", country_name);
        contValues.put("country_code", country_code);
        contValues.put("country_ISN", country_ISN);
        contValues.put("national_number", national_number);
        contValues.put("interNational_number", interNational_number);
        resolver.insert(ContentUri.ContactList.CONTENT_URI, contValues);
    }

    public  ArrayList<AContact>  getContactList(String categoryId) throws SQLException {
        String selectionString = "categeory_id=?";
        String[] selectionArgs = new String[]{""+categoryId};
        ContentResolver resolver = mContext.getContentResolver();
        Cursor cursor = resolver.query(ContentUri.ContactList.CONTENT_URI, null, selectionString, selectionArgs,null);
        ArrayList<AContact> contactList = new ArrayList<>();
        if(cursor.moveToFirst()){
            do{
             //   int id = cursor.getInt(0);
                int contact_id = cursor.getInt(cursor.getColumnIndexOrThrow("contact_id"));
                int categeory_id = cursor.getInt(cursor.getColumnIndexOrThrow("categeory_id"));
                String categeory_name = cursor.getString(cursor.getColumnIndexOrThrow("categeory_name"));
                String first_name = cursor.getString(cursor.getColumnIndexOrThrow("first_name"));
                String last_name = cursor.getString(cursor.getColumnIndexOrThrow("last_name"));
                String occupation = cursor.getString(cursor.getColumnIndexOrThrow("occupation"));
                String work_place = cursor.getString(cursor.getColumnIndexOrThrow("work_place"));
                String rating = cursor.getString(cursor.getColumnIndexOrThrow("rating"));
                String country_name = cursor.getString(cursor.getColumnIndexOrThrow("country_name"));
                String country_code = cursor.getString(cursor.getColumnIndexOrThrow("country_code"));
                String country_ISN = cursor.getString(cursor.getColumnIndexOrThrow("country_ISN"));
                String national_number = cursor.getString(cursor.getColumnIndexOrThrow("national_number"));
                String interNational_number = cursor.getString(cursor.getColumnIndexOrThrow("interNational_number"));
                contactList.add(new AContact(contact_id, categeory_id,categeory_name, first_name, last_name,occupation, work_place, rating, country_name, country_code,country_ISN, national_number, interNational_number));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return contactList;
    }

    public void deleteContactList(int categeory_id) {
        ContentResolver resolver = mContext.getContentResolver();
        String selectionString = "categeory_id=?";
        String[] selectionArgs = new String[]{""+categeory_id};
        int deletedRecord = resolver.delete(ContentUri.ContactList.CONTENT_URI, selectionString, selectionArgs);

    }

}
