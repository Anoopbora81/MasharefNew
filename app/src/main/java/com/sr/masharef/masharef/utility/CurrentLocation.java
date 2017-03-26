package com.sr.masharef.masharef.utility;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class CurrentLocation {
	
	public interface CurrentLocationListener{
		public void onTrackingCompleted(Location location, String errorInfo);
	}
	
	private static String DEFAULT_DISTANCE="0.00";
	private int AUTO_CANCEL_TIME;
	
	private static Location finalLocation;
	private Location location;
	private String errorInfo;
	private ArrayList<String> providers;
	private CurrentLocationListener listener;
	
	public static String TAG ="zhr_loc";
	
	//=====
	public static void quickLocationUpdate(final Context context){
		new Handler(context.getMainLooper()).post(new Runnable() {
			@Override
			public void run() {
				CurrentLocation location = new CurrentLocation(null, CurrentLocation.getDefaultLocationProviders(), 20000);
				location.requestLocationUpdate(context);
			}
		});
		
	}
	
	public CurrentLocation(CurrentLocationListener listener){
		initialize(listener, null, 0);
	}
	
	public CurrentLocation(CurrentLocationListener listener, ArrayList<String> providers, int maxCompTimeInMillis){
		initialize(listener, providers, maxCompTimeInMillis);
	}
	
	public void initialize(CurrentLocationListener listener, ArrayList<String> providers, int maxCompTimeInMillis){
		
		this.listener	= listener;
		errorInfo		= new String();
		location		= null;
		
		if(providers==null || providers.size() == 0){
			this.providers = getDefaultLocationProviders();
		}
		else{
			this.providers = providers;
		}
		
		if(maxCompTimeInMillis >= getDefaultCompletionTime()){
			AUTO_CANCEL_TIME = maxCompTimeInMillis/providers.size();
		}
		else{
			AUTO_CANCEL_TIME = getDefaultCompletionTime();
		}
		
	}
	
	
	public static ArrayList<String> getDefaultLocationProviders(){
		ArrayList<String> providers = new ArrayList<String>();
		providers.add(LocationManager.GPS_PROVIDER);
		providers.add(LocationManager.NETWORK_PROVIDER);
		return providers;
	}
	
	//=====10 Sec set as a default completion time for default providers
	public static int getDefaultCompletionTime(){
		return 10000; 
	}
	
	//=====
	
	public boolean requestLocationUpdate(final Context ctx){
		
		LocationListener listener = new LocationListener() {
			
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
				// TODO Auto-generated method stub
				Log.d(TAG, "onStatusChanged("+provider+", "+status+", "+extras.toString()+")");
			}
			
			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
				Log.d(TAG,"onProviderEnabled("+provider+")");
				
			}
			
			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
				Log.d(TAG,"onProviderDisabled("+provider+")");
				
			}
			
			@Override
			public void onLocationChanged(Location location) {
					CurrentLocation.this.location = location;
					CurrentLocation.setLocation(location, ctx);
					stopLocationUpdates(ctx, this);
			}
			
		};
		
		return requestLocationUpdate(listener, ctx);
		
	}
	
	public boolean requestLocationUpdate(final LocationListener listener, Context ctx){
		
		boolean requestingStatus = false;
		LocationManager _locationManager = (LocationManager)ctx.getSystemService(Context.LOCATION_SERVICE);
		
		try{
			
			String provider = providers.get(0);
			if(Utils.isLocationEnabled(ctx, provider)){
				_locationManager.requestLocationUpdates(provider, 1000, 0, listener);
				requestingStatus = true;
			}
			else{
				addError(provider+" Not Enabled");
			}
		}
		catch (SecurityException e) {
			e.printStackTrace();
			String error = "Error At: fetchLastKnownLocation(...) In: CurrentLocation with Message: "+e.getMessage();
			addError(error);
			Log.e(error);
		}
		catch(IllegalArgumentException e){
			e.printStackTrace();
			String error = "Error At: fetchLastKnownLocation(...) In: CurrentLocation with Message: "+e.getMessage();
			addError(error);
			Log.e(error);
		}
		catch(RuntimeException e){
			e.printStackTrace();
			String error = "Error At: fetchLastKnownLocation(...) In: CurrentLocation with Message: "+e.getMessage();
			addError(error);
			Log.e(error);
		}
		
		if(requestingStatus){
			autoStopLocationUpdates(ctx, listener);
		}	
		else{
			stopLocationUpdates(ctx, listener);
		}
		
		return requestingStatus;
		
	}
	
	private void addError(String msg){
		
		if(errorInfo.isEmpty()){
			errorInfo = msg;
		}
		else{
			errorInfo += "|||"+msg;
		}
		
	}
	
	private void autoStopLocationUpdates(final Context ctx, final LocationListener listener){
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(location == null)
					stopLocationUpdates(ctx, listener);
			}
		}, AUTO_CANCEL_TIME);
	}
	
	//=====
	private void stopLocationUpdates(Context ctx, LocationListener listener){
		try{
			LocationManager _locationManager = (LocationManager)ctx.getSystemService(Context.LOCATION_SERVICE);
			_locationManager.removeUpdates(listener);
		}
		catch(IllegalArgumentException e){
			e.printStackTrace();
			String error = "Error At: stopLocationUpdates(...) In: CurrentLocation with Message: "+e.getMessage();
			addError(error);
			Log.e(error);
		}
		retryOrFinish(ctx);
	}
	
	private void retryOrFinish(final Context ctx){
		
		
		new Handler(ctx.getMainLooper()).post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(location == null && providers.size()>1){
					providers.remove(0);
					requestLocationUpdate(ctx);
				}
				else{
					if(listener!=null){
						listener.onTrackingCompleted(location, errorInfo);
					}
				}
			}
		});
		
		/*((Activity) ctx).runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
			}
		});*/
		
	}
	
	//=====	
	public static Location getLocation(){
		return finalLocation;
	}
	
	//=====
	public static void setLocation(Location location, Context ctx){
		if(location!=null){
			Log.d(TAG, "Latest Location Info ==>> "+location);
			finalLocation = location;
		}
	}
	
	//=====
	public static Location fetchLastKnownLocation(Context ctx){
		
		LocationManager _locationManager = (LocationManager)ctx.getSystemService(Context.LOCATION_SERVICE);
		
		Location loc = null;
		
		try{
		
			if(Utils.gpsLocEnabled(ctx))
				loc = _locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			
			if(loc == null && Utils.networkLocEnabled(ctx)){
				loc=_locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			}
		}
		catch (SecurityException e) {
			e.printStackTrace();
			Log.e("Error At: fetchLastKnownLocation(...) In: CurrentLocation with Message: "+e.getMessage());
		}
		catch(IllegalArgumentException e){
			e.printStackTrace();
			Log.e("Error At: fetchLastKnownLocation(...) In: CurrentLocation with Message: "+e.getMessage());
		}
		
		return loc;
		
	}
	
	//=====
	 public String distance(String latStr, String lonStr, char unit) {
		 double lat1,lat2;
		 double lon1,lon2;
//		 Log.i("ash_deal","Location = "+location);
		 //************CURRENT LOCATION VALIDATION*************//
		 try{
			 if(location!=null){
				 
				 lat1=location.getLatitude();
				 lon1=location.getLongitude();
				 lat2= Double.parseDouble(latStr);
				 lon2= Double.parseDouble(lonStr);
				 
			 }
			 else{
				 return DEFAULT_DISTANCE;
			 }	
		 }
		 catch (NumberFormatException e) {

			 return DEFAULT_DISTANCE;
		 }

		 //**************************************************//
		 
         double theta = lon1 - lon2;
         double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
         dist = Math.acos(dist);
         dist = rad2deg(dist);
         dist = dist * 60 * 1.1515;
         if (unit == 'K') {
             
                 dist = dist * 1.609344;
         } else if (unit == 'N') {
                 
                 dist = dist * 0.8684;
         }if (unit == 'M') {
          
                 dist = dist * 1.609344*1000;
         }
         
         Log.d("distance = ", dist+"            ;   ");
         
         return (dist+"");
	 }
	 
	//======
     /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
     /*::  This function converts decimal degrees to radians             :*/
     /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
     public static double deg2rad(double deg) {
             
             return (deg * Math.PI / 180.0);
     }

     //======
     /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
     /*::  This function converts radians to decimal degrees             :*/
     /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
     public static double rad2deg(double rad) {
     
             return (rad * 180.0 / Math.PI);
     }
     
 	//=====
     public String drivingDistance(String latStr, String lonStr) {
		 double lat1,lat2;
		 double lon1,lon2;
		 String distance = null;
		 String url;
//		 Log.i("ash_deal","Location = "+location);
		 //************CURRENT LOCATION VALIDATION*************//
		 try{
			 if(location!=null){
				 
				 lat1=location.getLatitude();
				 lon1=location.getLongitude();
				 lat2= Double.parseDouble(latStr);
				 lon2= Double.parseDouble(lonStr);
				 
			 }
			 else{
				 return DEFAULT_DISTANCE;
			 }	
		 }
		 catch (NumberFormatException e) {

			 return DEFAULT_DISTANCE;
		 }
		 
		  		 
		try {
			
			url="http://maps.googleapis.com/maps/api/distancematrix/json?origins="+lat1+","+lon1+"&destinations="+lat2+","+lon2+"&units=Meter&sensor=false";
			distance= parsingDistanceFromJSON(getJSONFromURL(url));
			
		} 
		catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			distance=DEFAULT_DISTANCE;
		}
		
		return distance;
		
     }	 
     
 	//=====
     public static JSONObject getJSONFromURL(String url){
    	 JSONObject json=null;
	     
    	 try {
	    	 
	    	 URL googleMapDistance = new URL(url);
	         URLConnection gmd = googleMapDistance.openConnection();
	         BufferedReader in = new BufferedReader(new InputStreamReader(gmd.getInputStream(),"iso-8859-1"),8);
	         StringBuilder sb = new StringBuilder();
	         String line;
	         while ((line = in.readLine()) != null) 
	        	 sb.append(line + "\n");

	         in.close();
	         
	         json=new JSONObject(sb.toString());
	         
	     } 
	     catch (MalformedURLException e) {
	         // TODO Auto-generated catch block
	         e.printStackTrace();
	         
	         Log.d(TAG,"MalformedURLException");
	         
	     }
	     catch (IOException e) {
	         // TODO Auto-generated catch block
	         e.printStackTrace();
	         Log.d(TAG,"IOException");
	         
	     }
	     catch (JSONException e) {
	         // TODO Auto-generated catch block
	         e.printStackTrace();
	         Log.d(TAG,"JSONException");
	         
	     }
		
	     return json;
	     
     }
     
 	//=====
     public static String parsingDistanceFromJSON(JSONObject js) throws JSONException {
    	
    	Log.d(TAG,"json object = "+js); 
    	String distance="";
    	JSONArray elements;
    	
    	if(js==null)
    		 return DEFAULT_DISTANCE;
    	Log.d(TAG,"response status = "+js.getString("status"));
    	
    	elements=js.getJSONArray("rows").getJSONObject(0).getJSONArray("elements");
    	    	
    	Log.d(TAG,"distance response status = "+elements.getJSONObject(0).getString("status"));
    	
    	if(!js.getString("status").equals("OK") || !elements.getJSONObject(0).getString("status").equals("OK"))
    		 return DEFAULT_DISTANCE;
    	
    	Log.d(TAG,"Rows array = "+elements.getJSONObject(0).getJSONObject("distance").toString());
    	
    	distance=elements.getJSONObject(0).getJSONObject("distance").getString("value"); 
    	
    	
    	Log.d(TAG,"distance = "+distance);
    	if(distance.length()>0){
    		distance= String.valueOf(Integer.parseInt(distance)/1000.0);
    		return distance;
    	}
    		 
    	else
    		return DEFAULT_DISTANCE;
    	 
     }

	
}
