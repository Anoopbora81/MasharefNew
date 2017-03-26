
//ISO8601DateFormatter.java

//Created By Zuhair on Feb 9, 2017


package com.sr.masharef.masharef.common;

import com.sr.masharef.masharef.utility.Log;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public final class ISO8601DateFormatter {

	
	/*
	 * We can enable and use the method mentioned below instead of using the Joda Method in case
	 * joda-time library showing any issue.  
	 */
	
	 /* private static final ThreadLocal<DateFormat> TS_FROM_FORMAT = new ThreadLocal<DateFormat>()
	{
	    @Override
	    protected DateFormat initialValue() {
	    	final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS");
			TimeZone tz = TimeZone.getTimeZone("UTC");
	        dateFormat.setTimeZone(tz);
		    return dateFormat;
	    }
	};
	
	private static final ThreadLocal<DateFormat> TS_TO_FORMAT = new ThreadLocal<DateFormat>()
	{
	    @Override
	    protected DateFormat initialValue() {
	    	final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSZ");
	        TimeZone tz = TimeZone.getTimeZone("UTC");
	        dateFormat.setTimeZone(tz);
		    return dateFormat;
	    }
	};*/
	
	private static final ThreadLocal<DateTimeFormatter> TS_FROM_FORMAT = new ThreadLocal<DateTimeFormatter>()
	{
	    @Override
	    protected DateTimeFormatter initialValue() {
	    	final DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS")
	    			.withZoneUTC();
		    return dateFormat;
	    }
	};
	
	private static final ThreadLocal<DateTimeFormatter> TS_TO_FORMAT = new ThreadLocal<DateTimeFormatter>()
	{
	    @Override
	    protected DateTimeFormatter initialValue() {
	    	final DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSZ")
	    			.withZoneUTC();
		    return dateFormat;
	    }
	};

    /** 
     * Transform Calendar to ISO 8601 string. 
     */
	
    public static String fromCalendar(final Calendar calendar) {
        return fromDate(calendar.getTime());
    }

    /** 
     * Transform Date to ISO 8601 string. 
     */
    public static synchronized String fromDate(final Date date) {
    	String returnStr = "";
    	if(date != null){
    		DateTime jodaDate = new DateTime(date);
            String formatted = jodaDate.toString(TS_FROM_FORMAT.get());
    		returnStr = formatted+"Z";
    	}
    	return returnStr;
    }

    
    /** 
     * Get current date and time formatted as ISO 8601 string. 
     */
    public static String now() {
        return fromCalendar(GregorianCalendar.getInstance());
    }
    

    /** 
     * Transform ISO 8601 string to Calendar. 
     */
    public static Calendar parseToCalendar(final String iso8601string){
    	 Calendar calendar = GregorianCalendar.getInstance();
    	 Date date = parseToDate(iso8601string);
    	 if(date!=null)
    		 calendar.setTime(date);
    	 return calendar;
    }
    
    /** 
     * Transform ISO 8601 string to Date. 
     */
    public static Date parseToDate(final String iso8601string){
       
    	Date date = null;
    	
    	if(iso8601string!=null){
	        try {
	        	String s = iso8601string.replace("Z", "+00:00");
	            date = TS_TO_FORMAT.get().parseDateTime(s).toDateTime().toDate();
	        } catch (IndexOutOfBoundsException e) {
	        	e.printStackTrace();
	            Log.d(e.getMessage()+" At ISO6801 Date Pasrsing");
	        }
    	}    
        
        return date;
    }
    
    public static boolean isEqualMonth(Calendar cal1, Calendar cal2){
    	return compareMonth(cal1, cal2) == 0;
    }
    
    public static int compareMonth(Calendar cal1, Calendar cal2){
		
		if (cal1.get(Calendar.YEAR) != cal2.get(Calendar.YEAR))
			return cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR);
	 
		return cal1.get(Calendar.MONTH) - cal2.get(Calendar.MONTH);
		
	}
    
   
}