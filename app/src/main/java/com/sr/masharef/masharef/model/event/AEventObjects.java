package com.sr.masharef.masharef.model.event;

import com.sr.masharef.masharef.common.crypto.AJSONObject;
import com.sr.masharef.masharef.database.MDatabaseManager;
import com.sr.masharef.masharef.model.JsonObj;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Welcome on 23/01/17.
 */

public class AEventObjects extends JsonObj {

    private int event_id;
    private String event_name;
    private String eventDate;
    private String  description;
    private String venue;
    private String time;
    private String gender;
    private String deadlineDate;
    private int cost, not_going, maybe, going;
    private String status;
    private String costSymbol;


   /* public AEventObjects(HashMap<String, String> info) {
        super();
        {

            if (info != null) {
                this.event_id = Integer.parseInt(info.get("id").toString());
                this.event_name = info.get("name");
                this.eventDate = info.get("from").toString();
                this.description = info.get("description");
                this.venue = info.get("venue");
                this.time = getTimeInString(info.get("from").toString());
                this.gender = info.get("gender");
                this.deadlineDate =info.get("deadlineDate").toString();
                this.cost = Integer.parseInt(info.get("cost.value").toString());
                this.status = info.get("approved_status");
//                this.maybe = Integer.parseInt(AJSONObject.optString(AJSONObject.optJSONObject(map, "member_going"), "maybe").toString());
//                this.going = Integer.parseInt(AJSONObject.optString(AJSONObject.optJSONObject(map, "member_going"), "going").toString());
//                this.not_going = Integer.parseInt(AJSONObject.optString(AJSONObject.optJSONObject(map, "member_going"), "not_going").toString());
            }

        }
    }*/

    public AEventObjects(JSONObject map) {
            super(map);
      //  {"id":"1","name":"Event Name","description":"Event Description","venue":"Event Venue","window":{"from":"2017-01-23 00:00:00","to":"2017-01-23 00:00:00"},"gender":"1","cost":{"value":"100","symbol":"SGD"},"approved_status":"1"}
       //{"id":"1","created_at":"2017-01-27 01:19:27","updated_at":"2017-01-23 06:41:44","name":"Event Name","description":"Event Description","venue":"Event Venue","window":{"from":"2017-01-23 00:00:00","to":"2017-01-23 00:00:00"},"gender":"1","cost":{"value":"100","symbol":"SGD"},"approved_status":"1"}
            if (!isEmpty) {
                this.event_id = Integer.parseInt(AJSONObject.optString(map, "id").toString());
                this.event_name = AJSONObject.optString(map, "name");
//                this.eventDate = convertStringToDate(AJSONObject.optString(AJSONObject.optJSONObject(map, "window"), "from").toString());
                this.eventDate = (AJSONObject.optString(AJSONObject.optJSONObject(map, "window"), "from")).toString();
                this.description = AJSONObject.optString(map, "description");
                this.venue = AJSONObject.optString(map, "venue");
                this.time = getTimeInString(AJSONObject.optString(AJSONObject.optJSONObject(map, "window"), "from").toString());
                this.gender = AJSONObject.optString(map, "gender");
                this.deadlineDate = AJSONObject.optString(AJSONObject.optJSONObject(map, "window"), "to").toString();
                this.cost = Integer.parseInt(AJSONObject.optString(AJSONObject.optJSONObject(map, "cost"), "value").toString());
                this.costSymbol =AJSONObject.optString(AJSONObject.optJSONObject(map, "cost"), "symbol").toString();
                this.status = AJSONObject.optString(map, "approved_status");
                JSONObject memberGoingJSON =  AJSONObject.optJSONObject(map, "member_going");
                if(memberGoingJSON != null) {
                    this.maybe = Integer.parseInt(AJSONObject.optString(memberGoingJSON, "maybe").toString());
                    this.going = Integer.parseInt(AJSONObject.optString(memberGoingJSON, "going").toString());
                    this.not_going = Integer.parseInt(AJSONObject.optString(memberGoingJSON, "not_going").toString());
                }

                MDatabaseManager.getInstance().insertEventData(event_id,event_name, description, venue, eventDate, time, gender, cost, deadlineDate, status, maybe, going, not_going, costSymbol);
            }
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

    private String getTimeInString(String dateInString){
       DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
     //   DateFormat format = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss a", Locale.ENGLISH);
        Date date = null;
        try {
            date = format.parse(dateInString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
      //  java.util.Calendar eventCalendar = java.util.Calendar.getInstance();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int hours = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);
        String time = hours +":"+min;
        return time;
    }

    public int getEventId() {
        return event_id;
    }

    public String getEvent_name() {
        return event_name;
    }

    public String getEventDate() {
        return eventDate;
    }

    public String getDescription() {
        return description;
    }

    public String getVenue() {
        return venue;
    }

    public String getTime() {
        return time;
    }

    public String getGender() {
        return gender;
    }

    public String getDeadlineDate() {
        return deadlineDate;
    }

    public int getCost() {
        return cost;
    }

    public String getStatus() {
        return status;
    }
}
