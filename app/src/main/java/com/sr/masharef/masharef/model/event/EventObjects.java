package com.sr.masharef.masharef.model.event;

/**
 * Created by welcome on 1/12/2017.
 */

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EventObjects {
    private int id;
    private String event_name;
    private Date eventDate;
    private String  description;
    private String venue;
    private String time;
    private String gender;
    private Date deadlineDate;
    private int cost;
    private String status;

    private String currency;


    private int notGoingToEvent, mayBeGoingToEvent,  goingToEvent;



    /*   public EventObjects(String message, Date date) {
            this.event_name = event_name;
            this.date = date;
        }*/
    public EventObjects(int id, String event_name, String  description, String venue, Date remDate, String time, String gender, int cost, Date deadlineDate, String status,int notGoingToEvent, int mayBeGoingToEvent, int goingToEvent, String currency) {
        this.id = id;
        this.eventDate = remDate;
        this.event_name = event_name;
        this.description = description;
        this.venue = venue;
        this.time = time;
        this.gender = gender;
        this.cost = cost;
        this.deadlineDate = deadlineDate;
        this.status = status;
        this.notGoingToEvent = notGoingToEvent;
        this.mayBeGoingToEvent = mayBeGoingToEvent;
        this.goingToEvent = goingToEvent;
        this.currency = currency;
    }
    public int getId() {
        return id;
    }

    public Date getEventDate() {

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
        if(gender.equalsIgnoreCase("0")) {
            return "Male";
        }
        else
        {
            return "Female";
        }

        //return gender;
    }

    public int getCost() {
        return cost;
    }

    public String getEvent_name() {
        return event_name;
    }

    public Date getDeadlineDate() {
        return deadlineDate;
    }

    public String getStatus() {
        return status;
    }

    public int getNotGoingToEvent() {
        return notGoingToEvent;
    }

    public int getMayBeGoingToEvent() {
        return mayBeGoingToEvent;
    }

    public int getGoingToEvent() {
        return goingToEvent;
    }

    public String getCurrency() {
        return currency;
    }


}